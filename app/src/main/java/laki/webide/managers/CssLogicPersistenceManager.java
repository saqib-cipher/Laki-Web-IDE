package laki.webide.managers;

import laki.webide.core.BlockBean;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import laki.webide.beans.CssLogicData;
import laki.webide.core.LakiFiles;
import laki.webide.utility.FilePathUtil;
import laki.webide.utility.FileUtil;
import a.a.a.lC;
import a.a.a.yB;

/**
 * Optimized Persistence Manager for CSS Logic.
 * Uses Short-Key Trees to store logic structure without visual metadata bloat.
 */
public class CssLogicPersistenceManager {

    /**
     * Recursive Short-Key structure for tiny storage.
     */
    private static class ShortBlock {
        @Expose @SerializedName("o") public String opCode;
        @Expose @SerializedName("p") public List<String> params;
        @Expose @SerializedName("pt") public List<String> pTypes;
        @Expose @SerializedName("s1") public ShortBlock s1;
        @Expose @SerializedName("s2") public ShortBlock s2;
        @Expose @SerializedName("n") public ShortBlock next;
        @Expose @SerializedName("r") public Map<String, ShortBlock> reporters;
        
        public ShortBlock() {}
        
        public ShortBlock(BlockBean bean, Map<Integer, BlockBean> flatMap) {
            this.opCode = bean.opCode;
            this.params = new ArrayList<>(bean.parameters);
            this.pTypes = new ArrayList<>(bean.paramTypes);
            this.reporters = new HashMap<>();
            
            // Recurse into sub-stacks and next blocks
            if (bean.subStack1 >= 0 && flatMap.containsKey(bean.subStack1)) {
                this.s1 = new ShortBlock(flatMap.get(bean.subStack1), flatMap);
            }
            if (bean.subStack2 >= 0 && flatMap.containsKey(bean.subStack2)) {
                this.s2 = new ShortBlock(flatMap.get(bean.subStack2), flatMap);
            }
            if (bean.nextBlock >= 0 && flatMap.containsKey(bean.nextBlock)) {
                this.next = new ShortBlock(flatMap.get(bean.nextBlock), flatMap);
            }

            // Recurse into reporter blocks (parameters starting with @)
            for (String p : bean.parameters) {
                if (p != null && p.startsWith("@")) {
                    try {
                        int reporterId = Integer.parseInt(p.substring(1));
                        if (flatMap.containsKey(reporterId)) {
                            reporters.put(p, new ShortBlock(flatMap.get(reporterId), flatMap));
                        }
                    } catch (Exception ignored) {}
                }
            }
        }
    }

    public static void save(String sc_id, String filename, ArrayList<BlockBean> blocks, ArrayList<String> variables) {
        HashMap<String, Object> metadata = lC.b(sc_id);
        if (metadata == null) return;
        
        String projectName = yB.c(metadata, "my_ws_name");
        String projectRoot = LakiFiles.getProjectRoot(projectName, sc_id, false);
        String path = LakiFiles.getPageExtendBlocksPath(projectRoot, filename);

        // 1. Map blocks for tree construction
        Map<Integer, BlockBean> flatMap = new HashMap<>();
        for (BlockBean b : blocks) {
            try { flatMap.put(Integer.parseInt(b.id), b); } catch (Exception ignored) {}
        }
        
        // 2. Identify root blocks (blocks that are not children of any other block)
        List<BlockBean> roots = new ArrayList<>();
        for (BlockBean b : blocks) {
            boolean isChild = false;
            int myId = Integer.parseInt(b.id);
            for (BlockBean other : blocks) {
                if (other.nextBlock == myId || other.subStack1 == myId || other.subStack2 == myId) {
                    isChild = true;
                    break;
                }
                // Also check if this block is a reporter inside a parameter
                for (String p : other.parameters) {
                    if (p.equals("@" + myId)) {
                        isChild = true;
                        break;
                    }
                }
            }
            if (!isChild) roots.add(b);
        }

        // 3. Convert roots to Short-Key Tree
        List<ShortBlock> shortRoots = new ArrayList<>();
        for (BlockBean root : roots) {
            shortRoots.add(new ShortBlock(root, flatMap));
        }

        // 4. Save to JSON
        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("v", variables); // CSS Variables
        wrapper.put("b", shortRoots);
        
        FileUtil.writeFile(path, new Gson().toJson(wrapper));
    }

