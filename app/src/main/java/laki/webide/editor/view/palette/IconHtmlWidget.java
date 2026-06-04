package laki.webide.editor.view.palette;

import android.content.Context;
import com.besome.sketch.beans.LayoutBean;
import com.besome.sketch.beans.ViewBean;
import com.besome.sketch.editor.view.palette.IconBase;
import laki.webide.R;

public class IconHtmlWidget extends IconBase {
    private final String htmlTag;
    private final int viewType;
    private final int iconRes;

    public IconHtmlWidget(Context context, String tag, int viewType, int iconRes) {
        super(context);
        this.htmlTag = tag;
        this.viewType = viewType;
        this.iconRes = iconRes;
        initialize();
    }

    private void initialize() {
        setWidgetImage(iconRes);
        setWidgetName(htmlTag);
        setName(htmlTag);
    }

    @Override
    public ViewBean getBean() {
        ViewBean viewBean = new ViewBean();
        viewBean.type = viewType;
        LayoutBean layoutBean = viewBean.layout;
        layoutBean.paddingLeft = 8;
        layoutBean.paddingTop = 8;
        layoutBean.paddingRight = 8;
        layoutBean.paddingBottom = 8;
        
        viewBean.parentAttributes.put("html_tag", htmlTag);
        viewBean.convert = ""; // Clear CSS addon initially
        
        return viewBean;
    }
}
