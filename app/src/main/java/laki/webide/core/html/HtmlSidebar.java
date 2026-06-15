package laki.webide.core.html;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;

import laki.webide.R;
import laki.webide.core.*;

/**
 * Dedicated sidebar for HTML blocks in the Design Editor.
 * Displays a single vertical list of categories and their blocks.
 */
public class HtmlSidebar extends LinearLayout {

    private LinearLayout container;
    private ScrollView scrollView;
    private OnTouchListener touchListener;
    private float dip;

    public HtmlSidebar(Context context) {
        super(context);
        init(context);
    }

    public HtmlSidebar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        this.dip = LayoutUtil.getDip(context, 1.0f);
        inflate(context, R.layout.html_sidebar, this);
        this.container = findViewById(R.id.container);
        this.scrollView = findViewById(R.id.html_sidebar_scroll);
    }

    public void setScrollEnabled(boolean enabled) {
        if (scrollView != null) {
            scrollView.setOnTouchListener(enabled ? null : (v, event) -> true);
        }
    }

    public void setBlockTouchListener(OnTouchListener listener) {
        this.touchListener = listener;
    }

    public void populate(ArrayList<CreateBlock> blocks) {
        this.container.removeAllViews();
        String currentCategory = "";

        for (CreateBlock cb : blocks) {
            // Add a header if the category changes
            if (!cb.category.equals(currentCategory)) {
                currentCategory = cb.category;
                addHeader(currentCategory);
            }
            addBlock(cb);
        }
    }

    private void addHeader(String title) {
        TextView textView = new TextView(getContext());
        textView.setText(title);
        textView.setTextSize(12);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(0xFF333333);
        textView.setPadding(0, (int) (12 * dip), 0, (int) (4 * dip));
        this.container.addView(textView);
        
        // Add a small divider line under header
        View divider = new View(getContext());
        divider.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (1 * dip)));
        divider.setBackgroundColor(0xFFDDDDDD);
        this.container.addView(divider);
    }

    private void addBlock(CreateBlock cb) {
        // Add vertical spacer like PaletteBlock does
        View spacer = new View(getContext());
        spacer.setLayoutParams(new LayoutParams(-1, (int) (8.0f * this.dip)));
        this.container.addView(spacer);

        // Create the block
        Block block = new Block(getContext(), -1, cb.spec, cb.type, cb.opCode, new Object[0]);
        block.category = cb.category;
        block.outputType = cb.outputType;
        block.setBlockType(Block.BLOCK_TYPE_INPALETTE);
        block.mColor = cb.getColor();
        
        block.setTag(cb.opCode);
        block.setClickable(true);
        if (touchListener != null) {
            block.setOnTouchListener(touchListener);
        }

        this.container.addView(block);
    }
}
