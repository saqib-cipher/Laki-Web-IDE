package laki.webide;

import android.content.Context;
import android.util.Log;

import com.besome.sketch.beans.BlockBean;
import com.besome.sketch.beans.ProjectFileBean;
import com.besome.sketch.beans.SrcCodeBean;
import com.besome.sketch.editor.manage.library.material3.Material3LibraryManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import laki.webide.compiler.CssCodeGenerator;
import laki.webide.managers.CssLogicPersistenceManager;
import laki.webide.utility.FilePathUtil;
import mod.hey.studios.project.ProjectSettings;
import mod.hey.studios.util.ProjectFile;
import mod.hilal.saif.blocks.CommandBlock;
import laki.webide.compiler.HtmlGenerator;
import laki.webide.util.library.BuiltInLibraryManager;
import laki.webide.utility.FileUtil;

import a.a.a.*;

public class ProjectWorkspace {

    public final String assetsPath;
    public final String fontsPath;
    public final boolean isSimpleProject;
    public final String sc_id;
    public final String projectMyscPath;
    public final String projectName;
    public final String applicationName;
    public final String packageName;
    public final int colorAccent;
    public final int colorPrimary;
    public final int colorPrimaryDark;
    public final int colorControlHighlight;
    public final int colorControlNormal;
    public final ProjectSettings projectSettings;
    public final String resDirectoryPath;
    public final String javaFilesPath;
    public final HashMap<String, Object> metadata;
    private final Material3LibraryManager material3LibraryManager;
    private final oB fileUtil;
    private final Context context;
    public jq N;

    public ProjectWorkspace(Context context, String sc_id) {
        this(context, "", lC.b(sc_id));
    }

    public ProjectWorkspace(Context context, String myscFolderPath, HashMap<String, Object> metadata) {
        this.context = context;
        this.metadata = metadata;
        N = new jq();
        sc_id = yB.c(metadata, "sc_id");
        isSimpleProject = true; 
        material3LibraryManager = new Material3LibraryManager(sc_id);
        N.sc_id = sc_id;
        projectName = yB.c(metadata, "my_ws_name");
        packageName = "web.ide.project";
        
        projectSettings = new ProjectSettings(sc_id);
        fileUtil = new oB(true);

        projectMyscPath = FilePathUtil.getProjectRoot(sc_id) + File.separator;
        assetsPath = new FilePathUtil().getPathAssets(sc_id);
        fontsPath = assetsPath + File.separator + "fonts";
        resDirectoryPath = new FilePathUtil().getPathResource(sc_id);
        javaFilesPath = new FilePathUtil().getPathJava(sc_id);

        applicationName = yB.c(metadata, "my_app_name");

        colorAccent = yB.a(metadata, ProjectFile.COLOR_ACCENT, 0);
        colorPrimary = yB.a(metadata, ProjectFile.COLOR_PRIMARY, 0);
        colorPrimaryDark = yB.a(metadata, ProjectFile.COLOR_PRIMARY_DARK, 0);
        colorControlHighlight = yB.a(metadata, ProjectFile.COLOR_CONTROL_HIGHLIGHT, 0);
        colorControlNormal = yB.a(metadata, ProjectFile.COLOR_CONTROL_NORMAL, 0);

        String legacyPath = FileUtil.getExternalStorageDir() + "/.lakiwebsites/data/" + sc_id;
        if (FileUtil.isExistFile(legacyPath) && !FileUtil.isExistFile(projectMyscPath)) {
            try {
                FileUtil.copyDirectory(new File(legacyPath), new File(projectMyscPath));
                FileUtil.moveFile(projectMyscPath + "files/assets", projectMyscPath + "asset");
                FileUtil.moveFile(projectMyscPath + "files/java", projectMyscPath + "css");
                FileUtil.moveFile(projectMyscPath + "files/resource", projectMyscPath + "res");
            } catch (Exception e) {
                Log.e("Migration", "Error migrating project", e);
            }
        }
    }

    public void a(String fileName, String fileContent) {
        if (fileName.endsWith(".xml")) {
            fileName = fileName.replace(".xml", ".html");
        }
        String targetPath;
        if (fileName.endsWith(".html")) {
            targetPath = projectMyscPath + fileName;
        } else if (fileName.endsWith(".css")) {
            targetPath = projectMyscPath + "css" + File.separator + fileName;
        } else if (fileName.endsWith(".js")) {
            targetPath = projectMyscPath + "js" + File.separator + fileName;
        } else {
            targetPath = projectMyscPath + fileName;
        }
        fileUtil.b(targetPath, fileContent);
    }

