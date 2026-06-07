package a.a.a;

import java.util.Objects;

/**
 * An object representing a built-in library.
 */
public class Jp {
    private final String name;

    public Jp(String libraryName) {
        name = libraryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jp jp = (Jp) o;
        return name.equals(jp.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return "";
    }

    public boolean hasResources() {
        return false;
    }

    public boolean hasAssets() {
        return false;
    }
}
