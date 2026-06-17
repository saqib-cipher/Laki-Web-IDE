package laki.webide.core.panel;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import laki.webide.R;

public class PropertyItem extends RelativeLayout implements View.OnClickListener {

    public enum Type { INPUT, COLOR, SELECTOR }

    private Type type;
    private String key;
    private String value;
    private int colorValue;

    private TextView tvName;
    private TextView tvValue;
    private ImageView imgIcon;
    private View viewColor;
    private OnPropertyChangedListener listener;

    public PropertyItem(Context context, Type type, String key, String label, int iconRes) {
        super(context);
        this.type = type;
        this.key = key;
        init(context, label, iconRes);
    }

    private void init(Context context, String label, int iconRes) {
        // Use Sketchware's exact layout for the item
        LayoutInflater.from(context).inflate(R.layout.property_input_item, this, true);

        tvName = findViewById(R.id.tv_name);
        tvValue = findViewById(R.id.tv_value);
        imgIcon = findViewById(R.id.img_icon);
        
        // Setup visual state to match "Icon style" (Orientation 0)
        View propertyItem = findViewById(R.id.property_item);
        View propertyMenuItem = findViewById(R.id.property_menu_item);
        
        propertyItem.setVisibility(GONE);
        propertyMenuItem.setVisibility(VISIBLE);
        propertyMenuItem.setOnClickListener(this);

        // Map label and icon
        ((TextView) findViewById(R.id.tv_title)).setText(label);
        ((ImageView) findViewById(R.id.img_icon)).setImageResource(iconRes);

        // If it's a color type, we would normally handle the color preview square
        // here, but we're keeping it simple for the initial display test.
    }

    public void setValue(String value) {
        this.value = value;
        // In icon mode, sub_title is used for the value
        TextView subTitle = findViewById(R.id.tv_sub_title);
        subTitle.setVisibility(VISIBLE);
        subTitle.setText(value);
    }

    public void setListener(OnPropertyChangedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClicked(this);
        }
    }

    public String getKey() { return key; }
    public Type getType() { return type; }

    public interface OnPropertyChangedListener {
        void onClicked(PropertyItem item);
    }
}
