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
        addHtmlTag(viewEditor, "div", "linear", ViewBean.VIEW_TYPE_HTML_DIV);
        addHtmlTag(viewEditor, "header", "linear", ViewBean.VIEW_TYPE_HTML_HEADER);
        addHtmlTag(viewEditor, "footer", "linear", ViewBean.VIEW_TYPE_HTML_FOOTER);
        addHtmlTag(viewEditor, "section", "linear", ViewBean.VIEW_TYPE_HTML_SECTION);
        addHtmlTag(viewEditor, "nav", "linear", ViewBean.VIEW_TYPE_HTML_NAV);
        addHtmlTag(viewEditor, "main", "linear", ViewBean.VIEW_TYPE_HTML_MAIN);

        // 1. HTML Content
        viewEditor.paletteWidget.extraTitle("HTML Content", 1);
        addHtmlTag(viewEditor, "p", "text", ViewBean.VIEW_TYPE_HTML_P);
        addHtmlTag(viewEditor, "span", "text", ViewBean.VIEW_TYPE_HTML_SPAN);
        addHtmlTag(viewEditor, "h1", "text", ViewBean.VIEW_TYPE_HTML_H1);
        addHtmlTag(viewEditor, "h2", "text", ViewBean.VIEW_TYPE_HTML_H2);
        addHtmlTag(viewEditor, "h3", "text", ViewBean.VIEW_TYPE_HTML_H3);
        addHtmlTag(viewEditor, "h4", "text", ViewBean.VIEW_TYPE_HTML_H4);
        addHtmlTag(viewEditor, "h5", "text", ViewBean.VIEW_TYPE_HTML_H5);
        addHtmlTag(viewEditor, "h6", "text", ViewBean.VIEW_TYPE_HTML_H6);
        addHtmlTag(viewEditor, "a", "text", ViewBean.VIEW_TYPE_HTML_A);
        addHtmlTag(viewEditor, "img", "image", ViewBean.VIEW_TYPE_HTML_IMG);
        addHtmlTag(viewEditor, "ul", "linear", ViewBean.VIEW_TYPE_HTML_UL);
        addHtmlTag(viewEditor, "ol", "linear", ViewBean.VIEW_TYPE_HTML_OL);
        addHtmlTag(viewEditor, "li", "linear", ViewBean.VIEW_TYPE_HTML_LI);
        addHtmlTag(viewEditor, "br", "text", ViewBean.VIEW_TYPE_HTML_BR);
        addHtmlTag(viewEditor, "hr", "base", ViewBean.VIEW_TYPE_HTML_HR);

        // 2. HTML Forms
        viewEditor.paletteWidget.extraTitle("HTML Forms", 1);
        addHtmlTag(viewEditor, "form", "linear", ViewBean.VIEW_TYPE_HTML_FORM);
        addHtmlTag(viewEditor, "input", "edit", ViewBean.VIEW_TYPE_HTML_INPUT);
        addHtmlTag(viewEditor, "button", "button", ViewBean.VIEW_TYPE_HTML_BUTTON);
        addHtmlTag(viewEditor, "label", "text", ViewBean.VIEW_TYPE_HTML_LABEL);
        addHtmlTag(viewEditor, "select", "linear", ViewBean.VIEW_TYPE_HTML_SELECT);
        addHtmlTag(viewEditor, "option", "text", ViewBean.VIEW_TYPE_HTML_OPTION);
        addHtmlTag(viewEditor, "textarea", "edit", ViewBean.VIEW_TYPE_HTML_TEXTAREA);

        // 3. HTML Advanced
        viewEditor.paletteWidget.extraTitle("HTML Advanced", 1);
        addHtmlTag(viewEditor, "iframe", "linear", ViewBean.VIEW_TYPE_HTML_IFRAME);

        widgetsCreatorManager.addWidgetsByTitle("HTML");
    }

    private static void addHtmlTag(ViewEditor ve, String tag, String baseType, int id) {
        Context context = ve.getContext();
        IconBase iconBase = switch (baseType) {
            case "linear" -> new IconHtmlLayout(context, tag, id);
            case "text" -> new IconHtmlText(context, tag, id);
            case "edit" -> new IconHtmlWidget(context, tag, id, R.drawable.ic_mtrl_edittext);
            case "image" -> new IconHtmlWidget(context, tag, id, R.drawable.ic_mtrl_image);
            case "button" -> new IconHtmlWidget(context, tag, id, R.drawable.ic_mtrl_button_click);
            default -> new IconHtmlWidget(context, tag, id, R.drawable.ic_mtrl_view_horizontal);
        };
        iconBase.setText(tag);
        iconBase.setClickable(true);
        iconBase.setOnTouchListener(ve);
        ve.paletteWidget.widgetsContainer.addView(iconBase);
    }
}
