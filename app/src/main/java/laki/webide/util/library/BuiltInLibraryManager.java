package laki.webide.util.library;

import java.util.ArrayList;
import a.a.a.Jp;

/**
 * A class to keep track of a project's built-in libraries.
 */
public class BuiltInLibraryManager {

    private final ArrayList<Jp> libraries = new ArrayList<>();

    public BuiltInLibraryManager(String projectId) {
    }

    public void addLibrary(String libraryName) {
        // No libraries to add in Web IDE
    }

    public boolean containsLibrary(String libraryName) {
        return false;
    }

    public ArrayList<Jp> getLibraries() {
        return libraries;
    }
}
