package laki.webide.core;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import android.content.DialogInterface;

import laki.webide.R;
import com.besome.sketch.lib.ui.ColorPickerDialog;

public class BlockArg extends BlockBase {
    private Object argValue = "";
    private ViewGroup content;
    private int defaultArgWidth = 20;
    private boolean isEditable = false;
    private Context mContext;
    private AlertDialog mDlg;
    private String mMenuName = "";
    private TextView mTextView;
    private int paddingText = 4;

    public BlockArg(Context var1, String var2, int var3, String var4) {
        super(var1, var2, true);
        this.mContext = var1;
        this.mMenuName = var4;
        this.init(var1);
    }

    // $FF: synthetic method
    static AlertDialog access$000(BlockArg var0) {
        return var0.mDlg;
    }

    // $FF: synthetic method
    static ViewGroup access$100(BlockArg var0) {
        return var0.content;
    }

    private RadioButton createImageRadioButton(String var1) {
        RadioButton var2 = new RadioButton(this.getContext());
        var2.setText("");
        var2.setTag(var1);
        LayoutParams var3 = new LayoutParams(-2, (int)(60.0F * LayoutUtil.getDip(this.getContext(), 1.0F)));
        var2.setGravity(19);
        var2.setLayoutParams(var3);
        return var2;
    }

    private RadioButton createPairItem(String var1, String var2) {
        RadioButton var3 = new RadioButton(this.getContext());
        var3.setText(var1 + " : " + var2);
        var3.setTag(var2);
        LayoutParams var4 = new LayoutParams(-1, (int)(40.0F * LayoutUtil.getDip(this.getContext(), 1.0F)));
        var3.setGravity(19);
        var3.setLayoutParams(var4);
        return var3;
    }

    private RadioButton createRadioButton(String var1) {
        RadioButton var2 = new RadioButton(this.getContext());
        var2.setText("");
        var2.setTag(var1);
        LayoutParams var3 = new LayoutParams(-2, (int)(40.0F * LayoutUtil.getDip(this.getContext(), 1.0F)));
        var2.setGravity(19);
        var2.setLayoutParams(var3);
        return var2;
    }

    private LinearLayout createRadioImage(String var1, boolean var2) {
        float var3 = LayoutUtil.getDip(this.getContext(), 1.0F);
        LinearLayout var4 = new LinearLayout(this.getContext());
        var4.setLayoutParams(new LayoutParams(-1, (int)(60.0F * var3)));
        var4.setGravity(19);
        var4.setOrientation(LinearLayout.HORIZONTAL);
        TextView var5 = new TextView(this.getContext());
        LinearLayout.LayoutParams var6 = new LinearLayout.LayoutParams(0, -2);
        var6.weight = 1.0F;
        var5.setLayoutParams(var6);
        var5.setText(var1);
        var4.addView(var5);
        ImageView var7 = new ImageView(this.getContext());
        var7.setScaleType(ScaleType.CENTER_CROP);
        var7.setLayoutParams(new LayoutParams((int)(48.0F * var3), (int)(48.0F * var3)));
        if(var2) {
            var7.setImageResource(this.getContext().getResources().getIdentifier(var1, "drawable", this.getContext().getPackageName()));
        }/* else {
            Uri var8 = Uri.fromFile(new File(DesignActivity.resourceManager.getImagePathFromName(var1)));
            DrawableTypeRequest var9 = Glide.with(this.getContext()).load(var8);
            ResourceManager var10000 = DesignActivity.resourceManager;
            var9.signature(ResourceManager.getSignature()).error(R.drawable.ic_remove_grey600_24dp).into(var7);
        }*/

        var7.setBackgroundColor(-4342339);
        var4.addView(var7);
        return var4;
    }

    private RadioButton createSingleItem(String var1) {
        RadioButton var2 = new RadioButton(this.getContext());
        var2.setText(var1);
        LayoutParams var3 = new LayoutParams(-1, (int)(40.0F * LayoutUtil.getDip(this.getContext(), 1.0F)));
        var2.setGravity(19);
        var2.setLayoutParams(var3);
        return var2;
    }

    private int getLabelWidth() {
        Rect var1 = new Rect();
        String text = this.mTextView.getText().toString();
        if (text.isEmpty() && this.mTextView.getHint() != null) {
            text = this.mTextView.getHint().toString();
        }
        this.mTextView.getPaint().getTextBounds(text, 0, text.length(), var1);
        return var1.width() + this.paddingText;
    }

