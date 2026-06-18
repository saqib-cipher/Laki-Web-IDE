package laki.webide.core.html;

import android.view.View;
import laki.webide.core.Block;
import laki.webide.core.BlockArg;
import laki.webide.core.BlockPane;

/**
 * Handles automatic generation of IDs and Classes for HTML blocks.
 */
public class HtmlIdGenerator {

    public static void autoFill(BlockPane pane, Block block, String filename) {
        if (block.mOpCode == null || !block.mOpCode.startsWith("html_")) return;

        // 1. Get the tag name
        String tag = block.mOpCode.replace("html_", "");
        
        // 2. Count existing blocks of this type to find the index
        int index = countBlocks(pane, block.mOpCode) + 1;

        // 3. Handle special combined heading block
        if (tag.equals("h")) {
            block.setArgValue(0, "h1"); // Default Tag
            tag = "h1"; 
            
            String generatedId = filename + tag + index;
            String generatedClass = "c" + filename + tag + "_" + index;
            
            block.attributes.put("id", generatedId);
            block.attributes.put("class", generatedClass);
            
            // ID is Hole 1 for headings, set it and disable editing
            block.setArgValue(1, generatedId); 
            if (block.args.size() > 1 && block.args.get(1) instanceof BlockArg) {
                ((BlockArg) block.args.get(1)).setEditable(false);
            }
        } else {
            // 4. Generate the names for normal blocks
            String generatedId = filename + tag + index;
            String generatedClass = "c" + filename + tag + "_" + index;

            block.attributes.put("id", generatedId);
            block.attributes.put("class", generatedClass);
            
            // ID is Hole 0 for others, set it and disable editing
            block.setArgValue(0, generatedId); 
            if (!block.args.isEmpty() && block.args.get(0) instanceof BlockArg) {
                ((BlockArg) block.args.get(0)).setEditable(false);
            }
        }
        
        // 5. Set default content based on tag
        if (tag.startsWith("h") || tag.equals("p") || tag.equals("span") || tag.equals("label") || tag.equals("button")) {
            block.attributes.put("text", "Sample " + tag + " content");
        } else if (tag.equals("img")) {
            block.attributes.put("src", "https://via.placeholder.com/150");
        } else if (tag.equals("a")) {
            block.attributes.put("href", "#");
            block.attributes.put("text", "link");
        }
    }

    private static int countBlocks(BlockPane pane, String opCode) {
        int count = 0;
        for (int i = 0; i < pane.getChildCount(); i++) {
            View child = pane.getChildAt(i);
            if (child instanceof Block) {
                if (opCode.equals(((Block) child).mOpCode)) {
                    count++;
                }
            }
        }
        return count;
    }
}
