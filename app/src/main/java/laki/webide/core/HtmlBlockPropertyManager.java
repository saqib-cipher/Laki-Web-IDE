package laki.webide.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.besome.sketch.editor.property.*;

import java.util.HashMap;

import a.a.a.Kw;

public class HtmlBlockPropertyManager extends LinearLayout implements Kw {

    private Block currentBlock;
    private final HashMap<String, View> propertyViews = new HashMap<>();
    private OnPropertyValueChangedListener listener;
    private String sc_id;

    public interface OnPropertyValueChangedListener {
        void onPropertyValueChanged(Block block);
    }

    public HtmlBlockPropertyManager(Context context) {
        super(context);
        init();
    }

    public HtmlBlockPropertyManager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
    }

    public void setOnPropertyValueChangedListener(OnPropertyValueChangedListener listener) {
        this.listener = listener;
    }

    public void setProjectId(String sc_id) {
        this.sc_id = sc_id;
    }

    public void bindBlock(Block block) {
        this.currentBlock = block;
        removeAllViews();
        propertyViews.clear();

        if (block == null) return;

        // Common Attributes for all HTML blocks
        addInputItem("html_attr_id", block.attributes.getOrDefault("id", ""));
        addInputItem("html_attr_class", block.attributes.getOrDefault("class", ""));

        // Specialized attributes based on tag (opCode)
        String tag = block.mOpCode;
        if (tag.startsWith("html_")) {
            tag = tag.replace("html_", "");
        }

        switch (tag) {
            case "img":
                addInputItem("html_attr_src", block.attributes.getOrDefault("src", ""));
                addInputItem("html_attr_alt", block.attributes.getOrDefault("alt", ""));
                break;
            case "a":
                addInputItem("html_attr_href", block.attributes.getOrDefault("href", ""));
                addInputItem("html_attr_target", block.attributes.getOrDefault("target", ""));
                break;
            case "input":
                addInputItem("html_attr_type", block.attributes.getOrDefault("type", ""));
                addInputItem("html_attr_placeholder", block.attributes.getOrDefault("placeholder", ""));
                break;
        }

        // CSS Styling section (Simplified)
        addColorItem("html_css_color", block.attributes.getOrDefault("color", ""));
        addColorItem("html_css_background-color", block.attributes.getOrDefault("background-color", ""));
    }

    private void addHeader(String title) {
        PropertySubheader subheader = new PropertySubheader(getContext());
        subheader.setHeaderName(title);
        addView(subheader);
    }

    private void addInputItem(String key, String value) {
        PropertyInputItem item = new PropertyInputItem(getContext(), false);
        item.setOrientationItem(0);
        item.setWeb(true);
        item.setKey(key);
        item.setValue(value);
        item.setOnPropertyValueChangeListener(this);
        propertyViews.put(key, item);
        addView(item);
    }

    private void addColorItem(String key, String value) {
        PropertyColorItem item = new PropertyColorItem(getContext(), false, sc_id);
        item.setOrientationItem(0);
        item.setWeb(true);
        item.setKey(key);
        try {
            if (value != null && !value.isEmpty()) {
                if (value.startsWith("#")) {
                    item.setValue((int) Long.parseLong(value.replace("#", ""), 16));
                } else {
                    item.setValue(Integer.parseInt(value));
                }
            }
        } catch (Exception ignored) {}
        item.setOnPropertyValueChangeListener(this);
        propertyViews.put(key, item);
        addView(item);
    }

    @Override
    public void a(String key, Object value) {
        if (currentBlock == null) return;

        String attrName = key;
        if (key.startsWith("html_attr_")) {
            attrName = key.replace("html_attr_", "");
        } else if (key.startsWith("html_css_")) {
            attrName = key.replace("html_css_", "");
            if (value instanceof Integer) {
                value = String.format("#%06X", (0xFFFFFF & (Integer) value));
            }
        }

        currentBlock.attributes.put(attrName, String.valueOf(value));

        if (listener != null) {
            listener.onPropertyValueChanged(currentBlock);
        }
    }
}
