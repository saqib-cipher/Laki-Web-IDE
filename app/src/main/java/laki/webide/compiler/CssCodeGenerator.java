package laki.webide.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import laki.webide.core.BlockBean;
import a.a.a.jq;
import laki.webide.blockSystem.core.BlockPaletteManager;
import laki.webide.blockSystem.core.ModularBlockDefinition;

public class CssCodeGenerator {
    private String activityName;
    private jq buildConfig;
    private ArrayList<BlockBean> eventBlocks;
    private boolean isViewBindingEnabled;
    private Map<String, BlockBean> blockMap;

    public CssCodeGenerator(String activityName, jq buildConfig, ArrayList<BlockBean> eventBlocks, boolean isViewBindingEnabled) {
        this.activityName = activityName;
        this.buildConfig = buildConfig;
        this.eventBlocks = eventBlocks;
        this.isViewBindingEnabled = isViewBindingEnabled;
    }

    public String a() {
        blockMap = new HashMap<>();
        if (eventBlocks == null || eventBlocks.isEmpty()) return "";

        for (BlockBean bean : eventBlocks) {
            blockMap.put(bean.id, bean);
        }

        HashSet<Integer> childBlockIds = new HashSet<>();
        for (BlockBean bean : eventBlocks) {
            if (bean.subStack1 >= 0) childBlockIds.add(bean.subStack1);
            if (bean.subStack2 >= 0) childBlockIds.add(bean.subStack2);
            if (bean.nextBlock >= 0) childBlockIds.add(bean.nextBlock);
            for (String p : bean.parameters) {
                if (p != null && p.startsWith("@")) {
                    try {
                        childBlockIds.add(Integer.parseInt(p.substring(1)));
                    } catch (Exception ignored) {}
                }
            }
        }

        StringBuilder fullCss = new StringBuilder();
        for (BlockBean bean : eventBlocks) {
            int id = Integer.parseInt(bean.id);
            if (!childBlockIds.contains(id)) {
                fullCss.append(generateBlock(bean)).append("\n\n");
            }
        }

        String code = fullCss.toString();
        code = code.replaceAll("(?m)^(\\s*--[a-zA-Z0-9_-]+)\\s*=\\s*", "$1: ");
        code = code.replaceAll("(?<!var\\()(?<=[^\\s])(\\s*)(--[a-zA-Z0-9_-]+)", "$1var($2)");

        return code;
    }

    private String generateBlock(BlockBean bean) {
        ModularBlockDefinition def = BlockPaletteManager.getBlockByOpCode(bean.opCode);
        if (def != null) {
            String subStack1 = bean.subStack1 >= 0 ? generateBlock(blockMap.get(String.valueOf(bean.subStack1))) : "";
            String subStack2 = bean.subStack2 >= 0 ? generateBlock(blockMap.get(String.valueOf(bean.subStack2))) : "";
            
            ArrayList<String> params = new ArrayList<>();
            for (String p : bean.parameters) {
                if (p != null && p.startsWith("@")) {
                    params.add(generateBlock(blockMap.get(p.substring(1))));
                } else {
                    params.add(p);
                }
            }
            return def.generateCode(params, subStack1, subStack2);
        }
        
        // Fallback for non-modular blocks or next blocks
        String code = ""; 
        // Note: Basic CSS blocks should all be modular. 
        // If not, we would need the spec-based generator logic here.
        
        if (bean.nextBlock >= 0) {
            BlockBean next = blockMap.get(String.valueOf(bean.nextBlock));
            if (next != null) {
                code += (code.isEmpty() ? "" : "\n") + generateBlock(next);
            }
        }
        return code;
    }
}
