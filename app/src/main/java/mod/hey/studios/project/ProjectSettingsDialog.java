package mod.hey.studios.project;

import android.app.Activity;
import laki.webide.utility.SketchwareUtil;

public class ProjectSettingsDialog {

    public ProjectSettingsDialog(Activity activity, String sc_id) {
    }

    public void show() {
        SketchwareUtil.toast("Android Settings are disabled in Web IDE mode.");
    }
}