    public static CssLogicData load(String sc_id, String filename) {
        HashMap<String, Object> metadata = lC.b(sc_id);
        if (metadata == null) return new CssLogicData();
        
        String projectName = yB.c(metadata, "my_ws_name");
        String projectRoot = LakiFiles.getProjectRoot(projectName, sc_id, false);
        String path = LakiFiles.getPageExtendBlocksPath(projectRoot, filename);
        
        if (!FileUtil.isExistFile(path)) {
            // Migration Fallbacks for inconsistent extensions (.java, .css)
            String pathJava = LakiFiles.getPageExtendBlocksPath(projectRoot, filename + ".java");
            String pathCss = LakiFiles.getPageExtendBlocksPath(projectRoot, filename + ".css");
            if (FileUtil.isExistFile(pathJava)) path = pathJava;
            else if (FileUtil.isExistFile(pathCss)) path = pathCss;
            else return new CssLogicData();
        }
        
        try {
            String json = FileUtil.readFile(path);
            Map<String, Object> wrapper = new Gson().fromJson(json, Map.class);
            
            ArrayList<String> variables = (ArrayList<String>) wrapper.get("v");
            List<Map<String, Object>> shortRoots = (List<Map<String, Object>>) wrapper.get("b");
            
            ArrayList<BlockBean> flatBlocks = new ArrayList<>();
            IdGenerator idGen = new IdGenerator();
            
            if (shortRoots != null) {
                for (Map<String, Object> sr : shortRoots) {
                    inflate(sr, flatBlocks, idGen);
                }
            }
            
            // Automatic Variable Sync: Extract any CSS variables used in blocks
            if (variables == null) variables = new ArrayList<>();
            syncVariables(flatBlocks, variables);
            
            return new CssLogicData(flatBlocks, variables);
        } catch (Exception e) {
            return new CssLogicData();
        }
    }

    private static int inflate(Map<String, Object> data, List<BlockBean> flat, IdGenerator idGen) {
        String op = (String) data.get("o");
        List<String> ps = (List<String>) data.get("p");
        List<String> pt = (List<String>) data.get("pt");
        Map<String, Object> reps = (Map<String, Object>) data.get("r");

        int myId = idGen.getNextId();
        
        // Lookup definition for custom block specs and types
        laki.webide.core.CreateBlock def = laki.webide.core.CreateBlock.getDefinition(op);
        String spec = (def != null) ? def.spec : "";
        String type = (def != null) ? def.type : " ";
        int color = (def != null) ? def.getColor() : 0xFF607D8B; // Default to OTHER color
        
        // Create custom Laki BlockBean
        BlockBean bean = new BlockBean(String.valueOf(myId), spec, type, op);
        bean.color = color;
        if (def != null) bean.category = def.category;

        if (ps != null) {
            bean.parameters = new ArrayList<>(ps);
            // Fix reporter IDs in parameters
            if (reps != null) {
                for (int i = 0; i < bean.parameters.size(); i++) {
                    String p = bean.parameters.get(i);
                    if (p != null && p.startsWith("@") && reps.containsKey(p)) {
                        int newReporterId = inflate((Map<String, Object>) reps.get(p), flat, idGen);
                        bean.parameters.set(i, "@" + newReporterId);
                    }
                }
            }
        }

        if (pt != null) bean.paramTypes = new ArrayList<>(pt);
        flat.add(bean);
        
        // Handle nesting via recursion
        if (data.containsKey("s1") && data.get("s1") != null) {
            bean.subStack1 = inflate((Map<String, Object>) data.get("s1"), flat, idGen);
        }
        if (data.containsKey("s2") && data.get("s2") != null) {
            bean.subStack2 = inflate((Map<String, Object>) data.get("s2"), flat, idGen);
        }
        if (data.containsKey("n") && data.get("n") != null) {
            bean.nextBlock = inflate((Map<String, Object>) data.get("n"), flat, idGen);
        }
        
        return myId;
    }

    /**
     * Scans parameters for CSS Variables (--variable) and ensures they are in the list.
     */
    private static void syncVariables(List<BlockBean> blocks, List<String> variables) {
        for (BlockBean b : blocks) {
            for (String p : b.parameters) {
                if (p != null && p.startsWith("--") && !variables.contains(p)) {
                    variables.add(p);
                }
            }
        }
    }
    
    private static class IdGenerator {
        private int id = 1;
        public int getNextId() { return id++; }
    }
}
