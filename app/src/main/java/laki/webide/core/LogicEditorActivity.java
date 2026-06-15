package laki.webide.core;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import laki.webide.core.BlockBean;
import com.besome.sketch.lib.base.BaseAppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import laki.webide.R;
import laki.webide.activities.editor.view.CodeViewerActivity;
import laki.webide.managers.CssLogicPersistenceManager;

public class LogicEditorActivity extends BaseAppCompatActivity implements OnClickListener, OnBlockCategorySelectListener, OnTouchListener {
    public static final String LOGIC_NAME_SEPARATOR = "_";
    public static String filename = "";

    private Context context;
    private ObjectAnimator aniShowTopMenu;
    private ObjectAnimator aniHideTopMenu;
    private ObjectAnimator aniShowPalette;
    private ObjectAnimator aniHidePalette;
    private LinearLayout areaPalette;
    private boolean bActiveIconDelete = false;
    private boolean bInitIconDeleteAnimation = false;
    private boolean bInitPaletteAnimation = false;
    private boolean bShowIconDelete = false;
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
    private Menu menu;
    private int minDist = 0;
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
    private Toolbar toolbar;
    private boolean useVibrate;
    private Vibrator vibrator;

    public int BLOCK_DRAG_X = 0;
    public int BLOCK_DRAG_Y = -30;

    private void activeIconDelete(boolean z) {
        if (iconDelete != null) {
            iconDelete.setAlpha(z ? 1.0f : 0.5f);
            this.bActiveIconDelete = z;
        }
    }

    private void addBlockToPalette(CreateBlock cb) {
        BlockBase addBlock = this.paletteBlock.addBlock(cb);
        addBlock.setClickable(true);
        addBlock.setOnTouchListener(this);
    }

    private void addButtonToPalette(String str, String str2) {
        View addButton = this.paletteBlock.addButton(str);
        addButton.setTag(str2);
        addButton.setSoundEffectsEnabled(true);
        addButton.setOnClickListener(this);
    }

    private void allocatePalette(int var1) {
        if (2 == var1) {
            LayoutParams var2 = new LayoutParams((int) LayoutUtil.getDip(this, 320.0F), -1);
            this.areaPalette.setLayoutParams(var2);
            LayoutParams var3 = new LayoutParams(-2, -2);
            var3.gravity = 81;
            int var4 = (int) this.getResources().getDimension(R.dimen.action_button_margin);
            var3.setMargins(var4, var4, var4, var4);
            this.fab.setLayoutParams(var3);
            RelativeLayout.LayoutParams var5 = new RelativeLayout.LayoutParams(-2, -1);
            var5.addRule(10);
            var5.addRule(11);
            var5.topMargin = this.getSupportActionBar().getHeight();
            this.layoutPalette.setOrientation(LinearLayout.HORIZONTAL);
            this.layoutPalette.setLayoutParams(var5);
        } else {
            LayoutParams var6 = new LayoutParams(-1, (int) LayoutUtil.getDip(this, 240.0F));
            this.areaPalette.setLayoutParams(var6);
            LayoutParams var7 = new LayoutParams(-2, -2);
            var7.gravity = 21;
            int var8 = (int) this.getResources().getDimension(R.dimen.action_button_margin);
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

    private void cancelIconDeleteAnimation() {
        if (this.aniShowTopMenu != null && this.aniShowTopMenu.isRunning()) {
            this.aniShowTopMenu.cancel();
        }
        if (this.aniHideTopMenu != null && this.aniHideTopMenu.isRunning()) {
            this.aniHideTopMenu.cancel();
        }
    }

    private void cancelPaletteAnimation() {
        if (this.aniShowPalette != null && this.aniShowPalette.isRunning()) {
            this.aniShowPalette.cancel();
        }
        if (this.aniHidePalette != null && this.aniHidePalette.isRunning()) {
            this.aniHidePalette.cancel();
        }
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
            this.dummy.moveDummy(this.currentTouchedView, this.posInitX, this.posInitY, this.posInitX, this.posInitY, (float) BLOCK_DRAG_X, (float) BLOCK_DRAG_Y);
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
            public void onAnimationCancel(Animator var1) {}
            public void onAnimationEnd(Animator var1) { updateIconDeletePosition(); }
            public void onAnimationRepeat(Animator var1) {}
            public void onAnimationStart(Animator var1) {}
        });
        this.aniHidePalette.addListener(new Animator.AnimatorListener() {
            public void onAnimationCancel(Animator var1) {}
            public void onAnimationEnd(Animator var1) {}
            public void onAnimationRepeat(Animator var1) {}
            public void onAnimationStart(Animator var1) { updateIconDeletePosition(); }
        });
        this.aniShowPalette.setDuration(500);
        this.aniShowPalette.setInterpolator(new DecelerateInterpolator());
        this.aniHidePalette.setDuration(300);
        this.aniHidePalette.setInterpolator(new DecelerateInterpolator());
        this.bInitPaletteAnimation = true;
    }

