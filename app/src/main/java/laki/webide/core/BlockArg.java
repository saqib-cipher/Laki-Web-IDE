package laki.webide.core;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import android.content.DialogInterface;

import laki.webide.R;
import com.besome.sketch.lib.ui.ColorPickerDialog;

public class BlockArg extends BlockBase {
    private Object argValue = "";
    private int defaultArgWidth = 20;
    private boolean isEditable = true;
    private Context mContext;
    private AlertDialog mDlg;
    private String mMenuName = "";
    public String socketType = "";
    private TextView mTextView;
    private int paddingText = 4;

    public BlockArg(Context var1, String var2, int var3, String var4) {
        super(var1, var2, true);
        this.mContext = var1;
        this.mMenuName = var4;
        if (var2.equals("v")) {
            this.socketType = var4;
        }
        this.init(var1);
    }

    private boolean isInteractiveType() {
        String t = this.mType;
        return t.equals("m") || t.equals("d") || t.equals("n") || t.equals("s") || t.equals("v");
    }

    private void finalizeValue(Object value) {
        setArgValue(value);
        if (parentBlock != null) {
            parentBlock.recalcWidthToParent();
            parentBlock.topBlock().fixLayout();
            if (parentBlock.pane != null) {
                parentBlock.pane.calculateWidthHeight();
            }
        }
    }

    private int getLabelWidth() {
        Rect var1 = new Rect();
        String text = this.mTextView.getText().toString();
        if (text.isEmpty() && this.mTextView.getHint() != null) {
            text = this.mTextView.getHint().toString();
        }
        this.mTextView.getPaint().getTextBounds(text, 0, text.length(), var1);
        return var1.width() + this.paddingText;
    }

    private void init(Context var1) {
        byte var3;
        label48: {
            String var2 = this.mType;
            switch(var2.hashCode()) {
                case 98: if(var2.equals("b")) { var3 = 0; break label48; } break;
                case 100: if(var2.equals("d")) { var3 = 1; break label48; } break;
                case 109: if(var2.equals("m")) { var3 = 4; break label48; } break;
                case 118: if(var2.equals("v")) { var3 = 5; break label48; } break;
                case 110: if(var2.equals("n")) { var3 = 2; break label48; } break;
                case 115: if(var2.equals("s")) { var3 = 3; break label48; } break;
            }
            var3 = -1;
        }

        switch(var3) {
            case 0: this.mColor = 1342177280; this.defaultArgWidth = 25; break;
            case 1: this.mColor = -657931; break;
            case 2: this.mColor = -3155748; break;
            case 3: this.mColor = -1; break;
            case 4: this.mColor = 805306368; break;
            case 5: this.mColor = 805306368; break; // Dark Menu color
        }

        this.defaultArgWidth = (int)((float)this.defaultArgWidth * this.dip);
        this.paddingText = (int)((float)this.paddingText * this.dip);
        int finalWidth = this.defaultArgWidth;
        if(isInteractiveType()) {
            this.mTextView = this.makeEditText("");
            this.addView(this.mTextView);
            finalWidth = Math.max(this.defaultArgWidth, this.getLabelWidth());
            this.mTextView.getLayoutParams().width = finalWidth;
        }
        this.setWidthAndTopHeight((float)finalWidth, (float)this.labelAndArgHeight, false);
    }

    private TextView makeEditText(String var1) {
        TextView var2 = new TextView(this.mContext);
        var2.setText(var1);
        var2.setHint(getHintText());
        var2.setHintTextColor(0xB0FFFFFF); // White hint
        var2.setTextSize(9.0F);
        android.widget.RelativeLayout.LayoutParams var3 = new android.widget.RelativeLayout.LayoutParams(this.defaultArgWidth, this.labelAndArgHeight);
        var2.setLayoutParams(var3);
        var2.setBackgroundColor(0);
        var2.setSingleLine();
        var2.setGravity(17);
        if(!this.mType.equals("m") && !this.mType.equals("v")) {
            var2.setTextColor(-268435456);
        } else {
            var2.setTextColor(-1); // White text for menus
        }
        return var2;
    }

    private void showColorPopup() {
        if (!(mContext instanceof Activity)) return;
        int initialColor = 0;
        try {
            String value = argValue.toString();
            if (value.startsWith("#")) initialColor = Color.parseColor(value);
            else if (value.startsWith("0xFF")) initialColor = (int) Long.parseLong(value.substring(2), 16);
        } catch (Exception e) {}

        ColorPickerDialog colorPickerDialog = new ColorPickerDialog((Activity) mContext, initialColor, true, false);
        colorPickerDialog.a(new ColorPickerDialog.b() {
            @Override public void a(int color) { finalizeValue(String.format("#%06X", (0xFFFFFF & color))); colorPickerDialog.dismiss(); }
            @Override public void a(String var1, int var2) { a(var2); }
        });
        colorPickerDialog.showAtLocation(this, 17, 0, 0);
    }

    public Object getArgValue() {
        return !isInteractiveType() ? this.argValue : this.mTextView.getText();
    }

    public void setArgValue(Object var1) {
        this.argValue = var1;
        if(isInteractiveType()) {
            this.mTextView.setText(var1.toString());
            int var2 = Math.max(this.defaultArgWidth, this.getLabelWidth());
            this.mTextView.getLayoutParams().width = var2;
            this.setWidthAndTopHeight((float)var2, (float)this.labelAndArgHeight, true);
        }
    }

