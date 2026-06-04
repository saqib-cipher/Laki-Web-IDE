package laki.webide.blockSystem.blocks;

import laki.webide.blockSystem.BlockShape;
import laki.webide.blockSystem.core.ModularBlockDefinition;

public class GetCssVarBlock extends ModularBlockDefinition {
    @Override public String getOpCode() { return "get_css_var"; }
    @Override public String getSpec() { return "var(--%s)"; }
    @Override public BlockShape getShape() { return BlockShape.VALUE; }
    @Override public int getColor() { return 0xffee7d16; } // Variable Orange
    @Override public String getCodeTemplate() { return "var(--%s)"; }
    @Override public int getTargetPaletteId() { return 0; }
}
