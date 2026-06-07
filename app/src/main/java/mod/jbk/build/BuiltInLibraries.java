package mod.jbk.build;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.List;
import java.util.Optional;

import laki.webide.SketchApplication;

public class BuiltInLibraries {
    public static final File EXTRACTED_COMPILE_ASSETS_PATH = new File(SketchApplication.getContext().getFilesDir(), "libs");
    public static final File EXTRACTED_BUILT_IN_LIBRARIES_PATH = new File(EXTRACTED_COMPILE_ASSETS_PATH, "libs");
    public static String WAVE_SIDE_BAR = "wave-side-bar";

    public static final BuiltInLibrary[] KNOWN_BUILT_IN_LIBRARIES = {

            new BuiltInLibrary(WAVE_SIDE_BAR, List.of(), "com.sayuti.lib"),

    };

    public static class BuiltInLibrary implements Parcelable {
        public static final Creator<BuiltInLibrary> CREATOR = new Creator<>() {
            @Override
            public BuiltInLibrary createFromParcel(Parcel in) {
                return new BuiltInLibrary(in);
            }

            @Override
            public BuiltInLibrary[] newArray(int size) {
                return new BuiltInLibrary[size];
            }
        };
        private final String name;
        private final List<String> dependencyNames;
        private final String packageName;
        private final boolean hasResources;

        /**
         * @param packageName Can be <code>null</code> for no resources, though then
         *                    {@link #BuiltInLibrary(String, List)} is advised.
         */
        public BuiltInLibrary(String name, List<String> dependencyNames, String packageName) {
            this.name = name;
            this.dependencyNames = dependencyNames;
            this.packageName = packageName;
            hasResources = packageName != null;
        }

        /**
         * Constructs a {@link BuiltInLibrary} with specified dependencies but no resources.
         */
        public BuiltInLibrary(String name, List<String> dependencyNames) {
            this(name, dependencyNames, null);
        }

        /**
         * Constructs a {@link BuiltInLibrary} with no dependencies and resources.
         */
        protected BuiltInLibrary(Parcel in) {
            name = in.readString();
            dependencyNames = in.createStringArrayList();
            packageName = in.readString();
            hasResources = in.readInt() != 0;
        }

        public String getName() {
            return name;
        }

        public Optional<String> getPackageName() {
            return packageName == null ? Optional.empty() : Optional.of(packageName);
        }

        @Override
        @NonNull
        public String toString() {
            return "BuiltInLibrary{" +
                    "name='" + name + '\'' +
                    ", dependencyNames=" + dependencyNames +
                    ", packageName='" + packageName + '\'' +
                    ", hasResources=" + hasResources +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeStringList(dependencyNames);
            dest.writeString(packageName);
            dest.writeInt(hasResources ? 1 : 0);
        }
    }
}
