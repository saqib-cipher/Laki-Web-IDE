package laki.webide.core;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import androidx.annotation.Nullable;

import com.besome.sketch.lib.base.BaseAppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google. android.material.textfield.TextInputLayout;
import androidx.appcompat.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import androidx.appcompat.app.AppCompatActivity;
import android.animation.Animator;
import android.content.Context;
import java.util.Arrays;
import android.text.TextWatcher;
import android.text.Editable;
import android.content.DialogInterface;

import laki.webide.R;
import com.besome.sketch.editor.logic.LogicTopMenu;
import laki.webide.core.LayoutUtil;
import com.besome.sketch.beans.BlockBean;
import laki.webide.activities.editor.view.CodeViewerActivity;

public class LogicEditorActivity extends BaseAppCompatActivity implements OnClickListener, OnBlockCategorySelectListener, OnTouchListener {
		public static final String LOGIC_NAME_SEPARATOR = "_";

		public static String filename = "";
		
		private Context context;
		
		private LogicTopMenu topMenu;
		private ObjectAnimator aniShowTopMenu;
		private ObjectAnimator aniHideTopMenu;
		private ObjectAnimator aniShowPalette;
		private ObjectAnimator aniHidePalette;
		private LinearLayout areaPalette;
		private boolean bActiveIconDelete = false;
		private boolean bInitIconDeleteAnimation = false;
		private boolean bInitPaletteAnimation = false;
		private boolean bShowIconDelete = false;
		private RelativeLayout blockCopyInterface;
		private VariableNameValidator booleanValidator;
		private View currentTouchedView = null;
		private ViewDummy dummy;
		private ViewLogicEditor editor;
		private String eventName = "";
		private FloatingActionButton fab;
		private final Handler handler = new Handler();
		private ImageView iconDelete;
		private String id = "";
		private boolean isDragged = false;
        public String scId = "";
		private boolean isPaletteOpened = false;
		private LinearLayout layoutPalette;
		private Runnable longPressed = new Runnable() {
				@Override
				public void run() {
						dragStart();
				}
		};
		private AlertDialog mDlg;
		private Menu menu;
		private int minDist = 0;
		private VariableNameValidator numberValidator;
		private int originalArgIndex;
		private int originalInsertOption;
		private Block originalParent;
		private PaletteBlock paletteBlock;
		private PaletteSelector paletteSelector;
		private BlockPane pane;
		private int[] posDummy = new int[2];
		private float posInitX = 0.0f;
		private float posInitY = 0.0f;
		private int[] posOriginal = new int[2];
		private SharedPreferenceUtil prefBackup;
		private SharedPreferenceUtil prefInstall;
		private VariableNameValidator stringValidator;
		private Toolbar toolbar;
		private boolean useVibrate;
		private Vibrator vibrator;
		
		//new
		public int BLOCK_DRAG_X = 0;
		public int BLOCK_DRAG_Y = -30;
		
		private void activeIconDelete(boolean z) {
				if (iconDelete != null) {
						iconDelete.setAlpha(z ? 1.0f : 0.5f);
				}
		}
		
		private void addBlockToPalette(CreateBlock cb) {
				BlockBase addBlock = this.paletteBlock.addBlock(cb);
				addBlock.setClickable(true);
				addBlock.setOnTouchListener(this);
		}

		private void addBlockToPalette(String str, String str2, String str3, int i, Object... objArr) {
				BlockBase addBlock = this.paletteBlock.addBlock(str, str2, str3, i, objArr);
				addBlock.setClickable(true);
				addBlock.setOnTouchListener(this);
		}
		
		private void addButtonToPalette(String str, String str2) {
				View addButton = this.paletteBlock.addButton(str);
				addButton.setTag(str2);
				addButton.setSoundEffectsEnabled(true);
				addButton.setOnClickListener(this);
		}
		
		private void addFunctions() {
				Iterator it = DesignDataManager.getFunctions(filename).iterator();
				while (it.hasNext()) {
						addBlockToPalette((String) ((Pair) it.next()).second, " ", "definedFunc", -7711273, new Object[0]);
				}
		}
		
	 	
		private void addVariables() {
				// CSS variable getter with selector
				addBlockToPalette(new CreateBlock("s", "( %m.var )", "VARIABLE", "getVar", ""));
				// CSS variable setter with selector
				addBlockToPalette(new CreateBlock(" ", "set variable %m.var to %s.val", "VARIABLE", "setVar", ""));
		}
		
		private void allocatePalette(int var1) {
				if(2 == var1) {
						LayoutParams var2 = new LayoutParams((int)LayoutUtil.getDip(this, 320.0F), -1);
						this.areaPalette.setLayoutParams(var2);
						LayoutParams var3 = new LayoutParams(-2, -2);
						var3.gravity = 81;
						int var4 = (int)this.getResources().getDimension(R.dimen.action_button_margin);
						var3.setMargins(var4, var4, var4, var4);
						this.fab.setLayoutParams(var3);
						RelativeLayout.LayoutParams var5 = new RelativeLayout.LayoutParams(-2, -1);
						var5.addRule(10);
						var5.addRule(11);
						var5.topMargin = this.getSupportActionBar().getHeight();
						this.layoutPalette.setOrientation(LinearLayout.HORIZONTAL);
						this.layoutPalette.setLayoutParams(var5);
				} else {
						LayoutParams var6 = new LayoutParams(-1, (int)LayoutUtil.getDip(this, 240.0F));
						this.areaPalette.setLayoutParams(var6);
						LayoutParams var7 = new LayoutParams(-2, -2);
						var7.gravity = 21;
						int var8 = (int)this.getResources().getDimension(R.dimen.action_button_margin);
						var7.setMargins(var8, var8, var8, var8);
						this.fab.setLayoutParams(var7);
						RelativeLayout.LayoutParams var9 = new RelativeLayout.LayoutParams(-1, -2);
						var9.addRule(9);
						var9.addRule(12);
						this.layoutPalette.setOrientation(LinearLayout.VERTICAL);
						this.layoutPalette.setLayoutParams(var9);
				}
				
				this.initPaletteAnimation(var1);
		}
		
		private void backupCurrentData(Bundle bundle) {
		}
		
		private void cancelIconDeleteAnimation() {
				if (this.aniShowTopMenu != null && this.aniShowTopMenu.isRunning()) {
						this.aniShowTopMenu.cancel();
				}
				if (this.aniHideTopMenu != null && this.aniHideTopMenu.isRunning()) {
						this.aniHideTopMenu.cancel();
				}
		}
		
		private void cancelPaletteAnimation() {
				if (this.aniShowPalette.isRunning()) {
						this.aniShowPalette.cancel();
				}
				if (this.aniHidePalette.isRunning()) {
						this.aniHidePalette.cancel();
				}
		}
		
		private RadioButton createSingleItem(String str) {
				RadioButton radioButton = new RadioButton(this);
				radioButton.setText(str);
				ViewGroup.LayoutParams layoutParams = new LayoutParams(-1, (int) (40.0f * LayoutUtil.getDip(this.context, 1.0f)));
				radioButton.setGravity(19);
				radioButton.setLayoutParams(layoutParams);
				return radioButton;
		}
		
		private void dragStart() {
				if (this.currentTouchedView != null) {
						this.paletteBlock.setDragEnabled(false);
						this.editor.setScrollEnabled(false);
						if (this.useVibrate) {
								this.vibrator.vibrate(100);
						}
						this.isDragged = true;
						if (((Block) this.currentTouchedView).getBlockType() == 0) {
								getOriginalState((Block) this.currentTouchedView);
								showIconDelete(true);
								this.dummy.makeDummyWithBlock((Block) this.currentTouchedView);
								this.pane.setVisibleBlock((Block) this.currentTouchedView, 8);
								this.pane.removeRelation((Block) this.currentTouchedView);
						} else {
								this.dummy.makeDummyWithBlock((Block) this.currentTouchedView);
						}
						this.pane.prepareToDrag((Block) this.currentTouchedView);
						this.dummy.moveDummy(this.currentTouchedView, this.posInitX, this.posInitY, this.posInitX, this.posInitY, (float)BLOCK_DRAG_X, (float)BLOCK_DRAG_Y);
						this.dummy.getDummyPosition(this.posDummy);
						if (this.editor.hitTest((float) this.posDummy[0], (float) this.posDummy[1])) {
								this.dummy.setAllow(true);
								this.pane.updateFeedbackFor((Block) this.currentTouchedView, this.posDummy[0], this.posDummy[1]);
								return;
						}
						this.dummy.setAllow(false);
						this.pane.hideFeedbackShape();
				}
		}
		
		private int getLabelWidth(TextView textView) {
				Rect rect = new Rect();
				textView.getPaint().getTextBounds(textView.getText().toString(), 0, textView.getText().length(), rect);
				return rect.width();
		}
		