    public void a(iC projectLibraryManager, hC projectFileManager, eC projectDataManager) {
        a(projectLibraryManager, projectFileManager, projectDataManager, ExportType.DEBUG_APP);
    }

    public void a(iC projectLibraryManager, hC projectFileManager, eC projectDataManager, ExportType exportingType) {
        N = new jq();
        N.packageName = "web.ide.project";
        N.projectName = applicationName;
        N.sc_id = sc_id;
    }

    public ArrayList<SrcCodeBean> a(hC projectFileManager, eC projectDataManager, BuiltInLibraryManager builtInLibraryManager) {
        ArrayList<SrcCodeBean> srcCodeBeans = new ArrayList<>();
        for (ProjectFileBean layout : projectFileManager.b()) {
            String xmlName = layout.getXmlName();
            String headCode = laki.webide.managers.HeadEditorManager.getGeneratedHtml(sc_id, layout);
            HtmlGenerator htmlGen = new HtmlGenerator(xmlName, eC.a(projectDataManager.d(xmlName)), headCode);
            String outputCode = htmlGen.generate();
            srcCodeBeans.add(new SrcCodeBean(xmlName.replace(".xml", ".html"), CommandBlock.applyCommands(xmlName, outputCode)));
        }
        return srcCodeBeans;
    }

    public String getFileSrc(String filename, hC projectFileManager, eC projectDataManager, iC projectLibraryManager) {
        a(projectLibraryManager, projectFileManager, projectDataManager);
        if (filename.endsWith(".html")) {
            ProjectFileBean fileBean = null;
            for (ProjectFileBean bean : projectFileManager.b()) {
                if (filename.equalsIgnoreCase(bean.getXmlName().replace(".xml", ".html")) || filename.equalsIgnoreCase(bean.getXmlName())) {
                    fileBean = bean;
                    break;
                }
            }
            String headCode = laki.webide.managers.HeadEditorManager.getGeneratedHtml(sc_id, (fileBean != null ? fileBean : new ProjectFileBean(0, filename)));
            HtmlGenerator htmlGen = new HtmlGenerator(filename, eC.a(projectDataManager.d(filename)), headCode);
            return CommandBlock.applyCommands(filename, htmlGen.generate());
        } else if (filename.endsWith(".css")) {
            String baseName = filename.replace(".css", "");
            ProjectFileBean fileBean = null;
            for (ProjectFileBean bean : projectFileManager.b()) {
                if (bean.getXmlName().equalsIgnoreCase(baseName + ".html") || bean.getXmlName().equalsIgnoreCase(baseName + ".xml")) {
                    fileBean = bean;
                    break;
                }
            }
            String javaName = (fileBean != null) ? fileBean.getJavaName() : filename;
            String activityName = (fileBean != null) ? fileBean.getActivityName() : ProjectFileBean.getActivityName(baseName);
            ArrayList<BlockBean> cssBlocks = CssLogicPersistenceManager.load(sc_id, javaName).blocks;
            if (cssBlocks.isEmpty()) {
                cssBlocks = projectDataManager.a(javaName, laki.webide.events.ExtCSS.EVENT_ID + "_" + laki.webide.events.ExtCSS.LISTENER_TYPE);
            }
            String customCss = new CssCodeGenerator(activityName, N, cssBlocks, false).a();
            return "/* EXTEND_CSS_START */\n" + customCss + "\n/* EXTEND_CSS_END */";
        }
        return "";
    }

    public void b(hC projectFileManager, eC projectDataManger, iC projectLibraryManager, BuiltInLibraryManager builtInLibraryManager) {
        ArrayList<SrcCodeBean> srcCodeBeans = a(projectFileManager, projectDataManger, builtInLibraryManager);
        for (SrcCodeBean bean : srcCodeBeans) {
            a(bean.srcFileName, bean.source);
        }
    }

    public void a(Context context, String str) {}
    public void aa(String iconPath) {}
    public void f() {}
    public void e() {}
    public String getXMLString() { return ""; }
    public String getXMLColor() { return ""; }
    public String getXMLStyle() { return ""; }

    public enum ExportType {
        AAB,
        SIGN_APP,
        DEBUG_APP,
        ANDROID_STUDIO,
        SOURCE_CODE_VIEWING
    }
}
