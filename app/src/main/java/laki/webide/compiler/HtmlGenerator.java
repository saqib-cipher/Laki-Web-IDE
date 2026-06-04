package laki.webide.compiler;

import android.view.Gravity;
import android.view.ViewGroup;
import com.besome.sketch.beans.ViewBean;
import java.util.*;
import laki.webide.xml.XmlBuilder;

public class HtmlGenerator {

    private final ArrayList<ViewBean> allViews;
    private final String fileName;
    private final String headCode;

    public HtmlGenerator(String fileName, ArrayList<ViewBean> views) {
        this(fileName, views, "");
    }

    public HtmlGenerator(String fileName, ArrayList<ViewBean> views, String headCode) {
        this.fileName = fileName;
        this.allViews = views;
        this.headCode = headCode != null ? headCode : "";
    }

    public String generate() {
        allViews.sort(Comparator.comparingInt(bean -> bean.index));

        Set<String> allIds = new HashSet<>();
        for (ViewBean bean : allViews) allIds.add(bean.id);

        List<ViewBean> roots = new ArrayList<>();
        for (ViewBean bean : allViews) {
            if (bean.parent == null || bean.parent.isEmpty() || bean.parent.equals("root") || !allIds.contains(bean.parent)) {
                roots.add(bean);
            }
        }

        ViewBean htmlBean = null;
        ViewBean bodyBean = null;
        for (ViewBean root : roots) {
            String tag = determineTag(root).toLowerCase();
            if (tag.equals("html")) {
                if (htmlBean == null) htmlBean = root;
            } else if (tag.equals("body")) {
                if (bodyBean == null) bodyBean = root;
            }
        }

        List<ViewBean> contentRoots = new ArrayList<>();
        for (ViewBean root : roots) {
            if (root != htmlBean && root != bodyBean) {
                contentRoots.add(root);
            }
        }

        XmlBuilder htmlNode;
        if (htmlBean != null) {
            htmlNode = generateNode(htmlBean, contentRoots);
        } else {
            htmlNode = new XmlBuilder("html");
            XmlBuilder headNode = new XmlBuilder("head");
            StringBuilder headContent = new StringBuilder();
            if (headCode != null && !headCode.trim().isEmpty()) {
                headContent.append(headCode);
            } else {
                headContent.append("    <meta charset=\"UTF-8\" />\n");
                headContent.append("    <title>").append(fileName).append("</title>\n");
            }
            
            // Safe Fix: Always link the corresponding CSS file for this page
            String cssName = fileName.replace(".html", "") + ".css";
            headContent.append("    <link rel=\"stylesheet\" href=\"css/").append(cssName).append("\">\n");
            
            headNode.setRawChildValue(headContent.toString());
            htmlNode.addChildNode(headNode);
            
            XmlBuilder bodyNode;
            if (bodyBean != null) {
                bodyNode = generateNode(bodyBean, contentRoots);
            } else {
                bodyNode = new XmlBuilder("body");
                for (ViewBean content : contentRoots) {
                    bodyNode.addChildNode(generateNode(content, null));
                }
            }
            htmlNode.addChildNode(bodyNode);
        }

        return "<!DOCTYPE html>\n" + htmlNode.toCode();
    }

    private String determineTag(ViewBean bean) {
        String tag = bean.parentAttributes.get("html_tag");
        if (tag != null && !tag.isEmpty()) return tag;

        String className = bean.getClassInfo().getClassName();
        return switch (className) {
            case "LinearLayout", "RelativeLayout", "FrameLayout", "HorizontalScrollView", "ScrollView" -> "div";
            case "TextView" -> "p";
            case "ImageView" -> "img";
            case "Button" -> "button";
            case "EditText" -> "input";
            case "ListView", "GridView" -> "ul";
            case "CheckBox", "Switch", "RadioButton" -> "input";
            default -> "div";
        };
    }

