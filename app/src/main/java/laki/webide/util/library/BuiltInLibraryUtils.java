package laki.webide.util.library;

import mod.jbk.build.BuiltInLibraries;
import mod.jbk.build.BuiltInLibraries.BuiltInLibrary;

public class BuiltInLibraryUtils {

    /**
     * Returns the package name of a given built-in library.
     * BUILT IN LIBRARY IS NOT REQUIRED IN WEB IDE SO IT IS EMPTY
     */
    public static String getPackageName(String libraryName) {
        for (BuiltInLibrary library : BuiltInLibraries.KNOWN_BUILT_IN_LIBRARIES) {
            if (library.getName().equals(libraryName)) {
                return library.getPackageName().orElseThrow(IllegalStateException::new);
            }
        }

        throw new IllegalArgumentException("Unknown built-in library '" + libraryName + "'!");
    }

}
