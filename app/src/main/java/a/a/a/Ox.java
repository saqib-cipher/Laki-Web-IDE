package a.a.a;

import static com.besome.sketch.editor.property.PropertyAttributesItem.RELATIVE_IDS;

import android.annotation.SuppressLint;
import android.view.ViewGroup;

import com.besome.sketch.beans.ProjectFileBean;
import com.besome.sketch.beans.ViewBean;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import mod.jbk.util.LogUtil;
import laki.webide.managers.inject.InjectRootLayoutManager;
import laki.webide.xml.XmlBuilder;

@SuppressLint("RtlHardcoded")
public class Ox {

    private static final Set<String> HTML_TAGS = Stream.of(
            "html", "head", "body", "title", "meta", "link", "style", "script",
            "div", "header", "footer", "section", "nav", "aside", "main", "article",
            "p", "span", "h1", "h2", "h3", "h4", "h5", "h6", "a", "img", "br", "hr",
            "ul", "ol", "li", "dl", "dt", "dd",
            "form", "input", "button", "label", "select", "option", "textarea", "fieldset", "legend",
            "table", "thead", "tbody", "tr", "th", "td",
            "iframe", "canvas", "video", "audio", "source", "svg"
    ).collect(Collectors.toSet());

    private final jq buildConfig;
    private final InjectRootLayoutManager rootManager;
    private final ProjectFileBean projectFile;
    private ArrayList<ViewBean> views;
    private XmlBuilder rootLayout = null;

    public Ox(jq jq, ProjectFileBean projectFileBean) {
        buildConfig = jq;
        projectFile = projectFileBean;
        rootManager = new InjectRootLayoutManager(jq.sc_id);
    }

    public static String formatColor(int color) {
        int alpha = (color >> 24) & 0xff;

        if (alpha != 0xff) {
            return String.format("#%08X", color);
        } else {
            return String.format("#%06X", 0xFFFFFF & color);
        }
    }

    /**
     * @return The parameter String escaped properly for XML/HTML strings
     */
    private String escapeXML(String str) {
        CharBuffer buffer = CharBuffer.wrap(str);
        StringBuilder result = new StringBuilder(str.length());
        while (buffer.hasRemaining()) {
            char got = buffer.get();
            switch (got) {
                case '?' -> result.append("\\?");
                case '@' -> result.append("\\@");
                case '\"' -> result.append("&quot;");
                case '&' -> result.append("&amp;");
                case '<' -> result.append("&lt;");
                case '>' -> result.append("&gt;");
                case '\n' -> result.append("\\n");
                default -> result.append(got);
            }
        }
        return result.toString();
    }

    public void setExcludeAppCompat(boolean exclude) {
        // No longer used for HTML generation, but kept for compatibility
    }

    private boolean isHtml(String tag) {
        return HTML_TAGS.contains(tag.toLowerCase());
    }

    private void writeRootLayout() {
        var root = rootManager.getLayoutByFileName(projectFile.getXmlName());
        String className = root.getClassName();
        if (className.equals("LinearLayout") || className.equals("RelativeLayout")) {
            className = "div";
        }
        XmlBuilder nx = new XmlBuilder(className);
        boolean rootIsHtml = isHtml(className);
        var rootAttributes = root.getAttributes();

        for (Map.Entry<String, String> entry : rootAttributes.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("android:")) {
                if (key.equals("android:id")) {
                    nx.addAttribute("", "id", entry.getValue().replace("@+id/", ""));
                }
                continue;
            }
            nx.addAttribute(null, entry.getKey(), entry.getValue());
        }

        for (ViewBean viewBean : views) {
            String parent = viewBean.parent;
            if (parent == null || parent.isEmpty() || parent.equals("root")) {
                writeWidget(nx, viewBean);
            }
        }

