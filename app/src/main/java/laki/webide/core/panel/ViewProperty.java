package laki.webide.core.panel;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.*;

import com.besome.sketch.lib.ui.ColorPickerDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;

import laki.webide.R;
import laki.webide.core.LayoutUtil;
import laki.webide.core.panel.ViewPropertyItems;
import mod.hey.studios.util.Helper;

public class ViewProperty extends LinearLayout {

    private LinearLayout propertyContents;
    private ViewPropertyItems itemsContainer;
    private LinearLayout layoutPropertyGroup;
    private Spinner spnWidget;
    private LinearLayout layoutPropertySeeAll;
    
    private ObjectAnimator showAllShower;
    private ObjectAnimator showAllHider;
    private boolean showAllVisible = true;

    private String sc_id;
    private laki.webide.core.Block currentBlock;

    public ViewProperty(Context context) {
        super(context);
        init(context);
    }

    public ViewProperty(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        // Use Sketchware's main layout
        LayoutInflater.from(context).inflate(R.layout.view_property, this, true);

        propertyContents = findViewById(R.id.property_contents);
        layoutPropertyGroup = findViewById(R.id.layout_property_group);
        spnWidget = findViewById(R.id.spn_widget);
        layoutPropertySeeAll = findViewById(R.id.layout_property_see_all);
        
        // Setup Scrolling Effect (Ditto Logic)
        com.besome.sketch.lib.ui.CustomHorizontalScrollView hcvProperty = findViewById(R.id.hcv_property);
        initializeSeeAllAnimations();
        
        hcvProperty.setOnScrollChangedListener((scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (Math.abs(scrollX - oldScrollX) <= 5) return;
            
            int maxScrollX = hcvProperty.getChildAt(0).getWidth() - hcvProperty.getWidth();
            if (scrollX > 100 && scrollX < maxScrollX) {
                if (scrollX > oldScrollX) { // Scrolling Right
                    if (showAllVisible) {
                        showAllVisible = false;
                        showAllHider.start();
                    }
                } else { // Scrolling Left
                    if (!showAllVisible) {
                        showAllVisible = true;
                        showAllShower.start();
                    }
                }
            } else if (scrollX >= maxScrollX) {
                if (showAllVisible) {
                    showAllVisible = false;
                    showAllHider.start();
                }
            } else {
                if (!showAllVisible) {
                    showAllVisible = true;
                    showAllShower.start();
                }
            }
        });

        // Setup Tabs
        setupTabs();

        // Setup the items container
        itemsContainer = new ViewPropertyItems(context);
        propertyContents.addView(itemsContainer);

        // Setup "See All"
        setupSeeAll();
    }

    private void initializeSeeAllAnimations() {
        showAllShower = ObjectAnimator.ofFloat(layoutPropertySeeAll, View.TRANSLATION_Y, 0.0f);
        showAllShower.setDuration(400);
        showAllShower.setInterpolator(new DecelerateInterpolator());

        showAllHider = ObjectAnimator.ofFloat(layoutPropertySeeAll, View.TRANSLATION_Y, LayoutUtil.getDip(getContext(), 100.0f));
        showAllHider.setDuration(200);
        showAllHider.setInterpolator(new DecelerateInterpolator());
    }

    private void setupSeeAll() {
        LinearLayout layoutSeeAll = findViewById(R.id.layout_property_see_all);
        layoutSeeAll.setVisibility(VISIBLE);
        View seeAll = LayoutInflater.from(getContext()).inflate(R.layout.property_grid_item, layoutSeeAll, false);
        ((ImageView) seeAll.findViewById(R.id.img_icon)).setImageResource(R.drawable.color_more_96);
        ((TextView) seeAll.findViewById(R.id.tv_title)).setText("See All");
        seeAll.setOnClickListener(v -> Toast.makeText(getContext(), "Open Full Properties Activity", Toast.LENGTH_SHORT).show());
        layoutSeeAll.addView(seeAll);
    }

