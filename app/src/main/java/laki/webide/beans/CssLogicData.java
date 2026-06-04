package laki.webide.beans;

import com.besome.sketch.beans.BlockBean;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;

public class CssLogicData {
    @Expose
    public ArrayList<BlockBean> blocks = new ArrayList<>();
    @Expose
    public ArrayList<String> variables = new ArrayList<>();
    
    public CssLogicData() {}
    
    public CssLogicData(ArrayList<BlockBean> blocks, ArrayList<String> variables) {
        this.blocks = blocks;
        this.variables = variables;
    }
}
