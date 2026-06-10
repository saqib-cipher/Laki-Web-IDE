package laki.webide.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import laki.webide.R;

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
        addCategoryItem(3, "Spacing", 0xFF8BC34A);
        addCategoryItem(4, "Text Edit", 0xFF673AB7);
        addCategoryItem(5, "Border", 0xFF795548);
        addCategoryItem(6, "Background", 0xFF9C27B0);
        addCategoryItem(7, "Flex", 0xFF00BCD4);
        addCategoryItem(8, "Grid", 0xFF3F51B5);
        addCategoryItem(9, "Animation", 0xFFFFC107);
        addCategoryItem(10, "Responsive", 0xFF009688);
        addCategoryItem(11, "Other", 0xFF607D8B);
    }
    /** CATEGORY_COLORS.put("VARIABLE", 0xFFFF9800);   //1 Orange (Standard Variable color)
     CATEGORY_COLORS.put("SELECTOR", 0xFFE91E63);   //2 Pink
     CATEGORY_COLORS.put("LAYOUT", 0xFF2196F3);     //3 Blue
     CATEGORY_COLORS.put("SPACING", 0xFF8BC34A);    //4 Light Green
     CATEGORY_COLORS.put("TEXT", 0xFF673AB7);       //5 Deep Purple
     CATEGORY_COLORS.put("BORDER", 0xFF795548);     //6 Brown
     CATEGORY_COLORS.put("BACKGROUND", 0xFF9C27B0); //7 Purple
     CATEGORY_COLORS.put("FLEX", 0xFF00BCD4);       //8 Cyan
     CATEGORY_COLORS.put("OTHER", 0xFF607D8B);      //9 Blue Gray
     * */

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