        if (rootIsHtml && projectFile.fileType == ProjectFileBean.PROJECT_FILE_TYPE_ACTIVITY && !className.equals("html")) {
            XmlBuilder html = new XmlBuilder("html");
            XmlBuilder head = new XmlBuilder("head");

            XmlBuilder charset = new XmlBuilder("meta");
            charset.addAttribute("", "charset", "UTF-8");
            head.addChildNode(charset);

            XmlBuilder viewport = new XmlBuilder("meta");
            viewport.addAttribute("", "name", "viewport");
            viewport.addAttribute("", "content", "width=device-width, initial-scale=1.0");
            head.addChildNode(viewport);

            XmlBuilder title = new XmlBuilder("title");
            title.setNodeValue(projectFile.fileName);
            head.addChildNode(title);

            html.addChildNode(head);

            XmlBuilder body = new XmlBuilder("body");
            body.addChildNode(nx);
            html.addChildNode(body);
            rootLayout = html;
        } else {
            rootLayout = nx;
        }
    }

    public void a(ArrayList<ViewBean> arrayList) {
        a(arrayList, null);
    }

    public void a(ArrayList<ViewBean> arrayList, ViewBean viewBean) {
        views = arrayList;
        writeRootLayout();
    }

    public String b() {
        String code = rootLayout.toCode();
        if (isHtml(rootLayout.c())) {
            return "<!DOCTYPE html>\n" + code;
        }
        return code;
    }

    private void writeWidget(XmlBuilder nx, ViewBean viewBean) {
        String convert = viewBean.convert;
        String tagName = convert.isEmpty() ? viewBean.getClassInfo().getClassName() : convert.replaceAll(" ", "");
        XmlBuilder widgetTag = new XmlBuilder(tagName);
        boolean isHtml = isHtml(widgetTag.c());

        if (isHtml) {
            widgetTag.addAttribute("", "id", viewBean.id);

            int width = viewBean.layout.width;
            if (width == ViewGroup.LayoutParams.MATCH_PARENT) {
                widgetTag.addAttribute("", "width", "100%");
            } else if (width != ViewGroup.LayoutParams.WRAP_CONTENT) {
                widgetTag.addAttribute("", "width", width + "px");
            }

            int height = viewBean.layout.height;
            if (height == ViewGroup.LayoutParams.MATCH_PARENT) {
                widgetTag.addAttribute("", "height", "100%");
            } else if (height != ViewGroup.LayoutParams.WRAP_CONTENT) {
                widgetTag.addAttribute("", "height", height + "px");
            }

            if (viewBean.getClassInfo().a("TextView")) {
                if (tagName.equals("input")) {
                    String text = viewBean.text.text;
                    if (text != null && !text.isEmpty()) {
                        widgetTag.addAttribute("", "value", escapeXML(text));
                    }
                    String hint = viewBean.text.hint;
                    if (hint != null && !hint.isEmpty()) {
                        widgetTag.addAttribute("", "placeholder", escapeXML(hint));
                    }
                    if (!viewBean.inject.contains("type=")) {
                        widgetTag.addAttribute("", "type", "text");
                    }
                } else {
                    String text = viewBean.text.text;
                    if (text != null && !text.isEmpty()) {
                        widgetTag.setNodeValue(escapeXML(text));
                    }
                }

                if (tagName.equals("a")) {
                    if (!viewBean.inject.contains("href=")) {
                        widgetTag.addAttribute("", "href", "#");
                    }
                }
            }

            if (viewBean.getClassInfo().a("ImageView")) {
                String resName = viewBean.image.resName;
                if (!resName.isEmpty() && !"NONE".equals(resName)) {
                    widgetTag.addAttribute("", "src", "@drawable/" + resName.toLowerCase());
                } else if (!viewBean.inject.contains("src=")) {
                    widgetTag.addAttribute("", "src", "https://via.placeholder.com/150");
                }
                if (!viewBean.inject.contains("alt=")) {
                    widgetTag.addAttribute("", "alt", viewBean.id);
                }
            }

            if (tagName.equals("link")) {
                if (!viewBean.inject.contains("rel=")) {
                    widgetTag.addAttribute("", "rel", "stylesheet");
                }
                if (!viewBean.inject.contains("href=")) {
                    widgetTag.addAttribute("", "href", "style.css");
                }
            }

            if (tagName.equals("script")) {
                if (!viewBean.inject.contains("src=")) {
                    widgetTag.addAttribute("", "src", "script.js");
                }
            }
        }

        if (viewBean.getClassInfo().a("ViewGroup")) {
            for (ViewBean bean : views) {
                if (bean.parent != null && bean.parent.equals(viewBean.id)) {
                    writeWidget(widgetTag, bean);
                }
            }
        }

        if (!viewBean.inject.isEmpty()) {
            widgetTag.addAttributeValue(viewBean.inject.replaceAll(" ", ""));
        }

        if (!viewBean.parentAttributes.isEmpty()) {
            viewBean.parentAttributes.forEach((key, value) -> {
                String[] parts = key.split(":");
                widgetTag.addAttribute(parts[0], parts[1], RELATIVE_IDS.contains(key) ? "@id/" + value : value);
            });
        }

        nx.addChildNode(widgetTag);
    }

    public Set<String> readAttributesToReplace(ViewBean viewBean) {
        Set<String> toReplace = new HashSet<>();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader("<tag xmlns:android=\"http://schemas.android.com/apk/res/android\" " +
                    "xmlns:app=\"http://schemas.android.com/apk/res-auto\" " +
                    "xmlns:tools=\"http://schemas.android.com/tools\"" +
                    viewBean.inject + "></tag>"));

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    for (int i = 0; i < parser.getAttributeCount(); i++) {
                        if ("http://schemas.android.com/tools".equals(parser.getAttributeNamespace(i)) &&
                                "replace".equals(parser.getAttributeName(i))) {
                            toReplace.addAll(Arrays.asList(parser.getAttributeValue(i).split("\\s*,\\s*")));
                        }
                    }
                }

                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException | RuntimeException e) {
            LogUtil.e("a.a.a.Ox", "Failed to parse inject property of View " + viewBean.id, e);
        }

        return toReplace;
    }
}