		private void getOriginalState(Block block) {
				this.originalParent = null;
				this.originalArgIndex = -1;
				this.originalInsertOption = 0;
				this.posOriginal = new int[2];
				block.getLocationOnScreen(this.posOriginal);
				if (block.parentBlock != null) {
						this.originalParent = block.parentBlock;
				}
				if (this.originalParent != null) {
						if (this.originalParent.nextBlock == ((Integer) block.getTag()).intValue()) {
								this.originalInsertOption = 0;
						} else if (this.originalParent.subStack1 == ((Integer) block.getTag()).intValue()) {
								this.originalInsertOption = 2;
						} else if (this.originalParent.subStack2 == ((Integer) block.getTag()).intValue()) {
								this.originalInsertOption = 3;
						} else if (this.originalParent.args.contains(block)) {
								this.originalInsertOption = 5;
								this.originalArgIndex = this.originalParent.args.indexOf(block);
						}
				}
		}
		
		private boolean hitTestIconDelete(float f, float f2) {
				if (iconDelete == null) return false;
				int[] pos = new int[2];
				iconDelete.getLocationOnScreen(pos);
				return f >= (float) pos[0] && f <= (float) (pos[0] + iconDelete.getWidth()) && f2 >= (float) pos[1] && f2 <= (float) (pos[1] + iconDelete.getHeight());
		}
		
		private void initIconDeleteAnimation() {
				this.aniShowTopMenu = ObjectAnimator.ofFloat(this.iconDelete, "TranslationY", new float[]{0.0f});
				this.aniShowTopMenu.setDuration(300);
				this.aniShowTopMenu.setInterpolator(new DecelerateInterpolator());
				
				this.aniHideTopMenu = ObjectAnimator.ofFloat(this.iconDelete, "TranslationY", new float[]{LayoutUtil.getDip(this, 66.0f)});
				this.aniHideTopMenu.setDuration(200);
				this.aniHideTopMenu.setInterpolator(new DecelerateInterpolator());
				this.bInitIconDeleteAnimation = true;
		}
		
		private void initPaletteAnimation(int i) {
				if (2 == i) {
						if (this.isPaletteOpened) {
								this.layoutPalette.setTranslationX(0.0f);
								this.layoutPalette.setTranslationY(0.0f);
						} else {
								this.layoutPalette.setTranslationX((float) ((int) LayoutUtil.getDip(this, 320.0f)));
								this.layoutPalette.setTranslationY(0.0f);
						}
				} else if (this.isPaletteOpened) {
						this.layoutPalette.setTranslationX(0.0f);
						this.layoutPalette.setTranslationY(0.0f);
				} else {
						this.layoutPalette.setTranslationX(0.0f);
						this.layoutPalette.setTranslationY((float) ((int) LayoutUtil.getDip(this, 240.0f)));
				}
				if (2 == i) {
						this.aniShowPalette = ObjectAnimator.ofFloat(this.layoutPalette, "TranslationX", new float[]{0.0f});
						this.aniHidePalette = ObjectAnimator.ofFloat(this.layoutPalette, "TranslationX", new float[]{(float) ((int) LayoutUtil.getDip(this, 320.0f))});
				} else {
						this.aniShowPalette = ObjectAnimator.ofFloat(this.layoutPalette, "TranslationY", new float[]{0.0f});
						this.aniHidePalette = ObjectAnimator.ofFloat(this.layoutPalette, "TranslationY", new float[]{(float) ((int) LayoutUtil.getDip(this, 240.0f))});
				}
				this.aniShowPalette.removeAllListeners();
				this.aniHidePalette.removeAllListeners();
				this.aniShowPalette.addListener(new Animator.AnimatorListener() {
						public void onAnimationCancel(Animator var1) {
						}
						
						public void onAnimationEnd(Animator var1) {
								updateIconDeletePosition();
						}
						
						public void onAnimationRepeat(Animator var1) {
						}
						
						public void onAnimationStart(Animator var1) {
						}
				});
				this.aniHidePalette.addListener(new Animator.AnimatorListener() {
						public void onAnimationCancel(Animator var1) {
						}
						
						public void onAnimationEnd(Animator var1) {
						}
						
						public void onAnimationRepeat(Animator var1) {
						}
						
						public void onAnimationStart(Animator var1) {
								updateIconDeletePosition();
						}
				});
				this.aniShowPalette.setDuration(500);
				this.aniShowPalette.setInterpolator(new DecelerateInterpolator());
				this.aniHidePalette.setDuration(300);
				this.aniHidePalette.setInterpolator(new DecelerateInterpolator());
				this.bInitPaletteAnimation = true;
		}
		
		private void loadLogic() {
				Map hashMap = new HashMap();
				ArrayList blocks = DesignDataManager.getBlocks(filename, this.id + LOGIC_NAME_SEPARATOR + this.eventName);
				if (blocks != null) {
						Iterator it = blocks.iterator();
						int i = 1;
						while (it.hasNext()) {
								Block makeBlockFromBean = makeBlockFromBean((BlockBean) it.next());
								hashMap.put(Integer.valueOf(((Integer) makeBlockFromBean.getTag()).intValue()), makeBlockFromBean);
								this.pane.blockId = Math.max(this.pane.blockId, ((Integer) makeBlockFromBean.getTag()).intValue() + 1);
								this.pane.addBlock(makeBlockFromBean, 0, 0);
								makeBlockFromBean.setOnTouchListener(this);
								if (i != 0) {
										this.pane.getRoot().insertBlock(makeBlockFromBean);
										i = 0;
								}
						}
						Iterator it2 = blocks.iterator();
						while (it2.hasNext()) {
								BlockBean blockBean = (BlockBean) it2.next();
								Block block = (Block) hashMap.get(Integer.valueOf(blockBean.id));
								if (block != null) {
										Block block2;
										if (blockBean.subStack1 >= 0) {
												block2 = (Block) hashMap.get(Integer.valueOf(blockBean.subStack1));
												if (block2 != null) {
														block.insertBlockSub1(block2);
												}
										}
										if (blockBean.subStack2 >= 0) {
												block2 = (Block) hashMap.get(Integer.valueOf(blockBean.subStack2));
												if (block2 != null) {
														block.insertBlockSub2(block2);
												}
										}
										if (blockBean.nextBlock >= 0) {
												block2 = (Block) hashMap.get(Integer.valueOf(blockBean.nextBlock));
												if (block2 != null) {
														block.insertBlock(block2);
												}
										}
										int size = blockBean.parameters.size();
										for (int i2 = 0; i2 < size; i2++) {
												String str = (String) blockBean.parameters.get(i2);
												if (str != null && str.length() > 0) {
														if (str.charAt(0) == '@') {
																block2 = (Block) hashMap.get(Integer.valueOf(Integer.valueOf(str.substring(1)).intValue()));
																if (block2 != null) {
																		block.replaceArgWithBlock((BlockBase) block.args.get(i2), block2);
																}
														} else {
																((BlockArg) block.args.get(i2)).setArgValue(str);
																block.recalcWidthToParent();
														}
												}
										}
								}
						}
						this.pane.getRoot().fixLayout();
						this.pane.calculateWidthHeight();
				}
		}
		
		private void makeBlockWithSpec(ViewGroup viewGroup, ViewGroup viewGroup2, Block block, String str, ArrayList<Pair<String, String>> arrayList) {
				int i;
				int i2;
				viewGroup.removeAllViews();
				viewGroup.addView(block);
				Iterator it = arrayList.iterator();
				String str2 = str;
				while (it.hasNext()) {
						Pair pair = (Pair) it.next();
						str2 = ((String) pair.first).equals("b") ? str2 + " %b." + ((String) pair.second) : ((String) pair.first).equals("d") ? str2 + " %d." + ((String) pair.second) : ((String) pair.first).equals("s") ? str2 + " %s." + ((String) pair.second) : str2 + " " + ((String) pair.second);
				}
				block.setSpec(str2, null);
				int size = arrayList.size();
				int i3 = 0;
				for (i = 0; i < size; i++) {
						Pair pair = (Pair) arrayList.get(i);
						Block block2;
						int i4;
						if (((String) pair.first).equals("b")) {
								block2 = new Block(getApplicationContext(), arrayList.indexOf(pair) + 1, (String) pair.second, "b", "getParam", new Object[]{Integer.valueOf(-7711273), ""});
								viewGroup.addView(block2);
								i4 = i3 + 1;
								block.replaceArgWithBlock((BlockBase) block.args.get(i3), block2);
								i2 = i4;
						} else if (((String) pair.first).equals("d")) {
								block2 = new Block(getApplicationContext(), arrayList.indexOf(pair) + 1, (String) pair.second, "d", "getParam", new Object[]{Integer.valueOf(-7711273), ""});
								viewGroup.addView(block2);
								i4 = i3 + 1;
								block.replaceArgWithBlock((BlockBase) block.args.get(i3), block2);
								i2 = i4;
						} else if (((String) pair.first).equals("s")) {
								block2 = new Block(getApplicationContext(), arrayList.indexOf(pair) + 1, (String) pair.second, "s", "getParam", new Object[]{Integer.valueOf(-7711273), ""});
								viewGroup.addView(block2);
								i4 = i3 + 1;
								block.replaceArgWithBlock((BlockBase) block.args.get(i3), block2);
								i2 = i4;
						} else {
								i2 = i3;
						}
						i3 = i2;
				}
				block.fixLayout();
				viewGroup2.removeAllViews();
				i = block.labelsAndArgs.size();
				for (i3 = 0; i3 < i; i3++) {
						View view = (View) block.labelsAndArgs.get(i3);
						int i5 = 0;
						if (((String) block.argTypes.get(i3)).equals("label")) {
								i5 = getLabelWidth((TextView) view);
						}
						if (view instanceof Block) {
								i5 = ((Block) view).getWidthSum();
						}
						i2 = (int) (((float) i5) + LayoutUtil.getDip(getApplicationContext(), 4.0f));
						ImageView imageView = new ImageView(this);
						imageView.setImageResource(R.drawable.ic_remove_grey600_24dp);
						imageView.setScaleType(ScaleType.CENTER_INSIDE);
						imageView.setPadding(0, (int) LayoutUtil.getDip(getApplicationContext(), 4.0f), 0, (int) LayoutUtil.getDip(getApplicationContext(), 4.0f));
						imageView.setLayoutParams(new LayoutParams(i2, -1));
						viewGroup2.addView(imageView);
						if (i3 == 0) {
								imageView.setVisibility(View.INVISIBLE);
								imageView.setEnabled(false);
						} else {
								imageView.setOnClickListener(new LogicEditorActivity$18(this, arrayList, viewGroup2, viewGroup, block, str));
						}
				}
		}
		
		
		
