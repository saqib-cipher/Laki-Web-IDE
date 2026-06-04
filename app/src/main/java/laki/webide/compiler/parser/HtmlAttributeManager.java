package laki.webide.compiler.parser;

import com.besome.sketch.beans.ViewBean;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Smart module to handle HTML attribute mapping and validation.
 * Centralizes the logic for action, method, href, and placeholders.
 */
public class HtmlAttributeManager {

    private static final Set<String> RECOGNIZED_ATTRS = new HashSet<>(Arrays.asList(
        "id", "class", "style", "type", "placeholder", "value", "href", "target", "src", "alt", 
        "action", "method", "required", "readonly", "pattern", "min", "max", "step", "rows", "cols",
        "multiple", "size", "loading", "title", "controls", "autoplay", "loop", "muted", "role"
    ));

    private final StringBuilder customAttrs = new StringBuilder();

    public void reset() {
        customAttrs.setLength(0);
    }

    /**
     * Applies an HTML attribute to a ViewBean.
     * Recognized attributes map to specific fields. 
     * Unrecognized attributes are collected for the "Custom Attributes" (inject) field.
     */
    public void applyAttribute(ViewBean bean, String name, String value) {
        if (name == null || bean == null) return;
        
        String lowerName = name.toLowerCase();
        
        if (RECOGNIZED_ATTRS.contains(lowerName)) {
            switch (lowerName) {
                case "id" -> bean.id = value;
                case "class" -> bean.classNames = value;
                case "style" -> { /* Style parsing handled in HtmlParser */ }
                case "type" -> handleTypeAttribute(bean, value);
                case "placeholder" -> bean.text.hint = value;
                case "value" -> bean.text.text = value;
                default -> bean.parentAttributes.put(lowerName, value);
            }
        } else {
            // Unknown attribute? Add to the Custom Attributes bucket (inject field)
            if (customAttrs.length() > 0) customAttrs.append(" ");
            if (value == null || value.isEmpty()) {
                customAttrs.append(name); // Boolean attribute like 'required'
            } else {
                customAttrs.append(name).append("=\"").append(value).append("\"");
            }
        }
    }

    public void finalizeAttributes(ViewBean bean) {
        bean.inject = customAttrs.toString();
    }

    private void handleTypeAttribute(ViewBean bean, String value) {
        String tag = bean.parentAttributes.get("html_tag");
        if (tag == null) tag = "";
        
        if (tag.equalsIgnoreCase("input")) {
            bean.text.inputType = switch (value.toLowerCase()) {
                case "password" -> 129;
                case "tel" -> 3;
                case "email" -> 1001;
                case "url" -> 1002;
                case "date" -> 1003;
                case "color" -> 1004;
                case "range" -> 1005;
                case "hidden" -> 1006;
                case "file" -> 1007;
                case "number" -> 8194;
                default -> 1;
            };
        } else {
            bean.parentAttributes.put("type", value);
        }
    }
}
