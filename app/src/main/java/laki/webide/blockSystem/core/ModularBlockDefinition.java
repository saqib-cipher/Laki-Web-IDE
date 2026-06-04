package laki.webide.blockSystem.core;

import java.util.ArrayList;
import laki.webide.blockSystem.BlockShape;

/**
 * The base blueprint for all modular blocks.
 * Every separate block file (like IfThen.java) will extend this.
 */
public abstract class ModularBlockDefinition {
    
    public abstract String getOpCode();
    public abstract String getSpec();
    public abstract BlockShape getShape();
    public abstract int getColor();
    public abstract String getCodeTemplate();
    
    /** Default category ID (e.g., 1 for CSS Selectors) */
    public abstract int getTargetPaletteId();

    /** 
     * Returns the type identifier used by the Sketchware engine 
     * (e.g., " " for command, "b" for boolean)
     */
    public String getTypeCode() {
        return switch (getShape()) {
            case BOOLEAN -> "b";
            case VALUE -> "s";
            case CONTAINER -> "c";
            case MULTI_CONTAINER -> "e";
            default -> " ";
        };
    }

    /**
     * Generates the final code using the template and provided parameters/substacks.
     */
    public String generateCode(ArrayList<String> params, String subStack1, String subStack2) {
        String template = getCodeTemplate();
        if (template == null || template.isEmpty()) return "";

        ArrayList<Object> args = new ArrayList<>();
        // Process parameters: Strip quotes and convert Java colors to CSS hex
        for (String p : params) {
            String processed = p;
            if (processed.startsWith("\"") && processed.endsWith("\"") && processed.length() >= 2) {
                processed = processed.substring(1, processed.length() - 1);
            }
            // Web Color Fix: Convert 0xFF... to #... and Color.TRANSPARENT to transparent
            processed = processed.replace("0x", "#")
                                 .replace("Color.TRANSPARENT", "transparent");
            // Handle Alpha (0xFFRRGGBB -> #RRGGBB)
            if (processed.startsWith("#") && processed.length() == 9) {
                processed = "#" + processed.substring(3);
            }
            
            args.add(processed);
        }

        if (getShape() == BlockShape.CONTAINER) {
            args.add(subStack1);
        } else if (getShape() == BlockShape.MULTI_CONTAINER) {
            args.add(subStack1);
            args.add(subStack2);
        }

        try {
            String result = String.format(template, args.toArray());
            // Safe fix: prevent double prefixes (----) if user or system includes dashes redundantly
            return result.replace("----", "--").replace("---", "--");
        } catch (Exception e) {
            return "/* Error generating code for " + getOpCode() + ": " + e.getMessage() + " */";
        }
    }
}
