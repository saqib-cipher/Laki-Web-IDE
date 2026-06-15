package laki.webide;

import android.view.ViewGroup;
import laki.webide.core.CreateBlock;
import laki.webide.core.PaletteBlock;
import laki.webide.core.html.HtmlBlocks;

/**
 * Registry for populating HTML structural blocks into the editor's drawer.
 */
public class WidgetShowCased {

    /**
     * Fills the drawer (paletteBlock) with HTML blocks.
     */
    public static void setup(PaletteBlock paletteBlock) {
        if (paletteBlock == null) return;
        
        paletteBlock.removeAllBlocks();
        
        for (CreateBlock cb : HtmlBlocks.getTestBlocks()) {
            paletteBlock.addBlock(cb);
        }
    }
}
