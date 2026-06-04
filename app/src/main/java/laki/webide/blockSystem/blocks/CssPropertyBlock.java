package laki.webide.blockSystem.blocks;

import laki.webide.blockSystem.BlockShape;
import laki.webide.blockSystem.core.ModularBlockDefinition;

/**
 * A reusable modular block definition for standard CSS properties.
 * Supports both manual text input and dropdown menus.
 */
public class CssPropertyBlock extends ModularBlockDefinition {
    private final String property;
    private final String menuName;
    private final int paletteId;
    private final int color;
    private final String customTemplate;

    /**
     * Constructor for blocks with a manual text input hole (%s).
     */
    public CssPropertyBlock(int paletteId, String property, int color) {
        this(paletteId, property, null, color, null);
    }

    /**
     * Constructor for blocks with a dropdown menu hole (%m).
     */
    public CssPropertyBlock(int paletteId, String property, String menuName, int color) {
        this(paletteId, property, menuName, color, null);
    }

    /**
     * Constructor with a custom code template.
     */
    public CssPropertyBlock(int paletteId, String property, String menuName, int color, String template) {
        this.paletteId = paletteId;
        this.property = property;
        this.menuName = menuName;
        this.color = color;
        this.customTemplate = template;
    }

    @Override
    public String getOpCode() {
        return "set_css_" + property.replace("-", "_");
    }

    @Override
    public String getSpec() {
        return property + ": " + (menuName != null ? "%m." + menuName : "%s");
    }

    @Override
    public BlockShape getShape() {
        return BlockShape.COMMAND;
    }

    @Override
    public int getColor() {
        return this.color;
    }

    @Override
    public String getCodeTemplate() {
        return customTemplate != null ? customTemplate : property + ": %s;";
    }

    @Override
    public int getTargetPaletteId() {
        return this.paletteId;
    }
}
