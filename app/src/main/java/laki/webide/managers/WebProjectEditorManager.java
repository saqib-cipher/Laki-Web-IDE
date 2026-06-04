package laki.webide.managers;

import java.util.HashMap;
import a.a.a.lC;
import a.a.a.yB;
import com.besome.sketch.beans.ProjectFileBean;

public class WebProjectEditorManager {

    public static void onFileAdded(String sc_id, ProjectFileBean projectFileBean) {
        HashMap<String, Object> metadata = lC.b(sc_id);
        if (metadata != null) {
            String projectName = yB.c(metadata, "my_ws_name");
            String fileName = projectFileBean.fileName;
            if (fileName.contains(".")) {
                fileName = fileName.substring(0, fileName.lastIndexOf('.'));
            }

            if (projectFileBean.fileType == ProjectFileBean.PROJECT_FILE_TYPE_FRAGMENT) {
                // Treat Fragment as JAVASCRIPT file in Web IDE
                WebFileManager.createNewFile(projectName, fileName, "js");
            } else {
                // Treat Activity as HTML/CSS file
                WebFileManager.createNewFile(projectName, fileName, "html");
                WebFileManager.createNewFile(projectName, fileName, "css");
                
                // Initialize default head settings for the new page
                HeadEditorManager.initializeDefaults(sc_id, projectFileBean);
                
                // Immediately sync the file to disk to populate with default structure
                WebProjectSyncManager.syncCurrentFile(laki.webide.SketchApplication.getContext(), sc_id, projectFileBean);
            }
        }
    }
}
