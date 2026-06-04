package laki.webide.compiler.parser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import laki.webide.utility.FileUtil;

/**
 * Smart module to read and parse external CSS files.
 * Provides contextual awareness to the HTML parser.
 */
public class CssContextManager {

    private final Map<String, Map<String, String>> classRules = new HashMap<>();
    private final Map<String, Map<String, String>> idRules = new HashMap<>();

    /**
     * Loads and parses the style.css file from the project path.
     */
    public void loadProjectStyles(String projectPath) {
        classRules.clear();
        idRules.clear();
        
        String cssPath = projectPath + "/css/style.css";
        if (!FileUtil.isExistFile(cssPath)) return;

        String cssContent = FileUtil.readFile(cssPath);
        parseCss(cssContent);
    }

    private void parseCss(String css) {
        // Basic CSS parser using Regex to find blocks like .class { prop: val; }
        Pattern pattern = Pattern.compile("([.#][a-zA-Z0-9_-]+)\\s*\\{([^}]+)\\}");
        Matcher matcher = pattern.matcher(css);

        while (matcher.find()) {
            String selector = matcher.group(1).trim();
            String propertiesRaw = matcher.group(2).trim();
            
            Map<String, String> props = new HashMap<>();
            String[] lines = propertiesRaw.split(";");
            for (String line : lines) {
                String[] kv = line.split(":");
                if (kv.length == 2) {
                    props.put(kv[0].trim().toLowerCase(), kv[1].trim().toLowerCase());
                }
            }

            if (selector.startsWith(".")) {
                classRules.put(selector.substring(1), props);
            } else if (selector.startsWith("#")) {
                idRules.put(selector.substring(1), props);
            }
        }
    }

    public Map<String, String> getRulesForClass(String className) {
        return classRules.getOrDefault(className, new HashMap<>());
    }

    public Map<String, String> getRulesForId(String id) {
        return idRules.getOrDefault(id, new HashMap<>());
    }
}
