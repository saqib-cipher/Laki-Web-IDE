package laki.webide.xml;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import a.a.a.Jx;

public class XmlBuilder {

    private static final Set<String> VOID_ELEMENTS = Stream.of(
            "area", "base", "br", "col", "embed", "hr", "img", "input", "link", "meta", "param", "source", "track", "wbr"
    ).collect(Collectors.toSet());

    private final ArrayList<XmlBuilder> childNodes;
    private final boolean d;
    private final String rootElementName;
    private final ArrayList<AttributeBuilder> attributes;
    private String g;
    private int indentationLevel;
    private String nodeValue;
    private String rawChildValue;

    public XmlBuilder(String rootElementName) {
        this(rootElementName, false);
    }

    public XmlBuilder(String rootElementName, boolean z) {
        d = z;
        this.rootElementName = rootElementName;
        indentationLevel = 0;
        attributes = new ArrayList<>();
        childNodes = new ArrayList<>();
    }

    private String addZeroIndent() {
        return addIndent(0);
    }

    private String addIndent(int indentSize) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < indentationLevel + indentSize; i++) {
            str.append("\t");
        }
        return str.toString();
    }

    public void addNamespaceDeclaration(int position, String namespace, String attr, String value) {
        attributes.add(position, new AttributeBuilder(namespace, attr, value));
    }

    public void addChildNode(XmlBuilder xmlBuilder) {
        xmlBuilder.b(indentationLevel + 1);
        childNodes.add(xmlBuilder);
    }

    public void setNodeValue(String value) {
        nodeValue = value;
    }

    public void setRawChildValue(String value) {
        rawChildValue = value;
    }

    public void addAttribute(String namespace, String attr, String value) {
        attributes.add(new AttributeBuilder(namespace, attr, value));
    }

    public void addAttributeValue(String value) {
        attributes.add(new AttributeBuilder(value));
    }

    public String toCode() {
        StringBuilder resultCode = new StringBuilder();
        resultCode.append(addZeroIndent());
        resultCode.append("<");
        resultCode.append(rootElementName);
        for (AttributeBuilder attr : attributes) {
            if (attributes.size() <= 1 || d) {
                resultCode.append(" ");
            } else {
                resultCode.append("\r\n");
                resultCode.append(addIndent(1));
                g = "\r\n" + addIndent(1);
            }
            resultCode.append(attr.toCode());
        }

        boolean isVoid = VOID_ELEMENTS.contains(rootElementName.toLowerCase());
        boolean hasChildren = childNodes.size() > 0;
        boolean hasRawContent = rawChildValue != null && !rawChildValue.isEmpty();
        boolean hasNodeValue = nodeValue != null && !nodeValue.isEmpty();

        if (!hasChildren && !hasRawContent && !hasNodeValue) {
            if (isVoid) {
                resultCode.append(" />");
            } else {
                resultCode.append("></");
                resultCode.append(rootElementName);
                resultCode.append(">");
            }
        } else {
            resultCode.append(">");
            if (hasChildren || hasRawContent) {
                resultCode.append("\r\n");
                for (XmlBuilder xmlBuilder : childNodes) {
                    resultCode.append(xmlBuilder.toCode());
                }
                if (hasRawContent) {
                    resultCode.append(rawChildValue);
                    if (!rawChildValue.endsWith("\n") && !rawChildValue.endsWith("\r")) {
                        resultCode.append("\r\n");
                    }
                }
                resultCode.append(addZeroIndent());
            } else {
                resultCode.append(nodeValue);
            }
            resultCode.append("</");
            resultCode.append(rootElementName);
            resultCode.append(">");
        }
        resultCode.append("\r\n");
        return resultCode.toString();
    }

    public String c() {
        return Jx.WIDGET_NAME_PATTERN.matcher(rootElementName).replaceAll("");
    }

    private void b(int indentSize) {
        indentationLevel = indentSize;
        if (childNodes != null) {
            for (XmlBuilder nx : childNodes) {
                nx.b(indentSize + 1);
            }
        }
    }

    class AttributeBuilder {

        private final String value;
        private String namespace;
        private String attr;

        private AttributeBuilder(String namespace, String attr, String value) {
            this.namespace = namespace;
            this.attr = attr;
            this.value = value;
        }

        private AttributeBuilder(String value) {
            this.value = value;
        }

        private String toCode() {
            if (namespace != null && !namespace.isEmpty()) {
                return namespace + ":" + attr + "=" + "\"" + value + "\"";
            } else if (attr == null || attr.length() <= 0) {
                return g != null ? value.replaceAll("\n", g) : value;
            } else {
                return attr + "=" + "\"" + value + "\"";
            }
        }
    }
}