    private void init(Context var1) {
        byte var3;
        label48: {
            String var2 = this.mType;
            switch(var2.hashCode()) {
                case 98:
                    if(var2.equals("b")) {
                        var3 = 0;
                        break label48;
                    }
                    break;
                case 100:
                    if(var2.equals("d")) {
                        var3 = 1;
                        break label48;
                    }
                    break;
                case 109:
                    if(var2.equals("m")) {
                        var3 = 4;
                        break label48;
                    }
                    break;
                case 110:
                    if(var2.equals("n")) {
                        var3 = 2;
                        break label48;
                    }
                    break;
                case 115:
                    if(var2.equals("s")) {
                        var3 = 3;
                        break label48;
                    }
            }

            var3 = -1;
        }

        switch(var3) {
            case 0:
                this.mColor = 1342177280;
                this.defaultArgWidth = 25;
                break;
            case 1:
                this.mColor = -657931;
                break;
            case 2:
                this.mColor = -3155748;
                break;
            case 3:
                this.mColor = -1;
                break;
            case 4:
                this.mColor = 805306368;
        }

        this.defaultArgWidth = (int)((float)this.defaultArgWidth * this.dip);
        this.paddingText = (int)((float)this.paddingText * this.dip);
        int finalWidth = this.defaultArgWidth;
        if(this.mType.equals("m") || this.mType.equals("d") || this.mType.equals("n") || this.mType.equals("s")) {
            this.mTextView = this.makeEditText("");
            this.addView(this.mTextView);
            finalWidth = Math.max(this.defaultArgWidth, this.getLabelWidth());
            this.mTextView.getLayoutParams().width = finalWidth;
        }

        this.setWidthAndTopHeight((float)finalWidth, (float)this.labelAndArgHeight, false);
    }

    private TextView makeEditText(String var1) {
        TextView var2 = new TextView(this.mContext);
        var2.setText(var1);
        var2.setHint(getHintText());
        var2.setHintTextColor(0x60000000);
        var2.setTextSize(9.0F);
        android.widget.RelativeLayout.LayoutParams var3 = new android.widget.RelativeLayout.LayoutParams(this.defaultArgWidth, this.labelAndArgHeight);
        var3.setMargins(0, 0, 0, 0);
        var2.setPadding(5, 0, 0, 0);
        var2.setLayoutParams(var3);
        var2.setBackgroundColor(0);
        var2.setSingleLine();
        var2.setGravity(17);
        if(!this.mType.equals("m")) {
            var2.setTextColor(-268435456);
            return var2;
        } else {
            var2.setTextColor(-251658241);
            return var2;
        }
    }

    private void showColorPopup() {
        if (!(mContext instanceof Activity)) return;
        Activity activity = (Activity) mContext;

        new LakiColorPicker(activity)
            .setCurrentValue(argValue.toString())
            .setOnColorSelectedListener(value -> {
                setArgValue(value);
                parentBlock.recalcWidthToParent();
                parentBlock.topBlock().fixLayout();
                parentBlock.pane.calculateWidthHeight();
            })
            .show();
    }

 /*   private void showImagePopup() {
        View var1 = LayoutUtil.inflate(this.getContext(), R.layout.property_popup_selector_color);
        Builder var2 = new Builder(this.getContext());
        var2.setView(var1);
        var2.setTitle("Select image");
        RadioGroup var5 = (RadioGroup)var1.findViewById(R.id.rg);
        this.content = (LinearLayout)var1.findViewById(R.id.content);
        ArrayList var6 = DesignActivity.resourceManager.getImageNames();
        if(ScDefine.isCustomEditMode(DesignActivity.getScId())) {
            var6.add(0, "default_image");
        }

        Iterator var7 = var6.iterator();

        while(var7.hasNext()) {
            String var10 = (String)var7.next();
            RadioButton var11 = this.createImageRadioButton(var10);
            var5.addView(var11);
            if(var10.equals(this.argValue)) {
                var11.setChecked(true);
            }

            LinearLayout var12;
            if(ScDefine.isCustomEditMode(DesignActivity.getScId())) {
                if(var10.equals("default_image")) {
                    var12 = this.createRadioImage(var10, true);
                } else {
                    var12 = this.createRadioImage(var10, false);
                }
            } else {
                var12 = this.createRadioImage(var10, true);
            }

            var12.setOnClickListener(new 10(this, var5));
            this.content.addView(var12);
        }

        var2.setNegativeButton("Cancel", new 11(this));
        var2.setPositiveButton("Save", new 12(this, var5));
        this.mDlg = var2.create();
        this.mDlg.show();
    }*/

