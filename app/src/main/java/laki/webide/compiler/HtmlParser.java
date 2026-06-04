package laki.webide.compiler;

import android.content.Context;
import android.util.Xml;
import android.view.Gravity;
import android.view.ViewGroup;
import com.besome.sketch.beans.ViewBean;
import java.io.StringReader;
import java.util.*;
import org.xmlpull.v1.XmlPullParser;
import laki.webide.ProjectWorkspace;
import laki.webide.compiler.parser.CssContextManager;
import laki.webide.compiler.parser.HtmlAttributeManager;
import laki.webide.compiler.parser.HtmlHierarchyManager;
import laki.webide.utility.FileUtil;

/**
 * Smart HTML Parser that coordinates multiple modules for an AI-like IDE experience.
 * Uses a pre-processor to enable XML-based parsing of loose HTML code.
 */
public class HtmlParser {

    private static final Set<String> VOID_ELEMENTS = new HashSet<>(Arrays.asList(
            "area", "base", "br", "col", "embed", "hr", "img", "input", "link", "meta", "param", "source", "track", "wbr"
    ));

    private static final Map<String, Integer> idCounts = new HashMap<>();
    private static final Set<String> usedIds = new HashSet<>();
    private static final HtmlHierarchyManager hierarchyManager = new HtmlHierarchyManager();
    private static final HtmlAttributeManager attributeManager = new HtmlAttributeManager();
    private static final CssContextManager cssManager = new CssContextManager();

    private static final Set<String> RECOGNIZED_STYLES = new HashSet<>(Arrays.asList(
        "flex-direction", "width", "height", "background-color", "padding", "margin", "color", "font-size", 
        "opacity", "transform", "background-image", "justify-content", "align-items", "align-self", "text-align", 
        "font-weight", "font-style"
    ));

    public static ArrayList<ViewBean> parse(Context context, String sc_id, String xmlName) {
        ProjectWorkspace workspace = new ProjectWorkspace(context, sc_id);
        String htmlPath = workspace.projectMyscPath + "/" + xmlName;
        
        if (!FileUtil.isExistFile(htmlPath)) return new ArrayList<>();

        String htmlContent = FileUtil.readFile(htmlPath);
        return parseHtml(htmlContent, sc_id, context);
    }

    public static ArrayList<ViewBean> parseHtml(String html, String sc_id, Context context) {
        idCounts.clear();
        usedIds.clear();
        hierarchyManager.reset();
        
        if (sc_id != null && context != null) {
            ProjectWorkspace workspace = new ProjectWorkspace(context, sc_id);
            cssManager.loadProjectStyles(workspace.projectMyscPath);
        }
        
        // 1. Pre-process to make HTML valid XML (self-close void tags)
        html = preprocessHtml(html);
        
        // 2. Normalize and strip comments/doctype
        html = html.replaceFirst("(?i)<!DOCTYPE[^>]*>", "")
                   .replaceAll("(?s)<!--.*?-->", "")
                   .replace("&nbsp;", " ")
                   .replace("&copy;", "©")
                   .replace("&amp;", "&");
        
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(html));

