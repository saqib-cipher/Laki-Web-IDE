package com.besome.sketch.beans;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;
import java.util.HashMap;
import a.a.a.Gx;
import a.a.a.nA;
import laki.webide.R;

public class ViewBean extends nA implements Parcelable {
    public static final Parcelable.Creator<ViewBean> CREATOR = new Parcelable.Creator<>() {
        @Override
        public ViewBean createFromParcel(Parcel source) {
            return new ViewBean(source);
        }
        @Override
        public ViewBean[] newArray(int size) {
            return new ViewBean[size];
        }
    };

    // Base View Types used as wrappers
    public static final int VIEW_TYPE_LAYOUT_LINEAR = 0;
    public static final int VIEW_TYPE_LAYOUT_RELATIVE = 1;
    public static final int VIEW_TYPE_LAYOUT_HSCROLLVIEW = 2;
    public static final int VIEW_TYPE_WIDGET_BUTTON = 3;
    public static final int VIEW_TYPE_WIDGET_TEXTVIEW = 4;
    public static final int VIEW_TYPE_WIDGET_EDITTEXT = 5;
    public static final int VIEW_TYPE_WIDGET_IMAGEVIEW = 6;
    public static final int VIEW_TYPE_WIDGET_WEBVIEW = 7;
    public static final int VIEW_TYPE_LAYOUT_VSCROLLVIEW = 12;

    // HTML View Types
    public static final int VIEW_TYPE_HTML_DIV = 101;
    public static final int VIEW_TYPE_HTML_HEADER = 102;
    public static final int VIEW_TYPE_HTML_FOOTER = 103;
    public static final int VIEW_TYPE_HTML_SECTION = 104;
    public static final int VIEW_TYPE_HTML_NAV = 105;
    public static final int VIEW_TYPE_HTML_MAIN = 129;
    public static final int VIEW_TYPE_HTML_P = 106;
    public static final int VIEW_TYPE_HTML_SPAN = 107;
    public static final int VIEW_TYPE_HTML_H1 = 108;
    public static final int VIEW_TYPE_HTML_H2 = 109;
    public static final int VIEW_TYPE_HTML_H3 = 110;
    public static final int VIEW_TYPE_HTML_H4 = 111;
    public static final int VIEW_TYPE_HTML_H5 = 112;
    public static final int VIEW_TYPE_HTML_H6 = 113;
    public static final int VIEW_TYPE_HTML_A = 114;
    public static final int VIEW_TYPE_HTML_IMG = 115;
    public static final int VIEW_TYPE_HTML_UL = 116;
    public static final int VIEW_TYPE_HTML_OL = 117;
    public static final int VIEW_TYPE_HTML_LI = 118;
    public static final int VIEW_TYPE_HTML_BR = 119;
    public static final int VIEW_TYPE_HTML_HR = 120;
    public static final int VIEW_TYPE_HTML_FORM = 121;
    public static final int VIEW_TYPE_HTML_INPUT = 122;
    public static final int VIEW_TYPE_HTML_BUTTON = 123;
    public static final int VIEW_TYPE_HTML_LABEL = 124;
    public static final int VIEW_TYPE_HTML_SELECT = 125;
    public static final int VIEW_TYPE_HTML_OPTION = 126;
    public static final int VIEW_TYPE_HTML_TEXTAREA = 127;
    public static final int VIEW_TYPE_HTML_IFRAME = 128;

    @Expose public float alpha;
    @Expose public int checked;
    public Gx classInfo;
    @Expose public int clickable;
    @Expose public String convert;
    @Expose public String customView;
    @Expose public int enabled;
    @Expose public String id;
    @Expose public String classNames = "";
    @Expose public ImageBean image;
    @Expose public int index;
    @Expose public String inject;
    @Expose public LayoutBean layout;
    public String name;
    @Expose public String parent;
    public Gx parentClassInfo;
    @Expose public int parentType;
    @Expose public String preId;
    @Expose public int preIndex;
    @Expose public String preParent;
    @Expose public int preParentType;
    @Expose public float scaleX;
    @Expose public float scaleY;
    @Expose public TextBean text;
    @Expose public float translationX;
    @Expose public float translationY;
    @Expose public int type;
    @Expose public HashMap<String, String> parentAttributes;
    public boolean isCustomWidget;

    public boolean supportsText() {
        return switch (type) {
            case VIEW_TYPE_WIDGET_TEXTVIEW, VIEW_TYPE_WIDGET_BUTTON, VIEW_TYPE_WIDGET_EDITTEXT,
                 VIEW_TYPE_HTML_P, VIEW_TYPE_HTML_SPAN, VIEW_TYPE_HTML_H1, VIEW_TYPE_HTML_H2,
                 VIEW_TYPE_HTML_H3, VIEW_TYPE_HTML_H4, VIEW_TYPE_HTML_H5, VIEW_TYPE_HTML_H6,
                 VIEW_TYPE_HTML_A, VIEW_TYPE_HTML_BR, VIEW_TYPE_HTML_LABEL, VIEW_TYPE_HTML_OPTION,
                 VIEW_TYPE_HTML_BUTTON -> true;
            default -> false;
        };
    }

