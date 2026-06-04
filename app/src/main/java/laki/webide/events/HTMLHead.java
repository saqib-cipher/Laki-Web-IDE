package laki.webide.events;

import com.besome.sketch.beans.EventBean;
import mod.hey.studios.util.Helper;
import laki.webide.R;

/**
 * Centralized manager for the "Header Area" (formerly onCreate) logic.
 * This class serves as the single source of truth for UI display, 
 * icons, and event metadata for the Web IDE's header section.
 */
public class HTMLHead {

    // The internal unique ID used to save/load logic blocks in the .json files
    public static final String EVENT_ID = "Head of HTML";
    
    // The internal Sketchware listener type
    public static final String LISTENER_TYPE = "Head of HTML";
    
    // The event category (3 = Activity/Page level)
    public static final int EVENT_TYPE = EventBean.EVENT_TYPE_ACTIVITY;

    /**
     * Returns the name shown to the user in the Event tab and logic editor.
     * Maps to R.string.event_initialize ("header area").
     */
    public static String getDisplayName() {
        return Helper.getResString(R.string.event_initialize);
    }

    /**
     * Returns the icon shown in the Event list.
     */
    public static int getIconResource() {
        return R.drawable.bg_event_type_activity;
    }

    /**
     * Creates a pre-configured EventBean for the Header Area.
     * This can be used directly in fragments like rs.java to populate the list.
     */
    public static EventBean getEventBean() {
        // Use EVENT_ID as targetId string to avoid mismatch with -1
        EventBean bean = new EventBean(EVENT_TYPE, 0, EVENT_ID, LISTENER_TYPE);
        bean.targetId = EVENT_ID;
        bean.initValue();
        return bean;
    }

    /**
     * Checks if a given event ID belongs to the Header Area.
     */
    public static boolean isMatch(String eventId) {
        return EVENT_ID.equals(eventId);
    }
}
