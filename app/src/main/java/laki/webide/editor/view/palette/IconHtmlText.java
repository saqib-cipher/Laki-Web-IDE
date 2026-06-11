package laki.webide.editor.view.palette;

import android.content.Context;
import com.besome.sketch.beans.LayoutBean;
import com.besome.sketch.beans.ViewBean;
import com.besome.sketch.editor.view.palette.IconBase;
import laki.webide.R;

public class IconHtmlText extends IconBase {
    private final String htmlTag;

    private final int type;

    public IconHtmlText(Context context, String tag, int type) {
        super(context);
        this.htmlTag = tag;
        this.type = type;
        initialize();
    }

    private void initialize() {
        setWidgetImage(R.drawable.ic_mtrl_formattext);
        setWidgetName(htmlTag);
        setName(htmlTag);
    }

    @Override
    public ViewBean getBean() {
        ViewBean viewBean = new ViewBean();
        viewBean.type = type;
        LayoutBean layoutBean = viewBean.layout;
        layoutBean.paddingLeft = 8;
        layoutBean.paddingTop = 8;
        layoutBean.paddingRight = 8;
        layoutBean.paddingBottom = 8;
        
        // Initial text matches the tag name, e.g., <p>p</p>
        viewBean.text.text = htmlTag;
        
        // Tells the generator what tag to use
        viewBean.parentAttributes.put("html_tag", htmlTag);
        viewBean.convert = htmlTag;
        
        return viewBean;
    }
}