    public boolean supportsLayout() {
        return isLayout(type) || type >= 100;
    }

    public static boolean isLayout(int type) {
        return switch (type) {
            case VIEW_TYPE_LAYOUT_LINEAR, VIEW_TYPE_LAYOUT_RELATIVE, VIEW_TYPE_LAYOUT_HSCROLLVIEW,
                 VIEW_TYPE_LAYOUT_VSCROLLVIEW, VIEW_TYPE_HTML_DIV, VIEW_TYPE_HTML_HEADER,
                 VIEW_TYPE_HTML_FOOTER, VIEW_TYPE_HTML_SECTION, VIEW_TYPE_HTML_NAV,
                 VIEW_TYPE_HTML_MAIN, VIEW_TYPE_HTML_UL, VIEW_TYPE_HTML_OL, VIEW_TYPE_HTML_LI,
                 VIEW_TYPE_HTML_HR, VIEW_TYPE_HTML_FORM, VIEW_TYPE_HTML_SELECT,
                 VIEW_TYPE_HTML_IFRAME -> true;
            default -> type >= 100;
        };
    }

    public boolean supportsImage() {
        return type == VIEW_TYPE_WIDGET_IMAGEVIEW || type == VIEW_TYPE_HTML_IMG;
    }

    public boolean supportsInput() {
        return type == VIEW_TYPE_WIDGET_EDITTEXT || type == VIEW_TYPE_HTML_INPUT || type == VIEW_TYPE_HTML_TEXTAREA;
    }

    public ViewBean() {
        parent = null;
        parentType = -1;
        enabled = 1;
        clickable = 1;
        customView = "";
        checked = 0;
        alpha = 1.0f;
        translationX = 0;
        translationY = 0;
        scaleX = 1.0f;
        scaleY = 1.0f;
        layout = new LayoutBean();
        text = new TextBean();
        image = new ImageBean();
        inject = "";
        convert = "";
        parentAttributes = new HashMap<>();
        isCustomWidget = false;
    }