    private void setupTabs() {
        int[] labels = {R.string.property_group_basic, R.string.property_group_recent, R.string.property_group_event};
        for (int i = 0; i < labels.length; i++) {
            final int id = i;
            View tab = LayoutInflater.from(getContext()).inflate(R.layout.property_group_item, layoutPropertyGroup, false);
            TextView title = tab.findViewById(R.id.tv_title);
            title.setText(getContext().getString(labels[i]));
            tab.setTag(i);
            tab.setOnClickListener(v -> {
                for (int j = 0; j < layoutPropertyGroup.getChildCount(); j++) {
                    View child = layoutPropertyGroup.getChildAt(j);
                    boolean isSelected = (child == v);
                    child.setSelected(isSelected);
                    child.animate().scaleX(isSelected ? 0.9f : 0.8f).scaleY(isSelected ? 0.9f : 0.8f).setDuration(200).start();
                }
                if (id == 2) { 
                    findViewById(R.id.property_layout).setVisibility(GONE);
                    findViewById(R.id.view_event).setVisibility(VISIBLE);
                } else {
                    findViewById(R.id.property_layout).setVisibility(VISIBLE);
                    findViewById(R.id.view_event).setVisibility(GONE);
                }
            });
            layoutPropertyGroup.addView(tab);
            if (i == 0) {
                tab.setSelected(true);
                tab.setScaleX(0.9f); tab.setScaleY(0.9f);
            } else {
                tab.setScaleX(0.8f); tab.setScaleY(0.8f);
            }
        }
    }

    public void setBlock(laki.webide.core.Block block) {
        this.currentBlock = block;
        if (block == null) {
            itemsContainer.removeAllViews();
            return;
        }

        ArrayList<ViewPropertyItems.PropertyConfig> configs = new ArrayList<>();
        
        // Common Properties
        configs.add(new ViewPropertyItems.PropertyConfig(PropertyItem.Type.INPUT, "id", "ID", R.drawable.ic_mtrl_id, block.attributes.getOrDefault("id", "")));
        configs.add(new ViewPropertyItems.PropertyConfig(PropertyItem.Type.INPUT, "class", "Class", R.drawable.ic_mtrl_code, block.attributes.getOrDefault("class", "")));

        String tag = block.mOpCode.replace("html_", "");
        if (tag.equals("h")) {
            // It's the combined heading block, the tag is in the hole
            Object val = block.getArgValue(0);
            tag = val != null ? val.toString() : "h1";
        }

        // Tag-Specific Properties
        switch (tag) {
            case "p": case "span": case "label": case "h1": case "h2": case "h3": case "h4": case "h5": case "h6":
                configs.add(new ViewPropertyItems.PropertyConfig(PropertyItem.Type.INPUT, "text", "Text Content", R.drawable.ic_mtrl_text_select, block.attributes.getOrDefault("text", "")));
                break;
            case "img":
                configs.add(new ViewPropertyItems.PropertyConfig(PropertyItem.Type.INPUT, "src", "Image Source", R.drawable.ic_mtrl_link, block.attributes.getOrDefault("src", "")));
                configs.add(new ViewPropertyItems.PropertyConfig(PropertyItem.Type.INPUT, "alt", "Alt Text", R.drawable.ic_mtrl_formattext, block.attributes.getOrDefault("alt", "")));
                break;
            case "a":
                configs.add(new ViewPropertyItems.PropertyConfig(PropertyItem.Type.INPUT, "href", "Hyperlink", R.drawable.ic_mtrl_link, block.attributes.getOrDefault("href", "")));
                configs.add(new ViewPropertyItems.PropertyConfig(PropertyItem.Type.SELECTOR, "target", "Target", R.drawable.ic_mtrl_list, block.attributes.getOrDefault("target", "_self")));
                configs.add(new ViewPropertyItems.PropertyConfig(PropertyItem.Type.INPUT, "text", "Link Text", R.drawable.ic_mtrl_text_select, block.attributes.getOrDefault("text", "")));
                break;
            case "input":
                configs.add(new ViewPropertyItems.PropertyConfig(PropertyItem.Type.SELECTOR, "type", "Input Type", R.drawable.ic_mtrl_list, block.attributes.getOrDefault("type", "text")));
                configs.add(new ViewPropertyItems.PropertyConfig(PropertyItem.Type.INPUT, "placeholder", "Placeholder", R.drawable.ic_mtrl_bulb, block.attributes.getOrDefault("placeholder", "")));
                configs.add(new ViewPropertyItems.PropertyConfig(PropertyItem.Type.INPUT, "name", "Form Name", R.drawable.ic_mtrl_id, block.attributes.getOrDefault("name", "")));
                break;
            case "form":
                configs.add(new ViewPropertyItems.PropertyConfig(PropertyItem.Type.INPUT, "action", "Action URL", R.drawable.ic_mtrl_link, block.attributes.getOrDefault("action", "")));
                configs.add(new ViewPropertyItems.PropertyConfig(PropertyItem.Type.SELECTOR, "method", "Method", R.drawable.ic_mtrl_list, block.attributes.getOrDefault("method", "GET")));
                break;
            case "button":
                configs.add(new ViewPropertyItems.PropertyConfig(PropertyItem.Type.INPUT, "text", "Label", R.drawable.ic_mtrl_text_select, block.attributes.getOrDefault("text", "")));
                configs.add(new ViewPropertyItems.PropertyConfig(PropertyItem.Type.SELECTOR, "type", "Btn Type", R.drawable.ic_mtrl_list, block.attributes.getOrDefault("type", "button")));
                break;
            case "video": case "audio": case "iframe":
                configs.add(new ViewPropertyItems.PropertyConfig(PropertyItem.Type.INPUT, "src", "Media Source", R.drawable.ic_mtrl_link, block.attributes.getOrDefault("src", "")));
                break;
        }

        // Style Properties
        configs.add(new ViewPropertyItems.PropertyConfig(PropertyItem.Type.COLOR, "color", "Text Color", R.drawable.ic_mtrl_palette, block.attributes.getOrDefault("color", "#000000")));
        configs.add(new ViewPropertyItems.PropertyConfig(PropertyItem.Type.COLOR, "background-color", "BG Color", R.drawable.ic_mtrl_palette, block.attributes.getOrDefault("background-color", "#FFFFFF")));

        itemsContainer.setProperties(configs, item -> handlePropertyClick(item));
        syncSpinnerSelection();
    }

