package a.a.a;

import com.besome.sketch.beans.ProjectFileBean;
import java.util.ArrayList;
import laki.webide.util.library.BuiltInLibraryManager;
import laki.webide.xml.XmlBuilder;

public class Ix {
    private final BuiltInLibraryManager builtInLibraryManager;
    public XmlBuilder a = new XmlBuilder("manifest");
    public ArrayList<ProjectFileBean> b;
    public jq c;

    public Ix(jq jq, ArrayList<ProjectFileBean> projectFileBeans, BuiltInLibraryManager builtInLibraryManager) {
        c = jq;
        b = projectFileBeans;
        this.builtInLibraryManager = builtInLibraryManager;
    }

    public void setProjectWorkspace(laki.webide.ProjectWorkspace yqVar) {
    }

    /**
     * Builds an AndroidManifest.
     * Web IDE: Manifest generation is disabled.
     */
    public String a() {
        return "";
    }
}
