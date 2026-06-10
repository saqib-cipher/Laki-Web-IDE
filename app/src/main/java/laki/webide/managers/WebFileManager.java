package laki.webide.managers;

import java.io.File;
import laki.webide.core.LakiFiles;
import laki.webide.utility.FileUtil;
import a.a.a.lC;
import a.a.a.yB;

/**
 * Low-level File creator using the unified LakiFiles system.
 */
public class WebFileManager {

    public static void createNewFile(String sc_id, String fileName, String type) {
        String projectName = yB.c(lC.b(sc_id), "my_ws_name");
        String projectRoot = LakiFiles.getProjectRoot(projectName, sc_id, false);
        
        if (type.equalsIgnoreCase("html")) {
            LakiFiles.initializePageSettings(projectRoot, fileName);
            LakiFiles.createInitialWebFiles(projectRoot, fileName);
        } else if (type.equalsIgnoreCase("css")) {
            // Already handled by createInitialWebFiles if type was html
            // but for standalone creation:
            FileUtil.writeFile(LakiFiles.getCssPath(projectRoot) + File.separator + fileName + ".css", "/* CSS for " + fileName + " */");
        } else if (type.equalsIgnoreCase("js")) {
            FileUtil.writeFile(projectRoot + File.separator + "js" + File.separator + fileName + ".js", "// JS for " + fileName);
        }
    }
}
