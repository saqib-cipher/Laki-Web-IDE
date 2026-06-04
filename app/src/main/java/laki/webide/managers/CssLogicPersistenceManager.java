package laki.webide.managers;

import com.besome.sketch.beans.BlockBean;
import com.google.gson.Gson;
import java.io.File;
import java.util.ArrayList;
import laki.webide.beans.CssLogicData;
import laki.webide.utility.FileUtil;

public class CssLogicPersistenceManager {

    private static final String BASE_PATH = FileUtil.getExternalStorageDir() + "/.lakiwebsites/data/%s/files/logic_css/";

    public static void save(String sc_id, String filename, ArrayList<BlockBean> blocks, ArrayList<String> variables) {
        String path = String.format(BASE_PATH, sc_id);
        FileUtil.makeDir(path);
        
        CssLogicData data = new CssLogicData(blocks, variables);
        String json = new Gson().toJson(data);
        FileUtil.writeFile(path + filename + ".css_blocks", json);
    }

    public static CssLogicData load(String sc_id, String filename) {
        String path = String.format(BASE_PATH, sc_id) + filename + ".css_blocks";
        if (FileUtil.isExistFile(path)) {
            String json = FileUtil.readFile(path);
            try {
                return new Gson().fromJson(json, CssLogicData.class);
            } catch (Exception e) {
                return new CssLogicData();
            }
        }
        return new CssLogicData();
    }
}
