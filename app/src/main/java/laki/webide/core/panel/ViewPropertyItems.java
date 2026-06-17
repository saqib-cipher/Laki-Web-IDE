package laki.webide.core.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import java.util.ArrayList;

public class ViewPropertyItems extends LinearLayout {

    public ViewPropertyItems(Context context) {
        super(context);
        setOrientation(HORIZONTAL);
    }

    public ViewPropertyItems(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
    }

    public void setProperties(ArrayList<PropertyConfig> configs, PropertyItem.OnPropertyChangedListener listener) {
        removeAllViews();
        for (PropertyConfig config : configs) {
            PropertyItem item = new PropertyItem(getContext(), config.type, config.key, config.label, config.iconRes);
            item.setValue(config.value);
            item.setListener(listener);
            addView(item);
        }
    }

    public static class PropertyConfig {
        public PropertyItem.Type type;
        public String key;
        public String label;
        public int iconRes;
        public String value;

        public PropertyConfig(PropertyItem.Type type, String key, String label, int iconRes, String value) {
            this.type = type;
            this.key = key;
            this.label = label;
            this.iconRes = iconRes;
            this.value = value;
        }
    }
}
