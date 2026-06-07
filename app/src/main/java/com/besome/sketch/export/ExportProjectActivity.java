package com.besome.sketch.export;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.Toolbar;
import com.besome.sketch.lib.base.BaseAppCompatActivity;
import mod.hey.studios.util.Helper;
import laki.webide.R;

public class ExportProjectActivity extends BaseAppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.export_project);

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            View logo = findViewById(R.id.layout_main_logo);
            if (logo != null) logo.setVisibility(View.GONE);
            getSupportActionBar().setTitle(Helper.getResString(R.string.myprojects_export_project_actionbar_title));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            toolbar.setNavigationOnClickListener(Helper.getBackPressedClickListener(this));
        }
    }
}
