package laki.webide.compiler;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import a.a.a.*;
import com.besome.sketch.beans.BlockBean;
import com.besome.sketch.beans.ProjectFileBean;

import laki.webide.utility.FileUtil;

public class WebCompiler {

    /**
     * Synchronizes the logic blocks from the "Header Area" with the actual index.html file.
     */
    public static void syncHeader(String scId, ProjectFileBean projectFile, ArrayList<BlockBean> blocks) {
        // Run for all projects in the Web IDE
        var metadata = lC.b(scId);
        if (metadata == null) {
            return;
        }

        // 1. Generate the HTML snippet from the blocks
        Fx generator = new Fx(projectFile.getActivityName(), new jq(), blocks, false);
        String generatedHtml = generator.a();

        // 2. Identify the file to update
        String projectName = yB.c(metadata, "my_ws_name");
        String projectPath = wq.f(projectName);
        String htmlFileName = projectFile.getXmlName();
        String filePath = projectPath + File.separator + htmlFileName;

        if (FileUtil.isExistFile(filePath)) {
            String content = FileUtil.readFile(filePath);
            
            String startMarker = "<!-- HEAD_START -->";
            String endMarker = "<!-- HEAD_END -->";
            
            String replacement = startMarker + "\n" + generatedHtml + "\n" + endMarker;
            
            String newContent;
            if (content.contains(startMarker) && content.contains(endMarker)) {
                // If markers exist, replace content between them
                newContent = content.replaceAll(
                    Pattern.quote(startMarker) + "[\\s\\S]*" + Pattern.quote(endMarker),
                    Matcher.quoteReplacement(replacement)
                );
            } else if (content.contains("</head>")) {
                // If markers don't exist, inject them before </head>
                newContent = content.replace("</head>", replacement + "\n</head>");
            } else {
                // Fallback: just append (not ideal, but safe)
                newContent = content + "\n" + replacement;
            }
            
            FileUtil.writeFile(filePath, newContent);
        }
    }

    /**
     * Synchronizes the logic blocks from the "Extend CSS" area with the main.css file.
     */
    public static void syncCSS(String scId, ProjectFileBean projectFile, ArrayList<BlockBean> blocks) {
        var metadata = lC.b(scId);
        if (metadata == null) {
            return;
        }

        Fx generator = new Fx(projectFile.getActivityName(), new jq(), blocks, false);
        String generatedCss = generator.a();

        String projectName = yB.c(metadata, "my_ws_name");
        String projectPath = wq.f(projectName);
        String filePath = projectPath + File.separator + "css" + File.separator + projectFile.getJavaName();

        if (FileUtil.isExistFile(filePath)) {
            // In a real CSS IDE, you might want to append or use markers. 
            // For now, let's append it to the bottom of the existing CSS.
            String currentCss = FileUtil.readFile(filePath);
            
            String startMarker = "/* EXTEND_CSS_START */";
            String endMarker = "/* EXTEND_CSS_END */";
            
            String replacement = startMarker + "\n" + generatedCss + "\n" + endMarker;
            
            String newContent;
            if (currentCss.contains(startMarker) && currentCss.contains(endMarker)) {
                newContent = currentCss.replaceAll(
                    Pattern.quote(startMarker) + "[\\s\\S]*" + Pattern.quote(endMarker),
                    Matcher.quoteReplacement(replacement)
                );
            } else {
                newContent = currentCss + "\n\n" + replacement;
            }
            
            FileUtil.writeFile(filePath, newContent);
        }
    }
}
