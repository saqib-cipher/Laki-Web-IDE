package laki.webide.events;

import com.besome.sketch.beans.EventBean;
import mod.hey.studios.util.Helper;
import laki.webide.R;

/**
 * Centralized manager for the "Extend CSS" logic area.
 */
public class ExtCSS {

    public static final String EVENT_ID = "Extend CSS";
    public static final String LISTENER_TYPE = "Extend CSS";
    public static final int EVENT_TYPE = EventBean.EVENT_TYPE_ACTIVITY;

    public static String getDisplayName() {
        return EVENT_ID;
    }

    public static int getIconResource() {
        return R.drawable.bg_event_type_activity;
    }

    public static EventBean getEventBean() {
        // Use EVENT_ID as targetId string to avoid mismatch with -1
        EventBean bean = new EventBean(EVENT_TYPE, 0, EVENT_ID, LISTENER_TYPE);
        bean.targetId = EVENT_ID;
        bean.initValue();
        return bean;
    }

    public static boolean isMatch(String eventName) {
        return EVENT_ID.equals(eventName);
    }
}