    public void setEditable(boolean var1) {
        this.isEditable = var1;
    }

    public void showEditPopup(final boolean isNumber) {
        View v = LayoutUtil.inflate(this.getContext(), R.layout.property_popup_input_text);
        Builder builder = new Builder(this.getContext());
        builder.setView(v);
        builder.setTitle(isNumber ? "Enter Integer Value" : "Enter String Value");
        final EditText ed = (EditText) v.findViewById(R.id.ed_input);
        if(isNumber) { ed.setInputType(4098); ed.setImeOptions(6); ed.setMaxLines(1); }
        else { ed.setInputType(131073); ed.setImeOptions(1); }
        ed.setText(this.mTextView.getText());
        builder.setNegativeButton("Cancel", (dialog, which) -> mDlg.dismiss());
        builder.setPositiveButton("Save", (dialog, which) -> {
            String val = ed.getText().toString();
            if(isNumber) val = Integer.valueOf(val).toString();
            else if(val.length() > 0 && val.charAt(0) == 64) val = " " + val;
            finalizeValue(val);
            mDlg.dismiss();
        });
        builder.setNeutralButton("Code Editor", (dialog, which) -> {
            LakiAsdDialog asd = new LakiAsdDialog((Activity) getContext());
            asd.setContent(mTextView.getText().toString());
            asd.setOnSaveClickListener(this::finalizeValue);
            asd.show();
            mDlg.dismiss();
        });
        this.mDlg = builder.create();
        this.mDlg.show();
    }

    public void showPopup() {
        if (!isEditable) return;
        if(this.mType.equals("d") || this.mType.equals("n")) { showEditPopup(true); return; }
        if(this.mType.equals("s")) { showEditPopup(false); return; }
        if(this.mType.equals("m") || this.mType.equals("v")) {
            if(this.mMenuName.equals("color") || (this.mType.equals("v") && this.socketType.equals("color"))) { showColorPopup(); return; }
            if (this.mMenuName.equals("unit") || (this.mType.equals("v") && this.socketType.equals("unit"))) { showUnitPopup(); return; }
            if (this.mType.equals("v") && (this.socketType.equals("number") || this.socketType.equals("val"))) { showEditPopup(true); return; }
            showSelectPopup();
        }
    }

    public void showSelectPopup() {
        if (!(mContext instanceof Activity)) return;
        String title = CssRegistry.getMenuTitle(mMenuName);
        ArrayList<String> list = CssRegistry.getMenuData(mContext, mMenuName, argValue.toString());
        boolean isImg = mMenuName.equals("image") || mMenuName.equals("webImage");
        new LakiDialogBox((Activity) mContext, title)
            .setList(list)
            .setCurrentValue(this.argValue.toString())
            .setIsImagePicker(isImg)
            .setSelectionListener(this::finalizeValue)
            .show();
    }

    private void showUnitPopup() {
        View v = LayoutUtil.inflate(this.getContext(), R.layout.property_popup_measurement);
        Builder builder = new Builder(this.getContext());
        builder.setView(v);
        builder.setTitle("Enter CSS Value");
        final RadioGroup rg = v.findViewById(R.id.rg_width_height);
        final LinearLayout directInput = v.findViewById(R.id.direct_input);
        final EditText edInput = v.findViewById(R.id.ed_input);
        final TextView tvUnit = v.findViewById(R.id.tv_input_dp);
        String currentVal = mTextView.getText().toString();
        if (!currentVal.isEmpty()) {
            if (currentVal.equals("100%")) ((RadioButton)rg.findViewById(R.id.rb_matchparent)).setChecked(true);
            else if (currentVal.equals("auto")) ((RadioButton)rg.findViewById(R.id.rb_wrapcontent)).setChecked(true);
            else {
                ((RadioButton)rg.findViewById(R.id.rb_directinput)).setChecked(true);
                directInput.setVisibility(View.VISIBLE);
                edInput.setText(currentVal.replaceAll("[^0-9.-]", ""));
                String u = currentVal.replaceAll("[0-9.-]", "");
                tvUnit.setText(u.isEmpty() ? "px" : u);
            }
        }
        rg.setOnCheckedChangeListener((group, checkedId) -> directInput.setVisibility(checkedId == R.id.rb_directinput ? View.VISIBLE : View.GONE));
        tvUnit.setOnClickListener(view -> new LakiDialogBox((Activity)getContext(), "Select Unit").setList(new ArrayList<>(Arrays.asList("px", "%", "em", "rem", "vh", "vw", "auto"))).setSelectionListener(tvUnit::setText).show());
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Save", (dialog, which) -> {
            int id = rg.getCheckedRadioButtonId();
            finalizeValue(id == R.id.rb_matchparent ? "100%" : id == R.id.rb_wrapcontent ? "auto" : edInput.getText().toString() + tvUnit.getText().toString());
        });
        builder.create().show();
    }

    private String getHintText() {
        if (mType.equals("v") && socketType != null && !socketType.isEmpty()) return socketType;
        if (mMenuName == null || mMenuName.isEmpty()) return "";
        if (mMenuName.equals("htmlId")) return "id";
        if (mMenuName.equals("htmlClass") || mMenuName.equals("classname")) return "class";
        return mMenuName;
    }
}
