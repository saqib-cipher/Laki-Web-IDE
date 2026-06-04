package laki.webide.managers;

import java.io.File;
import laki.webide.utility.FileUtil;
import a.a.a.wq;

public class WebFileManager {

    public static void createNewFile(String projectName, String fileName, String type) {
        String projectPath = wq.f(projectName);
        String filePath;
        String content = "";
        
        if (type.equalsIgnoreCase("html")) {
            filePath = projectPath + File.separator + fileName + ".html";
            content = "<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n" +
                    "    <!-- HEAD_START -->\n" +
                    "    <meta charset=\"UTF-8\" />\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                    "    <title>" + fileName + "</title>\n" +
                    "    <link rel=\"stylesheet\" href=\"css/global.css\" />\n" +
                    "    <link rel=\"stylesheet\" href=\"css/" + fileName + ".css\" />\n" +
                    "    <!-- HEAD_END -->\n" +
                    "</head>\n<body>\n" +
                    "</body>\n</html>";
        } else if (type.equalsIgnoreCase("css")) {
            filePath = projectPath + File.separator + "css" + File.separator + fileName + ".css";
        } else if (type.equalsIgnoreCase("js")) {
            filePath = projectPath + File.separator + "js" + File.separator + fileName + ".js";
        } else {
            filePath = projectPath + File.separator + fileName;
        }
        
        FileUtil.writeFile(filePath, content);
    }
}
