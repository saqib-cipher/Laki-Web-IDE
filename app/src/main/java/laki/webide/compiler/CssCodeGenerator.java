package laki.webide.compiler;

import java.util.ArrayList;
import com.besome.sketch.beans.BlockBean;
import a.a.a.Fx;
import a.a.a.jq;

public class CssCodeGenerator extends Fx {
    
    public CssCodeGenerator(String activityName, jq buildConfig, ArrayList<BlockBean> eventBlocks, boolean isViewBindingEnabled) {
        super(activityName, buildConfig, eventBlocks, isViewBindingEnabled);
    }

    @Override
    public String a() {
        blockMap = new java.util.HashMap<>();
        if (eventBlocks == null || eventBlocks.isEmpty()) return "";

        for (com.besome.sketch.beans.BlockBean bean : eventBlocks) {
            blockMap.put(bean.id, bean);
        }

        // --- CSS MULTI-ROOT SCANNER ---
        // Unlike Java events, CSS can have multiple root selectors on one pane.
        // We scan for all blocks that are NOT sub-blocks or next-blocks of others.
        java.util.HashSet<Integer> childBlockIds = new java.util.HashSet<>();
        for (com.besome.sketch.beans.BlockBean bean : eventBlocks) {
            if (bean.subStack1 >= 0) childBlockIds.add(bean.subStack1);
            if (bean.subStack2 >= 0) childBlockIds.add(bean.subStack2);
            if (bean.nextBlock >= 0) childBlockIds.add(bean.nextBlock);
            for (String p : bean.parameters) {
                if (p != null && p.startsWith("@")) {
                    childBlockIds.add(Integer.parseInt(p.substring(1)));
                }
            }
        }

        StringBuilder fullCss = new StringBuilder();
        for (com.besome.sketch.beans.BlockBean bean : eventBlocks) {
            int id = Integer.parseInt(bean.id);
            if (!childBlockIds.contains(id)) {
                // This is a Root Selector (Body, .class, etc.)
                fullCss.append(generateBlock(bean, "")).append("\n\n");
            }
        }

        String code = fullCss.toString();
        // ------------------------------

        // 1. Fix variable assignments: change '--var = value;' to '--var: value;'
        // This targets lines starting with -- followed by name, optional space, = , space, and then value
        code = code.replaceAll("(?m)^(\\s*--[a-zA-Z0-9_-]+)\\s*=\\s*", "$1: ");

        // 2. Wrap CSS variables in var() function if used as values (not at start of line)
        // We avoid wrapping if it's already in var() or if it's a declaration at the start of a line.
        // We use (?<=[^\s]) to ensure there's a non-whitespace character somewhere before it, 
        // which excludes the start of the line.
        code = code.replaceAll("(?<!var\\()(?<=[^\\s])(\\s*)(--[a-zA-Z0-9_-]+)", "$1var($2)");

        return code;
    }
}