    public ViewBean(Parcel parcel) {
        id = parcel.readString();
        name = parcel.readString();
        type = parcel.readInt();
        parent = parcel.readString();
        parentType = parcel.readInt();
        index = parcel.readInt();
        enabled = parcel.readInt();
        clickable = parcel.readInt();
        customView = parcel.readString();
        checked = parcel.readInt();
        alpha = parcel.readFloat();
        translationX = parcel.readFloat();
        translationY = parcel.readFloat();
        scaleX = parcel.readFloat();
        scaleY = parcel.readFloat();
        preParent = parcel.readString();
        preParentType = parcel.readInt();
        preIndex = parcel.readInt();
        preId = parcel.readString();
        layout = parcel.readParcelable(LayoutBean.class.getClassLoader());
        text = parcel.readParcelable(TextBean.class.getClassLoader());
        image = parcel.readParcelable(ImageBean.class.getClassLoader());
        inject = parcel.readString();
        convert = parcel.readString();
        int size = parcel.readInt();
        parentAttributes = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            String key = parcel.readString();
            String value = parcel.readString();
            parentAttributes.put(key, value);
        }
        classNames = parcel.readString();
        isCustomWidget = parcel.readInt() != 0;
    }

    public ViewBean(String id, int type) {
        this();
        this.id = id;
        name = id;
        this.type = type;
        parent = null;
    }

    public static Parcelable.Creator<ViewBean> getCreator() {
        return CREATOR;
    }

    public static int getViewTypeByTypeName(String typeName) {
        return switch (typeName) {
            case "RelativeLayout" -> VIEW_TYPE_LAYOUT_RELATIVE;
            case "WebView" -> VIEW_TYPE_WIDGET_WEBVIEW;
            case "TextView" -> VIEW_TYPE_WIDGET_TEXTVIEW;
            case "ImageView" -> VIEW_TYPE_WIDGET_IMAGEVIEW;
            case "LinearLayout" -> VIEW_TYPE_LAYOUT_LINEAR;
            case "HScrollView" -> VIEW_TYPE_LAYOUT_HSCROLLVIEW;
            case "EditText" -> VIEW_TYPE_WIDGET_EDITTEXT;
            case "Button" -> VIEW_TYPE_WIDGET_BUTTON;
            case "ScrollView" -> VIEW_TYPE_LAYOUT_VSCROLLVIEW;
            default -> 0;
        };
    }

    public static String getViewTypeName(int type) {
        return switch (type) {
            case VIEW_TYPE_LAYOUT_LINEAR -> "LinearLayout";
            case VIEW_TYPE_LAYOUT_RELATIVE -> "RelativeLayout";
            case VIEW_TYPE_LAYOUT_HSCROLLVIEW -> "HScrollView";
            case VIEW_TYPE_WIDGET_BUTTON -> "Button";
            case VIEW_TYPE_WIDGET_TEXTVIEW -> "TextView";
            case VIEW_TYPE_WIDGET_EDITTEXT -> "EditText";
            case VIEW_TYPE_WIDGET_IMAGEVIEW -> "ImageView";
            case VIEW_TYPE_WIDGET_WEBVIEW -> "WebView";
            case VIEW_TYPE_LAYOUT_VSCROLLVIEW -> "ScrollView";
            case VIEW_TYPE_HTML_DIV -> "div";
            case VIEW_TYPE_HTML_HEADER -> "header";
            case VIEW_TYPE_HTML_FOOTER -> "footer";
            case VIEW_TYPE_HTML_SECTION -> "section";
            case VIEW_TYPE_HTML_NAV -> "nav";
            case VIEW_TYPE_HTML_MAIN -> "main";
            case VIEW_TYPE_HTML_P -> "p";
            case VIEW_TYPE_HTML_SPAN -> "span";
            case VIEW_TYPE_HTML_H1 -> "h1";
            case VIEW_TYPE_HTML_H2 -> "h2";
            case VIEW_TYPE_HTML_H3 -> "h3";
            case VIEW_TYPE_HTML_H4 -> "h4";
            case VIEW_TYPE_HTML_H5 -> "h5";
            case VIEW_TYPE_HTML_H6 -> "h6";
            case VIEW_TYPE_HTML_A -> "a";
            case VIEW_TYPE_HTML_IMG -> "img";
            case VIEW_TYPE_HTML_UL -> "ul";
            case VIEW_TYPE_HTML_OL -> "ol";
            case VIEW_TYPE_HTML_LI -> "li";
            case VIEW_TYPE_HTML_BR -> "br";
            case VIEW_TYPE_HTML_HR -> "hr";
            case VIEW_TYPE_HTML_FORM -> "form";
            case VIEW_TYPE_HTML_INPUT -> "input";
            case VIEW_TYPE_HTML_BUTTON -> "button";
            case VIEW_TYPE_HTML_LABEL -> "label";
            case VIEW_TYPE_HTML_SELECT -> "select";
            case VIEW_TYPE_HTML_OPTION -> "option";
            case VIEW_TYPE_HTML_TEXTAREA -> "textarea";
            case VIEW_TYPE_HTML_IFRAME -> "iframe";
            default -> "";
        };
    }

    public static int getViewTypeResId(int type) {
        return switch (type) {
            case VIEW_TYPE_LAYOUT_LINEAR, VIEW_TYPE_HTML_DIV, VIEW_TYPE_HTML_HEADER, 
                 VIEW_TYPE_HTML_FOOTER, VIEW_TYPE_HTML_SECTION, VIEW_TYPE_HTML_NAV, 
                 VIEW_TYPE_HTML_MAIN, VIEW_TYPE_HTML_UL, VIEW_TYPE_HTML_OL, 
                 VIEW_TYPE_HTML_LI, VIEW_TYPE_HTML_FORM, VIEW_TYPE_HTML_SELECT, 
                 VIEW_TYPE_HTML_IFRAME -> R.drawable.ic_mtrl_view_horizontal;
            case VIEW_TYPE_LAYOUT_RELATIVE -> R.drawable.ic_mtrl_view_relative;
            case VIEW_TYPE_LAYOUT_HSCROLLVIEW -> R.drawable.ic_mtrl_swipe_horizontal;
            case VIEW_TYPE_WIDGET_BUTTON, VIEW_TYPE_HTML_BUTTON -> R.drawable.ic_mtrl_button_click;
            case VIEW_TYPE_WIDGET_TEXTVIEW, VIEW_TYPE_HTML_P, VIEW_TYPE_HTML_SPAN, 
                 VIEW_TYPE_HTML_H1, VIEW_TYPE_HTML_H2, VIEW_TYPE_HTML_H3, 
                 VIEW_TYPE_HTML_H4, VIEW_TYPE_HTML_H5, VIEW_TYPE_HTML_H6, 
                 VIEW_TYPE_HTML_A, VIEW_TYPE_HTML_BR, VIEW_TYPE_HTML_LABEL, 
                 VIEW_TYPE_HTML_OPTION -> R.drawable.ic_mtrl_formattext;
            case VIEW_TYPE_WIDGET_EDITTEXT, VIEW_TYPE_HTML_INPUT, 
                 VIEW_TYPE_HTML_TEXTAREA -> R.drawable.ic_mtrl_edittext;
            case VIEW_TYPE_WIDGET_IMAGEVIEW, VIEW_TYPE_HTML_IMG -> R.drawable.ic_mtrl_image;
            case VIEW_TYPE_WIDGET_WEBVIEW -> R.drawable.ic_mtrl_web;
            case VIEW_TYPE_LAYOUT_VSCROLLVIEW -> R.drawable.ic_mtrl_swap_vertical;
            case VIEW_TYPE_HTML_HR -> R.drawable.ic_mtrl_view_horizontal;
            default -> 0;
        };
    }

    public Gx buildClassInfo(int type) {
        String name = getViewTypeName(type);
        if (name.isEmpty()) name = "View";
        return new Gx(name);
    }

    public void clearClassInfo() {
        classInfo = null;
    }

    @Override
    @NonNull
    public ViewBean clone() {
        ViewBean viewBean = new ViewBean();
        viewBean.copy(this);
        return viewBean;
    }

    public void copy(ViewBean other) {
        id = other.id;
        name = other.name;
        type = other.type;
        parent = other.parent;
        parentType = other.parentType;
        index = other.index;
        enabled = other.enabled;
        clickable = other.clickable;
        customView = other.customView;
        checked = other.checked;
        alpha = other.alpha;
        translationX = other.translationX;
        translationY = other.translationY;
        scaleX = other.scaleX;
        scaleY = other.scaleY;
        preParent = other.preParent;
        preParentType = other.preParentType;
        preIndex = other.preIndex;
        preId = other.preId;
        layout.copy(other.layout);
        text.copy(other.text);
        image.copy(other.image);
        inject = other.inject;
        convert = other.convert;
        classNames = other.classNames;
        parentAttributes = new HashMap<>(other.parentAttributes);
        isCustomWidget = other.isCustomWidget;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Gx getClassInfo() {
        if (classInfo == null) {
            classInfo = buildClassInfo(type);
        }
        return classInfo;
    }

    public Gx getParentClassInfo() {
        if (parentType == -1) {
            return null;
        }
        if (parentClassInfo == null) {
            parentClassInfo = buildClassInfo(parentType);
        }
        return parentClassInfo;
    }

    public boolean isEqual(ViewBean viewBean) {
        if (type != viewBean.type || parentType != viewBean.parentType || index != viewBean.index ||
                enabled != viewBean.enabled || clickable != viewBean.clickable || alpha != viewBean.alpha ||
                translationX != viewBean.translationX || translationY != viewBean.translationY ||
                scaleX != viewBean.scaleX || scaleY != viewBean.scaleY ||
                checked != viewBean.checked || !text.isEqual(viewBean.text) || !layout.isEqual(viewBean.layout) ||
                !image.isEqual(viewBean.image) ||
                !inject.equals(viewBean.inject) || !convert.equals(viewBean.convert) ||
                !parentAttributes.equals(viewBean.parentAttributes)) {
            return false;
        }

        String id = this.id;
        if (id != null) {
            if (!id.equals(viewBean.id)) {
                return false;
            }
        } else if (viewBean.id != null) {
            return false;
        }

        String parent = this.parent;
        if (parent != null) {
            if (!parent.equals(viewBean.parent)) {
                return false;
            }
        } else if (viewBean.parent != null) {
            return false;
        }

        String customView = this.customView;
        if (customView != null) {
            return customView.equals(viewBean.customView);
        } else return viewBean.customView == null;
    }

    public void print() {
        layout.print();
        text.print();
        image.print();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeInt(type);
        dest.writeString(parent);
        dest.writeInt(parentType);
        dest.writeInt(index);
        dest.writeInt(enabled);
        dest.writeInt(clickable);
        dest.writeString(customView);
        dest.writeInt(checked);
        dest.writeFloat(alpha);
        dest.writeFloat(translationX);
        dest.writeFloat(translationY);
        dest.writeFloat(scaleX);
        dest.writeFloat(scaleY);
        dest.writeString(preParent);
        dest.writeInt(preParentType);
        dest.writeInt(preIndex);
        dest.writeString(preId);
        dest.writeParcelable(layout, flags);
        dest.writeParcelable(text, flags);
        dest.writeParcelable(image, flags);
        dest.writeString(inject);
        dest.writeString(convert);
        dest.writeInt(parentAttributes.size());
        for (HashMap.Entry<String, String> entry : parentAttributes.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
        dest.writeString(classNames);
        dest.writeInt(isCustomWidget ? 1 : 0);
    }
}
