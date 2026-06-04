package laki.webide.managers;

import android.content.Context;
import com.besome.sketch.beans.ProjectFileBean;

import a.a.a.jC;

/**
 * Unified Controller for Web Project operations.
 * This class serves as the single entry point for saving, syncing, and initializing web files.
 */
public class WebProjectManager {

    /**
     * Performs a full save of the current web page, including Head settings,
     * Layout structure, and external CSS.
     */
    public static void savePage(Context context, String sc_id, ProjectFileBean projectFile) {
        // 1. Force commit logic database to project.json
        jC.a(sc_id).j();
        
        // 2. Sync memory state to physical files on disk
        WebProjectSyncManager.syncCurrentFile(context, sc_id, projectFile);
    }

    /**
     * Initializes a new web project with the default index/main page.
     */
    public static void createWebProject(String sc_id, String projectName) {
        // The first activity in Sketchware is always main.xml (index for us)
        ProjectFileBean mainPage = new ProjectFileBean(ProjectFileBean.PROJECT_FILE_TYPE_ACTIVITY, "main", 0, 0, 1);
        WebProjectEditorManager.onFileAdded(sc_id, mainPage);
    }

    /**
     * Initializes a new web project or page with standard boilerplates.
     */
    public static void initializeNewPage(String sc_id, ProjectFileBean projectFile) {
        WebProjectEditorManager.onFileAdded(sc_id, projectFile);
    }

    /**
     * Safely determines if the current context is a web project.
     */
    public static boolean isWebProject(String fileName) {
        return fileName != null && fileName.endsWith(".html");
    }
}