            int eventType = parser.getEventType();
            
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG -> {
                        String tagName = parser.getName().toLowerCase();
                        if (isIgnoredTag(tagName)) {
                            // Skip metadata tags
                        } else if (tagName.equals("body")) {
                            // Body is the permanent root marker
                        } else {
                            ViewBean bean = createViewBean(parser, tagName);
                            hierarchyManager.pushView(bean);
                            usedIds.add(bean.id);
                            
                            if (VOID_ELEMENTS.contains(tagName)) {
                                hierarchyManager.popCurrent();
                            }
                        }
                    }
                    case XmlPullParser.END_TAG -> {
                        String tagName = parser.getName().toLowerCase();
                        if (!VOID_ELEMENTS.contains(tagName) && !isIgnoredTag(tagName)) {
                            hierarchyManager.popView(tagName);
                        }
                    }
                    case XmlPullParser.TEXT -> {
                        String text = parser.getText();
                        if (text != null && !text.trim().isEmpty()) {
                            text = text.trim();
                            ViewBean currentParent = hierarchyManager.peek();
                            
                            if (isLayoutType(currentParent.type) && !currentParent.id.equals("root")) {
                                ViewBean textBean = createVirtualTextView(text, currentParent);
                                hierarchyManager.addBean(textBean);
                                usedIds.add(textBean.id);
                            } else if (!currentParent.id.equals("root")) {
                                if (currentParent.text.text == null || currentParent.text.text.isEmpty()) {
                                    currentParent.text.text = text;
                                }
                            }
                        }
                    }
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hierarchyManager.getResults();
    }

    private static String preprocessHtml(String html) {
        if (html == null) return "";
        for (String tag : VOID_ELEMENTS) {
            // Regex to find unclosed void tags (even with newlines) and close them
            html = html.replaceAll("(?si)<(" + tag + ")\\b([^>]*?)(?<!/)>", "<$1$2 />");
        }
        return html;
    }

    private static boolean isIgnoredTag(String tag) {
        return tag.equals("html") || tag.equals("head") || tag.equals("meta") || tag.equals("link") || tag.equals("title") || tag.equals("style") || tag.equals("script");
    }

    private static ViewBean createViewBean(XmlPullParser parser, String tag) {
        int type = mapTagToType(tag);
        
        String manualId = null;
        for (int i = 0; i < parser.getAttributeCount(); i++) {
            if (parser.getAttributeName(i).equalsIgnoreCase("id")) {
                manualId = parser.getAttributeValue(i);
                break;
            }
        }

        String uniqueId = (manualId != null && !usedIds.contains(manualId)) ? manualId : generateUniqueId(tag);
        
        ViewBean bean = new ViewBean(uniqueId, type);
        bean.parentAttributes.put("html_tag", tag);
        bean.convert = ""; 
        
        attributeManager.reset();
        for (int i = 0; i < parser.getAttributeCount(); i++) {
            String attrName = parser.getAttributeName(i);
            String attrValue = parser.getAttributeValue(i);
            
            if (attrName.equalsIgnoreCase("style")) {
                applyStyle(bean, attrValue);
            } else {
                attributeManager.applyAttribute(bean, attrName, attrValue);
            }
        }
        attributeManager.finalizeAttributes(bean);

        applyExternalStyles(bean);
        
        return bean;
    }

    private static void applyExternalStyles(ViewBean bean) {
        applyStyleMap(bean, cssManager.getRulesForId(bean.id));
        if (bean.classNames != null && !bean.classNames.isEmpty()) {
            String[] classes = bean.classNames.split("\\s+");
            for (String cls : classes) {
                applyStyleMap(bean, cssManager.getRulesForClass(cls));
            }
        }
    }

    private static void applyStyleMap(ViewBean bean, Map<String, String> rules) {
        if (rules == null || rules.isEmpty()) return;
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : rules.entrySet()) {
            sb.append(entry.getKey()).append(":").append(entry.getValue()).append(";");
        }
        applyStyle(bean, sb.toString());
    }

    private static ViewBean createVirtualTextView(String text, ViewBean parent) {
        String id = generateUniqueId("text_node");
        ViewBean bean = new ViewBean(id, ViewBean.VIEW_TYPE_WIDGET_TEXTVIEW);
        bean.text.text = text;
        bean.parentAttributes.put("html_tag", "span");
        bean.convert = ""; 
        return bean;
    }

    private static String generateUniqueId(String tag) {
        String base = tag.replaceAll("[^a-zA-Z0-9]", "_");
        int count = idCounts.getOrDefault(base, 0) + 1;
        String id = base + "_" + count;
        
        while (usedIds.contains(id)) {
            count++;
            id = base + "_" + count;
        }
        
        idCounts.put(base, count);
        return id;
    }

    private static boolean isLayoutType(int type) {
        return type == ViewBean.VIEW_TYPE_LAYOUT_LINEAR || 
               type == ViewBean.VIEW_TYPE_LAYOUT_RELATIVE || 
               type == ViewBean.VIEW_TYPE_LAYOUT_HSCROLLVIEW || 
               type == ViewBean.VIEW_TYPE_LAYOUT_VSCROLLVIEW;
    }

    private static int mapTagToType(String tag) {
        return switch (tag.toLowerCase()) {
            case "div", "header", "footer", "section", "nav", "form", "body", "iframe", "ul", "ol", "li" -> ViewBean.VIEW_TYPE_LAYOUT_LINEAR;
            case "p", "span", "h1", "h2", "h3", "h4", "h5", "h6", "label", "a", "option", "br" -> ViewBean.VIEW_TYPE_WIDGET_TEXTVIEW;
            case "img" -> ViewBean.VIEW_TYPE_WIDGET_IMAGEVIEW;
            case "button" -> ViewBean.VIEW_TYPE_WIDGET_BUTTON;
            case "input", "textarea" -> ViewBean.VIEW_TYPE_WIDGET_EDITTEXT;
            case "hr" -> ViewBean.VIEW_TYPE_LAYOUT_RELATIVE;
            default -> ViewBean.VIEW_TYPE_LAYOUT_LINEAR;
        };
    }

    private static void applyStyle(ViewBean bean, String style) {
        String[] parts = style.split(";");
        
        Map<String, String> addonMap = new LinkedHashMap<>();
        if (bean.convert != null && !bean.convert.isEmpty()) {
            for (String p : bean.convert.split(";")) {
                String[] kv = p.split(":");
                if (kv.length >= 2) addonMap.put(kv[0].trim().toLowerCase(), kv[1].trim());
            }
        }
        
        for (String part : parts) {
            String[] kv = part.split(":");
            if (kv.length >= 2) {
                String key = kv[0].trim().toLowerCase();
                String value = String.join(":", Arrays.copyOfRange(kv, 1, kv.length)).trim().toLowerCase();
                
                if (RECOGNIZED_STYLES.contains(key)) {
                    switch (key) {
                        case "flex-direction" -> {
                            if (value.equals("column")) bean.layout.orientation = 1;
                            else if (value.equals("row")) bean.layout.orientation = 0;
                        }
                        case "width" -> {
                            if (value.equals("100%")) bean.layout.width = ViewGroup.LayoutParams.MATCH_PARENT;
                            else if (value.endsWith("px")) {
                                try { bean.layout.width = Integer.parseInt(value.replace("px", "")); } catch (Exception ignored) {}
                            }
                        }
                        case "height" -> {
                            if (value.equals("100%")) bean.layout.height = ViewGroup.LayoutParams.MATCH_PARENT;
                            else if (value.endsWith("px")) {
                                try { bean.layout.height = Integer.parseInt(value.replace("px", "")); } catch (Exception ignored) {}
                            }
                        }
                        case "background", "background-color" -> bean.layout.backgroundColor = parseColor(value);
                        case "padding" -> parsePadding(bean, value);
                        case "margin" -> parseMargin(bean, value);
                        case "color" -> bean.text.textColor = parseColor(value);
                        case "font-size" -> {
                            if (value.endsWith("px")) {
                                try { bean.text.textSize = Integer.parseInt(value.replace("px", "")); } catch (Exception ignored) {}
                            }
                        }
                        case "opacity" -> {
                            try { bean.alpha = Float.parseFloat(value); } catch (Exception ignored) {}
                        }
                        case "transform" -> parseTransform(bean, value);
                        case "background-image" -> parseBackgroundImage(bean, value);
                        case "justify-content" -> parseJustifyContent(bean, value);
                        case "align-items" -> parseAlignItems(bean, value);
                        case "align-self" -> parseAlignSelf(bean, value);
                        case "text-align" -> {
                            int g = bean.layout.gravity;
                            if (value.equals("center")) g = (g & ~Gravity.HORIZONTAL_GRAVITY_MASK) | Gravity.CENTER_HORIZONTAL;
                            else if (value.equals("right")) g = (g & ~Gravity.HORIZONTAL_GRAVITY_MASK) | Gravity.RIGHT;
                            else if (value.equals("left")) g = (g & ~Gravity.HORIZONTAL_GRAVITY_MASK) | Gravity.LEFT;
                            bean.layout.gravity = g;
                        }
                        case "font-weight" -> {
                            if (value.equals("bold")) bean.text.textType |= 1;
                        }
                        case "font-style" -> {
                            if (value.equals("italic")) bean.text.textType |= 2;
                        }
                    }
                } else {
                    addonMap.put(key, value);
                }
            }
        }
        
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : addonMap.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("; ");
        }
        bean.convert = sb.toString().trim();
    }

    private static void parseTransform(ViewBean bean, String val) {
        if (val.contains("translate")) {
            try {
                String content = val.substring(val.indexOf("(") + 1, val.indexOf(")"));
                String[] parts = content.split(",");
                if (parts.length >= 1) bean.translationX = Float.parseFloat(parts[0].replace("px", "").trim());
                if (parts.length >= 2) bean.translationY = Float.parseFloat(parts[1].replace("px", "").trim());
            } catch (Exception ignored) {}
        }
    }

    private static void parseBackgroundImage(ViewBean bean, String val) {
        if (val.contains("url")) {
            try {
                String path = val.substring(val.indexOf("(") + 1, val.indexOf(")"));
                path = path.replace("'", "").replace("\"", "");
                if (path.contains("/")) {
                    String name = path.substring(path.lastIndexOf("/") + 1).replace(".png", "");
                    bean.layout.backgroundResource = name.toUpperCase();
                }
            } catch (Exception ignored) {}
        }
    }

    private static void parseJustifyContent(ViewBean bean, String val) {
        int g = bean.layout.gravity;
        if (bean.layout.orientation == 0) {
            switch (val) {
                case "center" -> g = (g & ~Gravity.HORIZONTAL_GRAVITY_MASK) | Gravity.CENTER_HORIZONTAL;
                case "flex-end" -> g = (g & ~Gravity.HORIZONTAL_GRAVITY_MASK) | Gravity.RIGHT;
                case "flex-start" -> g = (g & ~Gravity.HORIZONTAL_GRAVITY_MASK) | Gravity.LEFT;
            }
        } else {
            switch (val) {
                case "center" -> g = (g & ~Gravity.VERTICAL_GRAVITY_MASK) | Gravity.CENTER_VERTICAL;
                case "flex-end" -> g = (g & ~Gravity.VERTICAL_GRAVITY_MASK) | Gravity.BOTTOM;
                case "flex-start" -> g = (g & ~Gravity.VERTICAL_GRAVITY_MASK) | Gravity.TOP;
            }
        }
        bean.layout.gravity = g;
    }

    private static void parseAlignItems(ViewBean bean, String val) {
        int g = bean.layout.gravity;
        if (bean.layout.orientation == 0) {
            switch (val) {
                case "center" -> g = (g & ~Gravity.VERTICAL_GRAVITY_MASK) | Gravity.CENTER_VERTICAL;
                case "flex-end" -> g = (g & ~Gravity.VERTICAL_GRAVITY_MASK) | Gravity.BOTTOM;
                case "flex-start" -> g = (g & ~Gravity.VERTICAL_GRAVITY_MASK) | Gravity.TOP;
            }
        } else {
            switch (val) {
                case "center" -> g = (g & ~Gravity.HORIZONTAL_GRAVITY_MASK) | Gravity.CENTER_HORIZONTAL;
                case "flex-end" -> g = (g & ~Gravity.HORIZONTAL_GRAVITY_MASK) | Gravity.RIGHT;
                case "flex-start" -> g = (g & ~Gravity.HORIZONTAL_GRAVITY_MASK) | Gravity.LEFT;
            }
        }
        bean.layout.gravity = g;
    }

    private static void parseAlignSelf(ViewBean bean, String val) {
        int g = 0;
        switch (val) {
            case "center" -> g = Gravity.CENTER_VERTICAL;
            case "flex-end" -> g = Gravity.BOTTOM;
            case "flex-start" -> g = Gravity.TOP;
        }
        bean.layout.layoutGravity = g;
    }

    private static int parseColor(String color) {
        if (color.startsWith("#")) {
            try {
                if (color.length() == 4) {
                    String r = color.substring(1, 2);
                    String g = color.substring(2, 3);
                    String b = color.substring(3, 4);
                    return (int) Long.parseLong(r+r+g+g+b+b, 16) | 0xFF000000;
                } else if (color.length() == 7) {
                    return (int) Long.parseLong(color.substring(1), 16) | 0xFF000000;
                } else if (color.length() == 9) {
                    return (int) Long.parseLong(color.substring(1), 16);
                }
            } catch (Exception e) { return 0; }
        } else if (color.startsWith("rgba")) {
            try {
                String[] vals = color.substring(5, color.length()-1).split(",");
                int r = Integer.parseInt(vals[0].trim());
                int g = Integer.parseInt(vals[1].trim());
                int b = Integer.parseInt(vals[2].trim());
                int a = (int)(Float.parseFloat(vals[3].trim()) * 255);
                return (a << 24) | (r << 16) | (g << 8) | b;
            } catch (Exception e) { return 0; }
        }
        return 0;
    }

    private static void parsePadding(ViewBean bean, String val) {
        try {
            String[] p = val.replace("px", "").split("\\s+");
            if (p.length == 4) {
                bean.layout.paddingTop = Integer.parseInt(p[0]);
                bean.layout.paddingRight = Integer.parseInt(p[1]);
                bean.layout.paddingBottom = Integer.parseInt(p[2]);
                bean.layout.paddingLeft = Integer.parseInt(p[3]);
            } else if (p.length == 1) {
                int pad = Integer.parseInt(p[0]);
                bean.layout.paddingTop = bean.layout.paddingRight = bean.layout.paddingBottom = bean.layout.paddingLeft = pad;
            }
        } catch (Exception ignored) {}
    }
    
    private static void parseMargin(ViewBean bean, String val) {
        try {
            String[] m = val.replace("px", "").split("\\s+");
            if (m.length == 4) {
                bean.layout.marginTop = Integer.parseInt(m[0]);
                bean.layout.marginRight = Integer.parseInt(m[1]);
                bean.layout.marginBottom = Integer.parseInt(m[2]);
                bean.layout.marginLeft = Integer.parseInt(m[3]);
            } else if (m.length == 1) {
                int mar = Integer.parseInt(m[0]);
                bean.layout.marginTop = bean.layout.marginRight = bean.layout.marginBottom = bean.layout.marginLeft = mar;
            }
        } catch (Exception ignored) {}
    }

    private static int getNextIndexForParent(ArrayList<ViewBean> list, String parentId) {
        int count = 0;
        for (ViewBean b : list) {
            if (b.parent.equals(parentId)) count++;
        }
        return count;
    }
}
