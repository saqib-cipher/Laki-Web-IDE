package a.a.a;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.*;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.*;

import androidx.annotation.NonNull;

import com.besome.sketch.beans.ProjectFileBean;
import com.besome.sketch.beans.ViewBean;
import laki.webide.core.panel.ViewProperty;

import java.util.ArrayList;

import laki.webide.R;
import laki.webide.core.Block;
import laki.webide.core.BlockBase;
import laki.webide.core.BlockPane;
import laki.webide.core.ViewDummy;
import laki.webide.core.ViewLogicEditor;
import laki.webide.core.html.HtmlBlocks;
import laki.webide.core.html.HtmlSidebar;
import laki.webide.core.LayoutUtil;

public class ViewEditorFragment extends qA implements View.OnClickListener, View.OnTouchListener {

    private boolean bActiveIconDelete = false;
    private boolean bShowIconDelete = false;
    private View currentTouchedView = null;
    private ViewDummy dummy;
    public ViewLogicEditor viewEditor;
    private final Handler handler = new Handler();
    private ImageView iconDelete;
    private boolean isDragged = false;
    private Runnable longPressed = new Runnable() {
        @Override
        public void run() {
            dragStart();
        }
    };
    private int minDist = 0;
    private int originalArgIndex;
    private int originalInsertOption;
    private Block originalParent;
    private HtmlSidebar htmlSidebar;
    private BlockPane pane;
    private int[] posDummy = new int[2];
    private float posInitX = 0.0f;
    private float posInitY = 0.0f;
    private int[] posOriginal = new int[2];
    private boolean useVibrate;
    private Vibrator vibrator;

    public int BLOCK_DRAG_X = 0;
    public int BLOCK_DRAG_Y = -30;

    private String sc_id;
    private ProjectFileBean projectFileBean;
    private ViewProperty viewProperty;
    private Block lastSelectedBlock;
    
    private boolean isSidebarCollapsed = false;
    private int sidebarMaxWidth;
    private int sidebarMinWidth;
    private View sidebarContainer;

    public ViewEditorFragment() {
    }

