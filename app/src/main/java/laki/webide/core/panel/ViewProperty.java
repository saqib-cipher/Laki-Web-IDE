package laki.webide.core.panel;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.*;

import java.util.ArrayList;

import laki.webide.R;
import laki.webide.core.LayoutUtil;

public class ViewProperty extends LinearLayout {

    private LinearLayout propertyContents;
    private ViewPropertyItems itemsContainer;
    private LinearLayout layoutPropertyGroup;
    private Spinner spnWidget;
    private LinearLayout layoutPropertySeeAll;
    
    private ObjectAnimator showAllShower;
    private ObjectAnimator showAllHider;
    private boolean showAllVisible = true;

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

        // 1. Setup Spinner with dummy data
        String[] dummyViews = {"div_1", "span_1", "h1_header", "button_submit"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, dummyViews);
        spnWidget.setAdapter(adapter);

        // 2. Setup Property Tabs (Basic, Recent, Event)
        setupTabs();

        // 3. Setup the items container
        itemsContainer = new ViewPropertyItems(context);
        propertyContents.addView(itemsContainer);

        // 4. Setup "See All" (Complete Structure)
        setupSeeAll();

        // Add dummy values for testing
        loadDummyProperties();
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
        
        // We reuse the grid item layout for the "See All" button
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
                // Select animation logic (Original Sketchware style)
                for (int j = 0; j < layoutPropertyGroup.getChildCount(); j++) {
                    View child = layoutPropertyGroup.getChildAt(j);
                    boolean isSelected = (child == v);
                    child.setSelected(isSelected);
                    child.animate().scaleX(isSelected ? 0.9f : 0.8f).scaleY(isSelected ? 0.9f : 0.8f).setDuration(200).start();
                }

                // Switch visibility (Complete Structure Logic)
                if (id == 2) { // Event Tab
                    findViewById(R.id.property_layout).setVisibility(GONE);
                    findViewById(R.id.view_event).setVisibility(VISIBLE);
                } else {
                    findViewById(R.id.property_layout).setVisibility(VISIBLE);
                    findViewById(R.id.view_event).setVisibility(GONE);
                }
            });
            layoutPropertyGroup.addView(tab);

            // Set initial state for the first tab
            if (i == 0) {
                tab.setSelected(true);
                tab.setScaleX(0.9f);
                tab.setScaleY(0.9f);
            } else {
                tab.setScaleX(0.8f);
                tab.setScaleY(0.8f);
            }
        }
    }

    public void loadDummyProperties() {
        ArrayList<ViewPropertyItems.PropertyConfig> configs = new ArrayList<>();
        
        configs.add(new ViewPropertyItems.PropertyConfig(
                PropertyItem.Type.INPUT, "id", "ID", R.drawable.ic_mtrl_id, "my_div_01"));
        
        configs.add(new ViewPropertyItems.PropertyConfig(
                PropertyItem.Type.INPUT, "text", "Text", R.drawable.ic_mtrl_text_select, "Hello World"));
        
        configs.add(new ViewPropertyItems.PropertyConfig(
                PropertyItem.Type.COLOR, "color", "Color", R.drawable.ic_mtrl_palette, "#FF0000"));
 configs.add(new ViewPropertyItems.PropertyConfig(
                PropertyItem.Type.INPUT, "id", "ID2", R.drawable.ic_mtrl_id, "my_div_01"));

        configs.add(new ViewPropertyItems.PropertyConfig(
                PropertyItem.Type.INPUT, "text", "Text2", R.drawable.ic_mtrl_text_select, "Hello World"));

        configs.add(new ViewPropertyItems.PropertyConfig(
                PropertyItem.Type.COLOR, "color", "Color2", R.drawable.ic_mtrl_palette, "#FF0000"));

        itemsContainer.setProperties(configs, item -> {
            Toast.makeText(getContext(), "Clicked: " + item.getKey(), Toast.LENGTH_SHORT).show();
        });
    }

    // Compatibility methods for testing in the current IDE
    public void setBlock(laki.webide.core.Block block) {
        // When you click a block, we reload the dummy properties to show the panel is working
        loadDummyProperties();
    }

    public void a(String sc_id, com.besome.sketch.beans.ProjectFileBean projectFileBean) {
        // Initialization stub
    }
}
