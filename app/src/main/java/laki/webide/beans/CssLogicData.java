package laki.webide.beans;

import laki.webide.core.BlockBean;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;

/**
 * Data wrapper for CSS Logic, using Laki core beans.
 */
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