    private void initialize(ViewGroup viewGroup) {
        setHasOptionsMenu(true);
        this.viewEditor = viewGroup.findViewById(R.id.editor);
        this.pane = this.viewEditor.getBlockPane();
        this.htmlSidebar = viewGroup.findViewById(R.id.html_sidebar);
        this.dummy = viewGroup.findViewById(R.id.dummy);
        this.iconDelete = viewGroup.findViewById(R.id.icon_delete);
        
        this.minDist = ViewConfiguration.get(requireContext()).getScaledTouchSlop();
        this.vibrator = (Vibrator) requireContext().getSystemService(Context.VIBRATOR_SERVICE);
        this.useVibrate = true; 
        this.BLOCK_DRAG_X = (int) LayoutUtil.getDip(requireContext(), 30.0f);
        this.BLOCK_DRAG_Y = (int) LayoutUtil.getDip(requireContext(), (float) this.BLOCK_DRAG_Y);
        
        this.viewProperty = requireActivity().findViewById(R.id.view_property);
        this.pane.setOnTouchListener(this);
        
        // Connect the bridge: Refresh spinner when ID changes in panel
        if (this.viewProperty != null) {
            this.viewProperty.setOnPropertyChangeListener(new ViewProperty.OnPropertyChangeListener() {
                @Override
                public void onIdChanged() {
                    syncSpinner();
                }
                @Override
                public void onIdSelected(String id) {
                    Block found = pane.findBlockByHtmlId(id);
                    if (found != null && found != lastSelectedBlock) {
                        clearSelection();
                        lastSelectedBlock = found;
                        found.setSelectionVisual(true);
                        viewProperty.setBlock(found);
                    }
                }

                @Override
                public boolean isIdUnique(String id) {
                    return !pane.getHtmlBlockIds().contains(id);
                }

                @Override
                public boolean isClassUnique(String className) {
                    if (pane == null) return true;
                    ArrayList<String> existingClasses = pane.getAllHtmlClasses();
                    return !existingClasses.contains(className);
                }

                @Override
                public ArrayList<String> getAllClasses() {
                    return (pane != null) ? pane.getAllHtmlClasses() : new ArrayList<>();
                }

                @Override
                public ArrayList<String> getAllIds() {
                    return (pane != null) ? pane.getHtmlBlockIds() : new ArrayList<>();
                }

                @Override
                public void onIdRenamed(String oldId, String newId) {
                    if (pane == null || oldId.equals(newId)) return;
                    ArrayList<Block> blocks = pane.getAllBlocks();
                    for (Block b : blocks) {
                        if ("web_css_sel_id".equals(b.mOpCode)) {
                            Object val = b.getArgValue(0);
                            if (val != null && oldId.equals(val.toString())) {
                                b.setArgValue(0, newId);
                                b.fixLayout();
                            }
                        }
                    }
                }

                @Override
                public void onClassRenamed(String oldClass, String newClass) {
                    if (pane == null || oldClass.equals(newClass)) return;
                    ArrayList<Block> blocks = pane.getAllBlocks();
                    for (Block b : blocks) {
                        if ("web_css_sel_class".equals(b.mOpCode)) {
                            Object val = b.getArgValue(0);
                            if (val != null && oldClass.equals(val.toString())) {
                                b.setArgValue(0, newClass);
                                b.fixLayout();
                            }
                        }
                    }
                }

                @Override
                public void onPropertyChanged() {
                    saveLayoutState();
                }
            });
        }
        // Load HTML blocks into the new sidebar directly
        this.htmlSidebar.setBlockTouchListener(this);
        this.htmlSidebar.populate(HtmlBlocks.getAllHtmlBlocks());
        setupSidebarToggle(viewGroup);
        setupPreviewToggle(viewGroup);
        // Setup Root block for HTML
        this.pane.setupRoot("When Page Load", "onPageLoad", this);
        
        // Fix: Force redraw and size calculation for the orange root block
        this.handler.postDelayed(() -> {
            if (this.pane.getRoot() != null) {
                this.pane.getRoot().fixLayout();
                this.pane.calculateWidthHeight();
            }
            syncSpinner();
        }, 100);
    }

    private void syncSpinner() {
        if (viewProperty != null && pane != null) {
            viewProperty.updateSpinner(pane.getHtmlBlockIds());
        }
    }

