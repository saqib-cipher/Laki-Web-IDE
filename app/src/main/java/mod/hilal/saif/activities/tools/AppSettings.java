package mod.hilal.saif.activities.tools;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.besome.sketch.editor.manage.library.LibraryCategoryView;
import com.besome.sketch.editor.manage.library.LibraryItemView;
import com.besome.sketch.help.SystemSettingActivity;
import com.besome.sketch.lib.base.BaseAppCompatActivity;
import mod.hey.studios.util.Helper;
import mod.khaled.logcat.LogReaderActivity;
import laki.webide.R;
import laki.webide.activities.editor.component.ManageCustomComponentActivity;
import laki.webide.activities.settings.SettingsActivity;
import laki.webide.databinding.ActivityAppSettingsBinding;
import laki.webide.utility.SketchwareUtil;
import java.util.ArrayList;

public class AppSettings extends BaseAppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        enableEdgeToEdgeNoContrast();
        super.onCreate(savedInstanceState);
        var binding = ActivityAppSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.appBarLayout, (v, i) -> {
            Insets insets = i.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            v.setPadding(v.getPaddingLeft() + insets.left, v.getPaddingTop() + insets.top, v.getPaddingRight() + insets.right, v.getPaddingBottom() + insets.bottom);
            return i;
        });

        ViewCompat.setOnApplyWindowInsetsListener(binding.contentScroll, (v, i) -> {
            Insets insets = i.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), v.getPaddingBottom() + insets.bottom);
            return i;
        });

        binding.topAppBar.setNavigationOnClickListener(Helper.getBackPressedClickListener(this));
        setupPreferences(binding.content);
    }

    private void setupPreferences(ViewGroup content) {
        LibraryCategoryView managersCategory = new LibraryCategoryView(this);
        managersCategory.setTitle("Managers");
        
        managersCategory.addLibraryItem(createPreference(R.drawable.ic_mtrl_block, "Block manager", "Manage blocks", new ActivityLauncher(new Intent(this, BlocksManager.class))), true);
        managersCategory.addLibraryItem(createPreference(R.drawable.ic_mtrl_component, "Component manager", "Manage components", new ActivityLauncher(new Intent(this, ManageCustomComponentActivity.class))), true);
        managersCategory.addLibraryItem(createPreference(R.drawable.ic_mtrl_box, "Local library manager", "Manage and download local libraries", v -> SketchwareUtil.toast("Disabled in Web IDE")), true);
        managersCategory.addLibraryItem(createPreference(R.drawable.ic_mtrl_article, "Logcat reader", "View system logs", new ActivityLauncher(new Intent(this, LogReaderActivity.class))), false);

        LibraryCategoryView generalCategory = new LibraryCategoryView(this);
        generalCategory.setTitle("General");

        generalCategory.addLibraryItem(createPreference(R.drawable.ic_mtrl_settings_applications, "App settings", "Change app settings", new ActivityLauncher(new Intent(this, ConfigActivity.class))), true);
        generalCategory.addLibraryItem(createPreference(R.drawable.ic_mtrl_apk_document, "Sign an APK file", "Sign existing APK", v -> SketchwareUtil.toast("APK Signing is disabled in Web IDE")), true);
        generalCategory.addLibraryItem(createPreference(R.drawable.ic_mtrl_settings, "System settings", "Auto-save and vibrations", new ActivityLauncher(new Intent(this, SystemSettingActivity.class))), false);

        content.addView(managersCategory);
        content.addView(generalCategory);
    }

    private LibraryItemView createPreference(int icon, String title, String desc, View.OnClickListener listener) {
        LibraryItemView pref = new LibraryItemView(this);
        pref.enabled.setVisibility(View.GONE);
        pref.icon.setImageResource(icon);
        pref.title.setText(title);
        pref.description.setText(desc);
        pref.setOnClickListener(listener);
        return pref;
    }

    private View.OnClickListener openSettingsActivity(String fragmentTag) {
        return v -> {
            Intent intent = new Intent(v.getContext(), SettingsActivity.class);
            intent.putExtra(SettingsActivity.FRAGMENT_TAG_EXTRA, fragmentTag);
            v.getContext().startActivity(intent);
        };
    }

    private class ActivityLauncher implements View.OnClickListener {
        private final Intent launchIntent;
        ActivityLauncher(Intent launchIntent) { this.launchIntent = launchIntent; }
        @Override public void onClick(View v) { startActivity(launchIntent); }
    }
}