    private void loadLogic() {
        Map hashMap = new HashMap();
        ArrayList<BlockBean> blocks;
        
        if (laki.webide.events.ExtCSS.isMatch(this.id)) {
            laki.webide.beans.CssLogicData data = CssLogicPersistenceManager.load(scId, filename);
            blocks = data.blocks;
            
            // Sync variables to DesignDataManager for palette visibility
            if (data.variables != null) {
                ArrayList<String> existing = DesignDataManager.getAllLists(filename);
                for (String v : data.variables) {
                    if (!existing.contains(v)) {
                        DesignDataManager.addList(filename, 2, v);
                    }
                }
            }
        } else {
            blocks = DesignDataManager.getBlocks(filename, this.id + LOGIC_NAME_SEPARATOR + this.eventName);
        }

        if (blocks != null) {
            Iterator it = blocks.iterator();
            int i = 1;
            while (it.hasNext()) {
                Block makeBlockFromBean = Block.fromBean(this, (BlockBean) it.next());
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
                        if (block2 != null) block.insertBlockSub1(block2);
                    }
                    if (blockBean.subStack2 >= 0) {
                        block2 = (Block) hashMap.get(Integer.valueOf(blockBean.subStack2));
                        if (block2 != null) block.insertBlockSub2(block2);
                    }
                    if (blockBean.nextBlock >= 0) {
                        block2 = (Block) hashMap.get(Integer.valueOf(blockBean.nextBlock));
                        if (block2 != null) block.insertBlock(block2);
                    }
                    int size = blockBean.parameters.size();
                    for (int i2 = 0; i2 < size; i2++) {
                        String str = (String) blockBean.parameters.get(i2);
                        if (str != null && str.length() > 0) {
                            if (str.charAt(0) == '@') {
                                block2 = (Block) hashMap.get(Integer.valueOf(Integer.valueOf(str.substring(1)).intValue()));
                                if (block2 != null) block.replaceArgWithBlock((BlockBase) block.args.get(i2), block2);
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

    public String getScEventId() { return this.id; }
    public String getEventName() { return this.eventName; }
    public BlockPane getBlockPane() { return this.pane; }

    private void openPalette(boolean z) {
        if (!this.bInitPaletteAnimation) initPaletteAnimation(getResources().getConfiguration().orientation);
        if (this.isPaletteOpened != z) {
            this.isPaletteOpened = z;
            cancelPaletteAnimation();
            if (z) this.aniShowPalette.start();
            else this.aniHidePalette.start();
        }
    }

    private void saveLogic() {
        ArrayList<BlockBean> blocks = this.pane.getBlocks();
        if (laki.webide.events.ExtCSS.isMatch(this.id)) {
            // For CSS logic, we also need variables. 
            // In the current custom block system, variables are managed by DesignDataManager.
            ArrayList<String> variables = DesignDataManager.getAllLists(filename); // Temporary mapping
            CssLogicPersistenceManager.save(scId, filename, blocks, variables);
        } else {
            DesignDataManager.setBlocks(filename, this.id + LOGIC_NAME_SEPARATOR + this.eventName, blocks);
        }
    }

    private void showIconDelete(boolean z) {
        if (!this.bInitIconDeleteAnimation) initIconDeleteAnimation();
        if (this.bShowIconDelete != z) {
            this.bShowIconDelete = z;
            cancelIconDeleteAnimation();
            if (z) this.aniShowTopMenu.start();
            else this.aniHideTopMenu.start();
        }
    }

    private void updateIconDeletePosition() {
        if (this.isPaletteOpened && 1 == getResources().getConfiguration().orientation) {
            ((RelativeLayout.LayoutParams) this.iconDelete.getLayoutParams()).bottomMargin = (int) LayoutUtil.getDip(this, 240.0f);
            this.iconDelete.requestLayout();
            return;
        }
        ((RelativeLayout.LayoutParams) this.iconDelete.getLayoutParams()).bottomMargin = 0;
        this.iconDelete.requestLayout();
    }

    public boolean checkValidForever() { return true; }
    public boolean checkValidZero() { return true; }

    public void onBackPressed() {
        if (this.isPaletteOpened) {
            openPalette(!this.isPaletteOpened);
            return;
        }
        if (checkValidForever() && checkValidZero()) {
            saveLogic();
            super.onBackPressed();
        }
    }

    public void onBlockCategorySelect(int i, int i2) {
        this.paletteBlock.removeAllBlocks();
        ArrayList<CreateBlock> blocks = CreateBlock.getBlocksForCategory(i);
        for (CreateBlock cb : blocks) {
            if (cb.isButton) addButtonToPalette(cb.spec, cb.opCode);
            else addBlockToPalette(cb);
        }
    }

    public void onClick(View view) {
        if (view.getTag() != null) {
            if (view.getTag().equals("variableAdd")) LogicDialogHandler.showAddVarPopup(this);
            else if (view.getTag().equals("variableRemove")) LogicDialogHandler.showRemoveVarPopup(this);
            else if (view.getTag().equals("listAdd")) LogicDialogHandler.showAddListPopup(this);
            else if (view.getTag().equals("listRemove")) LogicDialogHandler.showRemoveListPopup(this);
            else if (view.getTag().equals("blockAdd")) LogicDialogHandler.showAddBlockPopup(this);
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
        applyEdgeToEdge(findViewById(R.id.editor));
        this.context = this.getApplicationContext();
        this.id = getIntent().getStringExtra("id");
        this.eventName = getIntent().getStringExtra("event");
        filename = getIntent().getStringExtra("filename");

        if (DesignDataManager.isInitialized) {
            this.toolbar = findViewById(R.id.toolbar);
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
            this.toolbar.setNavigationOnClickListener(v -> onBackPressed());

            BLOCK_DRAG_Y = (int) LayoutUtil.getDip(this, BLOCK_DRAG_Y);
            this.useVibrate = new SharedPreferenceUtil(this.context, "P12").getBoolean("P12I0", true);
            this.minDist = ViewConfiguration.get(this.context).getScaledTouchSlop();
            this.vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            String stringExtra = getIntent().getStringExtra("event_text");
            if (this.id.equals("onCreate")) getSupportActionBar().setTitle(stringExtra);
            else getSupportActionBar().setTitle(this.id + " : " + stringExtra);

            this.paletteSelector = findViewById(R.id.palette_selector);
            this.paletteSelector.setOnBlockCategorySelectListener(this);
            this.scId = getIntent().getStringExtra("sc_id");
            this.paletteBlock = findViewById(R.id.palette_block);
            this.dummy = findViewById(R.id.dummy);
            this.iconDelete = findViewById(R.id.icon_delete);
            this.editor = findViewById(R.id.editor);
            this.pane = this.editor.getBlockPane();
            onBlockCategorySelect(0, -1147626);
            this.layoutPalette = findViewById(R.id.layout_palette);
            this.areaPalette = findViewById(R.id.area_palette);
            this.fab = findViewById(R.id.fab_toggle_palette);
            this.fab.setOnClickListener(v -> openPalette(!isPaletteOpened));
            return;
        }
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logic_menu, menu);
        this.menu = menu;
        return true;
    }

    public void onDestroy() { super.onDestroy(); }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.menu_logic_showsource) {
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
        String spec = CreateBlock.getEventSpec(this.eventName, this.id, filename);
        this.pane.setupRoot(spec, this.eventName, this);
        this.loadLogic();
        this.allocatePalette(this.getResources().getConfiguration().orientation);
    }

    public void onResume() { super.onResume(); }
    protected void onSaveInstanceState(Bundle bundle) { super.onSaveInstanceState(bundle); }

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
                this.dummy.moveDummy(view, motionEvent.getX(), motionEvent.getY(), this.posInitX, this.posInitY, (float) BLOCK_DRAG_X, (float) BLOCK_DRAG_Y);
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
                        if (this.originalInsertOption == 0) this.originalParent.nextBlock = ((Integer) view.getTag()).intValue();
                        if (this.originalInsertOption == 2) this.originalParent.subStack1 = ((Integer) view.getTag()).intValue();
                        if (this.originalInsertOption == 3) this.originalParent.subStack2 = ((Integer) view.getTag()).intValue();
                        if (this.originalInsertOption == 5) this.originalParent.replaceArgWithBlock((BlockBase) this.originalParent.args.get(this.originalArgIndex), (Block) view);
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
