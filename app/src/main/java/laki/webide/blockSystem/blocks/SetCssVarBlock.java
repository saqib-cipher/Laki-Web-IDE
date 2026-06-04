package laki.webide.blockSystem.blocks;

import laki.webide.blockSystem.BlockShape;
import laki.webide.blockSystem.core.ModularBlockDefinition;

public class SetCssVarBlock extends ModularBlockDefinition {
    @Override public String getOpCode() { return "set_css_var"; }
    @Override public String getSpec() { return "set --%s to %s"; }
    @Override public BlockShape getShape() { return BlockShape.COMMAND; }
    @Override public int getColor() { return 0xffee7d16; } // Variable Orange
    @Override public String getCodeTemplate() { return "--%s: %s;"; }
    @Override public int getTargetPaletteId() { return 0; }
}
