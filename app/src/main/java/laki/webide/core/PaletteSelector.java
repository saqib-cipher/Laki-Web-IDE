package laki.webide.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import laki.webide.R;
//import com.besome.sketch.lib.utils.LayoutUtil;

public class PaletteSelector extends LinearLayout implements OnClickListener {
    private Context mContext;
    private OnBlockCategorySelectListener mListener;

    public PaletteSelector(Context context) {
        super(context);
        init(context);
    }

    public PaletteSelector(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    private void addCategory() {
        addCategoryItem(0, "Variable", 0xFFFF9800);
        addCategoryItem(1, "Selector", 0xFFE91E63);
        addCategoryItem(2, "Layout", 0xFF2196F3);
        addCategoryItem(3, "Text", 0xFF4CAF50);
        addCategoryItem(4, "Background", 0xFF9C27B0);
        addCategoryItem(5, "Border", 0xFF795548);
    }

    private void addCategoryItem(int i, String str, int i2) {
        PaletteSelectorItem paletteSelectorItem = new PaletteSelectorItem(this.mContext, i, str, i2);
        paletteSelectorItem.setOnClickListener(this);
        addView(paletteSelectorItem);
        if (i == 0) {
            paletteSelectorItem.setSelected(true);
        }
    }

    private void clearSelection() {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof PaletteSelectorItem) {
                ((PaletteSelectorItem) childAt).setSelected(false);
            }
        }
    }

    private void init(Context context) {
        this.mContext = context;
        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(new LayoutParams(-1, -2));
        int dip = (int) LayoutUtil.getDip(context, 8.0f);
        int dip2 = (int) LayoutUtil.getDip(context, 4.0f);
        setPadding(dip, dip2, dip, dip2);
        addCategory();
    }

    public void onClick(View view) {
        if (view instanceof PaletteSelectorItem) {
            clearSelection();
            PaletteSelectorItem paletteSelectorItem = (PaletteSelectorItem) view;
            paletteSelectorItem.setSelected(true);
            this.mListener.onBlockCategorySelect(paletteSelectorItem.getId(), paletteSelectorItem.getColor());
        }
    }

    public void setOnBlockCategorySelectListener(OnBlockCategorySelectListener onBlockCategorySelectListener) {
        this.mListener = onBlockCategorySelectListener;
    }
}
