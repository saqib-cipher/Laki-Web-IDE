package laki.webide.core;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;

import io.github.rosemoe.sora.widget.component.EditorAutoCompletion;
import mod.hey.studios.code.SrcCodeEditor;
import mod.hey.studios.util.Helper;
import laki.webide.R;
import laki.webide.databinding.CodeEditorHsAsdBinding;
import laki.webide.utility.EditorUtils;
import laki.webide.utility.SketchwareUtil;

public class LakiAsdDialog extends Dialog implements DialogInterface.OnDismissListener {
    private SharedPreferences pref;
    private Activity act;
    private CodeEditorHsAsdBinding binding;
    private String content;
    private OnSaveClickListener saveListener;

    public interface OnSaveClickListener {
        void onSave(String content);
    }

    public LakiAsdDialog(Activity activity) {
        super(activity, R.style.AsdEditorDialogTheme);
        act = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = CodeEditorHsAsdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        binding.editor.setTypefaceText(EditorUtils.getTypeface(act));
        binding.editor.setText(content);
        binding.editor.setWordwrap(false);

        // Load CSS config instead of Java for Web IDE
        EditorUtils.loadCssConfig(binding.editor);
        SrcCodeEditor.loadCESettings(act, binding.editor, "laki_dlg");
        pref = SrcCodeEditor.pref;

        Menu menu = binding.toolbar.getMenu();
        MenuItem itemWordwrap = menu.findItem(R.id.action_word_wrap);
        MenuItem itemAutocomplete = menu.findItem(R.id.action_autocomplete);
        MenuItem itemAutocompleteSymbolPair = menu.findItem(R.id.action_autocomplete_symbol_pair);

        if (itemWordwrap != null) itemWordwrap.setChecked(pref.getBoolean("dlg_ww", false));
        if (itemAutocomplete != null) itemAutocomplete.setChecked(pref.getBoolean("dlg_ac", false));
        if (itemAutocompleteSymbolPair != null) itemAutocompleteSymbolPair.setChecked(pref.getBoolean("dlg_acsp", true));

        binding.toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_undo) {
                binding.editor.undo();
            } else if (id == R.id.action_redo) {
                binding.editor.redo();
            } else if (id == R.id.action_word_wrap) {
                item.setChecked(!item.isChecked());
                binding.editor.setWordwrap(item.isChecked());
                pref.edit().putBoolean("dlg_ww", item.isChecked()).apply();
            } else if (id == R.id.action_autocomplete_symbol_pair) {
                item.setChecked(!item.isChecked());
                binding.editor.getProps().symbolPairAutoCompletion = item.isChecked();
                pref.edit().putBoolean("dlg_acsp", item.isChecked()).apply();
            } else if (id == R.id.action_autocomplete) {
                item.setChecked(!item.isChecked());
                binding.editor.getComponent(EditorAutoCompletion.class).setEnabled(item.isChecked());
                pref.edit().putBoolean("dlg_ac", item.isChecked()).apply();
            } else if (id == R.id.action_paste) {
                binding.editor.pasteText();
            } else if (id == R.id.action_find_replace) {
                binding.editor.getSearcher().stopSearch();
                binding.editor.beginSearchMode();
            }
            return true;
        });

        binding.btnSave.setOnClickListener(v -> {
            if (saveListener != null) {
                saveListener.onSave(binding.editor.getText().toString());
            }
            dismiss();
        });

        binding.btnCancel.setOnClickListener(v -> dismiss());

        setOnDismissListener(this);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (pref != null && binding != null) {
            pref.edit().putInt("dlg_ts", (int) (binding.editor.getTextSizePx() / act.getResources().getDisplayMetrics().scaledDensity)).apply();
        }
        pref = null;
        act = null;
    }

    public void setOnSaveClickListener(OnSaveClickListener listener) {
        this.saveListener = listener;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
