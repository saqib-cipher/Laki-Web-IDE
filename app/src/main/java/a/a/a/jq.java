package a.a.a;

import static android.text.TextUtils.isEmpty;
import com.besome.sketch.beans.AdTestDeviceBean;
import com.besome.sketch.beans.ProjectLibraryBean;
import java.util.ArrayList;
import java.util.HashMap;

public class jq {
    public static final int PERMISSION_CALL_PHONE = 1;
    public static final int PERMISSION_INTERNET = 2;
    public static final int PERMISSION_VIBRATE = 4;
    public static final int PERMISSION_ACCESS_NETWORK_STATE = 8;
    public static final int PERMISSION_CAMERA = 16;
    public static final int PERMISSION_READ_EXTERNAL_STORAGE = 32;
    public static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 64;
    public static final int PERMISSION_RECORD_AUDIO = 128;
    public static final int PERMISSION_BLUETOOTH = 256;
    public static final int PERMISSION_BLUETOOTH_ADMIN = 512;
    public static final int PERMISSION_ACCESS_FINE_LOCATION = 1024;

    public String packageName = "";
    public String projectName;
    public String versionCode;
    public String versionName;
    public boolean isDebugBuild = false;
    public boolean g = false;
    public boolean isFirebaseEnabled = false;
    public boolean isFirebaseAuthUsed = false;
    public boolean isFirebaseDatabaseUsed = false;
    public boolean isFirebaseStorageUsed = false;
    public boolean isAdMobEnabled = false;
    public boolean isMapUsed = false;
    public boolean isGlideUsed = false;
    public boolean isGsonUsed = false;
    public boolean isHttp3Used = false;
    public boolean isTextToSpeechUsed = false;
    public boolean isSpeechToTextUsed = false;

    public int q = 0;
    public String bannerAdUnitId = "ca-app-pub-3940256099942544/6300978111";
    public String interstitialAdUnitId = "ca-app-pub-3940256099942544/1033173712";
    public String rewardAdUnitId = "ca-app-pub-3940256099942544/5224354917";
    public String appId = "";
    public String sc_id = "";
    public ArrayList<String> t = new ArrayList<>();
    public boolean u = false;
    public String mapApiKey = "";
    public HashMap<String, a> w = new HashMap<>();

    public boolean hasPermissions() {
        return q == 0;
    }

    public void b() {
    }

    public boolean hasPermission(int permission) {
        return (q & permission) == permission;
    }

    public void addPermission(int permission) {
        q = permission | q;
    }

    public void setupGoogleMap(ProjectLibraryBean googleMapsLibrary) {
        if (googleMapsLibrary != null && googleMapsLibrary.data != null) {
            mapApiKey = googleMapsLibrary.data;
        }
    }

    public void addPermission(String activityName, int permission) {
        if (!w.containsKey(activityName)) {
            w.put(activityName, new a());
        }
        w.get(activityName).a(permission);
        addPermission(permission);
    }

    public a a(String activityName) {
        if (!w.containsKey(activityName)) {
            w.put(activityName, new a());
        }
        return w.get(activityName);
    }

    public void setupAdmob(ProjectLibraryBean projectLibraryBean) {
        if (projectLibraryBean != null && projectLibraryBean.testDevices != null) {
            for (AdTestDeviceBean adTestDeviceBean : projectLibraryBean.testDevices) {
                t.add(adTestDeviceBean.deviceId);
            }
        }
        if (projectLibraryBean != null && projectLibraryBean.reserved1 != null && !projectLibraryBean.reserved1.isEmpty()) {
            bannerAdUnitId = projectLibraryBean.reserved1.substring(projectLibraryBean.reserved1.lastIndexOf(" : ") + 3);
        }
        if (projectLibraryBean != null && projectLibraryBean.reserved2 != null && !projectLibraryBean.reserved2.isEmpty()) {
            interstitialAdUnitId = projectLibraryBean.reserved2.substring(projectLibraryBean.reserved2.lastIndexOf(" : ") + 3);
        }
        if (projectLibraryBean != null && projectLibraryBean.reserved3 != null && !projectLibraryBean.reserved3.isEmpty()) {
            rewardAdUnitId = projectLibraryBean.reserved3.substring(projectLibraryBean.reserved3.lastIndexOf(" : ") + 3);
        }
        if (projectLibraryBean != null && projectLibraryBean.appId != null && !isEmpty(projectLibraryBean.appId)) {
            appId = projectLibraryBean.appId;
        }
    }

    public static class a {
        public boolean hasDrawer = false;
        public boolean usesFirebaseAuth = false;
        public int c = 0;
        public void a(int i) {
            c = i | c;
        }
        public boolean b(int i) {
            return (c & i) == i;
        }
        public boolean a() {
            return b(PERMISSION_CALL_PHONE)
                    || b(PERMISSION_READ_EXTERNAL_STORAGE) || b(PERMISSION_WRITE_EXTERNAL_STORAGE)
                    || b(PERMISSION_CAMERA)
                    || b(PERMISSION_RECORD_AUDIO)
                    || b(PERMISSION_ACCESS_FINE_LOCATION);
        }
    }
}
