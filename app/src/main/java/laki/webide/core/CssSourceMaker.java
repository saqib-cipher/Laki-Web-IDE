package laki.webide.core;

import android.content.Context;
import android.view.View;
import java.util.ArrayList;
import java.util.HashSet;

public class CssSourceMaker {
    private Context context;
    private HashSet<Block> processed = new HashSet<>();

    public CssSourceMaker(Context context) {
        this.context = context;
    }

    public String getSource(int indent, ArrayList<Block> blocks) {
        processed.clear();
        StringBuilder sb = new StringBuilder();
        for (Block block : blocks) {
            // Process if it's not already handled by a chain and it's a top-level block 
            // (connected to root or floating)
            if (!processed.contains(block)) {
                if (block.parentBlock == null || block.parentBlock.getBlockType() == 2 || !blocks.contains(block.parentBlock)) {
                    if (block.getBlockType() != 2) { // Skip Hat/Root blocks
                        sb.append(makeSource(indent, block));
                    }
                }
            }
        }
        return sb.toString();
    }

    private String makeSource(int indent, Block block) {
        if (block == null || processed.contains(block)) return "";
        processed.add(block);
        
        StringBuilder sb = new StringBuilder();
        String indentation = getIndent(indent);
        
        if (block.mType.equals("c")) {
            // Selector block
            sb.append(indentation).append(getParamValue(block, 0)).append(" {\n");
            if (block.subStack1 != -1) {
                Block sub = (Block) block.pane.findViewWithTag(block.subStack1);
                sb.append(makeSource(indent + 1, sub));
            }
            sb.append(indentation).append("}\n\n");
        } else {
            // Property or variable block
            String op = block.mOpCode;
            if (op.startsWith("setVar_")) {
                String varName = op.substring(7);
                sb.append(indentation).append("--").append(varName).append(": ").append(getParamValue(block, 0)).append(";\n");
            } else if (op.equals("setVar")) {
                String varName = getParamValue(block, 0);
                sb.append(indentation).append("--").append(varName).append(": ").append(getParamValue(block, 1)).append(";\n");
            } else if (op.equals("anchor")) {
                String side = getParamValue(block, 0);
                String valueWithUnit = getParamValue(block, 1);
                sb.append(indentation).append(side).append(": ").append(valueWithUnit).append(";\n");
            } else if (op.equals("minDim")) {
                String dim = getParamValue(block, 0);
                String valueWithUnit = getParamValue(block, 1);
                sb.append(indentation).append("min-").append(dim).append(": ").append(valueWithUnit).append(";\n");
            } else if (op.equals("maxDim")) {
                String dim = getParamValue(block, 0);
                String valueWithUnit = getParamValue(block, 1);
                sb.append(indentation).append("max-").append(dim).append(": ").append(valueWithUnit).append(";\n");
            } else if (op.equals("spacingSide")) {
                String label = block.mSpec; // e.g. "margin-%m.side : %v.unit"
                String type = label.startsWith("margin") ? "margin" : "padding";
                String side = getParamValue(block, 0);
                String valueWithUnit = getParamValue(block, 1);
                sb.append(indentation).append(type).append("-").append(side).append(": ").append(valueWithUnit).append(";\n");
            } else if (op.equals("asd")) {
                sb.append(indentation).append(getParamValue(block, 0)).append("\n");
            } else {
                StringBuilder valBuilder = new StringBuilder();
                for (int i = 0; i < block.args.size(); i++) {
                    String paramValue = getParamValue(block, i);
                    if (paramValue != null && !paramValue.isEmpty()) {
                        String type = (String) block.argTypes.get(i);
                        
                        // Handle Image auto-url wrapping
                        if (type.contains(".image") && !paramValue.startsWith("url(") && !paramValue.contains("gradient")) {
                            paramValue = "url('" + paramValue + "')";
                        }

                        if (valBuilder.length() > 0 && !type.contains(".unit")) {
                            valBuilder.append(" ");
                        }
                        valBuilder.append(paramValue);
                    }
                }
                sb.append(indentation).append(op).append(": ").append(valBuilder.toString()).append(";\n");
            }
        }

        // Process next block in the chain
        if (block.nextBlock != -1) {
            Block next = (Block) block.pane.findViewWithTag(block.nextBlock);
            sb.append(makeSource(indent, next));
        }

        return sb.toString();
    }

    private String getParamValue(Block block, int index) {
        if (index < block.args.size()) {
            View arg = block.args.get(index);
            if (arg instanceof BlockArg) {
                return ((BlockArg) arg).getArgValue().toString();
            } else if (arg instanceof Block) {
                Block inner = (Block) arg;
                String op = inner.mOpCode;
                if (op.startsWith("getVar_")) {
                    return "var(--" + op.substring(7) + ")";
                } else if (op.equals("getVar")) {
                    return "var(--" + getParamValue(inner, 0) + ")";
                } else if (op.equals("linearGradient")) {
                    return "linear-gradient(" + getParamValue(inner, 0) + ", " + getParamValue(inner, 1) + ")";
                } else if (op.equals("radialGradient")) {
                    return "radial-gradient(" + getParamValue(inner, 0) + ", " + getParamValue(inner, 1) + ")";
                } else if (op.equals("url")) {
                    return "url(" + getParamValue(inner, 0) + ")";
                } else if (op.equals("calc")) {
                    return "calc(" + getParamValue(inner, 0) + ")";
                } else if (op.equals("var")) {
                    return "var(--" + getParamValue(inner, 0) + ")";
                }
                return "/* unknown block */";
            }
        }
        return "";
    }

    private String getIndent(int count) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < count; j++) {
            sb.append("    ");
        }
        return sb.toString();
    }
}
