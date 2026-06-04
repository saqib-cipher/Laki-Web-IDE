package laki.webide.blockSystem.menus;

import android.content.Context;
import java.util.ArrayList;

/**
 * Handles the logic for populating and showing selection menus (HTML IDs, Classes, etc.)
 */
public class SelectorMenuHandler {

    private final Context context;

    public SelectorMenuHandler(Context context) {
        this.context = context;
    }

    /**
     * Resolves the list of options based on the menu name.
     * Replaces the static lists previously found in ExtraMenuBean.
     */
    public ArrayList<String> getMenuOptions(String menuName, String sc_id, String htmlFile) {
        ArrayList<String> options = new ArrayList<>();
        // Logic to fetch from HtmlParser metadata will go here
        return options;
    }

    public void showMenuDialog(String title, ArrayList<String> options, MenuCallback callback) {
        // Dialog UI implementation
    }

    public interface MenuCallback {
        void onItemSelected(String item);
    }
}
