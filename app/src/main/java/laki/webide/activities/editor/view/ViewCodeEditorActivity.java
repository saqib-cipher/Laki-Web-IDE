package laki.webide.activities.editor.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;

import com.besome.sketch.beans.HistoryViewBean;
import com.besome.sketch.beans.ProjectFileBean;
import com.besome.sketch.beans.ProjectLibraryBean;
import com.besome.sketch.beans.ViewBean;
import com.besome.sketch.lib.base.BaseAppCompatActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import a.a.a.cC;
import a.a.a.jC;
import io.github.rosemoe.sora.widget.CodeEditor;
import mod.hey.studios.util.Helper;
import laki.webide.R;
import laki.webide.activities.preview.LayoutPreviewActivity;
import laki.webide.databinding.ViewCodeEditorBinding;
import laki.webide.managers.inject.InjectRootLayoutManager;
import laki.webide.compiler.HtmlParser;
import laki.webide.ProjectWorkspace;
import java.util.ArrayList;
import laki.webide.utility.EditorUtils;
import laki.webide.utility.SketchwareUtil;

public class ViewCodeEditorActivity extends BaseAppCompatActivity {
    private ViewCodeEditorBinding binding;
    private CodeEditor editor;

    private SharedPreferences prefs;

    private String sc_id;

    private String content;

    private boolean isEdited = false;

    private ProjectFileBean projectFile;
    private ProjectLibraryBean projectLibrary;

    private InjectRootLayoutManager rootLayoutManager;

