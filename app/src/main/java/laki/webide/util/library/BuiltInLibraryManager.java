package laki.webide.util.library;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import a.a.a.Jp;
import a.a.a.ProjectBuilder;
import mod.jbk.build.BuiltInLibraries;
import mod.jbk.editor.manage.library.ExcludeBuiltInLibrariesActivity;

/**
 * A class to keep track of a project's built-in libraries.
 */

public class BuiltInLibraryManager {

    private final ArrayList<Jp> libraries = new ArrayList<>();
    private final List<BuiltInLibraries.BuiltInLibrary> excludedLibraries;

    public BuiltInLibraryManager(String projectId) {
        excludedLibraries = ExcludeBuiltInLibrariesActivity.getExcludedLibraries(projectId);
    }

    public void addLibrary(String libraryName) {
        Optional<BuiltInLibraries.BuiltInLibrary> library = BuiltInLibraries.BuiltInLibrary.ofName(libraryName);
        //noinspection SimplifyOptionalCallChains because #isEmpty() isn't available on Android.

    }

    public boolean containsLibrary(String libraryName) {
        Optional<BuiltInLibraries.BuiltInLibrary> library = BuiltInLibraries.BuiltInLibrary.ofName(libraryName);
        //noinspection SimplifyOptionalCallChains because #isEmpty() isn't available on Android.
        if (!library.isPresent()) {
            return false;
        }
        return libraries.contains(new Jp(library.get().getName()));
    }

    public ArrayList<Jp> getLibraries() {
        return libraries;
    }
}
