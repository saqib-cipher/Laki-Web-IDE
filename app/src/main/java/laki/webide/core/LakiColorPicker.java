package laki.webide.core;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.besome.sketch.beans.ColorBean;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import a.a.a.sq;
import laki.webide.R;

public class LakiColorPicker extends Dialog {
    private final Activity activity;
    private String currentValue = "#FFFFFF";
    private OnColorSelectedListener listener;
    
    private RecyclerView colorRecycler;
    private LinearLayout colorGroupContainer;
    private View colorPreview;
    private EditText hexInput;
    private TabLayout tabLayout;
    private ViewGroup mainContainer;

    public interface OnColorSelectedListener {
        void onColorSelected(String colorValue);
    }

    public LakiColorPicker(@NonNull Activity activity) {
        super(activity, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        this.activity = activity;
    }

    public LakiColorPicker setCurrentValue(String value) {
        if (value != null && !value.isEmpty()) {
            this.currentValue = value;
        }
        return this;
    }

    public LakiColorPicker setOnColorSelectedListener(OnColorSelectedListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laki_color_picker_layout);
        
        mainContainer = findViewById(R.id.container);
        tabLayout = findViewById(R.id.tabs);

        setupTabs();
        showSolidPicker();
    }

    private void setupTabs() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mainContainer.removeAllViews();
                switch (tab.getPosition()) {
                    case 0: showSolidPicker(); break;
                    case 1: showKeywordsPicker(); break;
                    case 2: showGradientPicker(); break;
                }
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
        
