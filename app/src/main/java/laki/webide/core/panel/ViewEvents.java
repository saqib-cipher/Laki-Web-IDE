package laki.webide.core.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import laki.webide.R;

public class ViewEvents extends LinearLayout {

    public ViewEvents(Context context) {
        super(context);
        init(context);
    }

    public ViewEvents(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_events, this, true);
        // This is a grid layout (RecyclerView) in Sketchware.
        // For the dummy test, it will just show the empty container from XML.
    }
}