    public Object getArgValue() {
        return !this.mType.equals("d") && !this.mType.equals("m") && !this.mType.equals("s")?this.argValue:this.mTextView.getText();
    }

    public void setArgValue(Object var1) {
        this.argValue = var1;
        if(this.mType.equals("d") || this.mType.equals("m") || this.mType.equals("s")) {
            this.mTextView.setText(var1.toString());
            int var2 = Math.max(this.defaultArgWidth, this.getLabelWidth());
            this.mTextView.getLayoutParams().width = var2;
            this.setWidthAndTopHeight((float)var2, (float)this.labelAndArgHeight, true);
        }

    }

    public void setEditable(boolean var1) {
        this.isEditable = var1;
    }

    public void showEditPopup(final boolean var1) {
        View var2 = LayoutUtil.inflate(this.getContext(), R.layout.property_popup_input_text);
        Builder var3 = new Builder(this.getContext());
        var3.setView(var2);
        if(var1) {
            var3.setTitle("Enter Integer Value");
        } else {
            var3.setTitle("Enter String Value");
        }

        final EditText var6 = (EditText)var2.findViewById(R.id.ed_input);
        if(var1) {
            var6.setInputType(4098);
            var6.setImeOptions(6);
            var6.setMaxLines(1);
        } else {
            var6.setInputType(131073);
            var6.setImeOptions(1);
        }

        var6.setText(this.mTextView.getText());
        var3.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface var1, int var2) {
                    mDlg.dismiss();
                }
            });
        var3.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface var11, int var2) {
                    
                    String var3 = var6.getText().toString();
                    String var4;
                    if(var1) {
                        var4 = Integer.valueOf(var3).toString();
                    } else if(var3.length() > 0 && var3.charAt(0) == 64) {
                        var4 = " " + var3;
                    } else {
                        var4 = var3;
                    }

                    setArgValue(var4);
                    parentBlock.recalcWidthToParent();
                    parentBlock.topBlock().fixLayout();
                    parentBlock.pane.calculateWidthHeight();
                    
                    mDlg.dismiss();
                }
            });
        this.mDlg = var3.create();
        this.mDlg.show();
    }

  /*  public void showIntentDataPopup() {
        View var1 = LayoutUtil.inflate(this.getContext(), R.layout.property_popup_input_intent_data);
        Builder var2 = new Builder(this.getContext());
        var2.setView(var1);
        var2.setTitle("Enter data value");
        EditText var5 = (EditText)var1.findViewById(R.id.ed_input);
        var5.setInputType(1);
        var5.setText(this.mTextView.getText());
        var2.setNegativeButton("Cancel", new 1(this));
        var2.setPositiveButton("Save", new 2(this, var5));
        this.mDlg = var2.create();
        this.mDlg.show();
    }*/

    public void showPopup() {
        if(this.mType.equals("d")) {
            this.showEditPopup(true);
        } else {
            if(this.mType.equals("s")) {
                if(this.mMenuName.equals("intentData")) {
               //     this.showIntentDataPopup();
                    return;
                }

                this.showEditPopup(false);
                return;
            }

            if(this.mType.equals("m")) {
                if(this.mMenuName.equals("resource")) {
              //      this.showImagePopup();
                    return;
                }

                if(this.mMenuName.equals("color")) {
                    this.showColorPopup();
                    return;
                }

                if(!this.mMenuName.equals("view") && !this.mMenuName.equals("textview") && !this.mMenuName.equals("imageview") && !this.mMenuName.equals("listview") && !this.mMenuName.equals("spinner") && !this.mMenuName.equals("listSpn") && !this.mMenuName.equals("checkbox")) {
                    this.showSelectPopup();
                    return;
                }

            //    this.showSelectPairPopup();
                return;
            }
        }

    }

    public void showSelectPopup() {
        if (!(mContext instanceof Activity)) return;
        Activity activity = (Activity) mContext;
        
        String title = "Select Item";
        ArrayList<String> var4 = new ArrayList<>();
        if(this.mMenuName.equals("var")) {
            title = "Select CSS Variable";
            var4 = DesignDataManager.getVariablesByType(LogicEditorActivity.filename, 2);
        } else if(this.mMenuName.equals("htmlId")) {
            title = "Select HTML ID";
            String scId = (mContext instanceof LogicEditorActivity) ? ((LogicEditorActivity)mContext).scId : "";
            var4 = DesignDataManager.getHtmlSelectors(mContext, scId, LogicEditorActivity.filename, "htmlId");
        } else if(this.mMenuName.equals("htmlClass")) {
            title = "Select HTML Class";
            String scId = (mContext instanceof LogicEditorActivity) ? ((LogicEditorActivity)mContext).scId : "";
            var4 = DesignDataManager.getHtmlSelectors(mContext, scId, LogicEditorActivity.filename, "htmlClass");
        } else if(this.mMenuName.equals("htmlTag")) {
            title = "Select HTML Tag";
            String scId = (mContext instanceof LogicEditorActivity) ? ((LogicEditorActivity)mContext).scId : "";
            var4 = DesignDataManager.getHtmlSelectors(mContext, scId, LogicEditorActivity.filename, "htmlTag");
        } else if(this.mMenuName.equals("display")) {
            title = "Select Display Mode";
            var4 = new ArrayList<>(Arrays.asList("block", "inline", "inline-block", "flex", "grid", "inline-flex", "inline-grid", "none"));
        } else if(this.mMenuName.equals("position")) {
            title = "Select Position Mode";
            var4 = new ArrayList<>(Arrays.asList("static", "relative", "absolute", "fixed", "sticky"));
        } else if(this.mMenuName.equals("overflow")) {
            title = "Select Overflow Mode";
            var4 = new ArrayList<>(Arrays.asList("visible", "hidden", "scroll", "auto"));
        } else if(this.mMenuName.equals("visibility")) {
            title = "Select Visibility Mode";
            var4 = new ArrayList<>(Arrays.asList("visible", "hidden", "collapse"));
        } else if(this.mMenuName.equals("side")) {
            title = "Select Side / Anchor";
            var4 = new ArrayList<>(Arrays.asList("top", "right", "bottom", "left"));
        } else if(this.mMenuName.equals("unit")) {
            title = "Select CSS Unit";
            var4 = new ArrayList<>(Arrays.asList("px", "%", "em", "rem", "vh", "vw", "auto"));
        } else if(this.mMenuName.equals("direction")) {
            title = "Select Flex Direction";
            var4 = new ArrayList<>(Arrays.asList("row", "row-reverse", "column", "column-reverse"));
        } else if(this.mMenuName.equals("justify")) {
            title = "Select Content Justification";
            var4 = new ArrayList<>(Arrays.asList("flex-start", "flex-end", "center", "space-between", "space-around", "space-evenly"));
        } else if(this.mMenuName.equals("align")) {
            title = "Select Item Alignment";
            var4 = new ArrayList<>(Arrays.asList("stretch", "flex-start", "flex-end", "center", "baseline"));
        } else if(this.mMenuName.equals("wrap")) {
            title = "Select Flex Wrap";
            var4 = new ArrayList<>(Arrays.asList("nowrap", "wrap", "wrap-reverse"));
        } else if(this.mMenuName.equals("boxSizing")) {
            title = "Select Box Sizing";
            var4 = new ArrayList<>(Arrays.asList("border-box", "content-box"));
        } else if(this.mMenuName.equals("fontWeight")) {
            title = "Select Font Weight";
            var4 = new ArrayList<>(Arrays.asList("normal", "bold", "lighter", "bolder", "100", "200", "300", "400", "500", "600", "700", "800", "900"));
        } else if(this.mMenuName.equals("textAlign")) {
            title = "Select Text Align";
            var4 = new ArrayList<>(Arrays.asList("left", "right", "center", "justify", "start", "end"));
        } else if(this.mMenuName.equals("textTransform")) {
            title = "Select Text Transform";
            var4 = new ArrayList<>(Arrays.asList("none", "capitalize", "uppercase", "lowercase"));
        } else if(this.mMenuName.equals("textDecoration")) {
            title = "Select Text Decoration";
            var4 = new ArrayList<>(Arrays.asList("none", "underline", "overline", "line-through"));
        } else if(this.mMenuName.equals("fontStyle")) {
            title = "Select Font Style";
            var4 = new ArrayList<>(Arrays.asList("normal", "italic", "oblique"));
        } else if(this.mMenuName.equals("whiteSpace")) {
            title = "Select White Space";
            var4 = new ArrayList<>(Arrays.asList("normal", "nowrap", "pre", "pre-wrap", "pre-line"));
        } else if(this.mMenuName.equals("borderStyle")) {
            title = "Select Border Style";
            var4 = new ArrayList<>(Arrays.asList("none", "solid", "dotted", "dashed", "double", "groove", "ridge", "inset", "outset"));
        } else if(this.mMenuName.equals("bgRepeat")) {
            title = "Select Background Repeat";
            var4 = new ArrayList<>(Arrays.asList("repeat", "repeat-x", "repeat-y", "no-repeat", "round", "space"));
        } else if(this.mMenuName.equals("bgSize")) {
            title = "Select Background Size";
            var4 = new ArrayList<>(Arrays.asList("auto", "cover", "contain"));
        } else if(this.mMenuName.equals("bgAttachment")) {
            title = "Select Background Attachment";
            var4 = new ArrayList<>(Arrays.asList("scroll", "fixed", "local"));
        } else if(this.mMenuName.equals("textOverflow")) {
            title = "Select Text Overflow";
            var4 = new ArrayList<>(Arrays.asList("clip", "ellipsis"));
        } else if(this.mMenuName.equals("wordBreak")) {
            title = "Select Word Break";
            var4 = new ArrayList<>(Arrays.asList("normal", "break-all", "keep-all", "break-word"));
        } else if(this.mMenuName.equals("verticalAlign")) {
            title = "Select Vertical Align";
            var4 = new ArrayList<>(Arrays.asList("baseline", "top", "middle", "bottom", "sub", "super", "text-top", "text-bottom"));
        } else if(this.mMenuName.equals("cursor")) {
            title = "Select Cursor Type";
            var4 = new ArrayList<>(Arrays.asList("auto", "default", "pointer", "wait", "text", "move", "help", "not-allowed", "none"));
        } else if(this.mMenuName.equals("pointerEvents")) {
            title = "Select Pointer Events";
            var4 = new ArrayList<>(Arrays.asList("auto", "none", "inherit", "initial"));
        } else if(this.mMenuName.equals("userSelect")) {
            title = "Select User Select";
            var4 = new ArrayList<>(Arrays.asList("auto", "none", "text", "all"));
        } else if(this.mMenuName.equals("writingMode")) {
            title = "Select Writing Mode";
            var4 = new ArrayList<>(Arrays.asList("horizontal-tb", "vertical-rl", "vertical-lr"));
        } else if(this.mMenuName.equals("hyphens")) {
            title = "Select Hyphens";
            var4 = new ArrayList<>(Arrays.asList("none", "manual", "auto"));
        } else if(this.mMenuName.equals("bgOrigin") || this.mMenuName.equals("bgClip")) {
            title = "Select Box Area";
            var4 = new ArrayList<>(Arrays.asList("border-box", "padding-box", "content-box"));
        } else if(this.mMenuName.equals("blendMode")) {
            title = "Select Blend Mode";
            var4 = new ArrayList<>(Arrays.asList("normal", "multiply", "screen", "overlay", "darken", "lighten", "color-dodge", "color-burn", "hard-light", "soft-light", "difference", "exclusion", "hue", "saturation", "color", "luminosity"));
        } else if(this.mMenuName.equals("objectFit")) {
            title = "Select Object Fit";
            var4 = new ArrayList<>(Arrays.asList("fill", "contain", "cover", "none", "scale-down"));
        }

        new LakiDialogBox(activity, title)
            .setList(var4)
            .setCurrentValue(this.argValue.toString())
            .setSelectionListener(value -> {
                setArgValue(value);
                parentBlock.recalcWidthToParent();
                parentBlock.topBlock().fixLayout();
                parentBlock.pane.calculateWidthHeight();
            })
            .show();
    }

    private String getHintText() {
        if (mMenuName == null) return "";
        if (mMenuName.equals("htmlId")) return "id";
        if (mMenuName.equals("htmlClass") || mMenuName.equals("classname")) return "class";
        if (mMenuName.equals("unit")) return "unit";
        if (mMenuName.equals("var")) return "var";
        return "";
    }
}