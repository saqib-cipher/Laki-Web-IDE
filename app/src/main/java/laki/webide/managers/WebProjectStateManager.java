package laki.webide.managers;

import android.content.Context;
import com.besome.sketch.beans.ProjectFileBean;
import com.besome.sketch.beans.ViewBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.lang.reflect.Type;

import laki.webide.core.LakiFiles;
import laki.webide.utility.FileUtil;
import laki.webide.utility.SketchwareUtil;

public class WebProjectStateManager {

    public static void saveProjectState(Context context, String sc_id, ProjectFileBean projectFile, ArrayList<ViewBean> viewBeans) {
        if (projectFile == null || viewBeans == null) return;

        String projectName = a.a.a.yB.c(a.a.a.lC.b(sc_id), "my_ws_name");
        String projectRoot = LakiFiles.getProjectRoot(projectName, sc_id, false);
        String tagsPath = LakiFiles.getPageHtmlTagsPath(projectRoot, projectFile.getXmlName());

        ArrayList<ViewBean> cleanViews = SketchwareUtil.sanitizeViewBeans(viewBeans);
        FileUtil.writeFile(tagsPath, new Gson().toJson(cleanViews));
    }

    public static ArrayList<ViewBean> loadProjectState(Context context, String sc_id, ProjectFileBean projectFile) {
        if (projectFile == null) return new ArrayList<>();

        String projectName = a.a.a.yB.c(a.a.a.lC.b(sc_id), "my_ws_name");
        String projectRoot = LakiFiles.getProjectRoot(projectName, sc_id, false);
        String tagsPath = LakiFiles.getPageHtmlTagsPath(projectRoot, projectFile.getXmlName());

        if (FileUtil.isExistFile(tagsPath)) {
            String json = FileUtil.readFile(tagsPath);
            if (json != null && !json.trim().isEmpty()) {
                try {
                    Type type = new TypeToken<ArrayList<ViewBean>>(){}.getType();
                    ArrayList<ViewBean> loadedViews = new Gson().fromJson(json, type);
                    return SketchwareUtil.sanitizeViewBeans(loadedViews);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return new ArrayList<>();
    }

}
