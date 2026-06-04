package laki.webide.managers;

import android.content.Context;
import android.content.Intent;
import com.google.gson.Gson;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import a.a.a.wq;
import a.a.a.lC;
import a.a.a.yB;
import com.besome.sketch.beans.ProjectFileBean;
import laki.webide.activities.editor.HeadEditorActivity;
import laki.webide.utility.FileUtil;

public class HeadEditorManager {

    private static final String FILE_NAME = "head_structured.json";

    public static class HeadData {
        public String charset = "UTF-8";
        public String title = "";
        public List<TagPair> metas = new ArrayList<>();
        public List<TagPair> links = new ArrayList<>();
        public List<TagPair> scripts = new ArrayList<>();
    }

    public static class TagPair {
        public String key = "";
        public String value = "";
        public TagPair() {} // Required for GSON
        public TagPair(String k, String v) { this.key = k; this.value = v; }
    }

    public static void show(Context context, String sc_id, ProjectFileBean projectFile) {
        Intent intent = new Intent(context, HeadEditorActivity.class);
        intent.putExtra("sc_id", sc_id);
        intent.putExtra("project_file", projectFile);
        context.startActivity(intent);
    }

    public static String getGeneratedHtml(String sc_id, ProjectFileBean projectFile) {
        HashMap<String, Object> metadata = lC.b(sc_id);
        if (metadata == null) return "";
        
        String projectName = yB.c(metadata, "my_ws_name");
        if (projectName == null || projectName.isEmpty()) return "";
        
        String settingsFilePath = wq.f(projectName) + File.separator + "settings" + File.separator + projectFile.getXmlName() + "_" + FILE_NAME;
        HeadData data = load(settingsFilePath);
        
        StringBuilder sb = new StringBuilder();
        if (data.charset != null && !data.charset.isEmpty()) sb.append("    <meta charset=\"").append(data.charset).append("\" />\n");
        sb.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n");
        if (data.title != null && !data.title.isEmpty()) sb.append("    <title>").append(data.title).append("</title>\n");
        
        if (data.metas != null) {
            for (TagPair p : data.metas) {
                if (p != null && p.key != null && p.value != null) {
                    sb.append("    <meta name=\"").append(p.key).append("\" content=\"").append(p.value).append("\" />\n");
                }
            }
        }
        if (data.links != null) {
            for (TagPair p : data.links) {
                if (p != null && p.key != null && p.value != null) {
                    sb.append("    <link rel=\"").append(p.key).append("\" href=\"").append(p.value).append("\" />\n");
                }
            }
        }
        if (data.scripts != null) {
            for (TagPair p : data.scripts) {
                if (p != null && p.key != null && p.value != null) {
                    sb.append("    <script type=\"").append(p.key).append("\" src=\"").append(p.value).append("\"></script>\n");
                }
            }
        }
        
        return sb.toString();
    }

    public static void initializeDefaults(String sc_id, ProjectFileBean projectFile) {
        String projectPath = wq.f(yB.c(lC.b(sc_id), "my_ws_name"));
        String path = projectPath + File.separator + "settings" + File.separator + projectFile.getXmlName() + "_" + FILE_NAME;
        if (!FileUtil.isExistFile(path)) {
            HeadData d = new HeadData();
            d.title = projectFile.getXmlName().replace(".html", "");
            d.links.add(new TagPair("stylesheet", "css/global.css"));
            d.links.add(new TagPair("stylesheet", "css/" + d.title + ".css"));
            save(path, d);
        }
    }

    private static HeadData load(String path) {
        try {
            HeadData data = new Gson().fromJson(FileUtil.readFile(path), HeadData.class);
            return data != null ? data : new HeadData();
        } catch (Exception e) {
            return new HeadData();
        }
    }

    private static void save(String path, HeadData data) {
        FileUtil.makeDir(new File(path).getParent());
        FileUtil.writeFile(path, new Gson().toJson(data));
    }
}