    private void handlePropertyClick(PropertyItem item) {
        String key = item.getKey();
        if (item.getType() == PropertyItem.Type.COLOR) {
            showColorPickerDialog(key);
        } else if (item.getType() == PropertyItem.Type.SELECTOR) {
            showSelectorDialog(key);
        } else {
            showTextInputDialog(key);
        }
    }

    private void showTextInputDialog(String key) {
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(getContext());
        dialog.setTitle("Edit " + key.toUpperCase());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.property_popup_input_text, null);
        EditText edInput = view.findViewById(R.id.ed_input);
        edInput.setText(currentBlock.attributes.getOrDefault(key, ""));
        dialog.setView(view);
        dialog.setPositiveButton(Helper.getResString(R.string.common_word_save), (d, which) -> {
            updateBlock(key, edInput.getText().toString());
            setBlock(currentBlock);
        });
        dialog.setNegativeButton(Helper.getResString(R.string.common_word_cancel), null);
        dialog.show();
    }

    private void showSelectorDialog(String key) {
        String[] options;
        switch (key) {
            case "target": options = new String[]{"_self", "_blank", "_parent", "_top"}; break;
            case "method": options = new String[]{"GET", "POST"}; break;
            case "type": 
                if (currentBlock.mOpCode.contains("input")) options = new String[]{"text", "password", "number", "email", "tel", "url", "date", "checkbox", "radio", "file"};
                else options = new String[]{"button", "submit", "reset"};
                break;
            default: options = new String[]{"Default"}; break;
        }

        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(getContext());
        dialog.setTitle("Select " + key.toUpperCase());
        dialog.setItems(options, (d, which) -> {
            updateBlock(key, options[which]);
            setBlock(currentBlock);
        });
        dialog.show();
    }

    private void showColorPickerDialog(String key) {
        String color = currentBlock.attributes.getOrDefault(key, "#FFFFFF");
        ColorPickerDialog picker = new ColorPickerDialog((Activity) getContext(), color, true, true, sc_id);
        picker.a(new ColorPickerDialog.b() {
            @Override public void a(int var1) {
                updateBlock(key, String.format("#%06X", (0xFFFFFF & var1)));
                setBlock(currentBlock);
            }
            @Override public void a(String var1, int var2) {
                updateBlock(key, "@color/" + var1);
                setBlock(currentBlock);
            }
        });
        picker.showAtLocation(this, Gravity.CENTER, 0, 0);
    }

    public void updateBlock(String key, String value) {
        if (currentBlock == null) return;
        currentBlock.attributes.put(key, value);
        if (key.equals("id")) currentBlock.setArgValue(0, value);
    }

    private void syncSpinnerSelection() {
        if (currentBlock == null || spnWidget == null) return;
        Object idVal = currentBlock.getArgValue(0);
        if (idVal == null) return;
        String idStr = idVal.toString();
        for (int i = 0; i < spnWidget.getCount(); i++) {
            if (spnWidget.getItemAtPosition(i).toString().equals(idStr)) {
                spnWidget.setSelection(i); break;
            }
        }
    }

    public void updateSpinner(ArrayList<String> ids) {
        if (spnWidget == null) return;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, ids);
        spnWidget.setAdapter(adapter);
    }

    public void a(String sc_id, com.besome.sketch.beans.ProjectFileBean projectFileBean) {
        this.sc_id = sc_id;
    }
}
