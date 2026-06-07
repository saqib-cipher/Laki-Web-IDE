package laki.webide.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import laki.webide.databinding.ProjectConfigLayoutBinding;
import laki.webide.utility.SketchwareUtil;

public class BuildSettingsBottomSheet extends BottomSheetDialogFragment {
    public static final String TAG = BuildSettingsBottomSheet.class.getSimpleName();
    
    // Missing settings constants defined here to fix compilation errors
    public static final String SETTING_ANDROID_JAR_PATH = "android_jar_path";
    public static final String SETTING_CLASSPATH = "classpath";
    public static final String SETTING_DEXER = "dexer";
    public static final String SETTING_JAVA_VERSION = "java_version";
    public static final String SETTING_NO_WARNINGS = "no_warnings";
    public static final String SETTING_NO_HTTP_LEGACY = "no_http_legacy";
    public static final String SETTING_ENABLE_LOGCAT = "enable_logcat";

    private static int totalViews = 0;
    private static final int VIEW_ANDROIR_JAR_PATH = totalViews++;
    private static final int VIEW_CLASS_PATH = totalViews++;
    private static final int VIEW_DEXER = totalViews++;
    private static final int VIEW_JAVA_VERSION = totalViews++;
    private static final int VIEW_NO_WARNINGS = totalViews++;
    private static final int VIEW_NO_HTTP_LEGACY = totalViews++;
    private static final int VIEW_ENABLE_LOGCAT = totalViews++;
    private View[] views;

    private ProjectConfigLayoutBinding binding;
    public static BuildSettingsBottomSheet newInstance(String sc_id) {
        BuildSettingsBottomSheet sheet = new BuildSettingsBottomSheet();
        Bundle arguments = new Bundle();
        arguments.putString("sc_id", sc_id);
        sheet.setArguments(arguments);
        return sheet;
    }

    public static String[] getAvailableJavaVersions() {
        return new String[]{"1.7", "1.8"};
    }

    public static void handleJavaVersionChange(String choice) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        views = new View[totalViews];
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ProjectConfigLayoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews();

        binding.noWarnings.setOnClickListener(v -> binding.cbNoWarnings.performClick());
        binding.noHttpLegacy.setOnClickListener(v -> binding.cbNoHttpLegacy.performClick());
        binding.enableLogcat.setOnClickListener(v -> binding.cbEnableLogcat.performClick());

        binding.tilAndroidJar.getEditText().setText("");
        binding.tilClasspath.getEditText().setText("");

        setRadioGroupOptions(binding.rgDexer, new String[]{"Dx", "D8"}, SETTING_DEXER, "Dx");
        setRadioGroupOptions(binding.rgJavaVersion, getAvailableJavaVersions(), SETTING_JAVA_VERSION, "1.7");

        setCheckboxValue(binding.cbNoWarnings, SETTING_NO_WARNINGS, true);
        setCheckboxValue(binding.cbNoHttpLegacy, SETTING_NO_HTTP_LEGACY, false);
        setCheckboxValue(binding.cbEnableLogcat, SETTING_ENABLE_LOGCAT, true);

        binding.btnCancel.setOnClickListener(v -> dismiss());
        binding.btnSave.setOnClickListener(v -> {
            SketchwareUtil.toast("Settings saving is disabled in Web IDE");
            dismiss();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initializeViews() {
        binding.tilAndroidJar.getEditText().setTag(SETTING_ANDROID_JAR_PATH);
        binding.tilClasspath.getEditText().setTag(SETTING_CLASSPATH);
        binding.rgDexer.setTag(SETTING_DEXER);
        binding.rgJavaVersion.setTag(SETTING_JAVA_VERSION);
        binding.cbNoWarnings.setTag(SETTING_NO_WARNINGS);
        binding.cbNoHttpLegacy.setTag(SETTING_NO_HTTP_LEGACY);
        binding.cbEnableLogcat.setTag(SETTING_ENABLE_LOGCAT);

        views[VIEW_ANDROIR_JAR_PATH] = binding.tilAndroidJar.getEditText();
        views[VIEW_CLASS_PATH] = binding.tilClasspath.getEditText();
        views[VIEW_DEXER] = binding.rgDexer;
        views[VIEW_ENABLE_LOGCAT] = binding.cbEnableLogcat;
        views[VIEW_JAVA_VERSION] = binding.rgJavaVersion;
        views[VIEW_NO_HTTP_LEGACY] = binding.cbNoHttpLegacy;
        views[VIEW_NO_WARNINGS] = binding.cbNoWarnings;
    }

    private void setRadioGroupOptions(RadioGroup radioGroup, String[] options, String key, String defaultValue) {
        radioGroup.removeAllViews();
        String value = defaultValue; // projectSettings was removed
        for (String option : options) {
            RadioButton radioButton = new RadioButton(radioGroup.getContext());
            radioButton.setText(option);
            radioButton.setId(View.generateViewId());
            radioButton.setLayoutParams(new RadioGroup.LayoutParams(0, -2, 1f));
            if (value.equals(option)) {
                radioButton.setChecked(true);
            }
            radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (!isChecked) return;
                if (key.equals(SETTING_JAVA_VERSION)) {
                    handleJavaVersionChange(option);
                }
            });
            radioGroup.addView(radioButton);
        }
    }

    private void setCheckboxValue(CheckBox checkBox, String key, boolean defaultValue) {
        checkBox.setChecked(defaultValue); // projectSettings was removed

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (key.equals(SETTING_NO_HTTP_LEGACY)) {
                    SketchwareUtil.toast("Note that this option may cause issues if RequestNetwork component is used");
                }
            }
        });
    }
}
