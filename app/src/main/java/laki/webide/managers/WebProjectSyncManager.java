package laki.webide.managers;

import android.content.Context;
import laki.webide.ProjectWorkspace;
import laki.webide.compiler.HtmlGenerator;
import a.a.a.jC;
import a.a.a.hC;
import a.a.a.eC;
import a.a.a.iC;
import com.besome.sketch.beans.ProjectFileBean;
import com.besome.sketch.beans.ViewBean;
import com.google.gson.Gson;
import java.util.ArrayList;
import laki.webide.core.LakiFiles;
import laki.webide.utility.FileUtil;

public class WebProjectSyncManager {

    public static void sync(Context context, String sc_id) {
        ProjectWorkspace workspace = new ProjectWorkspace(context, sc_id);
        sync(workspace, sc_id);
    }

    public static void sync(ProjectWorkspace workspace, String sc_id) {
        if (workspace.isSimpleProject) {
            hC projectFileManager = jC.b(sc_id);
            eC projectDataManager = jC.a(sc_id);
            iC projectLibraryManager = jC.c(sc_id);
            
            ArrayList<ProjectFileBean> files = new ArrayList<>(projectFileManager.b());
            files.addAll(projectFileManager.c());
            
            for (ProjectFileBean file : files) {
                syncFile(workspace, file, projectFileManager, projectDataManager, projectLibraryManager);
            }
        }
    }

    /**
     * Syncs a specific project file (HTML and its corresponding CSS) to disk.
     */
    public static void syncFile(ProjectWorkspace workspace, ProjectFileBean file, hC projectFileManager, eC projectDataManager, iC projectLibraryManager) {
        if (!workspace.isSimpleProject) return;

        // Sync HTML
        String htmlCode = workspace.getFileSrc(file.getXmlName(), projectFileManager, projectDataManager, projectLibraryManager);
        if (htmlCode == null || htmlCode.trim().isEmpty()) {
            // If empty, generate a default structure
            HtmlGenerator defaultGen = new HtmlGenerator(file.getXmlName(), new ArrayList<>(), "");
            htmlCode = defaultGen.generate();
        }
        workspace.a(file.getXmlName(), htmlCode);
        
        // Sync CSS
        String cssFileName = file.getXmlName().replace(".xml", "").replace(".html", "") + ".css";
        String cssCode = workspace.getFileSrc(cssFileName, projectFileManager, projectDataManager, projectLibraryManager);
        workspace.a(cssFileName, cssCode);

        // Ensure project structure exists (Settings, etc.)
        LakiFiles.createSimpleProjectStructure(workspace.projectMyscPath);

        // Sync Designer State (HTML Tags & Visual History)
        String tagsPath = LakiFiles.getPageHtmlTagsPath(workspace.projectMyscPath, file.fileName);
        ArrayList<ViewBean> currentViews = eC.a(projectDataManager.d(file.getXmlName()));
        FileUtil.writeFile(tagsPath, new Gson().toJson(currentViews));

        // Sync extra page settings
        syncExtraSettings(workspace, file);
    }

    private static void syncExtraSettings(ProjectWorkspace workspace, ProjectFileBean file) {
        String projectRoot = workspace.projectMyscPath;
        String pageName = file.fileName;

        // Sync HTML Head (if not already handled)
        String headPath = LakiFiles.getPageHtmlHeadPath(projectRoot, pageName);
        if (!FileUtil.isExistFile(headPath)) {
            FileUtil.writeFile(headPath, "{}");
        }

        // Sync Extend CSS Blocks (if not already handled)
        String blocksPath = LakiFiles.getPageExtendBlocksPath(projectRoot, pageName);
        if (!FileUtil.isExistFile(blocksPath)) {
            FileUtil.writeFile(blocksPath, "[]");
        }
    }
    
    /**
     * Convenience method to sync current state from sc_id and ProjectFileBean
     */
    public static void syncCurrentFile(Context context, String sc_id, ProjectFileBean file) {
        ProjectWorkspace workspace = new ProjectWorkspace(context, sc_id);
        syncCurrentFile(workspace, sc_id, file);
    }

    public static void syncCurrentFile(ProjectWorkspace workspace, String sc_id, ProjectFileBean file) {
        if (workspace != null && workspace.isSimpleProject) {
            syncFile(workspace, file, jC.b(sc_id), jC.a(sc_id), jC.c(sc_id));
        }
    }
}
