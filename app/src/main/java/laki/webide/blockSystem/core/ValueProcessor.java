package laki.webide.blockSystem.core;

import java.util.ArrayList;

/**
 * Handles the "Value Passing" layer.
 * Converts interactive inputs into final strings for the Code Generator.
 */
public class ValueProcessor {

    /**
     * Processes a raw template and replaces placeholders with user-entered values.
     * @param template e.g., ".%s { "
     * @param values List of values from the input holes
     * @return Formatted code string
     */
    public String processTemplate(String template, ArrayList<String> values) {
        if (template == null || values == null) return "";
        try {
            return String.format(template, (Object[]) values.toArray(new String[0]));
        } catch (Exception e) {
            return "/* Error processing block values */";
        }
    }
}
