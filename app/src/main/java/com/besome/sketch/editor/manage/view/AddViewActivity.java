package com.besome.sketch.editor.manage.view;

import android.content.Intent;
import android.os.Bundle;

import com.besome.sketch.beans.ProjectFileBean;
import com.besome.sketch.lib.base.BaseAppCompatActivity;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;

import a.a.a.YB;
import a.a.a.bB;
import a.a.a.uq;
import mod.hey.studios.util.Helper;
import laki.webide.R;
import laki.webide.databinding.ManageScreenActivityAddTempBinding;

public class AddViewActivity extends BaseAppCompatActivity {

    public static final int REQUEST_CODE_EDIT = 265;
    public static final int REQUEST_CODE_ADD = 264;
    
    public static final int VIEW_TYPE_ACTIVITY = 0;
    public static final int VIEW_TYPE_FRAGMENT = 1;

    private YB nameValidator;
    private int requestCode;
    private ProjectFileBean projectFileBean;
    private ArrayList<String> screenNames;
    private ManageScreenActivityAddTempBinding binding;

    private boolean isValid(YB validator) {
        return validator.b();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ManageScreenActivityAddTempBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setTitle("Create new file");
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        Intent intent1 = getIntent();
        screenNames = intent1.getStringArrayListExtra("screen_names");
        requestCode = intent1.getIntExtra("request_code", REQUEST_CODE_ADD);
        projectFileBean = intent1.getParcelableExtra("project_file");

        if (projectFileBean != null) {
            binding.toolbar.setTitle("Edit " + projectFileBean.fileName);
            binding.btnSave.setText("Update");
        }

        binding.btnSave.setOnClickListener(v -> {
            if (REQUEST_CODE_EDIT == requestCode) {
                handleEditFile();
            } else if (isValid(nameValidator)) {
                handleCreateFile();
            }
        });

        binding.btnCancel.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        if (requestCode == REQUEST_CODE_EDIT) {
            handleEditModeInitialization();
        } else {
            handleCreateModeInitialization();
        }
    }

    private void handleEditFile() {
        // Defaults for Web IDE
        projectFileBean.orientation = 0;
        projectFileBean.keyboardSetting = 0;
        projectFileBean.options = ProjectFileBean.OPTION_ACTIVITY_TOOLBAR;
        
        Intent intent = new Intent();
        intent.putExtra("project_file", projectFileBean);
        setResult(RESULT_OK, intent);
        bB.a(getApplicationContext(), "File updated successfully", bB.TOAST_NORMAL).show();
        finish();
    }

    private void handleCreateFile() {
        String fileName = Helper.getText(binding.edName);
        int fileType = getSelectedButtonIndex(binding.viewTypeSelector) == 1 ? 
                       ProjectFileBean.PROJECT_FILE_TYPE_FRAGMENT : 
                       ProjectFileBean.PROJECT_FILE_TYPE_ACTIVITY;
                       
        // Create file with default Android options (they are ignored in Web compiler)
        ProjectFileBean projectFileBean = new ProjectFileBean(fileType, fileName, 0, 0, true, false, false, false);
        
        Intent intent = new Intent();
        intent.putExtra("project_file", projectFileBean);
        setResult(RESULT_OK, intent);
        bB.a(getApplicationContext(), "File created successfully", bB.TOAST_NORMAL).show();
        finish();
    }

    private void handleEditModeInitialization() {
        nameValidator = new YB(getApplicationContext(), binding.tiName, uq.b, new ArrayList<>(), projectFileBean.fileName);
        binding.edName.setText(projectFileBean.fileName);
        binding.edName.setEnabled(false);
        binding.tiName.setEnabled(false);
        
        if (projectFileBean.fileType == ProjectFileBean.PROJECT_FILE_TYPE_FRAGMENT) {
            binding.viewTypeSelector.check(R.id.select_fragment);
        } else {
            binding.viewTypeSelector.check(R.id.select_activity);
        }
        binding.viewTypeSelector.setEnabled(false);
    }

    private void handleCreateModeInitialization() {
        nameValidator = new YB(getApplicationContext(), binding.tiName, uq.b, screenNames);
    }

    public int getSelectedButtonIndex(MaterialButtonToggleGroup toggleGroup) {
        for (int i = 0; i < toggleGroup.getChildCount(); i++) {
            var button = toggleGroup.getChildAt(i);
            if (toggleGroup.getCheckedButtonIds().contains(button.getId())) {
                return i;
            }
        }
        return -1;
    }
}
