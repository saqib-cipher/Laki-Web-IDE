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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

import laki.webide.R;

public class LakiDialogBox {
    private final Activity activity;
    private final String title;
    private ArrayList<String> list = new ArrayList<>();
    private OnValueSelectedListener listener;
    private String currentValue = "";
    private boolean manualEntryEnabled = true;

    public interface OnValueSelectedListener {
        void onValueSelected(String value);
    }

    public LakiDialogBox(Activity activity, String title) {
        this.activity = activity;
        this.title = title;
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
            RadioButton rb = createItem(item);
            rg.addView(rb);
            if (item.equals(currentValue)) {
                rb.setChecked(true);
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
                    if (child instanceof RadioButton) {
                        String text = ((RadioButton) child).getText().toString().toLowerCase();
                        child.setVisibility(text.contains(query) ? View.VISIBLE : View.GONE);
                    }
                }
            }
        });

        builder.setView(customView);
        builder.setPositiveButton("Select", (dialog, which) -> {
            int checkedId = rg.getCheckedRadioButtonId();
            if (checkedId != -1) {
                RadioButton rb = rg.findViewById(checkedId);
                if (listener != null) listener.onValueSelected(rb.getText().toString());
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
}