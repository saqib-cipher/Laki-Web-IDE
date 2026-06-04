package laki.webide.blockSystem.core;

import laki.webide.blockSystem.blocks.*;

/**
 * Handles the registration of all modular blocks when the editor starts.
 */
public class BlockSystemInitializer {

    private static boolean initialized = false;

    public static void initialize() {
        if (initialized) return;
        
        // Category 0: Variables
        BlockPaletteManager.register(new SetCssVarBlock());
        BlockPaletteManager.register(new GetCssVarBlock());

        // Category 1: CSS Selectors
        BlockPaletteManager.register(new CssClassSelectorBlock());
        BlockPaletteManager.register(new CssIdSelectorBlock());
        
        // Category 2: Layout
        int layCat = 2;
        int layClr = 0xff5cb722; // Green
        BlockPaletteManager.register(new CssPropertyBlock(layCat, "display", "display", layClr));
        BlockPaletteManager.register(new CssPropertyBlock(layCat, "position", "position", layClr));
        BlockPaletteManager.register(new CssPropertyBlock(layCat, "width", layClr));
        BlockPaletteManager.register(new CssPropertyBlock(layCat, "height", layClr));
        BlockPaletteManager.register(new CssPropertyBlock(layCat, "overflow", "overflow", layClr));
        BlockPaletteManager.register(new CssPropertyBlock(layCat, "flexbox", "flexbox", layClr));
        BlockPaletteManager.register(new CssPropertyBlock(layCat, "grid", "grid", layClr));
        BlockPaletteManager.register(new CssPropertyBlock(layCat, "z-index", layClr));
        BlockPaletteManager.register(new CssPropertyBlock(layCat, "visibility", "visibility", layClr));
        // Anchors (Top, Right, Bottom, Left)
        BlockPaletteManager.register(new CssMultiPropBlock(layCat, "anchors", "%m.borderSide : %s", "%s: %s;", layClr));

        // Category 3: Spacing
        int spcCat = 3;
        int spcClr = 0xff5cb722; // Green
        BlockPaletteManager.register(new CssPropertyBlock(spcCat, "margin", spcClr));
        BlockPaletteManager.register(new CssPropertyBlock(spcCat, "padding", spcClr));
        BlockPaletteManager.register(new CssPropertyBlock(spcCat, "gap", spcClr));
        // Side Spacing
        BlockPaletteManager.register(new CssMultiPropBlock(spcCat, "margin_side", "margin- %m.borderSide : %s", "margin-%s: %s;", spcClr));
        BlockPaletteManager.register(new CssMultiPropBlock(spcCat, "padding_side", "padding- %m.borderSide : %s", "padding-%s: %s;", spcClr));

        // Category 4: Text Edit
        int txtCat = 4;
        int txtClr = 0xff7c83db; // Purple/Blue
        BlockPaletteManager.register(new CssPropertyBlock(txtCat, "color", "color", txtClr));
        BlockPaletteManager.register(new CssPropertyBlock(txtCat, "font-size", txtClr));
        BlockPaletteManager.register(new CssPropertyBlock(txtCat, "font-weight", "fontWeight", txtClr));
        BlockPaletteManager.register(new CssPropertyBlock(txtCat, "font-family", txtClr));
        BlockPaletteManager.register(new CssPropertyBlock(txtCat, "letter-spacing", txtClr));
        BlockPaletteManager.register(new CssPropertyBlock(txtCat, "text-align", "textAlign", txtClr));
        BlockPaletteManager.register(new CssPropertyBlock(txtCat, "line-height", txtClr));
        BlockPaletteManager.register(new CssPropertyBlock(txtCat, "text-shadow", txtClr));

        // Category 5: Background
        int bgCat = 5;
        int bgClr = 0xffe1a92a; // Orange/Gold
        BlockPaletteManager.register(new CssPropertyBlock(bgCat, "background-color", "color", bgClr));
        BlockPaletteManager.register(new CssPropertyBlock(bgCat, "background-image", "webImage", bgClr, "background-image: url(\"assets/images/%s\");"));
        BlockPaletteManager.register(new CssPropertyBlock(bgCat, "background", bgClr)); // For manual gradients
        BlockPaletteManager.register(new CssPropertyBlock(bgCat, "background-size", "bgSize", bgClr));
        BlockPaletteManager.register(new CssPropertyBlock(bgCat, "background-position", "bgPos", bgClr));
        BlockPaletteManager.register(new CssPropertyBlock(bgCat, "background-repeat", "bgRepeat", bgClr));
        BlockPaletteManager.register(new CssPropertyBlock(bgCat, "background-clip", "bgClip", bgClr));
        BlockPaletteManager.register(new CssPropertyBlock(bgCat, "background-blend-mode", "bgBlend", bgClr));

        // Category 6: Border & shadow
        int brdCat = 6;
        int brdClr = 0xff4a6cd4; // Indigo
        BlockPaletteManager.register(new CssPropertyBlock(brdCat, "border", brdClr));
        BlockPaletteManager.register(new CssPropertyBlock(brdCat, "border-width", brdClr));
        BlockPaletteManager.register(new CssPropertyBlock(brdCat, "border-style", brdClr));
        BlockPaletteManager.register(new CssPropertyBlock(brdCat, "border-color", "color", brdClr));
        BlockPaletteManager.register(new CssPropertyBlock(brdCat, "border-radius", brdClr));
        BlockPaletteManager.register(new CssPropertyBlock(brdCat, "box-shadow", brdClr));
        // Advanced Border Side Shorthand
        BlockPaletteManager.register(new CssMultiPropBlock(brdCat, "border_side", "border- %m.borderSide : %s %m.borderStyle %m.color", "border-%s: %s %s %s;", brdClr));
        // Border Side Specifics
        BlockPaletteManager.register(new CssMultiPropBlock(brdCat, "border_side_width", "border- %m.borderSide -width: %s", "border-%s-width: %s;", brdClr));
        BlockPaletteManager.register(new CssMultiPropBlock(brdCat, "border_side_style", "border- %m.borderSide -style: %m.borderStyle", "border-%s-style: %s;", brdClr));
        BlockPaletteManager.register(new CssMultiPropBlock(brdCat, "border_side_color", "border- %m.borderSide -color: %m.color", "border-%s-color: %s;", brdClr));
        // Corner Radius
        BlockPaletteManager.register(new CssMultiPropBlock(brdCat, "border_corner_radius", "border- %m.corner -radius: %s", "border-%s-radius: %s;", brdClr));

        // Category 7: Effects
        int effCat = 7;
        int effClr = 0xff2ca5e2; // Blue
        BlockPaletteManager.register(new CssPropertyBlock(effCat, "opacity", effClr));
        BlockPaletteManager.register(new CssMultiPropBlock(effCat, "filter", "filter: %m.filterType ( %s )", "filter: %s(%s);", effClr));
        BlockPaletteManager.register(new CssPropertyBlock(effCat, "cursor", "cursor", effClr));
        BlockPaletteManager.register(new CssMultiPropBlock(effCat, "outline", "outline: %s %m.borderStyle %m.color", "outline: %s %s %s;", effClr));

        // Category 8: Animation
        int aniCat = 8;
        int aniClr = 0xff23b9a9; // Teal
        BlockPaletteManager.register(new CssMultiPropBlock(aniCat, "transition", "transition: %s %s %m.timing", "transition: %s %s %s;", aniClr));
        BlockPaletteManager.register(new CssMultiPropBlock(aniCat, "animation", "animation: %s %s %m.timing %m.animDirection %m.animFill", "animation: %s %s %s %s %s;", aniClr));

        // Category 9: Responsive
        int resCat = 9;
        int resClr = 0xff8a55d7; // Deep Purple
        BlockPaletteManager.register(new CssMultiPropBlock(resCat, "media_query", "@media ( %m.mediaCondition : %s ) {", "@media (%s: %s) {\n%s\n}", resClr, laki.webide.blockSystem.BlockShape.CONTAINER));
        BlockPaletteManager.register(new CssMultiPropBlock(resCat, "media_screen", "@media screen and ( %m.mediaCondition : %s ) {", "@media screen and (%s: %s) {\n%s\n}", resClr, laki.webide.blockSystem.BlockShape.CONTAINER));
        BlockPaletteManager.register(new CssMultiPropBlock(resCat, "media_orientation", "@media ( orientation : %m.mediaOrientation ) {", "@media (orientation: %s) {\n%s\n}", resClr, laki.webide.blockSystem.BlockShape.CONTAINER));
        BlockPaletteManager.register(new CssMultiPropBlock(resCat, "media_dark", "@media ( prefers-color-scheme: dark ) {", "@media (prefers-color-scheme: dark) {\n%s\n}", resClr, laki.webide.blockSystem.BlockShape.CONTAINER));

        initialized = true;
    }
}
