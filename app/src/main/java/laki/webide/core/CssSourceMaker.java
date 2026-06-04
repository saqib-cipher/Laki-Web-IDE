package laki.webide.core;

import android.content.Context;
import android.view.View;
import java.util.ArrayList;

public class CssSourceMaker {
    private Context context;

    public CssSourceMaker(Context context) {
        this.context = context;
    }

    public String getSource(int indent, ArrayList<Block> blocks) {
        StringBuilder sb = new StringBuilder();
        for (Block block : blocks) {
            if (block.parentBlock == null) { // Only top-level blocks
                sb.append(makeSource(indent, block));
            }
        }
        return sb.toString();
    }

    private String makeSource(int indent, Block block) {
        StringBuilder sb = new StringBuilder();
        String indentation = getIndent(indent);
        
        // This is a simplified logic. In a real system, you'd map opCodes to templates.
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
            } else {
                sb.append(indentation).append(op).append(": ").append(getParamValue(block, 0)).append(";\n");
            }
        }

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
                if (inner.mOpCode.startsWith("getVar_")) {
                    return "var(--" + inner.mOpCode.substring(7) + ")";
                }
                return "calc(...)";
            }
        }
        return "";
    }

    private String getIndent(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append("    ");
        }
        return sb.toString();
    }
}
