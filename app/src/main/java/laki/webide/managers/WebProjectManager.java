package laki.webide.managers;

import android.content.Context;
import com.besome.sketch.beans.ProjectFileBean;
import a.a.a.jC;
import laki.webide.core.LakiFiles;

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
     * Initializes a new web project with the default index/main page using LakiFiles.
     */
    public static void createWebProject(String sc_id, String projectName) {
        // 1. Resolve the professional project root: simple/[ProjectName]_[sc_id]
        String projectRoot = LakiFiles.getProjectRoot(projectName, sc_id, false);
        
        // 2. Physically create the folders: html/, css/, assets/, settings/
        LakiFiles.createSimpleProjectStructure(projectRoot);
        
        // 3. Initialize the first page: main
        String firstPageName = "main";
        LakiFiles.initializePageSettings(projectRoot, firstPageName);
        LakiFiles.createInitialWebFiles(projectRoot, firstPageName);
        
        // 4. Register the file in Sketchware's virtual system to make it visible in the Editor
        ProjectFileBean mainPage = new ProjectFileBean(ProjectFileBean.PROJECT_FILE_TYPE_ACTIVITY, firstPageName, 0, 0, 1);
        // We skip WebProjectEditorManager here because we've already done the physical work above.
    }

    /**
     * Initializes a new web page with standard boilerplates using LakiFiles.
     */
    public static void initializeNewPage(String sc_id, String projectName, ProjectFileBean projectFile) {
        String projectRoot = LakiFiles.getProjectRoot(projectName, sc_id, false);
        String pageName = projectFile.fileName;
        
        LakiFiles.initializePageSettings(projectRoot, pageName);
        LakiFiles.createInitialWebFiles(projectRoot, pageName);
    }

    /**
     * Safely determines if the current context is a web project.
     */
    public static boolean isWebProject(String fileName) {
        return fileName != null && (fileName.endsWith(".html") || fileName.endsWith(".css"));
    }
}
