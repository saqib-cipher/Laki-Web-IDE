package laki.webide;

import android.content.Context;
import android.view.View;

import com.besome.sketch.beans.ViewBean;
import com.besome.sketch.editor.view.ViewEditor;
import com.besome.sketch.editor.view.palette.*;
import laki.webide.editor.view.palette.*;
import laki.webide.widgets.WidgetsCreatorManager;

public class WidgetShowCased {

    public static void setup(ViewEditor viewEditor, WidgetsCreatorManager widgetsCreatorManager) {
        viewEditor.removeWidgetsAndLayouts();
        viewEditor.setPaletteLayoutVisible(View.VISIBLE);

        // 0. HTML Structural
        viewEditor.paletteWidget.extraTitle("HTML Structure", 1);
        addHtmlTag(viewEditor, "div", "linear");
        addHtmlTag(viewEditor, "header", "linear");
        addHtmlTag(viewEditor, "footer", "linear");
        addHtmlTag(viewEditor, "section", "linear");
        addHtmlTag(viewEditor, "nav", "linear");

        // 1. HTML Content
        viewEditor.paletteWidget.extraTitle("HTML Content", 1);
        addHtmlTag(viewEditor, "p", "text");
        addHtmlTag(viewEditor, "span", "text");
        addHtmlTag(viewEditor, "h1", "text");
        addHtmlTag(viewEditor, "h2", "text");
        addHtmlTag(viewEditor, "h3", "text");
        addHtmlTag(viewEditor, "h4", "text");
        addHtmlTag(viewEditor, "h5", "text");
        addHtmlTag(viewEditor, "h6", "text");
        addHtmlTag(viewEditor, "a", "text");
        addHtmlTag(viewEditor, "img", "image");
        addHtmlTag(viewEditor, "ul", "linear");
        addHtmlTag(viewEditor, "ol", "linear");
        addHtmlTag(viewEditor, "li", "linear");
        addHtmlTag(viewEditor, "br", "text");
        addHtmlTag(viewEditor, "hr", "base");

        // 2. HTML Forms
        viewEditor.paletteWidget.extraTitle("HTML Forms", 1);
        addHtmlTag(viewEditor, "form", "linear");
        addHtmlTag(viewEditor, "input", "edit");
        addHtmlTag(viewEditor, "button", "button");
        addHtmlTag(viewEditor, "label", "text");
        addHtmlTag(viewEditor, "select", "linear");
        addHtmlTag(viewEditor, "option", "text");
        addHtmlTag(viewEditor, "textarea", "edit");

        // 3. HTML Advanced
        viewEditor.paletteWidget.extraTitle("HTML Advanced", 1);
        addHtmlTag(viewEditor, "iframe", "linear");

        widgetsCreatorManager.addWidgetsByTitle("HTML");
    }

    private static void addHtmlTag(ViewEditor ve, String tag, String baseType) {
        Context context = ve.getContext();
        IconBase iconBase = switch (baseType) {
            case "linear" -> new IconHtmlLayout(context, tag);
            case "text" -> new IconHtmlText(context, tag);
            case "edit" -> new IconHtmlWidget(context, tag, ViewBean.VIEW_TYPE_WIDGET_EDITTEXT, R.drawable.ic_mtrl_edittext);
            case "image" -> new IconHtmlWidget(context, tag, ViewBean.VIEW_TYPE_WIDGET_IMAGEVIEW, R.drawable.ic_mtrl_image);
            case "button" -> new IconHtmlWidget(context, tag, ViewBean.VIEW_TYPE_WIDGET_BUTTON, R.drawable.ic_mtrl_button_click);
            default -> new IconHtmlWidget(context, tag, ViewBean.VIEW_TYPE_LAYOUT_RELATIVE, R.drawable.ic_mtrl_view_horizontal);
        };
        iconBase.setText(tag);
        iconBase.setClickable(true);
        iconBase.setOnTouchListener(ve);
        ve.paletteWidget.widgetsContainer.addView(iconBase);
    }
}
