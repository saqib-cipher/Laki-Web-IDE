package laki.webide.core;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

import laki.webide.R;
import com.bumptech.glide.Glide;
import java.io.File;

public class LakiDialogBox {
    private final Activity activity;
    private final String title;
    private ArrayList<String> list = new ArrayList<>();
    private OnValueSelectedListener listener;
    private String currentValue = "";
    private boolean manualEntryEnabled = true;
    private boolean isImagePicker = false;
    private String scId = "";

    public interface OnValueSelectedListener {
        void onValueSelected(String value);
    }

    public LakiDialogBox(Activity activity, String title) {
        this.activity = activity;
        this.title = title;
        if (activity instanceof LogicEditorActivity) {
            this.scId = ((LogicEditorActivity) activity).scId;
        }
    }

    public LakiDialogBox setIsImagePicker(boolean isImagePicker) {
        this.isImagePicker = isImagePicker;
        return this;
    }

    public LakiDialogBox setList(ArrayList<String> list) {
        this.list = list;
        return this;
    }

    public LakiDialogBox setSelectionListener(OnValueSelectedListener listener) {
        this.listener = listener;
        return this;
    }

    public LakiDialogBox setCurrentValue(String value) {
        this.currentValue = value;
        return this;
    }

    public LakiDialogBox setManualEntryEnabled(boolean enabled) {
        this.manualEntryEnabled = enabled;
        return this;
    }

    public void show() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);
        builder.setTitle(title);

        View customView = LayoutInflater.from(activity).inflate(R.layout.property_popup_selector_single, null);
        final RadioGroup rg = customView.findViewById(R.id.rg_content);
        final EditText searchInput = new EditText(activity);
        
        // Add search bar if list is long
        if (list.size() > 5) {
            searchInput.setHint("Search...");
            searchInput.setTextSize(14f);
            int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, activity.getResources().getDisplayMetrics());
            searchInput.setPadding(padding, padding / 2, padding, padding / 2);
            ((ViewGroup) rg.getParent()).addView(searchInput, 0);
        }

        for (String item : list) {
            if (isImagePicker) {
                View itemView = createImageItem(item, rg);
                rg.addView(itemView);
            } else {
                RadioButton rb = createItem(item);
                rg.addView(rb);
                if (item.equals(currentValue)) {
                    rb.setChecked(true);
                }
            }
        }

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().toLowerCase();
                for (int i = 0; i < rg.getChildCount(); i++) {
                    View child = rg.getChildAt(i);
                    String text = "";
                    if (child instanceof RadioButton) {
                        text = ((RadioButton) child).getText().toString();
                    } else {
                        TextView tv = child.findViewById(R.id.text_view);
                        if (tv != null) text = tv.getText().toString();
                    }
                    child.setVisibility(text.toLowerCase().contains(query) ? View.VISIBLE : View.GONE);
                }
            }
        });

        builder.setView(customView);
        builder.setPositiveButton("Select", (dialog, which) -> {
            int checkedId = rg.getCheckedRadioButtonId();
            if (checkedId != -1) {
                RadioButton rb = rg.findViewById(checkedId);
                String result = "";
                if (isImagePicker) {
                    // For image picker, the tag of the RadioButton stores the filename
                    result = rb.getTag().toString();
                } else {
                    result = rb.getText().toString();
                }
                if (listener != null) listener.onValueSelected(result);
            }
        });

        builder.setNegativeButton("Cancel", null);

        if (manualEntryEnabled) {
            builder.setNeutralButton("Code Editor", (dialog, which) -> {
                LakiAsdDialog asd = new LakiAsdDialog(activity);
                asd.setContent(currentValue);
                asd.setOnSaveClickListener(value -> {
                    if (listener != null) listener.onValueSelected(value);
                });
                asd.show();
            });
        }

        builder.show();
    }

    private RadioButton createItem(String text) {
        RadioButton rb = new RadioButton(activity);
        rb.setText(text);
        rb.setTextSize(14f);
        RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        rb.setLayoutParams(lp);
        return rb;
    }

    private View createImageItem(String fileName, RadioGroup rg) {
        View v = LayoutInflater.from(activity).inflate(R.layout.image_picker_item, rg, false);
        RadioButton rb = v.findViewById(R.id.radio_button);
        TextView tv = v.findViewById(R.id.text_view);
        LinearLayout layoutImg = v.findViewById(R.id.layoutImg);
        View overlay = v.findViewById(R.id.transparentOverlay);

        int uniqueId = View.generateViewId();
        rb.setId(uniqueId);
        rb.setTag(fileName);
        tv.setText(fileName);

        // Preview Image
        android.widget.ImageView iv = new android.widget.ImageView(activity);
        iv.setLayoutParams(new LinearLayout.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, activity.getResources().getDisplayMetrics()), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, activity.getResources().getDisplayMetrics())));
        iv.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
        layoutImg.addView(iv);

        String path = DesignDataManager.getAssetPath(scId, fileName);
        Glide.with(activity).load(new File(path)).placeholder(R.drawable.ic_mtrl_image).into(iv);

        if (fileName.equals(currentValue)) {
            rb.setChecked(true);
        }

        View.OnClickListener clickListener = view -> {
            for (int i = 0; i < rg.getChildCount(); i++) {
                View child = rg.getChildAt(i);
                RadioButton childRb = child.findViewById(R.id.radio_button);
                if (childRb != null) childRb.setChecked(false);
            }
            rb.setChecked(true);
            rg.check(uniqueId);
        };

        overlay.setOnClickListener(clickListener);
        rb.setOnClickListener(clickListener);

        return v;
    }
}