		class LogicEditorActivity$18 implements OnClickListener {
				// $FF: synthetic field
				final LogicEditorActivity this$0;
				// $FF: synthetic field
				final ArrayList val$args;
				// $FF: synthetic field
				final Block val$b;
				// $FF: synthetic field
				final ViewGroup val$removeArea;
				// $FF: synthetic field
				final String val$spec;
				// $FF: synthetic field
				final ViewGroup val$v;
				
				LogicEditorActivity$18(LogicEditorActivity var1, ArrayList var2, ViewGroup var3, ViewGroup var4, Block var5, String var6) {
						this.this$0 = var1;
						this.val$args = var2;
						this.val$removeArea = var3;
						this.val$v = var4;
						this.val$b = var5;
						this.val$spec = var6;
				}
				
				public void onClick(View var1) {
						this.val$args.remove(-1 + this.val$removeArea.indexOfChild(var1));
						ArrayList var3 = new ArrayList(Arrays.asList(new String[0]/*DefineSource.getUsedWord(DesignActivity.getScId())*/));
						Iterator var4 = this.val$args.iterator();
						
						while(var4.hasNext()) {
								Pair var5 = (Pair)var4.next();
								if(!((String)var5.first).equals("t")) {
										var3.add(var5.second);
								}
						}
						
						booleanValidator.setUsedWords((String[])var3.toArray(new String[var3.size()]));
						numberValidator.setUsedWords((String[])var3.toArray(new String[var3.size()]));
						stringValidator.setUsedWords((String[])var3.toArray(new String[var3.size()]));
						makeBlockWithSpec(this.val$v, this.val$removeArea, this.val$b, this.val$spec, this.val$args);
				}
		}
		
		private void openPalette(boolean z) {
				if (!this.bInitPaletteAnimation) {
						initPaletteAnimation(getResources().getConfiguration().orientation);
				}
				if (this.isPaletteOpened != z) {
						this.isPaletteOpened = z;
						cancelPaletteAnimation();
						if (z) {
								this.aniShowPalette.start();
						} else {
								this.aniHidePalette.start();
						}
				}
		}

		private void saveLogic() {
				DesignDataManager.setBlocks(filename, this.id + LOGIC_NAME_SEPARATOR + this.eventName, this.pane.getBlocks());
		}
		