        // Add a third tab for Keywords if it doesn't exist in XML
        if (tabLayout.getTabCount() < 3) {
            tabLayout.addTab(tabLayout.newTab().setText("Keywords"));
        }
    }

    private void showSolidPicker() {
        View view = activity.getLayoutInflater().inflate(R.layout.laki_color_picker_solid_full, mainContainer, true);
        
        colorPreview = view.findViewById(R.id.color_preview);
        hexInput = view.findViewById(R.id.hex_input);
        colorRecycler = view.findViewById(R.id.color_recycler);
        colorGroupContainer = view.findViewById(R.id.color_group_container);
        
        hexInput.setText(currentValue);
        updatePreview(currentValue);

        hexInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                updatePreview(s.toString());
            }
        });

        setupColorGroups();
        
        view.findViewById(R.id.btn_apply).setOnClickListener(v -> {
            if (listener != null) listener.onColorSelected(hexInput.getText().toString());
            dismiss();
        });

        view.findViewById(R.id.btn_close).setOnClickListener(v -> dismiss());
        
        view.findViewById(R.id.btn_rgba).setOnClickListener(v -> {
            hexInput.setText("rgba(255, 255, 255, 1.0)");
        });
    }

    private void setupColorGroups() {
        ColorBean[][] allGroups = {
            sq.p, sq.q, sq.r, sq.s, sq.t, sq.u, sq.v, sq.w, sq.x, sq.y, sq.z, 
            sq.A, sq.B, sq.C, sq.D, sq.E, sq.F, sq.G, sq.H, sq.I, sq.J
        };

        colorGroupContainer.removeAllViews();
        for (ColorBean[] group : allGroups) {
            if (group == null || group.length == 0) continue;
            
            View groupView = LayoutInflater.from(activity).inflate(R.layout.laki_color_group_item, colorGroupContainer, false);
            View indicator = groupView.findViewById(R.id.group_indicator);
            indicator.setBackgroundColor(group[0].colorCode);
            
            groupView.setOnClickListener(v -> {
                updateShades(group);
            });
            colorGroupContainer.addView(groupView);
        }
        
        updateShades(sq.p);
    }

    private void updateShades(ColorBean[] shades) {
        colorRecycler.setLayoutManager(new LinearLayoutManager(activity));
        colorRecycler.setAdapter(new ShadeAdapter(shades));
    }

    private void updatePreview(String val) {
        try {
            if (val.startsWith("#")) {
                colorPreview.setBackgroundColor(Color.parseColor(val));
            }
        } catch (Exception ignored) {}
    }

    private void showKeywordsPicker() {
        View view = activity.getLayoutInflater().inflate(R.layout.property_popup_selector_single, mainContainer, true);
        RadioGroup rg = view.findViewById(R.id.rg_content);
        
        String[] keywords = {"transparent", "aliceblue", "antiquewhite", "aqua", "aquamarine", "azure", "beige", "bisque", "black", "blanchedalmond", "blue", "blueviolet", "brown", "burlywood", "cadetblue", "chartreuse", "chocolate", "coral", "cornflowerblue", "cornsilk", "crimson", "cyan", "darkblue", "darkcyan", "darkgoldenrod", "darkgray", "darkgreen", "darkgrey", "darkkhaki", "darkmagenta", "darkolivegreen", "darkorange", "darkorchid", "darkred", "darksalmon", "darkseagreen", "darkslateblue", "darkslategray", "darkslategrey", "darkturquoise", "darkviolet", "deeppink", "deepskyblue", "dimgray", "dimgrey", "dodgerblue", "firebrick", "floralwhite", "forestgreen", "fuchsia", "gainsboro", "ghostwhite", "gold", "goldenrod", "gray", "green", "greenyellow", "grey", "honeydew", "hotpink", "indianred", "indigo", "ivory", "khaki", "lavender", "lavenderblush", "lawngreen", "lemonchiffon", "lightblue", "lightcoral", "lightcyan", "lightgoldenrodyellow", "lightgray", "lightgreen", "lightgrey", "lightpink", "lightsalmon", "lightseagreen", "lightskyblue", "lightslategray", "lightslategrey", "lightsteelblue", "lightyellow", "lime", "limegreen", "linen", "magenta", "maroon", "mediumaquamarine", "mediumblue", "mediumorchid", "mediumpurple", "mediumseagreen", "mediumslateblue", "mediumspringgreen", "mediumturquoise", "mediumvioletred", "midnightblue", "mintcream", "mistyrose", "moccasin", "navajowhite", "navy", "oldlace", "olive", "olivedrab", "orange", "orangered", "orchid", "palegoldenrod", "palegreen", "paleturquoise", "palevioletred", "papayawhip", "peachpuff", "peru", "pink", "plum", "powderblue", "purple", "rebeccapurple", "red", "rosybrown", "royalblue", "saddlebrown", "salmon", "sandybrown", "seagreen", "seashell", "sienna", "silver", "skyblue", "slateblue", "slategray", "slategrey", "snow", "springgreen", "steelblue", "tan", "teal", "thistle", "tomato", "turquoise", "violet", "wheat", "white", "whitesmoke", "yellow", "yellowgreen"};
        
        for (String key : keywords) {
            android.widget.RadioButton rb = new android.widget.RadioButton(activity);
            rb.setText(key);
            rb.setOnClickListener(v -> {
                if (listener != null) listener.onColorSelected(key);
                dismiss();
            });
            rg.addView(rb);
        }
    }

    private void showGradientPicker() {
        View view = activity.getLayoutInflater().inflate(R.layout.laki_color_picker_gradient, mainContainer, true);
        EditText output = view.findViewById(R.id.gradient_output);
        
        view.findViewById(R.id.btn_linear).setOnClickListener(v -> output.setText("linear-gradient(to right, #FF0000, #FFFF00)"));
        view.findViewById(R.id.btn_radial).setOnClickListener(v -> output.setText("radial-gradient(circle, #FF0000, #FFFF00)"));
        
        view.findViewById(R.id.btn_apply_gradient).setOnClickListener(v -> {
            if (listener != null) listener.onColorSelected(output.getText().toString());
            dismiss();
        });
    }

    private class ShadeAdapter extends RecyclerView.Adapter<ShadeAdapter.ViewHolder> {
        private final ColorBean[] shades;
        public ShadeAdapter(ColorBean[] shades) { this.shades = shades; }

        @NonNull @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.laki_color_shade_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ColorBean bean = shades[position];
            holder.colorView.setBackgroundColor(bean.colorCode);
            holder.nameText.setText(bean.colorName);
            holder.hexText.setText(String.format("#%06X", (0xFFFFFF & bean.colorCode)));
            holder.itemView.setOnClickListener(v -> hexInput.setText(String.format("#%06X", (0xFFFFFF & bean.colorCode))));
        }

        @Override public int getItemCount() { return shades.length; }

        class ViewHolder extends RecyclerView.ViewHolder {
            View colorView;
            TextView nameText, hexText;
            public ViewHolder(View v) {
                super(v);
                colorView = v.findViewById(R.id.color_shade);
                nameText = v.findViewById(R.id.color_name);
                hexText = v.findViewById(R.id.color_hex);
            }
        }
    }
}
