package laki.webide.core;

import java.util.ArrayList;

public class CreateBlock {
    public String type;
    public String spec;
    public String category;
    public String cssTemplate;
    public String opCode;
    public String outputType = "";
    public boolean isButton = false;

    public CreateBlock(String type, String spec, String category, String opCode, String cssTemplate) {
        this.type = type;
        this.spec = spec;
        this.category = category;
        this.opCode = opCode;
        this.cssTemplate = cssTemplate;
    }

    public CreateBlock(String type, String spec, String category, String opCode, String cssTemplate, String outputType) {
        this(type, spec, category, opCode, cssTemplate);
        this.outputType = outputType;
    }
    
    // For palette buttons (Add/Remove variable)
    public CreateBlock(String label, String opCode, boolean isButton) {
        this.spec = label;
        this.opCode = opCode;
        this.isButton = isButton;
    }

    public int getColor() {
        return CategoryRegistry.getColor(this.category);
    }

    public static ArrayList<CreateBlock> getBlocksForCategory(int id) {
        ArrayList<CreateBlock> list = new ArrayList<>();
        switch (id) {
            case 0:
                // VARIABLES
                list.add(new CreateBlock("Add variable", "variableAdd", true));
                list.add(new CreateBlock("Remove variable", "variableRemove", true));
                list.add(new CreateBlock("s", "get variable %m.var", "VARIABLE", "getVar", ""));
                list.add(new CreateBlock(" ", "set variable %m.var to value %s", "VARIABLE", "setVar", ""));
                break;

            case 1:
                // SELECTORS
                list.add(new CreateBlock("c", "class %m.htmlClass", "SELECTOR", ".class", ""));
                list.add(new CreateBlock("c", "ID %m.htmlId", "SELECTOR", "#id", ""));
                list.add(new CreateBlock("c", "tag %m.htmlTag", "SELECTOR", "tag", ""));
                break;

            case 2:
                // LAYOUT
                list.add(new CreateBlock(" ", "set display mode to %m.display", "LAYOUT", "display", ""));
                list.add(new CreateBlock(" ", "set position to %m.position", "LAYOUT", "position", ""));
                list.add(new CreateBlock(" ", "set box sizing to %m.boxSizing", "LAYOUT", "box-sizing", ""));
                list.add(new CreateBlock(" ", "set width size %d.val %m.unit", "LAYOUT", "width", ""));
                list.add(new CreateBlock(" ", "set height size %d.val %m.unit", "LAYOUT", "height", ""));
                list.add(new CreateBlock(" ", "set min %m.dimension size to %d.val %m.unit", "LAYOUT", "minDim", ""));
                list.add(new CreateBlock(" ", "set max %m.dimension size to %d.val %m.unit", "LAYOUT", "maxDim", ""));
                list.add(new CreateBlock(" ", "set overflow mode to %m.overflow", "LAYOUT", "overflow", ""));
                list.add(new CreateBlock(" ", "set z-index value to %d.val", "LAYOUT", "z-index", ""));
                list.add(new CreateBlock(" ", "set visibility mode to %m.visibility", "LAYOUT", "visibility", ""));
                list.add(new CreateBlock(" ", "set opacity value to %d.val", "LAYOUT", "opacity", ""));
                list.add(new CreateBlock(" ", "set anchor %m.side with size %d.val %m.unit", "LAYOUT", "anchor", ""));
                list.add(new CreateBlock(" ", "set cursor type to %m.cursor", "LAYOUT", "cursor", ""));
                list.add(new CreateBlock(" ", "set pointer events to %m.pointerEvents", "LAYOUT", "pointer-events", ""));
                list.add(new CreateBlock(" ", "set user select mode to %m.userSelect", "LAYOUT", "user-select", ""));
                list.add(new CreateBlock(" ", "set object fit mode to %m.objectFit", "LAYOUT", "object-fit", ""));
                break;

            case 3:
                // SPACING
                list.add(new CreateBlock(" ", "set margin size to %d.val %m.unit", "SPACING", "margin", ""));
                list.add(new CreateBlock(" ", "set padding size to %d.val %m.unit", "SPACING", "padding", ""));
                list.add(new CreateBlock(" ", "set gap between items %d.val %m.unit", "SPACING", "gap", ""));
                list.add(new CreateBlock(" ", "set row gap size to %d.val %m.unit", "SPACING", "row-gap", ""));
                list.add(new CreateBlock(" ", "set column gap size to %d.val %m.unit", "SPACING", "column-gap", ""));
                list.add(new CreateBlock(" ", "set margin %m.side size to %d.val %m.unit", "SPACING", "spacingSide", ""));
                list.add(new CreateBlock(" ", "set padding %m.side size to %d.val %m.unit", "SPACING", "spacingSide", ""));
                break;

            case 4:
                // TEXT EDIT
                list.add(new CreateBlock(" ", "set text color to %v.color", "TEXT", "color", ""));
                list.add(new CreateBlock(" ", "set font size to %d.val %m.unit", "TEXT", "font-size", ""));
                list.add(new CreateBlock(" ", "set font weight to %m.fontWeight", "TEXT", "font-weight", ""));
                list.add(new CreateBlock(" ", "set font family to %v.font", "TEXT", "font-family", ""));
                list.add(new CreateBlock(" ", "set font style to %m.fontStyle", "TEXT", "font-style", ""));
                list.add(new CreateBlock(" ", "set text align to %m.textAlign", "TEXT", "text-align", ""));
                list.add(new CreateBlock(" ", "set text transform to %m.textTransform", "TEXT", "text-transform", ""));
                list.add(new CreateBlock(" ", "set text decoration to %m.textDecoration", "TEXT", "text-decoration", ""));
                list.add(new CreateBlock(" ", "set line height to %d.val %m.unit", "TEXT", "line-height", ""));
                list.add(new CreateBlock(" ", "set letter spacing to %d.val %m.unit", "TEXT", "letter-spacing", ""));
                list.add(new CreateBlock(" ", "set white space mode to %m.whiteSpace", "TEXT", "white-space", ""));
                list.add(new CreateBlock(" ", "set text overflow mode to %m.textOverflow", "TEXT", "text-overflow", ""));
                list.add(new CreateBlock(" ", "set word break mode to %m.wordBreak", "TEXT", "word-break", ""));
                list.add(new CreateBlock(" ", "set vertical align to %m.verticalAlign", "TEXT", "vertical-align", ""));
                list.add(new CreateBlock(" ", "set writing mode to %m.writingMode", "TEXT", "writing-mode", ""));
                list.add(new CreateBlock(" ", "set hyphens mode to %m.hyphens", "TEXT", "hyphens", ""));
                list.add(new CreateBlock(" ", "set text shadow with value %s.val", "TEXT", "text-shadow", ""));
                break;

            case 5:
                // BORDER
                list.add(new CreateBlock(" ", "set border width to %d.val %m.unit", "BORDER", "border-width", ""));
                list.add(new CreateBlock(" ", "set border style to %m.borderStyle", "BORDER", "border-style", ""));
                list.add(new CreateBlock(" ", "set border color to %v.color", "BORDER", "border-color", ""));
                list.add(new CreateBlock(" ", "set border radius to %d.val %m.unit", "BORDER", "border-radius", ""));
                list.add(new CreateBlock(" ", "set border %m.side width to %d.val %m.unit", "BORDER", "border-side-width", ""));
                list.add(new CreateBlock(" ", "set border %m.side color to %v.color", "BORDER", "border-side-color", ""));
                list.add(new CreateBlock(" ", "set border %m.side style to %m.borderStyle", "BORDER", "border-side-style", ""));
                list.add(new CreateBlock(" ", "set outline width %d.val %m.unit style %m.borderStyle and color %v.color", "BORDER", "outline", ""));
                break;

            case 6:
                // BACKGROUND
                list.add(new CreateBlock(" ", "set background color to %v.color", "BACKGROUND", "background-color", ""));
                list.add(new CreateBlock(" ", "set background image from %v.image", "BACKGROUND", "background-image", ""));
                list.add(new CreateBlock(" ", "set background size to %m.bgSize", "BACKGROUND", "background-size", ""));
                list.add(new CreateBlock(" ", "set background repeat to %m.bgRepeat", "BACKGROUND", "background-repeat", ""));
                list.add(new CreateBlock(" ", "set background attachment to %m.bgAttachment", "BACKGROUND", "background-attachment", ""));
                list.add(new CreateBlock(" ", "set background origin to %m.bgOrigin", "BACKGROUND", "background-origin", ""));
                list.add(new CreateBlock(" ", "set background clip mode to %m.bgClip", "BACKGROUND", "background-clip", ""));
                list.add(new CreateBlock(" ", "set background blend mode to %m.blendMode", "BACKGROUND", "background-blend-mode", ""));
                list.add(new CreateBlock(" ", "set background position to %s.pos", "BACKGROUND", "background-position", ""));
                break;

            case 7:
                // FLEX
                list.add(new CreateBlock(" ", "set flex direction to %m.direction", "LAYOUT", "flex-direction", ""));
                list.add(new CreateBlock(" ", "set justify content to %m.justify", "LAYOUT", "justify-content", ""));
                list.add(new CreateBlock(" ", "set align items to %m.align", "LAYOUT", "align-items", ""));
                list.add(new CreateBlock(" ", "set flex wrap mode to %m.wrap", "LAYOUT", "flex-wrap", ""));
                list.add(new CreateBlock(" ", "set flex grow factor to %d.val", "LAYOUT", "flex-grow", ""));
                list.add(new CreateBlock(" ", "set flex shrink factor to %d.val", "LAYOUT", "flex-shrink", ""));
                list.add(new CreateBlock(" ", "set flex basis size to %d.val %m.unit", "LAYOUT", "flex-basis", ""));
                list.add(new CreateBlock(" ", "set align self to %m.align", "LAYOUT", "align-self", ""));
                break;

            case 8:
                // OTHER
                list.add(new CreateBlock("s", "linear gradient from %m.color and %m.color", "OTHER", "linearGradient", "", "image"));
                list.add(new CreateBlock("s", "radial gradient from %m.color and %m.color", "OTHER", "radialGradient", "", "image"));
                list.add(new CreateBlock("s", "get image from url %s.url", "OTHER", "url", "", "image"));
                list.add(new CreateBlock("s", "calculate math %s.expression", "OTHER", "calc", "", "unit"));
                list.add(new CreateBlock("s", "value %d.val unit %m.unit", "OTHER", "unitValue", "", "unit"));
                list.add(new CreateBlock("s", "get variable value %m.var", "OTHER", "var", ""));
                list.add(new CreateBlock(" ", "apply transform effects %v.transform", "OTHER", "transform", ""));
                list.add(new CreateBlock(" ", "apply filter effects %v.filter", "OTHER", "filter", ""));
                list.add(new CreateBlock(" ", "set transition timing to %s.val", "OTHER", "transition", ""));
                list.add(new CreateBlock(" ", "Add Source Directly %s", "OTHER", "asd", ""));
                break;
        }
        return list;
    }

    public static String getEventSpec(String eventName, String id, String filename) {
        if (eventName.equals("initializeLogic")) {
            return "When initializeLogic";
        }
            return "When " + id + " " + eventName;
    }
}
