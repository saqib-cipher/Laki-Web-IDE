package laki.webide.blockSystem.inputs;

import android.content.Context;
import android.view.View;

/**
 * Base class for any interactive element within a block (Holes/Inputs).
 */
public abstract class BaseBlockInput extends View {
    protected String value = "";
    protected String type; // e.g., "s", "d", "b", "m"

    public BaseBlockInput(Context context, String type) {
        super(context);
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
        invalidate();
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }
    
    /** Triggered when the user clicks the input hole */
    public abstract void onInputClicked();
}
