package laki.webide.blockSystem.blocks;

import laki.webide.blockSystem.BlockShape;
import laki.webide.blockSystem.core.ModularBlockDefinition;

public class CssIdSelectorBlock extends ModularBlockDefinition {
    @Override public String getOpCode() { return "cssIdSelector"; }
    @Override public String getSpec() { return "#%m.htmlId {"; }
    @Override public BlockShape getShape() { return BlockShape.CONTAINER; }
    @Override public int getColor() { return 0xffcc5b22; } // CSS Selector Color
    @Override public String getCodeTemplate() { return "#%s {\n%s\n}"; }
    @Override public int getTargetPaletteId() { return 1; }
}