    private XmlBuilder generateNode(ViewBean bean, List<ViewBean> contentToInject) {
        String tag = determineTag(bean).toLowerCase();
        XmlBuilder builder = new XmlBuilder(tag);
        
        builder.addAttribute("", "id", bean.id);
        if (bean.classNames != null && !bean.classNames.isEmpty()) {
            builder.addAttribute("", "class", bean.classNames);
        }
        
        if (bean.parentAttributes != null) {
            for (Map.Entry<String, String> entry : bean.parentAttributes.entrySet()) {
                String key = entry.getKey();
                if (!key.equals("html_tag") && !key.contains("layout_")) {
                    builder.addAttribute("", key, entry.getValue());
                }
            }
        }

        StringBuilder style = new StringBuilder();
        
        // Use a set to track which styles have been written from the Addon
        // to prevent duplication when standard fields are added later.
        Set<String> writtenStyles = new HashSet<>();
        if (bean.convert != null && !bean.convert.isEmpty()) {
            style.append(bean.convert).append(" ");
            for (String part : bean.convert.split(";")) {
                String[] kv = part.split(":");
                if (kv.length >= 1) writtenStyles.add(kv[0].trim().toLowerCase());
            }
        }

        if (!writtenStyles.contains("width")) {
            if (bean.layout.width == ViewGroup.LayoutParams.MATCH_PARENT) style.append("width: 100%; ");
            else if (bean.layout.width == ViewGroup.LayoutParams.WRAP_CONTENT) style.append("width: fit-content; ");
            else if (bean.layout.width > 0) style.append("width: ").append(bean.layout.width).append("px; ");
        }
        
        if (!writtenStyles.contains("height")) {
            if (bean.layout.height == ViewGroup.LayoutParams.MATCH_PARENT) style.append("height: 100%; ");
            else if (bean.layout.height == ViewGroup.LayoutParams.WRAP_CONTENT) style.append("height: fit-content; ");
            else if (bean.layout.height > 0) style.append("height: ").append(bean.layout.height).append("px; ");
        }

        if (!writtenStyles.contains("background-color") && !writtenStyles.contains("background")) {
            if (((bean.layout.backgroundColor >> 24) & 0xFF) != 0) {
                style.append("background-color: ").append(formatColor(bean.layout.backgroundColor)).append("; ");
            }
        }

        if (!writtenStyles.contains("padding")) {
            if (bean.layout.paddingLeft > 0 || bean.layout.paddingTop > 0 || bean.layout.paddingRight > 0 || bean.layout.paddingBottom > 0) {
                style.append(String.format("padding: %dpx %dpx %dpx %dpx; ", 
                    bean.layout.paddingTop, bean.layout.paddingRight, bean.layout.paddingBottom, bean.layout.paddingLeft));
            }
        }
        
        if (!writtenStyles.contains("margin")) {
            if (bean.layout.marginLeft > 0 || bean.layout.marginTop > 0 || bean.layout.marginRight > 0 || bean.layout.marginBottom > 0) {
                style.append(String.format("margin: %dpx %dpx %dpx %dpx; ", 
                    bean.layout.marginTop, bean.layout.marginRight, bean.layout.marginBottom, bean.layout.marginLeft));
            }
        }

        if (!writtenStyles.contains("opacity") && bean.alpha < 1.0f) {
            style.append(String.format(Locale.US, "opacity: %.2f; ", bean.alpha));
        }
        
        if (!writtenStyles.contains("transform") && (bean.translationX != 0 || bean.translationY != 0)) {
            style.append(String.format(Locale.US, "transform: translate(%.1fpx, %.1fpx); ", bean.translationX, bean.translationY));
        }
        
        if (!writtenStyles.contains("background-image")) {
            if (bean.layout.backgroundResource != null && !bean.layout.backgroundResource.isEmpty() && !bean.layout.backgroundResource.equals("NONE")) {
                style.append("background-image: url('assets/images/").append(bean.layout.backgroundResource.toLowerCase()).append(".png'); ");
                if (!writtenStyles.contains("background-size")) style.append("background-size: cover; ");
            }
        }

        String className = bean.getClassInfo().getClassName();
        boolean isLayout = className.contains("Layout") || className.contains("ScrollView");
        
        if (isLayout || bean.layout.gravity != 0) {
            if (!writtenStyles.contains("display")) style.append("display: flex; ");
            if (!writtenStyles.contains("flex-direction")) {
                if (bean.layout.orientation == 1) style.append("flex-direction: column; ");
                else style.append("flex-direction: row; ");
            }
            
            if (bean.layout.gravity != 0) {
                if (bean.layout.orientation == 1) {
                    if (!writtenStyles.contains("justify-content")) style.append("justify-content: ").append(mapGravityToFlex(bean.layout.gravity, true)).append("; ");
                    if (!writtenStyles.contains("align-items")) style.append("align-items: ").append(mapGravityToFlex(bean.layout.gravity, false)).append("; ");
                } else {
                    if (!writtenStyles.contains("justify-content")) style.append("justify-content: ").append(mapGravityToFlex(bean.layout.gravity, false)).append("; ");
                    if (!writtenStyles.contains("align-items")) style.append("align-items: ").append(mapGravityToFlex(bean.layout.gravity, true)).append("; ");
                }
            }
        }

        if (!writtenStyles.contains("align-self") && bean.layout.layoutGravity != 0) {
            style.append("align-self: ").append(mapGravityToFlex(bean.layout.layoutGravity, true)).append("; ");
        }

        if (bean.text != null) {
            if (!writtenStyles.contains("color") && ((bean.text.textColor >> 24) & 0xFF) != 0) {
                style.append("color: ").append(formatColor(bean.text.textColor)).append("; ");
            }
            if (!writtenStyles.contains("font-size") && bean.text.textSize > 0) {
                style.append("font-size: ").append(bean.text.textSize).append("px; ");
            }
            if (!writtenStyles.contains("font-weight") && (bean.text.textType & 1) == 1) style.append("font-weight: bold; ");
            if (!writtenStyles.contains("font-style") && (bean.text.textType & 2) == 2) style.append("font-style: italic; ");
            
            if (!writtenStyles.contains("text-align")) {
                int hGravity = bean.layout.gravity & Gravity.HORIZONTAL_GRAVITY_MASK;
                if (hGravity == Gravity.CENTER_HORIZONTAL) style.append("text-align: center; ");
                else if (hGravity == Gravity.RIGHT) style.append("text-align: right; ");
                else if (hGravity == Gravity.LEFT) style.append("text-align: left; ");
            }
        }

        if (style.length() > 0) {
            builder.addAttribute("", "style", style.toString().trim());
        }

        String text = bean.text.text;
        String hint = bean.text.hint;

        if (tag.equals("input") || tag.equals("textarea")) {
            if (text != null && !text.isEmpty()) {
                if (tag.equals("textarea")) builder.setNodeValue(escapeXML(text));
                else builder.addAttribute("", "value", escapeXML(text));
            }
            if (hint != null && !hint.isEmpty()) builder.addAttribute("", "placeholder", escapeXML(hint));
            
            if (tag.equals("input")) {
                className = bean.getClassInfo().getClassName();
                if (className.equals("CheckBox")) {
                    builder.addAttribute("", "type", "checkbox");
                    if (bean.checked != 0) builder.addAttribute("", "checked", "checked");
                } else if (className.equals("Switch")) {
                    builder.addAttribute("", "type", "checkbox");
                    builder.addAttribute("", "role", "switch");
                    if (bean.checked != 0) builder.addAttribute("", "checked", "checked");
                } else if (className.equals("RadioButton")) {
                    builder.addAttribute("", "type", "radio");
                    if (bean.checked != 0) builder.addAttribute("", "checked", "checked");
                } else {
                    String inputType = switch (bean.text.inputType) {
                        case 129 -> "password";
                        case 3 -> "tel";
                        case 1001 -> "email";
                        case 1002 -> "url";
                        case 1003 -> "date";
                        case 1004 -> "color";
                        case 1005 -> "range";
                        case 1006 -> "hidden";
                        case 1007 -> "file";
                        case 8194 -> "number";
                        default -> "text";
                    };
                    if (!bean.inject.contains("type=") && bean.parentAttributes.get("type") == null) {
                         builder.addAttribute("", "type", inputType);
                    }
                }
            }
        } else if (tag.equals("img")) {
            String resName = bean.image.resName;
            if (resName != null && !resName.isEmpty() && !resName.equals("NONE")) {
                builder.addAttribute("", "src", "assets/images/" + resName.toLowerCase() + ".png");
            } else if (!bean.inject.contains("src=") && bean.parentAttributes.get("src") == null) {
                builder.addAttribute("", "src", "https://via.placeholder.com/150");
            }
        } else if (tag.equals("a")) {
            if (!bean.inject.contains("href=") && bean.parentAttributes.get("href") == null) builder.addAttribute("", "href", "#");
            if (text != null && !text.isEmpty()) builder.setNodeValue(escapeXML(text));
        } else if (tag.equals("button") || tag.equals("li")) {
             if (text != null && !text.isEmpty()) builder.setNodeValue(escapeXML(text));
        } else {
            if (text != null && !text.isEmpty()) builder.setNodeValue(escapeXML(text));
        }

        if (bean.inject != null && !bean.inject.isEmpty()) {
            builder.addAttributeValue(bean.inject);
        }

        for (ViewBean child : allViews) {
            if (bean.id.equals(child.parent)) {
                String childTag = determineTag(child).toLowerCase();
                if (childTag.equals("body") && contentToInject != null) {
                    builder.addChildNode(generateNode(child, contentToInject));
                    contentToInject = null;
                } else {
                    builder.addChildNode(generateNode(child, null));
                }
            }
        }

        if (tag.equals("body") && contentToInject != null) {
            for (ViewBean content : contentToInject) {
                builder.addChildNode(generateNode(content, null));
            }
        }

        return builder;
    }

    private String formatColor(int color) {
        int a = (color >> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        return (a == 0xFF) ? String.format("#%02X%02X%02X", r, g, b) : String.format("rgba(%d, %d, %d, %.2f)", r, g, b, a / 255.0);
    }

    private String mapGravityToFlex(int gravity, boolean isVertical) {
        if (isVertical) {
            int v = gravity & Gravity.VERTICAL_GRAVITY_MASK;
            if (v == Gravity.CENTER_VERTICAL) return "center";
            if (v == Gravity.BOTTOM) return "flex-end";
        } else {
            int h = gravity & Gravity.HORIZONTAL_GRAVITY_MASK;
            if (h == Gravity.CENTER_HORIZONTAL) return "center";
            if (h == Gravity.RIGHT) return "flex-end";
        }
        return "flex-start";
    }

    private String escapeXML(String str) {
        if (str == null) return "";
        StringBuilder result = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            switch (c) {
                case '&' -> result.append("&amp;");
                case '<' -> result.append("&lt;");
                case '>' -> result.append("&gt;");
                case '\"' -> result.append("&quot;");
                case '\'' -> result.append("&apos;");
                default -> result.append(c);
            }
        }
        return result.toString();
    }
}
