package laki.webide.core;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import laki.webide.R;

public class LogicDialogHandler {

    public static void showAddBlockPopup(LogicEditorActivity activity) {
        View inflate = LayoutUtil.inflate(activity, R.layout.logic_popup_add_block);
        Builder builder = new Builder(activity);
        builder.setView(inflate);
        builder.setTitle("Make new Block");
        ArrayList<Pair<String, String>> args = new ArrayList<>();
        RelativeLayout blockArea = (RelativeLayout) inflate.findViewById(R.id.block_area);
        LinearLayout removeArea = (LinearLayout) inflate.findViewById(R.id.remove_area);
        Block block = new Block(activity, 0, "", " ", "definedFunc", new Object[]{Integer.valueOf(-7711273)});
        blockArea.addView(block);

        TextInputLayout tiName = (TextInputLayout) inflate.findViewById(R.id.ti_name);
        TextInputLayout tiBoolean = (TextInputLayout) inflate.findViewById(R.id.ti_boolean);
        TextInputLayout tiNumber = (TextInputLayout) inflate.findViewById(R.id.ti_number);
        TextInputLayout tiString = (TextInputLayout) inflate.findViewById(R.id.ti_string);

        VariableNameValidator nameValidator = new VariableNameValidator(activity, tiName, DefineSource.RESERVED_WORD, DefineSource.getUsedWord(DesignActivity.getScId()), DesignDataManager.getAllNamesForValid(LogicEditorActivity.filename));
        VariableNameValidator booleanValidator = new VariableNameValidator(activity, tiBoolean, DefineSource.RESERVED_WORD, DefineSource.getUsedWord(DesignActivity.getScId()), new ArrayList<>());
        VariableNameValidator numberValidator = new VariableNameValidator(activity, tiNumber, tiNumber.findViewById(R.id.ed_number) != null ? null : null, DefineSource.getUsedWord(DesignActivity.getScId()), new ArrayList<>());
        VariableNameValidator stringValidator = new VariableNameValidator(activity, tiString, DefineSource.RESERVED_WORD, DefineSource.getUsedWord(DesignActivity.getScId()), new ArrayList<>());

        EditText edName = (EditText) inflate.findViewById(R.id.ed_name);
        EditText edBoolean = (EditText) inflate.findViewById(R.id.ed_boolean);
        EditText edNumber = (EditText) inflate.findViewById(R.id.ed_number);
        EditText edString = (EditText) inflate.findViewById(R.id.ed_string);
        EditText edLabel = (EditText) inflate.findViewById(R.id.ed_label);

        edName.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(android.text.Editable s) {
                makeBlockWithSpec(activity, blockArea, removeArea, block, edName, args, booleanValidator, numberValidator, stringValidator);
            }
        });

        ((Button) inflate.findViewById(R.id.add_boolean)).setOnClickListener(v -> {
            if (booleanValidator.isValid()) {
                args.add(new Pair<>("b", edBoolean.getText().toString()));
                updateValidatorsAndBlock(activity, blockArea, removeArea, block, edName, args, booleanValidator, numberValidator, stringValidator);
                edBoolean.setText("");
            }
        });

        ((Button) inflate.findViewById(R.id.add_number)).setOnClickListener(v -> {
            if (numberValidator.isValid()) {
                args.add(new Pair<>("d", edNumber.getText().toString()));
                updateValidatorsAndBlock(activity, blockArea, removeArea, block, edName, args, booleanValidator, numberValidator, stringValidator);
                edNumber.setText("");
            }
        });

        ((Button) inflate.findViewById(R.id.add_string)).setOnClickListener(v -> {
            if (stringValidator.isValid()) {
                args.add(new Pair<>("s", edString.getText().toString()));
                updateValidatorsAndBlock(activity, blockArea, removeArea, block, edName, args, booleanValidator, numberValidator, stringValidator);
                edString.setText("");
            }
        });

        ((Button) inflate.findViewById(R.id.add_label)).setOnClickListener(v -> {
            args.add(new Pair<>("t", edLabel.getText().toString()));
            makeBlockWithSpec(activity, blockArea, removeArea, block, edName, args, booleanValidator, numberValidator, stringValidator);
        });

        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Save", null);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                if (nameValidator.isValid()) {
                    DesignDataManager.addFunction(LogicEditorActivity.filename, edName.getText().toString(), block.mSpec);
                    activity.onBlockCategorySelect(5, -7711273);
                    dialog.dismiss();
                }
            });
        });
        dialog.show();
    }

    private static void updateValidatorsAndBlock(Context context, ViewGroup blockArea, ViewGroup removeArea, Block block, EditText edName, ArrayList<Pair<String, String>> args, VariableNameValidator bv, VariableNameValidator nv, VariableNameValidator sv) {
        makeBlockWithSpec(context, blockArea, removeArea, block, edName, args, bv, nv, sv);
        ArrayList<String> used = new ArrayList<>(Arrays.asList(DefineSource.getUsedWord(DesignActivity.getScId())));
        for (Pair<String, String> p : args) {
            if (!p.first.equals("t")) used.add(p.second);
        }
        String[] usedArr = used.toArray(new String[0]);
        bv.setUsedWords(usedArr);
        nv.setUsedWords(usedArr);
        sv.setUsedWords(usedArr);
    }

    private static void makeBlockWithSpec(Context context, ViewGroup blockArea, ViewGroup removeArea, Block block, EditText edName, ArrayList<Pair<String, String>> args, VariableNameValidator bv, VariableNameValidator nv, VariableNameValidator sv) {
        blockArea.removeAllViews();
        blockArea.addView(block);
        StringBuilder spec = new StringBuilder(edName.getText().toString());
        for (Pair<String, String> p : args) {
            if (p.first.equals("b")) spec.append(" %b.").append(p.second);
            else if (p.first.equals("d")) spec.append(" %d.").append(p.second);
            else if (p.first.equals("s")) spec.append(" %s.").append(p.second);
            else spec.append(" ").append(p.second);
        }
        block.setSpec(spec.toString(), null);

        int argIndex = 0;
        for (Pair<String, String> p : args) {
            if (!p.first.equals("t")) {
                Block argBlock = new Block(context, args.indexOf(p) + 1, p.second, p.first, "getParam", new Object[]{-7711273, ""});
                blockArea.addView(argBlock);
                block.replaceArgWithBlock((BlockBase) block.args.get(argIndex), argBlock);
                argIndex++;
            }
        }
        block.fixLayout();
        
        removeArea.removeAllViews();
        for (int i = 0; i < block.labelsAndArgs.size(); i++) {
            View v = (View) block.labelsAndArgs.get(i);
            int width = 0;
            if (block.argTypes.get(i).equals("label")) {
                TextView tv = (TextView) v;
                Rect rect = new Rect();
                String text = tv.getText().toString();
                tv.getPaint().getTextBounds(text, 0, text.length(), rect);
                width = rect.width();
            } else if (v instanceof Block) {
                width = ((Block) v).getWidthSum();
            }
            
            ImageView iv = new ImageView(context);
            iv.setImageResource(R.drawable.ic_remove_grey600_24dp);
            iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            iv.setLayoutParams(new LinearLayout.LayoutParams((int)(width + LayoutUtil.getDip(context, 4.0f)), -1));
            removeArea.addView(iv);
            
            if (i == 0) {
                iv.setVisibility(View.INVISIBLE);
            } else {
                final int finalI = i;
                iv.setOnClickListener(view -> {
                    args.remove(finalI - 1);
                    updateValidatorsAndBlock(context, blockArea, removeArea, block, edName, args, bv, nv, sv);
                });
            }
        }
    }

    public static void showAddVarPopup(LogicEditorActivity activity) {
        View inflate = LayoutUtil.inflate(activity, R.layout.logic_popup_add_variable);
        Builder builder = new Builder(activity);
        builder.setView(inflate);
        builder.setTitle("Add Variable");
        EditText edInput = (EditText) inflate.findViewById(R.id.ed_input);
        VariableNameValidator validator = new VariableNameValidator(activity, (TextInputLayout) inflate.findViewById(R.id.ti_input), DefineSource.RESERVED_WORD, DefineSource.getUsedWord(DesignActivity.getScId()), DesignDataManager.getAllNamesForValid(LogicEditorActivity.filename));
        
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Save", null);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                if (validator.isValid()) {
                    DesignDataManager.addVariable(LogicEditorActivity.filename, 2, edInput.getText().toString());
                    activity.onBlockCategorySelect(0, -1147626);
                    dialog.dismiss();
                }
            });
        });
        dialog.show();
    }

    public static void showRemoveVarPopup(LogicEditorActivity activity) {
        View inflate = LayoutUtil.inflate(activity, R.layout.property_popup_selector_single);
        Builder builder = new Builder(activity);
        builder.setView(inflate);
        builder.setTitle("Remove a variable");
        RadioGroup rg = (RadioGroup) inflate.findViewById(R.id.rg_content);
        
        for (Pair<Integer, String> p : DesignDataManager.getVariables(LogicEditorActivity.filename)) {
            RadioButton rb = new RadioButton(activity);
            rb.setText(p.second);
            rg.addView(rb);
        }
        
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Save", (d, w) -> {
            int id = rg.getCheckedRadioButtonId();
            if (id != -1) {
                RadioButton rb = rg.findViewById(id);
                String name = rb.getText().toString();
                if (activity.getBlockPane().isExistVariableBlock(name) || DesignDataManager.isExistVariableBlock(LogicEditorActivity.filename, name, activity.getScEventId() + "_" + activity.getEventName())) {
                    Toast.makeText(activity, "Selected variable is currently used", Toast.LENGTH_SHORT).show();
                    return;
                }
                DesignDataManager.removeVariable(LogicEditorActivity.filename, name);
                activity.onBlockCategorySelect(0, -1147626);
            }
        });
        builder.show();
    }

    public static void showAddListPopup(LogicEditorActivity activity) {
        View inflate = LayoutUtil.inflate(activity, R.layout.logic_popup_add_list);
        Builder builder = new Builder(activity);
        builder.setView(inflate);
        builder.setTitle("Add List");
        RadioGroup rgType = (RadioGroup) inflate.findViewById(R.id.rg_type);
        EditText edInput = (EditText) inflate.findViewById(R.id.ed_input);
        VariableNameValidator validator = new VariableNameValidator(activity, (TextInputLayout) inflate.findViewById(R.id.ti_input), DefineSource.RESERVED_WORD, DefineSource.getUsedWord(DesignActivity.getScId()), DesignDataManager.getAllNamesForValid(LogicEditorActivity.filename));
        
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Save", null);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                if (validator.isValid()) {
                    int type = rgType.getCheckedRadioButtonId() == R.id.rb_int ? 1 : 2;
                    DesignDataManager.addList(LogicEditorActivity.filename, type, edInput.getText().toString());
                    activity.onBlockCategorySelect(1, -3384542);
                    dialog.dismiss();
                }
            });
        });
        dialog.show();
    }

    public static void showRemoveListPopup(LogicEditorActivity activity) {
        View inflate = LayoutUtil.inflate(activity, R.layout.property_popup_selector_single);
        Builder builder = new Builder(activity);
        builder.setView(inflate);
        builder.setTitle("Remove list");
        RadioGroup rg = (RadioGroup) inflate.findViewById(R.id.rg_content);
        
        for (Pair<Integer, String> p : DesignDataManager.getLists(LogicEditorActivity.filename)) {
            RadioButton rb = new RadioButton(activity);
            rb.setText(p.second);
            rg.addView(rb);
        }
        
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Save", (d, w) -> {
            int id = rg.getCheckedRadioButtonId();
            if (id != -1) {
                RadioButton rb = rg.findViewById(id);
                String name = rb.getText().toString();
                if (activity.getBlockPane().isExistListBlock(name) || DesignDataManager.isExistListBlock(LogicEditorActivity.filename, name, activity.getScEventId() + "_" + activity.getEventName())) {
                    Toast.makeText(activity, "Selected list is currently used", Toast.LENGTH_SHORT).show();
                    return;
                }
                DesignDataManager.removeList(LogicEditorActivity.filename, name);
                activity.onBlockCategorySelect(1, -3384542);
            }
        });
        builder.show();
    }
}