    private void dragStart() {
        if (this.currentTouchedView != null) {
            // Prevent dragging the Root block
            if (this.currentTouchedView == this.pane.getRoot()) {
                this.currentTouchedView = null;
                return;
            }

            this.viewEditor.setScrollEnabled(false);
            if (this.htmlSidebar != null) {
                this.htmlSidebar.setScrollEnabled(false);
            }
            if (this.useVibrate && this.vibrator != null) {
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
            if (this.viewEditor.hitTest((float) this.posDummy[0], (float) this.posDummy[1])) {
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

    private void showIconDelete(boolean z) {
        if (this.bShowIconDelete != z) {
            this.bShowIconDelete = z;
            if (z) {
                this.iconDelete.animate().translationY(0.0f).setDuration(300).setInterpolator(new DecelerateInterpolator()).start();
            } else {
                this.iconDelete.animate().translationY(LayoutUtil.getDip(requireContext(), 66.0f)).setDuration(200).setInterpolator(new DecelerateInterpolator()).start();
            }
        }
    }

    private void activeIconDelete(boolean z) {
        if (iconDelete != null) {
            iconDelete.setAlpha(z ? 1.0f : 0.5f);
            this.bActiveIconDelete = z;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view == this.pane) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (viewProperty != null) {
                    viewProperty.setBlock(null);
                    viewProperty.animate().translationY(LayoutUtil.getDip(requireContext(), 170.0f)).setDuration(300).start();
                    clearSelection();
                }
            }
            return true;
        }

        int action = motionEvent.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            this.isDragged = false;
            this.handler.postDelayed(this.longPressed, (long) (ViewConfiguration.getLongPressTimeout() / 2));
            this.posInitX = motionEvent.getX();
            this.posInitY = motionEvent.getY();
            this.currentTouchedView = view;
            return true;
        } else if (action == MotionEvent.ACTION_MOVE) {
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
                
                // Pure Copy from Activity: Use posDummy directly
                if (this.viewEditor.hitTest((float) this.posDummy[0], (float) this.posDummy[1])) {
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
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            View draggedView = this.currentTouchedView;
            this.currentTouchedView = null;
            this.handler.removeCallbacks(this.longPressed);
            if (this.isDragged && draggedView != null) {
                this.viewEditor.setScrollEnabled(true);
                if (this.htmlSidebar != null) {
                    this.htmlSidebar.setScrollEnabled(true);
                }
                this.dummy.setDummyVisibility(View.GONE);
                if (this.dummy.getAllow()) {
                    if (this.bActiveIconDelete) {
                        activeIconDelete(false);
                        // Only remove if it's actually in the pane (workspace block)
                        if (((Block) draggedView).getBlockType() == 0) {
                            if (draggedView == lastSelectedBlock) {
                                if (viewProperty != null) {
                                    viewProperty.setBlock(null);
                                    viewProperty.animate().translationY(LayoutUtil.getDip(requireContext(), 170.0f)).setDuration(300).start();
                                }
                                clearSelection();
                            }
                            this.pane.removeBlock((Block) draggedView);
                        }
                        this.pane.draggingDone();
                        syncSpinner();
                        saveLayoutState();
                    } else if (draggedView instanceof Block) {
                        this.dummy.getDummyPosition(this.posDummy);
                        
                        // Pure Copy from Activity: Use posDummy directly
                        if (((Block) draggedView).getBlockType() == 1) { // Sidebar block
                            this.pane.blockDropped((Block) draggedView, this.posDummy[0], this.posDummy[1], false).setOnTouchListener(this);
                        } else {
                            this.pane.setVisibleBlock((Block) draggedView, 0);
                            this.pane.blockDropped((Block) draggedView, this.posDummy[0], this.posDummy[1], true);
                        }
                        this.pane.draggingDone();
                        syncSpinner();
                        saveLayoutState();
                    }
                } else if (draggedView instanceof Block && ((Block) draggedView).getBlockType() == 0) {
                    this.pane.setVisibleBlock((Block) draggedView, 0);
                    if (this.originalParent != null) {
                        if (this.originalInsertOption == 0) this.originalParent.nextBlock = ((Integer) draggedView.getTag()).intValue();
                        if (this.originalInsertOption == 2) this.originalParent.subStack1 = ((Integer) draggedView.getTag()).intValue();
                        if (this.originalInsertOption == 3) this.originalParent.subStack2 = ((Integer) draggedView.getTag()).intValue();
                        if (this.originalInsertOption == 5) this.originalParent.replaceArgWithBlock((BlockBase) this.originalParent.args.get(this.originalArgIndex), (Block) draggedView);
                        ((Block) draggedView).parentBlock = this.originalParent;
                        this.originalParent.topBlock().fixLayout();
                    } else {
                        ((Block) draggedView).topBlock().fixLayout();
                    }
                    this.pane.draggingDone();
                    saveLayoutState();
                }
                this.dummy.setAllow(false);
                showIconDelete(false);
                this.isDragged = false;
                return true;
            }
            if ((view instanceof Block) && ((Block) view).getBlockType() == 0) {
                Block block = (Block) view;
                if (block.mOpCode != null && block.mOpCode.startsWith("html_")) {
                    if (viewProperty != null) {
                        syncSpinner();
                        clearSelection();
                        lastSelectedBlock = block;
                        block.setSelectionVisual(true);
                        viewProperty.setBlock(block);
                        viewProperty.animate().translationY(0.0f).setDuration(300).start();
                    }
                    // FIX: Allow click to reach block holes (selectors/inputs)
                    block.actionClick(motionEvent.getX(), motionEvent.getY());
                } else {
                    if (viewProperty != null) {
                        viewProperty.setBlock(null);
                        viewProperty.animate().translationY(LayoutUtil.getDip(requireContext(), 170.0f)).setDuration(300).start();
                        clearSelection();
                    }
                    block.actionClick(motionEvent.getX(), motionEvent.getY());
                }
            }
            return false;
        }
        return false;
    }

    private void clearSelection() {
        if (lastSelectedBlock != null) {
            lastSelectedBlock.setSelectionVisual(false);
            lastSelectedBlock = null;
        }
    }

    @Override
    public void onClick(View v) {
    }

    public void initialize(ProjectFileBean projectFileBean) {
        this.projectFileBean = projectFileBean;
        if (this.pane != null && projectFileBean != null) {
            this.pane.setCurrentFilename(projectFileBean.fileName);
            
            // Load project block state
            ArrayList<ViewBean> viewBeans = laki.webide.managers.WebProjectStateManager.loadProjectState(requireContext(), sc_id, projectFileBean);
            if (viewBeans.isEmpty()) {
                String projectName = yB.c(lC.b(sc_id), "my_ws_name");
                String projectRoot = laki.webide.core.LakiFiles.getProjectRoot(projectName, sc_id, false);
                String htmlPath = laki.webide.core.LakiFiles.getHtmlPath(projectRoot) + java.io.File.separator + projectFileBean.fileName;
                if (laki.webide.utility.FileUtil.isExistFile(htmlPath)) {
                    String htmlContent = laki.webide.utility.FileUtil.readFile(htmlPath);
                    if (htmlContent != null && !htmlContent.trim().isEmpty()) {
                        try {
                            viewBeans = laki.webide.compiler.HtmlParser.parseHtml(htmlContent, sc_id, requireContext());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            laki.webide.managers.WebProjectStateManager.loadProjectStateIntoPane(this.pane, viewBeans, this);
        }
        if (viewProperty != null) {
            viewProperty.a(sc_id, this.projectFileBean);
        }
        if (getActivity() != null) {
            getActivity().invalidateOptionsMenu();
        }
    }

    public void showHidePropertyView(boolean visible) {
        if (viewProperty != null) {
            if (visible && lastSelectedBlock != null) {
                viewProperty.animate().translationY(0.0f).setDuration(300).start();
            } else {
                viewProperty.animate().translationY(LayoutUtil.getDip(requireContext(), 170.0f)).setDuration(300).start();
                if (!visible) clearSelection();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, Bundle bundle) {
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.fr_graphic_editor, parent, false);
        if (bundle != null) {
            sc_id = bundle.getString("sc_id");
        } else {
            sc_id = requireActivity().getIntent().getStringExtra("sc_id");
        }
        initialize(viewGroup);
        return viewGroup;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.design_view_menu, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (lastSelectedBlock == null && viewProperty != null) {
            viewProperty.setTranslationY(LayoutUtil.getDip(requireContext(), 170.0f));
        }
        if (this.pane != null && projectFileBean != null) {
            ArrayList<ViewBean> viewBeans = laki.webide.managers.WebProjectStateManager.loadProjectState(requireContext(), sc_id, projectFileBean);
            if (viewBeans.isEmpty()) {
                String projectName = yB.c(lC.b(sc_id), "my_ws_name");
                String projectRoot = laki.webide.core.LakiFiles.getProjectRoot(projectName, sc_id, false);
                String htmlPath = laki.webide.core.LakiFiles.getHtmlPath(projectRoot) + java.io.File.separator + projectFileBean.fileName;
                if (laki.webide.utility.FileUtil.isExistFile(htmlPath)) {
                    String htmlContent = laki.webide.utility.FileUtil.readFile(htmlPath);
                    if (htmlContent != null && !htmlContent.trim().isEmpty()) {
                        try {
                            viewBeans = laki.webide.compiler.HtmlParser.parseHtml(htmlContent, sc_id, requireContext());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            laki.webide.managers.WebProjectStateManager.loadProjectStateIntoPane(this.pane, viewBeans, this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle newInstanceState) {
        newInstanceState.putString("sc_id", sc_id);
        super.onSaveInstanceState(newInstanceState);
    }

    private void setupSidebarToggle(ViewGroup root) {
        View btnToggle = root.findViewById(R.id.btn_sidebar_toggle);
        ImageView imgToggle = root.findViewById(R.id.img_sidebar_toggle);
        sidebarContainer = root.findViewById(R.id.layout_sidebar_container);
        
        sidebarMaxWidth = (int) LayoutUtil.getDip(requireContext(), 140.0f);
        sidebarMinWidth = (int) LayoutUtil.getDip(requireContext(), 30.0f);

        btnToggle.setOnClickListener(v -> {
            isSidebarCollapsed = !isSidebarCollapsed;
            ValueAnimator animator = isSidebarCollapsed ? 
                ValueAnimator.ofInt(sidebarMaxWidth, sidebarMinWidth) : 
                ValueAnimator.ofInt(sidebarMinWidth, sidebarMaxWidth);
            
            animator.addUpdateListener(animation -> {
                int val = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams lp = sidebarContainer.getLayoutParams();
                lp.width = val;
                sidebarContainer.setLayoutParams(lp);
            });
            
            animator.setDuration(300);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.start();

            // Handle content visibility
            htmlSidebar.animate().alpha(isSidebarCollapsed ? 0f : 1f).setDuration(200).start();

            // Rotate arrow icon
            imgToggle.animate().rotation(isSidebarCollapsed ? 180f : 0f).setDuration(300).start();
            imgToggle.setImageResource(isSidebarCollapsed ? R.drawable.ic_mtrl_chevron_right_24 : R.drawable.ic_mtrl_chevron_left_24);
        });
    }

    private void saveLayoutState() {
        if (projectFileBean == null || pane == null) return;
        ArrayList<ViewBean> viewBeans = laki.webide.managers.WebProjectStateManager.blockTreeToViewBeans(pane);
        laki.webide.managers.WebProjectStateManager.saveProjectState(requireContext(), sc_id, projectFileBean, viewBeans);
        if (requireActivity() instanceof com.besome.sketch.design.DesignActivity designActivity) {
            laki.webide.managers.WebProjectSyncManager.syncCurrentFile(designActivity.getProjectWorkspace(), sc_id, projectFileBean);
        }
    }

    private void setupPreviewToggle(ViewGroup root) {
        View btnEditor = root.findViewById(R.id.btn_editor);
        View btnPreview = root.findViewById(R.id.btn_preview);
        com.google.android.material.button.MaterialButtonToggleGroup toggleGroup = root.findViewById(R.id.editor_preview_toggle);
        
        if (toggleGroup != null) {
            toggleGroup.check(R.id.btn_editor);
            toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
                if (isChecked) {
                    if (checkedId == R.id.btn_preview) {
                        toggleGroup.check(R.id.btn_editor);
                        if (projectFileBean != null) {
                            Intent intent = new Intent(requireContext(), laki.webide.activities.preview.LiveActivity.class);
                            intent.putExtra("sc_id", sc_id);
                            intent.putExtra("xml", projectFileBean.getXmlName());
                            intent.putExtra("title", projectFileBean.fileName);
                            startActivity(intent);
                        } else {
                            Toast.makeText(requireContext(), "No file loaded yet", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
}
