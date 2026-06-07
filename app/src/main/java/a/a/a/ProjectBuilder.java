package a.a.a;

import android.content.Context;
import android.os.StrictMode;
import mod.hey.studios.util.SystemLogPrinter;
import laki.webide.util.library.BuiltInLibraryManager;
import laki.webide.utility.FilePathUtil;
import laki.webide.ProjectWorkspace;
import java.util.*;

public class ProjectBuilder {

    private final Context context;

    public ProjectWorkspace ProjectWorkspace;
    public FilePathUtil fpu;
    public BuiltInLibraryManager builtInLibraryManager;


    public ProjectBuilder(Context context, ProjectWorkspace yqVar) {
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        SystemLogPrinter.start();

        this.context = context;
        this.ProjectWorkspace = yqVar;
        this.fpu = new FilePathUtil();
        this.builtInLibraryManager = new BuiltInLibraryManager(yqVar.sc_id);
    }

    public ProjectBuilder(mod.jbk.build.BuildProgressReceiver buildAsyncTask, Context context, ProjectWorkspace yqVar) {
        this(context, yqVar);
    }

    public void buildBuiltInLibraryInformation() {}
    public BuiltInLibraryManager getBuiltInLibraryManager() { return builtInLibraryManager; }
 }
