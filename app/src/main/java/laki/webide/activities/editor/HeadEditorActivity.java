package laki.webide.activities.editor;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.besome.sketch.beans.ProjectFileBean;
import com.besome.sketch.lib.base.BaseAppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import a.a.a.lC;
import a.a.a.wq;
import a.a.a.yB;
import laki.webide.R;
import laki.webide.managers.HeadEditorManager;
import laki.webide.managers.WebProjectSyncManager;
import laki.webide.utility.FileUtil;
import laki.webide.databinding.ActivityHeadEditorBinding;
import laki.webide.databinding.ItemHeadTagRowBinding;

public class HeadEditorActivity extends BaseAppCompatActivity {

    private ActivityHeadEditorBinding binding;
    private String sc_id;
    private ProjectFileBean projectFile;
    private String settingsFilePath;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHeadEditorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sc_id = getIntent().getStringExtra("sc_id");
        projectFile = getIntent().getParcelableExtra("project_file");

        if (sc_id == null || projectFile == null) {
            finish();
            return;
        }

        setupToolbar();
        loadData();
        setupListeners();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void loadData() {
        String projectPath = wq.f(yB.c(lC.b(sc_id), "my_ws_name"));
        settingsFilePath = projectPath + File.separator + "settings" + File.separator + projectFile.getXmlName() + "_" + "head_structured.json";

        HeadEditorManager.HeadData data = load(settingsFilePath);
        if (data.title == null || data.title.isEmpty()) data.title = projectFile.getXmlName().replace(".html", "");
        
        binding.etCharset.setText(data.charset);
        binding.etTitle.setText(data.title);

        if (data.metas != null) {
            for (HeadEditorManager.TagPair p : data.metas) {
                addTagRow(binding.metaContainer, p.key, p.value, "Name", "Content");
            }
        }
        if (data.links != null) {
            for (HeadEditorManager.TagPair p : data.links) {
                addTagRow(binding.linkContainer, p.key, p.value, "Rel", "Href");
            }
        }
        if (data.scripts != null) {
            for (HeadEditorManager.TagPair p : data.scripts) {
                addTagRow(binding.scriptContainer, p.key, p.value, "Type", "Src");
            }
        }
    }

    private void setupListeners() {
        binding.btnAddMeta.setOnClickListener(v -> addTagRow(binding.metaContainer, "", "", "Name", "Content"));
        binding.btnAddLink.setOnClickListener(v -> addTagRow(binding.linkContainer, "stylesheet", "", "Rel", "Href"));
        binding.btnAddScript.setOnClickListener(v -> addTagRow(binding.scriptContainer, "text/javascript", "", "Type", "Src"));
    }

    private void addTagRow(LinearLayout container, String key, String value, String hintKey, String hintValue) {
        ItemHeadTagRowBinding rowBinding = ItemHeadTagRowBinding.inflate(getLayoutInflater(), container, false);
        rowBinding.tilKey.setHint(hintKey);
        rowBinding.tilValue.setHint(hintValue);
        rowBinding.etKey.setText(key);
        rowBinding.etValue.setText(value);
        rowBinding.btnDelete.setOnClickListener(v -> container.removeView(rowBinding.getRoot()));
        container.addView(rowBinding.getRoot());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.head_editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            saveData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveData() {
        HeadEditorManager.HeadData newData = new HeadEditorManager.HeadData();
        newData.charset = binding.etCharset.getText().toString();
        newData.title = binding.etTitle.getText().toString();
        newData.metas = collect(binding.metaContainer);
        newData.links = collect(binding.linkContainer);
        newData.scripts = collect(binding.scriptContainer);
        
        FileUtil.makeDir(new File(settingsFilePath).getParent());
        FileUtil.writeFile(settingsFilePath, new Gson().toJson(newData));
        
        WebProjectSyncManager.syncCurrentFile(this, sc_id, projectFile);
        finish();
    }

    private List<HeadEditorManager.TagPair> collect(LinearLayout container) {
        List<HeadEditorManager.TagPair> list = new ArrayList<>();
        for (int i = 0; i < container.getChildCount(); i++) {
            View rowView = container.getChildAt(i);
            TextInputEditText etKey = rowView.findViewById(R.id.et_key);
            TextInputEditText etValue = rowView.findViewById(R.id.et_value);
            list.add(new HeadEditorManager.TagPair(etKey.getText().toString(), etValue.getText().toString()));
        }
        return list;
    }

    private HeadEditorManager.HeadData load(String path) {
        try {
            HeadEditorManager.HeadData data = new Gson().fromJson(FileUtil.readFile(path), HeadEditorManager.HeadData.class);
            return data != null ? data : new HeadEditorManager.HeadData();
        } catch (Exception e) {
            return new HeadEditorManager.HeadData();
        }
    }
}