		private void showAddBlockPopup() {
				View inflate = LayoutUtil.inflate(this, R.layout.logic_popup_add_block);
				Builder builder = new Builder(this);
				builder.setView(inflate);
				builder.setTitle("Make new Block");
				ArrayList arrayList = new ArrayList();
				RelativeLayout relativeLayout = (RelativeLayout) inflate.findViewById(R.id.block_area);
				LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.remove_area);
				Block block = new Block(this, 0, "", " ", "definedFunc", new Object[]{Integer.valueOf(-7711273)});
				relativeLayout.addView(block);
				TextInputLayout textInputLayout = (TextInputLayout) inflate.findViewById(R.id.ti_boolean);
				TextInputLayout textInputLayout2 = (TextInputLayout) inflate.findViewById(R.id.ti_number);
				TextInputLayout textInputLayout3 = (TextInputLayout) inflate.findViewById(R.id.ti_string);
				VariableNameValidator variableNameValidator = new VariableNameValidator(this.context, (TextInputLayout) inflate.findViewById(R.id.ti_name), DefineSource.RESERVED_WORD, DefineSource.getUsedWord(DesignActivity.getScId()), DesignDataManager.getAllNamesForValid(filename));
				this.booleanValidator = new VariableNameValidator(this.context, textInputLayout, DefineSource.RESERVED_WORD, DefineSource.getUsedWord(DesignActivity.getScId()), new ArrayList());
				this.numberValidator = new VariableNameValidator(this.context, textInputLayout2, DefineSource.RESERVED_WORD, DefineSource.getUsedWord(DesignActivity.getScId()), new ArrayList());
				this.stringValidator = new VariableNameValidator(this.context, textInputLayout3, DefineSource.RESERVED_WORD, DefineSource.getUsedWord(DesignActivity.getScId()), new ArrayList());
				EditText editText = (EditText) inflate.findViewById(R.id.ed_name);
				EditText editText2 = (EditText) inflate.findViewById(R.id.ed_boolean);
				EditText editText3 = (EditText) inflate.findViewById(R.id.ed_number);
				EditText editText4 = (EditText) inflate.findViewById(R.id.ed_string);
				EditText editText5 = (EditText) inflate.findViewById(R.id.ed_label);
				editText.setPrivateImeOptions("defaultInputmode=english;");
				editText2.setPrivateImeOptions("defaultInputmode=english;");
				editText3.setPrivateImeOptions("defaultInputmode=english;");
				editText4.setPrivateImeOptions("defaultInputmode=english;");
				editText5.setPrivateImeOptions("defaultInputmode=english;");
				editText.addTextChangedListener(new LogicEditorActivity$12(this, relativeLayout, linearLayout, block, arrayList));
				((Button) inflate.findViewById(R.id.add_boolean)).setOnClickListener(new LogicEditorActivity$13(this, arrayList, editText2, relativeLayout, linearLayout, block, editText, editText3, editText4));
				((Button) inflate.findViewById(R.id.add_number)).setOnClickListener(new LogicEditorActivity$14(this, arrayList, editText3, relativeLayout, linearLayout, block, editText, editText2, editText4));
				((Button) inflate.findViewById(R.id.add_string)).setOnClickListener(new LogicEditorActivity$15(this, arrayList, editText4, relativeLayout, linearLayout, block, editText, editText2, editText3));
				((Button) inflate.findViewById(R.id.add_label)).setOnClickListener(new LogicEditorActivity$16(this, arrayList, editText5, relativeLayout, linearLayout, block, editText));
				builder.setNegativeButton("Cancel", null);
				builder.setPositiveButton("Save", null);
				this.mDlg = builder.create();
				this.mDlg.setOnShowListener(new LogicEditorActivity$17(this, variableNameValidator, editText, block));
				this.mDlg.show();
		}
		
		
		class LogicEditorActivity$17 implements DialogInterface.OnShowListener {
				// $FF: synthetic field
				final LogicEditorActivity this$0;
				// $FF: synthetic field
				final Block val$block;
				// $FF: synthetic field
				final EditText val$edName;
				// $FF: synthetic field
				final VariableNameValidator val$varNameValidator;
				
				LogicEditorActivity$17(LogicEditorActivity var1, VariableNameValidator var2, EditText var3, Block var4) {
						this.this$0 = var1;
						this.val$varNameValidator = var2;
						this.val$edName = var3;
						this.val$block = var4;
				}
				
				public void onShow(DialogInterface var1) {
						LogicEditorActivity.access$800(this.this$0).getButton(-1).setOnClickListener(new LogicEditorActivity$17$1(this));
				}
		}
		
		class LogicEditorActivity$17$1 implements OnClickListener {
				// $FF: synthetic field
				final LogicEditorActivity$17 this$1;
				
				LogicEditorActivity$17$1(LogicEditorActivity$17 var1) {
						this.this$1 = var1;
				}
				
				public void onClick(View var1) {
						if(this.this$1.val$varNameValidator.isValid()) {
								DesignDataManager.addFunction(LogicEditorActivity.filename, this.this$1.val$edName.getText().toString(), this.this$1.val$block.mSpec);
								this.this$1.this$0.onBlockCategorySelect(5, -7711273);
								LogicEditorActivity.access$800(this.this$1.this$0).dismiss();
						}
				}
		}
		
		
		
		class LogicEditorActivity$16 implements OnClickListener {
				// $FF: synthetic field
				final LogicEditorActivity this$0;
				// $FF: synthetic field
				final ArrayList val$args;
				// $FF: synthetic field
				final Block val$block;
				// $FF: synthetic field
				final RelativeLayout val$blockArea;
				// $FF: synthetic field
				final EditText val$edLabel;
				// $FF: synthetic field
				final EditText val$edName;
				// $FF: synthetic field
				final LinearLayout val$removeArea;
				
				LogicEditorActivity$16(LogicEditorActivity var1, ArrayList var2, EditText var3, RelativeLayout var4, LinearLayout var5, Block var6, EditText var7) {
						this.this$0 = var1;
						this.val$args = var2;
						this.val$edLabel = var3;
						this.val$blockArea = var4;
						this.val$removeArea = var5;
						this.val$block = var6;
						this.val$edName = var7;
				}
				
				public void onClick(View var1) {
						this.val$args.add(new Pair("t", this.val$edLabel.getText().toString()));
						LogicEditorActivity.access$1200(this.this$0, this.val$blockArea, this.val$removeArea, this.val$block, this.val$edName.getText().toString(), this.val$args);
				}
		}
		
		
		
		
		class LogicEditorActivity$15 implements OnClickListener {
				// $FF: synthetic field
				final LogicEditorActivity this$0;
				// $FF: synthetic field
				final ArrayList val$args;
				// $FF: synthetic field
				final Block val$block;
				// $FF: synthetic field
				final RelativeLayout val$blockArea;
				// $FF: synthetic field
				final EditText val$edBoolean;
				// $FF: synthetic field
				final EditText val$edName;
				// $FF: synthetic field
				final EditText val$edNumber;
				// $FF: synthetic field
				final EditText val$edString;
				// $FF: synthetic field
				final LinearLayout val$removeArea;
				
				LogicEditorActivity$15(LogicEditorActivity var1, ArrayList var2, EditText var3, RelativeLayout var4, LinearLayout var5, Block var6, EditText var7, EditText var8, EditText var9) {
						this.this$0 = var1;
						this.val$args = var2;
						this.val$edString = var3;
						this.val$blockArea = var4;
						this.val$removeArea = var5;
						this.val$block = var6;
						this.val$edName = var7;
						this.val$edBoolean = var8;
						this.val$edNumber = var9;
				}
				
				public void onClick(View var1) {
						if(LogicEditorActivity.access$1500(this.this$0).isValid()) {
								this.val$args.add(new Pair("s", this.val$edString.getText().toString()));
								LogicEditorActivity.access$1200(this.this$0, this.val$blockArea, this.val$removeArea, this.val$block, this.val$edName.getText().toString(), this.val$args);
								ArrayList var3 = new ArrayList(Arrays.asList(DefineSource.getUsedWord(DesignActivity.getScId())));
								Iterator var4 = this.val$args.iterator();
								
								while(var4.hasNext()) {
										Pair var5 = (Pair)var4.next();
										if(!((String)var5.first).equals("t")) {
												var3.add(var5.second);
										}
								}
								
								LogicEditorActivity.access$1300(this.this$0).setUsedWords((String[])var3.toArray(new String[var3.size()]));
								LogicEditorActivity.access$1300(this.this$0).setText(this.val$edBoolean.getText().toString());
								LogicEditorActivity.access$1400(this.this$0).setUsedWords((String[])var3.toArray(new String[var3.size()]));
								LogicEditorActivity.access$1400(this.this$0).setText(this.val$edNumber.getText().toString());
								LogicEditorActivity.access$1500(this.this$0).setUsedWords((String[])var3.toArray(new String[var3.size()]));
								this.val$edString.setText("");
						}
				}
		}

		class LogicEditorActivity$14 implements OnClickListener {
				// $FF: synthetic field
				final LogicEditorActivity this$0;
				// $FF: synthetic field
				final ArrayList val$args;
				// $FF: synthetic field
				final Block val$block;
				// $FF: synthetic field
				final RelativeLayout val$blockArea;
				// $FF: synthetic field
				final EditText val$edBoolean;
				// $FF: synthetic field
				final EditText val$edName;
				// $FF: synthetic field
				final EditText val$edNumber;
				// $FF: synthetic field
				final EditText val$edString;
				// $FF: synthetic field
				final LinearLayout val$removeArea;
				
				LogicEditorActivity$14(LogicEditorActivity var1, ArrayList var2, EditText var3, RelativeLayout var4, LinearLayout var5, Block var6, EditText var7, EditText var8, EditText var9) {
						this.this$0 = var1;
						this.val$args = var2;
						this.val$edNumber = var3;
						this.val$blockArea = var4;
						this.val$removeArea = var5;
						this.val$block = var6;
						this.val$edName = var7;
						this.val$edBoolean = var8;
						this.val$edString = var9;
				}
				
				public void onClick(View var1) {
						if(LogicEditorActivity.access$1400(this.this$0).isValid()) {
								this.val$args.add(new Pair("d", this.val$edNumber.getText().toString()));
								LogicEditorActivity.access$1200(this.this$0, this.val$blockArea, this.val$removeArea, this.val$block, this.val$edName.getText().toString(), this.val$args);
								ArrayList var3 = new ArrayList(Arrays.asList(DefineSource.getUsedWord(DesignActivity.getScId())));
								Iterator var4 = this.val$args.iterator();
								
								while(var4.hasNext()) {
										Pair var5 = (Pair)var4.next();
										if(!((String)var5.first).equals("t")) {
												var3.add(var5.second);
										}
								}
								
								LogicEditorActivity.access$1300(this.this$0).setUsedWords((String[])var3.toArray(new String[var3.size()]));
								LogicEditorActivity.access$1300(this.this$0).setText(this.val$edBoolean.getText().toString());
								LogicEditorActivity.access$1400(this.this$0).setUsedWords((String[])var3.toArray(new String[var3.size()]));
								LogicEditorActivity.access$1500(this.this$0).setUsedWords((String[])var3.toArray(new String[var3.size()]));
								LogicEditorActivity.access$1500(this.this$0).setText(this.val$edString.getText().toString());
								this.val$edNumber.setText("");
						}
				}
		}
		
		class LogicEditorActivity$12 implements TextWatcher {
				// $FF: synthetic field
				final LogicEditorActivity this$0;
				// $FF: synthetic field
				final ArrayList val$args;
				// $FF: synthetic field
				final Block val$block;
				// $FF: synthetic field
				final RelativeLayout val$blockArea;
				// $FF: synthetic field
				final LinearLayout val$removeArea;
				
				LogicEditorActivity$12(LogicEditorActivity var1, RelativeLayout var2, LinearLayout var3, Block var4, ArrayList var5) {
						this.this$0 = var1;
						this.val$blockArea = var2;
						this.val$removeArea = var3;
						this.val$block = var4;
						this.val$args = var5;
				}
				
				public void afterTextChanged(Editable var1) {
						makeBlockWithSpec(this.val$blockArea, this.val$removeArea, this.val$block, var1.toString(), this.val$args);
				}
				
				public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
				}
				
				public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
				}
		}
		
		
		class LogicEditorActivity$13 implements OnClickListener {
				// $FF: synthetic field
				final LogicEditorActivity this$0;
				// $FF: synthetic field
				final ArrayList val$args;
				// $FF: synthetic field
				final Block val$block;
				// $FF: synthetic field
				final RelativeLayout val$blockArea;
				// $FF: synthetic field
				final EditText val$edBoolean;
				// $FF: synthetic field
				final EditText val$edName;
				// $FF: synthetic field
				final EditText val$edNumber;
				// $FF: synthetic field
				final EditText val$edString;
				// $FF: synthetic field
				final LinearLayout val$removeArea;
				
				LogicEditorActivity$13(LogicEditorActivity var1, ArrayList var2, EditText var3, RelativeLayout var4, LinearLayout var5, Block var6, EditText var7, EditText var8, EditText var9) {
						this.this$0 = var1;
						this.val$args = var2;
						this.val$edBoolean = var3;
						this.val$blockArea = var4;
						this.val$removeArea = var5;
						this.val$block = var6;
						this.val$edName = var7;
						this.val$edNumber = var8;
						this.val$edString = var9;
				}
				
				public void onClick(View var1) {
						if(LogicEditorActivity.access$1300(this.this$0).isValid()) {
								this.val$args.add(new Pair("b", this.val$edBoolean.getText().toString()));
								LogicEditorActivity.access$1200(this.this$0, this.val$blockArea, this.val$removeArea, this.val$block, this.val$edName.getText().toString(), this.val$args);
								ArrayList var3 = new ArrayList(Arrays.asList(DefineSource.getUsedWord(DesignActivity.getScId())));
								Iterator var4 = this.val$args.iterator();
								
								while(var4.hasNext()) {
										Pair var5 = (Pair)var4.next();
										if(!((String)var5.first).equals("t")) {
												var3.add(var5.second);
										}
								}
								
								LogicEditorActivity.access$1300(this.this$0).setUsedWords((String[])var3.toArray(new String[var3.size()]));
								LogicEditorActivity.access$1400(this.this$0).setUsedWords((String[])var3.toArray(new String[var3.size()]));
								LogicEditorActivity.access$1400(this.this$0).setText(this.val$edNumber.getText().toString());
								LogicEditorActivity.access$1500(this.this$0).setUsedWords((String[])var3.toArray(new String[var3.size()]));
								LogicEditorActivity.access$1500(this.this$0).setText(this.val$edString.getText().toString());
								this.val$edBoolean.setText("");
						}
				}
		}
		
		// $FF: synthetic method
		static boolean access$000(LogicEditorActivity var0) {
				return var0.isPaletteOpened;
		}
		
		// $FF: synthetic method
		static void access$100(LogicEditorActivity var0, boolean var1) {
				var0.openPalette(var1);
		}
		
		// $FF: synthetic method
		static String access$1000(LogicEditorActivity var0) {
				return var0.id;
		}
		
		// $FF: synthetic method
		static String access$1100(LogicEditorActivity var0) {
				return var0.eventName;
		}
		
		// $FF: synthetic method
		static void access$1200(LogicEditorActivity var0, ViewGroup var1, ViewGroup var2, Block var3, String var4, ArrayList var5) {
				var0.makeBlockWithSpec(var1, var2, var3, var4, var5);
		}
		
		// $FF: synthetic method
		static VariableNameValidator access$1300(LogicEditorActivity var0) {
				return var0.booleanValidator;
		}
		
		// $FF: synthetic method
		static VariableNameValidator access$1400(LogicEditorActivity var0) {
				return var0.numberValidator;
		}
		
		// $FF: synthetic method
		static VariableNameValidator access$1500(LogicEditorActivity var0) {
				return var0.stringValidator;
		}
		
		// $FF: synthetic method
		static void access$200(LogicEditorActivity var0) {
				var0.updateIconDeletePosition();
		}
		
		// $FF: synthetic method
		static void access$300(LogicEditorActivity var0) {
				var0.dragStart();
		}
		
		// $FF: synthetic method
		static void access$400(LogicEditorActivity var0) {
				var0.saveLogic();
		}
		
		/*   // $FF: synthetic method
static void access$500(LogicEditorActivity var0) {
var0.dismissProgress();
}

// $FF: synthetic method
static void access$600(LogicEditorActivity var0) {
var0.dismissProgress();
}
*/
		// $FF: synthetic method
		static Context access$700(LogicEditorActivity var0) {
				return var0.context;
		}
		
		// $FF: synthetic method
		static AlertDialog access$800(LogicEditorActivity var0) {
				return var0.mDlg;
		}
		
		// $FF: synthetic method
		static BlockPane access$900(LogicEditorActivity var0) {
				return var0.pane;
		}

		private void showAddListPopup() {
				View inflate = LayoutUtil.inflate(this, R.layout.logic_popup_add_list);
				Builder builder = new Builder(this);
				builder.setView(inflate);
				builder.setTitle("Add List");
				RadioGroup radioGroup = (RadioGroup) inflate.findViewById(R.id.rg_type);
				EditText editText = (EditText) inflate.findViewById(R.id.ed_input);
				VariableNameValidator variableNameValidator = new VariableNameValidator(this.context, (TextInputLayout) inflate.findViewById(R.id.ti_input), DefineSource.RESERVED_WORD, DefineSource.getUsedWord(DesignActivity.getScId()), DesignDataManager.getAllNamesForValid(filename));
				editText.setPrivateImeOptions("defaultInputmode=english;");
				builder.setNegativeButton("Cancel", new LogicEditorActivity$9(this));
				builder.setPositiveButton("Save", null);
				this.mDlg = builder.create();
				this.mDlg.setOnShowListener(new LogicEditorActivity$10(this, variableNameValidator, radioGroup, editText));
				this.mDlg.show();
		}
		
		
		
		
		class LogicEditorActivity$10 implements DialogInterface.OnShowListener {
				// $FF: synthetic field
				final LogicEditorActivity this$0;
				// $FF: synthetic field
				final EditText val$edInput;
				// $FF: synthetic field
				final RadioGroup val$rgType;
				// $FF: synthetic field
				final VariableNameValidator val$varNameValidator;
				
				LogicEditorActivity$10(LogicEditorActivity var1, VariableNameValidator var2, RadioGroup var3, EditText var4) {
						this.this$0 = var1;
						this.val$varNameValidator = var2;
						this.val$rgType = var3;
						this.val$edInput = var4;
				}
				
				public void onShow(DialogInterface var1) {
						LogicEditorActivity.access$800(this.this$0).getButton(-1).setOnClickListener(new LogicEditorActivity$10$1(this));
				}
		}
		
		
		
		class LogicEditorActivity$10$1 implements OnClickListener {
				// $FF: synthetic field
				final LogicEditorActivity$10 this$1;
				
				LogicEditorActivity$10$1(LogicEditorActivity$10 var1) {
						this.this$1 = var1;
				}
				
				public void onClick(View var1) {
						if(this.this$1.val$varNameValidator.isValid()) {
								byte var2 = 1;
								if(this.this$1.val$rgType.getCheckedRadioButtonId() == R.id.rb_int) {
										var2 = 1;
								} else if(this.this$1.val$rgType.getCheckedRadioButtonId() == R.id.rb_string) {
										var2 = 2;
								}
								
								String var3 = this.this$1.val$edInput.getText().toString();
								DesignDataManager.addList(LogicEditorActivity.filename, var2, var3);
								this.this$1.this$0.onBlockCategorySelect(1, -3384542);
								LogicEditorActivity.access$800(this.this$1.this$0).dismiss();
						}
				}
		}
		
		
		
		
		
		class LogicEditorActivity$9 implements DialogInterface.OnClickListener {
				// $FF: synthetic field
				final LogicEditorActivity this$0;
				
				LogicEditorActivity$9(LogicEditorActivity var1) {
						this.this$0 = var1;
				}
				
				public void onClick(DialogInterface var1, int var2) {
						LogicEditorActivity.access$800(this.this$0).dismiss();
				}
		}

		private void showAddVarPopup() {
				View inflate = LayoutUtil.inflate(this, R.layout.logic_popup_add_variable);
				Builder builder = new Builder(this);
				builder.setView(inflate);
				builder.setTitle("Add Variable");
				EditText editText = (EditText) inflate.findViewById(R.id.ed_input);
				editText.setPrivateImeOptions("defaultInputmode=english;");
				VariableNameValidator variableNameValidator = new VariableNameValidator(this.context, (TextInputLayout) inflate.findViewById(R.id.ti_input), DefineSource.RESERVED_WORD, DefineSource.getUsedWord(DesignActivity.getScId()), DesignDataManager.getAllNamesForValid(filename));
				builder.setNegativeButton("Cancel", null);
				builder.setPositiveButton("Save", null);
				this.mDlg = builder.create();
				this.mDlg.setOnShowListener(new LogicEditorActivity$7(this, editText, variableNameValidator));
				this.mDlg.show();
		}

		class LogicEditorActivity$7 implements DialogInterface.OnShowListener {
				final LogicEditorActivity this$0;
				final EditText val$edInput;
				final VariableNameValidator val$varNameValidator;
				
				LogicEditorActivity$7(LogicEditorActivity var1, EditText var3, VariableNameValidator var4) {
						this.this$0 = var1;
						this.val$edInput = var3;
						this.val$varNameValidator = var4;
				}
				
				public void onShow(DialogInterface var1) {
						LogicEditorActivity.access$800(this.this$0).getButton(-1).setOnClickListener(new LogicEditorActivity$7$1(this));
				}
		}
		
		class LogicEditorActivity$7$1 implements OnClickListener {
				final LogicEditorActivity$7 this$1;
				
				LogicEditorActivity$7$1(LogicEditorActivity$7 var1) {
						this.this$1 = var1;
				}
				
				public void onClick(View var1) {
						String var3 = this.this$1.val$edInput.getText().toString();
						if(this.this$1.val$varNameValidator.isValid()) {
								// Always use String type (2) for CSS variables
								DesignDataManager.addVariable(LogicEditorActivity.filename, 2, var3);
								this.this$1.this$0.onBlockCategorySelect(0, -1147626);
								LogicEditorActivity.access$800(this.this$1.this$0).dismiss();
						}
				}
		}

		private void showIconDelete(boolean z) {
				if (!this.bInitIconDeleteAnimation) {
						initIconDeleteAnimation();
				}
				if (this.bShowIconDelete != z) {
						this.bShowIconDelete = z;
						cancelIconDeleteAnimation();
						if (z) {
								this.aniShowTopMenu.start();
						} else {
								this.aniHideTopMenu.start();
						}
				}
		}
		
		private void showRemoveListPopup() {
				View inflate = LayoutUtil.inflate(this, R.layout.property_popup_selector_single);
				Builder builder = new Builder(this);
				builder.setView(inflate);
				builder.setTitle("Remove list");
				ViewGroup viewGroup = (ViewGroup) inflate.findViewById(R.id.rg_content);
				ArrayList arrayList = new ArrayList();
				Iterator it = DesignDataManager.getLists(filename).iterator();
				while (it.hasNext()) {
						viewGroup.addView(createSingleItem((String) ((Pair) it.next()).second));
				}
				builder.setNegativeButton("Cancel", null);
				builder.setPositiveButton("Save", null);
				this.mDlg = builder.create();
				this.mDlg.setOnShowListener(new LogicEditorActivity$11(this, viewGroup));
				this.mDlg.show();
		}
		
		
		class LogicEditorActivity$11 implements DialogInterface.OnShowListener {
				// $FF: synthetic field
				final LogicEditorActivity this$0;
				// $FF: synthetic field
				final ViewGroup val$content;
				
				LogicEditorActivity$11(LogicEditorActivity var1, ViewGroup var2) {
						this.this$0 = var1;
						this.val$content = var2;
				}
				
				public void onShow(DialogInterface var1) {
						LogicEditorActivity.access$800(this.this$0).getButton(-1).setOnClickListener(new LogicEditorActivity$11$1(this));
				}
		}

		class LogicEditorActivity$11$1 implements OnClickListener {
				// $FF: synthetic field
				final LogicEditorActivity$11 this$1;
				
				LogicEditorActivity$11$1(LogicEditorActivity$11 var1) {
						this.this$1 = var1;
				}
				
				public void onClick(View var1) {
						int var2 = this.this$1.val$content.getChildCount();
						int var3 = 0;
						
						while(true) {
								if(var3 < var2) {
										RadioButton var4 = (RadioButton)this.this$1.val$content.getChildAt(var3);
										if(!var4.isChecked()) {
												++var3;
												continue;
										}
										
										if(LogicEditorActivity.access$900(this.this$1.this$0).isExistListBlock(var4.getText().toString()) || DesignDataManager.isExistListBlock(LogicEditorActivity.filename, var4.getText().toString(), LogicEditorActivity.access$1000(this.this$1.this$0) + "_" + LogicEditorActivity.access$1100(this.this$1.this$0))) {
												Toast.makeText(this.this$1.this$0.getApplicationContext(), "Selected list is currently used\nPlease try again after removing the blocks with this list", Toast.LENGTH_SHORT).show();
												return;
										}
										
										DesignDataManager.removeList(LogicEditorActivity.filename, var4.getText().toString());
										this.this$1.this$0.onBlockCategorySelect(1, -3384542);
								}
								
								LogicEditorActivity.access$800(this.this$1.this$0).dismiss();
								return;
						}
				}
		}
		
		
		
		
		
		private void showRemoveVarPopup() {
				View inflate = LayoutUtil.inflate(this, R.layout.property_popup_selector_single);
				Builder builder = new Builder(this);
				builder.setView(inflate);
				builder.setTitle("Remove a variable");
				ViewGroup viewGroup = (ViewGroup) inflate.findViewById(R.id.rg_content);
				ArrayList arrayList = new ArrayList();
				Iterator it = DesignDataManager.getVariables(filename).iterator();
				while (it.hasNext()) {
						viewGroup.addView(createSingleItem((String) ((Pair) it.next()).second));
				}
				builder.setNegativeButton("Cancel", null);
				builder.setPositiveButton("Save", null);
				this.mDlg = builder.create();
				this.mDlg.setOnShowListener(new LogicEditorActivity$8(this, viewGroup));
				this.mDlg.show();
		}
		
		
		
		class LogicEditorActivity$8 implements DialogInterface.OnShowListener {
				// $FF: synthetic field
				final LogicEditorActivity this$0;
				// $FF: synthetic field
				final ViewGroup val$content;
				
				LogicEditorActivity$8(LogicEditorActivity var1, ViewGroup var2) {
						this.this$0 = var1;
						this.val$content = var2;
				}
				
				public void onShow(DialogInterface var1) {
						LogicEditorActivity.access$800(this.this$0).getButton(-1).setOnClickListener(new LogicEditorActivity$8$1(this));
				}
		}
		
		
		class LogicEditorActivity$8$1 implements OnClickListener {
				// $FF: synthetic field
				final LogicEditorActivity$8 this$1;
				
				LogicEditorActivity$8$1(LogicEditorActivity$8 var1) {
						this.this$1 = var1;
				}
				
				public void onClick(View var1) {
						int var2 = this.this$1.val$content.getChildCount();
						int var3 = 0;
						
						while(true) {
								if(var3 < var2) {
										RadioButton var4 = (RadioButton)this.this$1.val$content.getChildAt(var3);
										if(!var4.isChecked()) {
												++var3;
												continue;
										}
										
										if(LogicEditorActivity.access$900(this.this$1.this$0).isExistVariableBlock(var4.getText().toString()) || DesignDataManager.isExistVariableBlock(LogicEditorActivity.filename, var4.getText().toString(), LogicEditorActivity.access$1000(this.this$1.this$0) + "_" + LogicEditorActivity.access$1100(this.this$1.this$0))) {
												Toast.makeText(this.this$1.this$0.getApplicationContext(), "Selected variable is currently used\nPlease try again after removing the blocks with this variable", Toast.LENGTH_SHORT).show();
												return;
										}
										
										DesignDataManager.removeVariable(LogicEditorActivity.filename, var4.getText().toString());
										this.this$1.this$0.onBlockCategorySelect(0, -1147626);
								}
								
								LogicEditorActivity.access$800(this.this$1.this$0).dismiss();
								return;
						}
				}
		}
		
		
		
		
		
		
		
		private void startBlockCopyInterface() {
		}
		
		/*   private void startLogicTutorialActivity() {
Intent intent = new Intent(this.context, LogicTutorialActivity.class);
intent.setFlags(536870912);
intent.putExtra("sc_id", DesignActivity.getScId());
startActivity(intent);
}

private void startManageImageActivity() {
Intent intent = new Intent(getApplicationContext(), ManageImageActivity.class);
intent.setFlags(536870912);
intent.putExtra("sc_id", DesignActivity.getScId());
startActivityForResult(intent, 209);
}*/
		
		private void updateIconDeletePosition() {
				// LogicTopMenu is anchored to top, so bottomMargin doesn't apply to it
		}
		
		public boolean checkValidForever() {
				int childCount = this.pane.getChildCount();
				int i = 0;
				while (i < childCount) {
						View childAt = this.pane.getChildAt(i);
						i = (!(childAt instanceof Block) || ((Block) childAt).mOpCode.equals("Forever")) ? i + 1 : i + 1;
				}
				return true;
		}
		
		public boolean checkValidZero() {
				return true;
		}
		
		public Block makeBlockFromBean(BlockBean blockBean) {
				return new Block(this, Integer.valueOf(blockBean.id).intValue(), blockBean.spec, blockBean.type, blockBean.opCode, new Object[]{Integer.valueOf(blockBean.color)});
		}
		
		public void onBackPressed() {
				if (this.isPaletteOpened) {
						openPalette(!this.isPaletteOpened);
						return;
				}
				//   showProgress();
				if (checkValidForever() && checkValidZero()) {
						try {
								//             new Handler().postDelayed(new Runnable() { @Override public void run() { finish(); } }, 500);
						} catch (Exception e) {
								e.printStackTrace();
						}
						super.onBackPressed();
				}
		}
		
		public void onBlockCategorySelect(int i, int i2) {
				this.paletteBlock.removeAllBlocks();
				switch (i) {
						case 0:
						// VARIABLES
						addButtonToPalette("Add variable", "variableAdd");
						addButtonToPalette("Remove variable", "variableRemove");
						addVariables();
						return;
						case 1:
						// SELECTORS
                            addBlockToPalette(new CreateBlock("c", "Class: .%m.htmlClass", "SELECTOR", ".class", ""));
                            addBlockToPalette(new CreateBlock("c", "ID: #%m.htmlId", "SELECTOR", "#id", ""));
                            addBlockToPalette(new CreateBlock("c", "Tag: %m.htmlTag", "SELECTOR", "tag", ""));
                        return;
						case 2:
						// LAYOUT
						addBlockToPalette(new CreateBlock(" ", "display: %m.display", "LAYOUT", "display", ""));
						addBlockToPalette(new CreateBlock(" ", "position: %m.position", "LAYOUT", "position", ""));
						addBlockToPalette(new CreateBlock(" ", "box-sizing: %m.boxSizing", "LAYOUT", "box-sizing", ""));
						addBlockToPalette(new CreateBlock(" ", "width: %d.val %m.unit", "LAYOUT", "width", ""));
						addBlockToPalette(new CreateBlock(" ", "min-width: %d.val %m.unit", "LAYOUT", "min-width", ""));
						addBlockToPalette(new CreateBlock(" ", "max-width: %d.val %m.unit", "LAYOUT", "max-width", ""));
						addBlockToPalette(new CreateBlock(" ", "height: %d.val %m.unit", "LAYOUT", "height", ""));
						addBlockToPalette(new CreateBlock(" ", "min-height: %d.val %m.unit", "LAYOUT", "min-height", ""));
						addBlockToPalette(new CreateBlock(" ", "max-height: %d.val %m.unit", "LAYOUT", "max-height", ""));
						
						addBlockToPalette(new CreateBlock(" ", "overflow: %m.overflow", "LAYOUT", "overflow", ""));
						addBlockToPalette(new CreateBlock(" ", "z-index: %d.val", "LAYOUT", "z-index", ""));
						addBlockToPalette(new CreateBlock(" ", "visibility: %m.visibility", "LAYOUT", "visibility", ""));
						addBlockToPalette(new CreateBlock(" ", "opacity: %d.val", "LAYOUT", "opacity", ""));
						addBlockToPalette(new CreateBlock(" ", "Anchor %m.side : %d.val %m.unit", "LAYOUT", "anchor", ""));
						addBlockToPalette(new CreateBlock(" ", "cursor: %m.cursor", "LAYOUT", "cursor", ""));
						addBlockToPalette(new CreateBlock(" ", "pointer-events: %m.pointerEvents", "LAYOUT", "pointer-events", ""));
						addBlockToPalette(new CreateBlock(" ", "user-select: %m.userSelect", "LAYOUT", "user-select", ""));
						addBlockToPalette(new CreateBlock(" ", "object-fit: %m.objectFit", "LAYOUT", "object-fit", ""));
						return;
						case 3:
						// SPACING
						addBlockToPalette(new CreateBlock(" ", "margin: %d.val %m.unit", "SPACING", "margin", ""));
						addBlockToPalette(new CreateBlock(" ", "padding: %d.val %m.unit", "SPACING", "padding", ""));
						addBlockToPalette(new CreateBlock(" ", "gap: %d.val %m.unit", "SPACING", "gap", ""));
						addBlockToPalette(new CreateBlock(" ", "row-gap: %d.val %m.unit", "SPACING", "row-gap", ""));
						addBlockToPalette(new CreateBlock(" ", "column-gap: %d.val %m.unit", "SPACING", "column-gap", ""));
						addBlockToPalette(new CreateBlock(" ", "margin-%m.side : %d.val %m.unit", "SPACING", "spacingSide", ""));
						addBlockToPalette(new CreateBlock(" ", "padding-%m.side : %d.val %m.unit", "SPACING", "spacingSide", ""));
						return;
						case 4:
						// TEXT EDIT
						addBlockToPalette(new CreateBlock(" ", "color: %m.color", "TEXT", "color", ""));
						addBlockToPalette(new CreateBlock(" ", "font-size: %d.val %m.unit", "TEXT", "font-size", ""));
						addBlockToPalette(new CreateBlock(" ", "font-weight: %m.fontWeight", "TEXT", "font-weight", ""));
						addBlockToPalette(new CreateBlock(" ", "font-family: %s.family", "TEXT", "font-family", ""));
						addBlockToPalette(new CreateBlock(" ", "font-style: %m.fontStyle", "TEXT", "font-style", ""));
						addBlockToPalette(new CreateBlock(" ", "text-align: %m.textAlign", "TEXT", "text-align", ""));
						addBlockToPalette(new CreateBlock(" ", "text-transform: %m.textTransform", "TEXT", "text-transform", ""));
						addBlockToPalette(new CreateBlock(" ", "text-decoration: %m.textDecoration", "TEXT", "text-decoration", ""));
						addBlockToPalette(new CreateBlock(" ", "line-height: %d.val %m.unit", "TEXT", "line-height", ""));
						addBlockToPalette(new CreateBlock(" ", "letter-spacing: %d.val %m.unit", "TEXT", "letter-spacing", ""));
						addBlockToPalette(new CreateBlock(" ", "white-space: %m.whiteSpace", "TEXT", "white-space", ""));
						addBlockToPalette(new CreateBlock(" ", "text-overflow: %m.textOverflow", "TEXT", "text-overflow", ""));
						addBlockToPalette(new CreateBlock(" ", "word-break: %m.wordBreak", "TEXT", "word-break", ""));
						addBlockToPalette(new CreateBlock(" ", "vertical-align: %m.verticalAlign", "TEXT", "vertical-align", ""));
						addBlockToPalette(new CreateBlock(" ", "writing-mode: %m.writingMode", "TEXT", "writing-mode", ""));
						addBlockToPalette(new CreateBlock(" ", "hyphens: %m.hyphens", "TEXT", "hyphens", ""));
						addBlockToPalette(new CreateBlock(" ", "text-shadow: %s.val", "TEXT", "text-shadow", ""));
						return;
						case 5:
						// BORDER
						addBlockToPalette(new CreateBlock(" ", "border-width: %d.val %m.unit", "BORDER", "border-width", ""));
						addBlockToPalette(new CreateBlock(" ", "border-style: %m.borderStyle", "BORDER", "border-style", ""));
						addBlockToPalette(new CreateBlock(" ", "border-color: %m.color", "BORDER", "border-color", ""));
						addBlockToPalette(new CreateBlock(" ", "border-radius: %d.val %m.unit", "BORDER", "border-radius", ""));
						addBlockToPalette(new CreateBlock(" ", "border-%m.side-width: %d.val %m.unit", "BORDER", "border-side-width", ""));
						addBlockToPalette(new CreateBlock(" ", "border-%m.side-color: %m.color", "BORDER", "border-side-color", ""));
						addBlockToPalette(new CreateBlock(" ", "border-%m.side-style: %m.borderStyle", "BORDER", "border-side-style", ""));
						addBlockToPalette(new CreateBlock(" ", "outline: %d.val %m.unit %m.borderStyle %m.color", "BORDER", "outline", ""));
						return;
						case 6:
						// BACKGROUND
						addBlockToPalette(new CreateBlock(" ", "background-color: %m.color", "BACKGROUND", "background-color", ""));
						addBlockToPalette(new CreateBlock(" ", "background-image: url(%s.url)", "BACKGROUND", "background-image", ""));
						addBlockToPalette(new CreateBlock(" ", "background-size: %m.bgSize", "BACKGROUND", "background-size", ""));
						addBlockToPalette(new CreateBlock(" ", "background-repeat: %m.bgRepeat", "BACKGROUND", "background-repeat", ""));
						addBlockToPalette(new CreateBlock(" ", "background-attachment: %m.bgAttachment", "BACKGROUND", "background-attachment", ""));
						addBlockToPalette(new CreateBlock(" ", "background-origin: %m.bgOrigin", "BACKGROUND", "background-origin", ""));
						addBlockToPalette(new CreateBlock(" ", "background-clip: %m.bgClip", "BACKGROUND", "background-clip", ""));
						addBlockToPalette(new CreateBlock(" ", "background-blend-mode: %m.blendMode", "BACKGROUND", "background-blend-mode", ""));
						addBlockToPalette(new CreateBlock(" ", "background-position: %s.pos", "BACKGROUND", "background-position", ""));
						return;
					case 7:
						// FLEX
						addBlockToPalette(new CreateBlock(" ", "flex-direction: %m.direction", "LAYOUT", "flex-direction", ""));
						addBlockToPalette(new CreateBlock(" ", "justify-content: %m.justify", "LAYOUT", "justify-content", ""));
						addBlockToPalette(new CreateBlock(" ", "align-items: %m.align", "LAYOUT", "align-items", ""));
						addBlockToPalette(new CreateBlock(" ", "flex-wrap: %m.wrap", "LAYOUT", "flex-wrap", ""));
						return;
					case 8:
						return;
						default:
						return;
				}
		}
		
		public void onClick(View view) {
				if (view.getTag() != null) {
						if (view.getTag().equals("variableAdd")) {
								showAddVarPopup();
						} else if (view.getTag().equals("variableRemove")) {
								showRemoveVarPopup();
						} else if (view.getTag().equals("listAdd")) {
								showAddListPopup();
						} else if (view.getTag().equals("listRemove")) {
								showRemoveListPopup();
						} else if (view.getTag().equals("blockAdd")) {
								showAddBlockPopup();
						}
				}

		}
		
		public void onConfigurationChanged(Configuration configuration) {
				super.onConfigurationChanged(configuration);
				allocatePalette(configuration.orientation);
				updateIconDeletePosition();
		}
		
		public void onCreate(Bundle bundle) {
				super.onCreate(bundle);
				setContentView(R.layout.the_logic_editor);
				
				this.context = this.getApplicationContext();
				
				this.id = getIntent().getStringExtra("id");
				this.eventName = getIntent().getStringExtra("event");
				filename = getIntent().getStringExtra("filename");
				if (DesignDataManager.isInitialized) {
						this.prefInstall = new SharedPreferenceUtil(this.context, "P1");
						/* if (this.prefInstall.getBoolean("P1I5" + DesignActivity.getScId(), true) && !ScDefine.isCustomEditMode(DesignActivity.getScId())) {
                                startLogicTutorialActivity();
                            }*/
						this.toolbar = (Toolbar) findViewById(R.id.toolbar);
						this.toolbar.setBackgroundColor(0xFF008DCD);
						this.toolbar.setTitleTextColor(0xFFFFFFFF);
						setSupportActionBar(this.toolbar);
						if (getSupportActionBar() != null) {
								getSupportActionBar().setDisplayHomeAsUpEnabled(true);
								getSupportActionBar().setHomeButtonEnabled(true);
								this.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
								this.toolbar.getNavigationIcon().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.SRC_ATOP);
						}
						findViewById(R.id.layout_main_logo).setVisibility(View.GONE);
						this.toolbar.setNavigationOnClickListener(new OnClickListener() {
								public void onClick(View v) {
										onBackPressed();
								}
						});
						
						BLOCK_DRAG_Y = (int)LayoutUtil.getDip(this, BLOCK_DRAG_Y);
						
						//    this.toolbar.setPopupTheme(R.style.ThemeOverlay.ToolbarMenu);
						this.useVibrate = new SharedPreferenceUtil(this.context, "P12").getBoolean("P12I0", true);
						this.minDist = ViewConfiguration.get(this.context).getScaledTouchSlop();
						this.vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
						String stringExtra = getIntent().getStringExtra("event_text");
						if (this.id.equals("onCreate")) {
								getSupportActionBar().setTitle(stringExtra);
						} else {
								getSupportActionBar().setTitle(this.id + " : " + stringExtra);
						}
						this.paletteSelector = (PaletteSelector) findViewById(R.id.palette_selector);
						this.paletteSelector.setOnBlockCategorySelectListener(this);
                    this.scId = getIntent().getStringExtra("sc_id");
                        this.paletteBlock = (PaletteBlock) findViewById(R.id.palette_block);
						this.dummy = (ViewDummy) findViewById(R.id.dummy);
						this.iconDelete = (ImageView) findViewById(R.id.icon_delete);
						// this.topMenu = (LogicTopMenu) findViewById(R.id.top_menu);
						// this.topMenu.toggleLayoutVisibility(false);
						this.editor = (ViewLogicEditor) findViewById(R.id.editor);
						this.pane = this.editor.getBlockPane();
						onBlockCategorySelect(0, -1147626);
						this.blockCopyInterface = (RelativeLayout) findViewById(R.id.block_copy_interface);
					this.layoutPalette = (LinearLayout) findViewById(R.id.layout_palette);
						this.areaPalette = (LinearLayout) findViewById(R.id.area_palette);
						this.fab = (FloatingActionButton) findViewById(R.id.fab_toggle_palette);
						this.fab.setOnClickListener(new OnClickListener() {
								public void onClick(View v) {
										openPalette(!isPaletteOpened);
								}
						});
						return;
				}
				if (bundle != null) {
						backupCurrentData(bundle);
				}
				finish();
		}
		
		public boolean onCreateOptionsMenu(Menu menu) {
				getMenuInflater().inflate(R.menu.logic_menu, menu);
				this.menu = menu;
				return true;
		}
		
		public void onDestroy() {
				super.onDestroy();
		}
		
		public boolean onOptionsItemSelected(MenuItem menuItem) {
				int itemId = menuItem.getItemId();
				if (itemId == R.id.menu_logic_showsource) {
						showSourceCode();
						return true;
				}
				return super.onOptionsItemSelected(menuItem);
		}

		private void showSourceCode() {
				CssSourceMaker csm = new CssSourceMaker(this);
				String result = csm.getSource(0, pane.getAllBlocks());
				
				Intent intent = new Intent(this, CodeViewerActivity.class);
				intent.putExtra("code", result);
				intent.putExtra("scheme", CodeViewerActivity.SCHEME_CSS);
				startActivity(intent);
		}
		
		protected void onPostCreate(@Nullable Bundle var1) {
				super.onPostCreate(var1);
				String var2;
				if(this.eventName.equals("initializeLogic")) {
						var2 = "When initializeLogic";
				} else if(this.eventName.equals("moreBlock")) {
						String var20 = DesignDataManager.getFunctionSpec(filename, this.id);
						var2 = "Define" + " " + var20;
				} else if(this.eventName.equals("onClick")) {
						var2 = "When" + " " + this.id + " " + "clicked";
				} else if(this.eventName.equals("onCheckedChange")) {
						var2 = "When" + " " + this.id + " " + "checkChanged %b.isChecked";
				} else if(this.eventName.equals("onItemSelected")) {
						var2 = "When" + " " + this.id + " " + "itemSelected %d.position";
				} else if(this.eventName.equals("onItemClicked")) {
						var2 = "When" + " " + this.id + " " + "itemClicked %d.position";
				} else if(this.eventName.equals("onTextChanged")) {
						var2 = "When" + " " + this.id + " " + " " + "textChanged %s.text";
				} else {
						var2 = "When" + " " + this.id + " " + this.eventName;
				}
				
				this.pane.addRoot(var2, this.eventName);
				ArrayList var3 = StringUtil.tokenize(var2);
				int var4 = 0;
				
				for(int var5 = 0; var5 < var3.size(); ++var5) {
						String var6 = (String)var3.get(var5);
						if(var6.charAt(0) == 37) {
								Block var11;
								if(var6.charAt(1) == 98) {
										Context var16 = this;
										int var17 = var4 + 1;
										String var18 = var6.substring(3);
										Object[] var19 = new Object[]{Integer.valueOf(-7711273)};
										var11 = new Block(var16, var17, var18, "b", "getArg", var19);
								} else if(var6.charAt(1) == 100) {
										Context var12 = this;
										int var13 = var4 + 1;
										String var14 = var6.substring(3);
										Object[] var15 = new Object[]{Integer.valueOf(-7711273)};
										var11 = new Block(var12, var13, var14, "d", "getArg", var15);
								} else {
										if(var6.charAt(1) != 115) {
												continue;
										}
										
										Context var7 = this;
										int var8 = var4 + 1;
										String var9 = var6.substring(3);
										Object[] var10 = new Object[]{Integer.valueOf(-7711273)};
										var11 = new Block(var7, var8, var9, "s", "getArg", var10);
								}
								
								var11.setBlockType(1);
								this.pane.addView(var11);
								this.pane.getRoot().replaceArgWithBlock((BlockBase)this.pane.getRoot().args.get(var4), var11);
								var11.setOnTouchListener(this);
								++var4;
						}
				}
				
				this.pane.getRoot().fixLayout();
				this.loadLogic();
				this.allocatePalette(this.getResources().getConfiguration().orientation);
		}
		
		public void onResume() {
				super.onResume();
				//     this.mTracker.setScreenName(getClass().getSimpleName().toString());
				//      this.mTracker.send(new ScreenViewBuilder().build());
		}
		
		protected void onSaveInstanceState(Bundle bundle) {
				super.onSaveInstanceState(bundle);
		}
		
		public boolean onTouch(View view, MotionEvent motionEvent) {
				int action = motionEvent.getAction();
				if (action == 0) {
						this.isDragged = false;
						this.handler.postDelayed(this.longPressed, (long) (ViewConfiguration.getLongPressTimeout() / 2));
						this.posInitX = motionEvent.getX();
						this.posInitY = motionEvent.getY();
						this.currentTouchedView = view;
						return true;
				} else if (action == 2) {
						if (this.isDragged) {
								this.handler.removeCallbacks(this.longPressed);
								this.dummy.moveDummy(view, motionEvent.getX(), motionEvent.getY(), this.posInitX, this.posInitY, (float)BLOCK_DRAG_X, (float)BLOCK_DRAG_Y);
								if (hitTestIconDelete(motionEvent.getRawX(), motionEvent.getRawY())) {
										this.dummy.setAllow(true);
										activeIconDelete(true);
										return true;
								}
								activeIconDelete(false);
								this.dummy.getDummyPosition(this.posDummy);
								if (this.editor.hitTest((float) this.posDummy[0], (float) this.posDummy[1])) {
										this.dummy.setAllow(true);
										this.pane.updateFeedbackFor((Block) view, this.posDummy[0], this.posDummy[1]);
								} else {
										this.dummy.setAllow(false);
										this.pane.hideFeedbackShape();
								}
								return true;
						} else if (Math.abs(this.posInitX - motionEvent.getX()) < ((float) this.minDist) && Math.abs(this.posInitY - motionEvent.getY()) < ((float) this.minDist)) {
								return false;
						} else {
								this.currentTouchedView = null;
								this.handler.removeCallbacks(this.longPressed);
								return false;
						}
				} else if (action == 1) {
						this.currentTouchedView = null;
						this.handler.removeCallbacks(this.longPressed);
						if (this.isDragged) {
								this.paletteBlock.setDragEnabled(true);
								this.editor.setScrollEnabled(true);
								this.dummy.setDummyVisibility(8);
								if (this.dummy.getAllow()) {
										if (this.bActiveIconDelete) {
												activeIconDelete(false);
												this.pane.removeBlock((Block) view);
										} else if (view instanceof Block) {
												this.dummy.getDummyPosition(this.posDummy);
												if (((Block) view).getBlockType() == 1) {
														this.pane.blockDropped((Block) view, this.posDummy[0], this.posDummy[1], false).setOnTouchListener(this);
												} else {
														this.pane.setVisibleBlock((Block) view, 0);
														this.pane.blockDropped((Block) view, this.posDummy[0], this.posDummy[1], true);
												}
												this.pane.draggingDone();
										}
								} else if (((Block) view).getBlockType() == 0) {
										this.pane.setVisibleBlock((Block) view, 0);
										if (this.originalParent != null) {
												if (this.originalInsertOption == 0) {
														this.originalParent.nextBlock = ((Integer) view.getTag()).intValue();
												}
												if (this.originalInsertOption == 2) {
														this.originalParent.subStack1 = ((Integer) view.getTag()).intValue();
												}
												if (this.originalInsertOption == 3) {
														this.originalParent.subStack2 = ((Integer) view.getTag()).intValue();
												}
												if (this.originalInsertOption == 5) {
														this.originalParent.replaceArgWithBlock((BlockBase) this.originalParent.args.get(this.originalArgIndex), (Block) view);
												}
												((Block) view).parentBlock = this.originalParent;
												this.originalParent.topBlock().fixLayout();
										} else {
												((Block) view).topBlock().fixLayout();
										}
								}
								this.dummy.setAllow(false);
								showIconDelete(false);
								this.isDragged = false;
								return true;
						}
						if ((view instanceof Block) && ((Block) view).getBlockType() == 0) {
								((Block) view).actionClick(motionEvent.getX(), motionEvent.getY());
						}
						return false;
				} else if (action == 3) {
						this.handler.removeCallbacks(this.longPressed);
						this.isDragged = false;
						return false;
				} else if (action != 8) {
						return true;
				} else {
						this.handler.removeCallbacks(this.longPressed);
						this.isDragged = false;
						return false;
				}
		}
		
}
