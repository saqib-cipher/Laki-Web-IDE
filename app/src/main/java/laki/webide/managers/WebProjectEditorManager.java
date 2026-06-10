package laki.webide.managers;

import java.util.HashMap;
import a.a.a.lC;
import a.a.a.yB;
import com.besome.sketch.beans.ProjectFileBean;
import laki.webide.core.LakiFiles;

/**
 * Handles the logic when a user adds a new file (Activity/Fragment) inside the Editor.
 */
public class WebProjectEditorManager {

    public static void onFileAdded(String sc_id, ProjectFileBean projectFileBean) {
        HashMap<String, Object> metadata = lC.b(sc_id);
        if (metadata != null) {
            String projectName = yB.c(metadata, "my_ws_name");
            
            // Delegate initialization to the Unified WebProjectManager
            WebProjectManager.initializeNewPage(sc_id, projectName, projectFileBean);
            
            // Initialize default head settings for the new page
            HeadEditorManager.initializeDefaults(sc_id, projectFileBean);
            
            // Sync initial state
            WebProjectSyncManager.syncCurrentFile(laki.webide.SketchApplication.getContext(), sc_id, projectFileBean);
        }
    }
}
