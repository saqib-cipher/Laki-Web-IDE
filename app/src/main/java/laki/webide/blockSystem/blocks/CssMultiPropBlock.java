package laki.webide.blockSystem.blocks;

import laki.webide.blockSystem.BlockShape;
import laki.webide.blockSystem.core.ModularBlockDefinition;

/**
 * The most flexible modular block. 
 * Allows full control over spec and template for multi-parameter CSS rules.
 */
public class CssMultiPropBlock extends ModularBlockDefinition {
    private final int paletteId;
    private final String opCode;
    private final String spec;
    private final String template;
    private final int color;
    private final BlockShape shape;

    /**
     * Standard constructor (Defaults to COMMAND shape).
     */
    public CssMultiPropBlock(int paletteId, String opCode, String spec, String template, int color) {
        this(paletteId, opCode, spec, template, color, BlockShape.COMMAND);
    }

    /**
     * Advanced constructor with BlockShape support.
     */
    public CssMultiPropBlock(int paletteId, String opCode, String spec, String template, int color, BlockShape shape) {
        this.paletteId = paletteId;
        this.opCode = opCode;
        this.spec = spec;
        this.template = template;
        this.color = color;
        this.shape = shape;
    }

    @Override public String getOpCode() { return "css_multi_" + opCode; }
    @Override public String getSpec() { return spec; }
    @Override public BlockShape getShape() { return shape != null ? shape : BlockShape.COMMAND; }
    @Override public int getColor() { return color; }
    @Override public String getCodeTemplate() { return template; }
    @Override public int getTargetPaletteId() { return paletteId; }
}
