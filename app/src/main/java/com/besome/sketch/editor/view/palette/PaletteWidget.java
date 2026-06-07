package com.besome.sketch.editor.view.palette;

import static laki.webide.utility.SketchwareUtil.dpToPx;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.besome.sketch.lib.ui.CustomScrollView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.color.MaterialColors;

import java.util.HashMap;

import a.a.a.wB;
import laki.webide.R;
import laki.webide.widgets.IconCustomWidget;

public class PaletteWidget extends LinearLayout {

    public MaterialCardView cardView;
    public MaterialCardView btnEditHead;
    private LinearLayout layoutContainer;
    public LinearLayout widgetsContainer;
    private CustomScrollView scrollView;

    public PaletteWidget(Context context) {
        super(context);
        initialize(context);
    }

    public PaletteWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public void addCustomWidgets(View view) {
        layoutContainer.addView(view);
    }

    public View customWidget(HashMap<String, Object> map) {
        String title = map.get("title").toString();
        String name = map.get("name").toString();
        if (map.get("Class").toString().equals("Layouts")) {
            LinearLayout iconBase;
            Context context = getContext();
            iconBase = new IconCustomWidget(map, context);
            layoutContainer.addView(iconBase);
            return iconBase;
        } else {
            IconBase iconBase;
            Context context = getContext();
            iconBase = new IconCustomWidget(map, context);
            iconBase.setText(title);
            iconBase.setName(name);
            if (map.get("Class").toString().equals("AndroidX")) {
                layoutContainer.addView(iconBase);
            } else {
                widgetsContainer.addView(iconBase);
            }
            return iconBase;
        }
    }

    public View a(PaletteWidget.a layoutType, String tag) {
        LinearLayout layout = switch (layoutType) {
            case a -> new IconLinearHorizontal(getContext());
            case b -> new IconLinearVertical(getContext());
            case c -> new IconScrollViewHorizontal(getContext());
            case d -> new IconScrollViewVertical(getContext());
        };

        if (tag != null && !tag.isEmpty()) {
            layout.setTag(tag);
        }

        layoutContainer.addView(layout);
        return layout;
    }

    public View a(PaletteWidget.b widgetType, String tag, String text, String resourceName) {
        IconBase iconBase;
        switch (widgetType) {
            case a -> iconBase = new IconButton(getContext());
            case c -> iconBase = new IconEditText(getContext());
            case b -> iconBase = new IconTextView(getContext());
            case d -> {
                iconBase = new IconImageView(getContext());
                ((IconImageView) iconBase).setResourceName(resourceName);
            }
            case e -> iconBase = new IconListView(getContext());
            case f -> iconBase = new IconSpinner(getContext());
            case g -> iconBase = new IconCheckBox(getContext());
            case h -> iconBase = new IconWebView(getContext());
            case i -> iconBase = new IconSwitch(getContext());
            case j -> iconBase = new IconSeekBar(getContext());
            case k -> iconBase = new IconCalendarView(getContext());
            case l -> iconBase = new IconAdView(getContext());
            case m -> iconBase = new IconProgressBar(getContext());
            case n -> iconBase = new IconMapView(getContext());
            default -> iconBase = new IconBase(getContext());
        }

        if (tag != null && !tag.isEmpty()) {
            iconBase.setTag(tag);
        }

        iconBase.setText(text);
        iconBase.setName(resourceName);
        widgetsContainer.addView(iconBase);
        return iconBase;
    }

    public void removeWidgetLayouts() {
        layoutContainer.removeAllViews();
    }

    private void initialize(Context context) {
        wB.a(context, this, R.layout.palette_widget);
        layoutContainer = findViewById(R.id.layout);
        widgetsContainer = findViewById(R.id.widget);
        scrollView = findViewById(R.id.scv);
        cardView = findViewById(R.id.cardView);
        btnEditHead = findViewById(R.id.btn_edit_head);
    }

    public void removeWidgets() {
        widgetsContainer.removeAllViews();
    }

    public void extraTitle(String title, int targetType) {
        LinearLayout target = targetType == 0 ? layoutContainer : widgetsContainer;

        TextView titleView = new TextView(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4));
        titleView.setLayoutParams(layoutParams);
        titleView.setText(title);
        titleView.setTextSize(12);
        titleView.setTextColor(MaterialColors.getColor(titleView, R.attr.colorPrimary));
        target.addView(titleView);
    }

    public View extraWidget(String tag, String title, String name) {
        IconBase iconBase;
        Context context = getContext();
        iconBase = switch (title) {
            default -> new IconBase(context);
        };
        if (tag != null && !tag.isEmpty()) {
            iconBase.setTag(tag);
        }

        iconBase.setText(title);
        iconBase.setName(name);
        widgetsContainer.addView(iconBase);
        return iconBase;
    }

    public View extraWidgetLayout(String tag, String name) {
        IconBase iconBase;
        Context context = getContext();
        iconBase = switch (name) {
            case "RelativeLayout" -> new IconRelativeLayout(context);
            default -> new IconBase(context);
        };
        if (tag != null && !tag.isEmpty()) {
            iconBase.setTag(tag);
        }

        layoutContainer.addView(iconBase);
        return iconBase;
    }

    public void setLayoutVisible(int visibility) {
        layoutContainer.setVisibility(visibility);
    }

    public void setScrollEnabled(boolean scrollEnabled) {
        if (scrollEnabled) {
            scrollView.b();
        } else {
            scrollView.a();
        }
    }

    public void setWidgetVisible(int visibility) {
        widgetsContainer.setVisibility(visibility);
    }

    public enum a {
        a, //eLinearHorizontal
        b, //eLinearVertical
        c, //eScrollHorizontal
        d //eScrollVertical
    }

    public enum b {
        a, //eButton
        b, //eTextView
        c, //eEditText
        d, //eImageView
        e, //eListView
        f, //eSpinner
        g, //eCheckBox
        h, //eWebView
        i, //eSwitch
        j, //eSeekBar
        k, //eCalenderView
        l, //eAddView
        m, //eProgressBar
        n, //eMapView
        o //eRadioButton
    }
}