    private final OnBackPressedCallback onBackPressedCallback =
            new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    if (isContentModified()) {
                        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(ViewCodeEditorActivity.this);
                        dialog.setIcon(R.drawable.ic_warning_96dp);
                        dialog.setTitle(Helper.getResString(R.string.common_word_warning));
                        dialog.setMessage(Helper.getResString(
                                R.string
                                        .src_code_editor_unsaved_changes_dialog_warning_message));

                        dialog.setPositiveButton(Helper.getResString(R.string.common_word_exit), (v, which) -> {
                            v.dismiss();
                            exitWithEditedContent();
                            finish();
                        });

                        dialog.setNegativeButton(Helper.getResString(R.string.common_word_cancel),
                                null);
                        dialog.show();
                    } else {
                        if (isEdited) {
                            exitWithEditedContent();
                            finish();
                            return;
                        }
                        setEnabled(false);
                        getOnBackPressedDispatcher().onBackPressed();
                    }
                }
            };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        enableEdgeToEdgeNoContrast();
        super.onCreate(savedInstanceState);
        binding = ViewCodeEditorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prefs = getSharedPreferences("dce", Activity.MODE_PRIVATE);
        if (savedInstanceState == null) {
            sc_id = getIntent().getStringExtra("sc_id");
        } else {
            sc_id = savedInstanceState.getString("sc_id");
        }
        rootLayoutManager = new InjectRootLayoutManager(sc_id);
        String title = getIntent().getStringExtra("title");
        projectFile = jC.b(sc_id).b(title);
        projectLibrary = jC.c(sc_id).c();
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Code Editor");
        getSupportActionBar().setSubtitle(title);
        binding.toolbar.setNavigationOnClickListener(v -> {
            if (onBackPressedCallback.isEnabled()) {
                onBackPressedCallback.handleOnBackPressed();
            }
        });
        content = getIntent().getStringExtra("content");
        editor = binding.editor;
        editor.setTypefaceText(EditorUtils.getTypeface(this));
        editor.setTextSize(14);
        editor.setText(content);
        if (title.endsWith(".html")) {
            EditorUtils.loadHtmlConfig(editor);
        } else if (title.endsWith(".css")) {
            EditorUtils.loadCssConfig(editor);
        } else {
            EditorUtils.loadHtmlConfig(editor);
        }
        binding.close.setOnClickListener(v -> {
            prefs.edit().putInt("note_" + sc_id, 1).apply();
            setNote(null);
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("sc_id", sc_id);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 0, Menu.NONE, "Undo")
                .setIcon(AppCompatResources.getDrawable(this, R.drawable.ic_mtrl_undo))
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(Menu.NONE, 1, Menu.NONE, "Redo")
                .setIcon(AppCompatResources.getDrawable(this, R.drawable.ic_mtrl_redo))
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(Menu.NONE, 2, Menu.NONE, "Save")
                .setIcon(AppCompatResources.getDrawable(this, R.drawable.ic_mtrl_save))
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(Menu.NONE, 3, Menu.NONE, "Pretty print");
        menu.add(Menu.NONE, 4, Menu.NONE, "Reload color schemes");
        menu.add(Menu.NONE, 5, Menu.NONE, "Layout Preview");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 0 -> {
                editor.undo();
                return true;
            }
            case 1 -> {
                editor.redo();
                return true;
            }
            case 2 -> {
                save();
                return true;
            }
            case 3 -> {
                String title = getIntent().getStringExtra("title");
                if (title.endsWith(".html")) {
                    String format = mod.hey.studios.code.SrcCodeEditor.prettifyXml(editor.getText().toString(), 4, getIntent());
                    if (format != null) {
                        editor.setText(format);
                    } else {
                        SketchwareUtil.toastError("Failed to format file");
                    }
                } else {
                    SketchwareUtil.toast("Only HTML files can be formatted");
                }
                return true;
            }
            case 4 -> {
                EditorUtils.loadHtmlConfig(binding.editor);
                return true;
            }
            case 5 -> {
                toLayoutPreview();
                return true;
            }
            default -> {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void toLayoutPreview() {
        var intent = new Intent(getApplicationContext(), LayoutPreviewActivity.class);
        intent.putExtras(getIntent());
        intent.putExtra("xml", editor.getText().toString());
        startActivity(intent);
    }

    private void setNote(String note) {
        if (prefs.getInt("note_" + sc_id, 0) < 1 && (note != null && !note.isEmpty())) {
            binding.noteCard.setVisibility(View.VISIBLE);
        } else {
            binding.noteCard.setVisibility(View.GONE);
            return;
        }
        binding.noteCard.setVisibility(View.VISIBLE);
        binding.note.setText(note);
        binding.note.setSelected(true);
    }

    private void save() {
        try {
            if (isContentModified()) {
                String editedContent = editor.getText().toString();
                String filename = getIntent().getStringExtra("title");
                
                // For Web projects, we use HtmlParser and skip circular dependency checks
                HtmlParser.parseHtml(editedContent, sc_id, this); 

                // Update content only after validation
                content = editedContent;
                if (!isEdited) {
                    isEdited = true;
                }
                SketchwareUtil.toast("Saved");
            } else {
                SketchwareUtil.toast("No changes to save");
            }
        } catch (Exception e) {
            SketchwareUtil.toastError(e.toString());
        }

    }

    private boolean isContentModified() {
        return !content.equals(editor.getText().toString());
    }

    private void exitWithEditedContent() {
        String filename = getIntent().getStringExtra("title");
        try {
            ArrayList<ViewBean> parsedLayout;
            parsedLayout = HtmlParser.parseHtml(content, sc_id, this);
            // For Web projects, we also persist the HTML directly to disk
            var workspace = new ProjectWorkspace(this, sc_id);
            workspace.a(filename, content);

            HistoryViewBean bean = new HistoryViewBean();
            bean.actionOverride(parsedLayout, jC.a(sc_id).d(filename));
            var cc = cC.c(sc_id);
            if (!cc.c.containsKey(filename)) {
                cc.e(filename);
            }
            cc.a(filename);
            cc.a(filename, bean);
            // Replace the view beans with the parsed layout
            jC.a(sc_id).c.put(filename, parsedLayout);
            setResult(RESULT_OK);
        } catch (Exception e) {
            SketchwareUtil.toastError(e.toString());
        }
    }
}
