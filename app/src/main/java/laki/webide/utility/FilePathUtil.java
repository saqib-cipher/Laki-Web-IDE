package laki.webide.utility;

import android.os.Environment;
import java.io.File;
import a.a.a.lC;
import a.a.a.yB;
import laki.webide.core.LakiFiles;
import java.util.HashMap;

public class FilePathUtil {
    private static final HashMap<String, String> rootCache = new HashMap<>();

    public static String getProjectRoot(String sc_id) {
        if (rootCache.containsKey(sc_id)) return rootCache.get(sc_id);
        
        HashMap<String, Object> metadata = lC.b(sc_id);
        String name = sc_id;
        String projectName = "";
        if (metadata != null) {
            projectName = yB.c(metadata, "my_ws_name");
        }
        
        String path = LakiFiles.getProjectRoot(projectName, sc_id, false);
        rootCache.put(sc_id, path);
        return path;
    }

    public static String getLastCompileLogPath(String sc_id) {
        return new File(getProjectRoot(sc_id), "compile_log").getAbsolutePath();
    }

    public String getPathPermission(String sc_id) {
        return new File(getProjectRoot(sc_id), "settings/permission").getAbsolutePath();
    }

    public String getPathImport(String sc_id) {
        return new File(getProjectRoot(sc_id), "settings/import").getAbsolutePath();
    }

    public String getPathBroadcast(String sc_id) {
        return new File(getProjectRoot(sc_id), "js/broadcast").getAbsolutePath();
    }

    public String getPathSvg(String sc_id) {
        return new File(getProjectRoot(sc_id), "asset/converted-vectors").getAbsolutePath();
    }

    public String getSvgFullPath(String sc_id, String resName) {
        return new File(getPathSvg(sc_id) + File.separator + resName + ".svg").getAbsolutePath();
    }

    public String getPathService(String sc_id) {
        return new File(getProjectRoot(sc_id), "js/service").getAbsolutePath();
    }

    public String getPathAssets(String sc_id) {
        return new File(getProjectRoot(sc_id), "asset").getAbsolutePath();
    }

    public String getPathJava(String sc_id) {
        return new File(getProjectRoot(sc_id), "css").getAbsolutePath();
    }

    public String getPathResource(String sc_id) {
        return new File(getProjectRoot(sc_id), "res").getAbsolutePath();
    }

    public String getPathProguard(String sc_id) {
        return new File(getProjectRoot(sc_id), "settings/proguard-rules.pro").getAbsolutePath();
    }

    public String getPathLocalLibrary(String sc_id) {
        return new File(getProjectRoot(sc_id), "settings/local_library").getAbsolutePath();
    }

    public String getJarPathLocalLibraryUser(String sc_id) {
        return new File(getProjectRoot(sc_id), "settings/library/jar").getAbsolutePath();
    }

    public String getDexPathLocalLibraryUser(String sc_id) {
        return new File(getProjectRoot(sc_id), "settings/library/dex").getAbsolutePath();
    }

    public String getResPathLocalLibraryUser(String sc_id) {
        return new File(getProjectRoot(sc_id), "settings/library/res").getAbsolutePath();
    }

    public String getManifestJava(String sc_id) {
        return new File(getProjectRoot(sc_id), "js/manifest").getAbsolutePath();
    }

    public String getManifestBroadcast(String sc_id) {
        return new File(getProjectRoot(sc_id), "js/broadcast_manifest").getAbsolutePath();
    }

    public String getPathNativelibs(String sc_id) {
        return new File(getProjectRoot(sc_id), "settings/native_libs").getAbsolutePath();
    }

    public String getManifestService(String sc_id) {
        return new File(getProjectRoot(sc_id), "js/service_manifest").getAbsolutePath();
    }
}
