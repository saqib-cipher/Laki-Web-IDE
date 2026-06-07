package com.besome.sketch.design;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.MaterialShapeUtils;
import com.google.android.material.shape.ShapeAppearanceModel;

import mod.hey.studios.util.Helper;
import laki.webide.R;
import laki.webide.databinding.DesignDrawerItemBinding;
import laki.webide.utility.SketchwareUtil;
import laki.webide.utility.ThemeUtils;
import laki.webide.utility.UI;

public class DesignDrawer extends LinearLayout {
    @SuppressLint("NonConstantResourceId")
    private final View.OnClickListener drawerItemClickListener = v -> {
        Activity activity = (Activity) getContext();
        if (!(activity instanceof DesignActivity designActivity)) return;
        int id = v.getId();

        if (id == R.id.item_library_manager) {
            designActivity.toLibraryManager();
        } else if (id == R.id.item_view_manager) {
            designActivity.toViewManager();
        } else if (id == R.id.item_image_manager) {
            designActivity.toImageManager();
        } else if (id == R.id.item_sound_manager) {
            designActivity.toSoundManager();
        } else if (id == R.id.item_font_manager) {
            designActivity.toFontManager();
        } else if (id == R.id.item_java_manager) {
            designActivity.toJavaManager();
        } else if (id == R.id.item_resource_manager) {
            designActivity.toResourceManager();
        } else if (id == R.id.item_resource_editor) {
            designActivity.toResourceEditor();
        } else if (id == R.id.item_assets_manager) {
            designActivity.toAssetManager();
        } else if (id == R.id.item_permission_manager) {
            SketchwareUtil.toast("Disabled in Web IDE");
        } else if (id == R.id.item_appcompat_manager) {
            SketchwareUtil.toast("Disabled in Web IDE");
        } else if (id == R.id.item_manifest_manager) {
            SketchwareUtil.toast("Disabled in Web IDE");
        } else if (id == R.id.item_used_custom_blocks) {
            designActivity.toCustomBlocksViewer();
        } else if (id == R.id.item_code_shrinking_manager) {
            SketchwareUtil.toast("Disabled in Web IDE");
        } else if (id == R.id.item_stringfog_manager) {
            SketchwareUtil.toast("Disabled in Web IDE");
        } else if (id == R.id.item_show_src) {
            designActivity.toSourceCodeViewer();
        } else if (id == R.id.item_xml_command_manager) {
            designActivity.toXMLCommandManager();
        } else if (id == R.id.item_logcat_reader) {
            designActivity.toLogReader();
        } else if (id == R.id.item_collection_manager) {
            designActivity.toCollectionManager();
        }
    };

    public DesignDrawer(Context context) {
        this(context, null);
    }

    @SuppressLint("NonConstantResourceId")
    public DesignDrawer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        setFocusable(true);
        setClickable(true);

        ShapeAppearanceModel shape = ShapeAppearanceModel.builder()
                .setTopLeftCornerSize(SketchwareUtil.getDip(24))
                .setBottomLeftCornerSize(SketchwareUtil.getDip(24))
                .build();

        MaterialShapeDrawable background = new MaterialShapeDrawable(shape);
        background.setFillColor(ColorStateList.valueOf(ThemeUtils.getColor(context, R.attr.colorSurfaceContainerLow)));
        background.initializeElevationOverlay(context);
        setBackground(background);
        setElevation(3f);
        setPadding(0, 0, 0, SketchwareUtil.dpToPx(4));

        ScrollView scrollView = new ScrollView(context);
        scrollView.setFillViewport(true);
        scrollView.setClipToPadding(false);
        addView(scrollView, new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1));

        LinearLayout content = new LinearLayout(getContext());
        content.setOrientation(VERTICAL);
        scrollView.addView(content);

        UI.addSystemWindowInsetToPadding(scrollView, false, true, false, false);
        UI.addSystemWindowInsetToPadding(this, false, false, true, true);

        addDrawerSubheaderItem(R.string.design_drawer_menu_title, content);
        addDrawerItem(R.id.item_library_manager, R.drawable.ic_mtrl_category, R.string.design_drawer_menu_title_library, R.string.design_drawer_menu_description_library, content);
        addDrawerItem(R.id.item_view_manager, R.drawable.ic_mtrl_devices, R.string.design_drawer_menu_title_view, R.string.design_drawer_menu_description_view, content);
        addDrawerItem(R.id.item_image_manager, R.drawable.ic_mtrl_image, R.string.design_drawer_menu_title_image, R.string.design_drawer_menu_description_image, content);
        addDrawerItem(R.id.item_sound_manager, R.drawable.ic_mtrl_music, R.string.design_drawer_menu_title_sound, R.string.design_drawer_menu_description_sound, content);
        addDrawerItem(R.id.item_font_manager, R.drawable.ic_mtrl_font, R.string.design_drawer_menu_title_font, R.string.design_drawer_menu_description_font, content);
        addDrawerItem(R.id.item_java_manager, R.drawable.ic_mtrl_java, "Css Manager", "Import Css files", content);
        addDrawerItem(R.id.item_resource_manager, R.drawable.ic_mtrl_folder, "Html Manager", "Import HTML file", content);
        addDrawerItem(R.id.item_resource_editor, R.drawable.ic_mtrl_folder_code, R.string.text_title_menu_resource_editor, R.string.text_subtitle_menu_resource_editor, content);
        addDrawerItem(R.id.item_assets_manager, R.drawable.ic_mtrl_file_present, R.string.text_title_menu_assets, R.string.text_subtitle_menu_assets, content);
        addDrawerItem(R.id.item_used_custom_blocks, R.drawable.ic_mtrl_block, R.string.design_drawer_menu_customblocks, R.string.design_drawer_menu_customblocks_subtitle, content);
        addDrawerItem(R.id.item_show_src, R.drawable.ic_mtrl_frame_source, R.string.design_drawer_menu_title_source_code, R.string.design_drawer_menu_description_source_code, content);
        addDrawerItem(R.id.item_xml_command_manager, R.drawable.ic_mtrl_code, R.string.design_drawer_menu_title_xml_command, R.string.design_drawer_menu_description_xml_command, content);

        addDrawerDivider(this);
        addDrawerItem(R.id.item_collection_manager, R.drawable.ic_mtrl_bookmark, R.string.design_drawer_menu_title_collection, R.string.design_drawer_menu_description_collection, this);
    }

    private void addDrawerItem(int id, int iconResId, int titleResId, int descriptionResId, ViewGroup view) {
        DrawerItem drawerItem = new DrawerItem(getContext());
        drawerItem.setContent(iconResId, Helper.getResString(drawerItem, titleResId), Helper.getResString(drawerItem, descriptionResId));
        drawerItem.setOnClickListener(id, drawerItemClickListener);
        view.addView(drawerItem);
    }
    private void addDrawerItem(int id, int iconResId, String titleResId, String descriptionResId, ViewGroup view) {
        DrawerItem drawerItem = new DrawerItem(getContext());
        drawerItem.setContent(iconResId, titleResId, descriptionResId);
        drawerItem.setOnClickListener(id, drawerItemClickListener);
        view.addView(drawerItem);
    }

    private void addDrawerSubheaderItem(@StringRes int subheaderResId, ViewGroup view) {
        TextView subheader = new TextView(getContext());
        subheader.setEllipsize(TextUtils.TruncateAt.END);
        subheader.setGravity(Gravity.CENTER_VERTICAL);
        subheader.setText(subheaderResId);

        LayoutParams textLp = new LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textLp.topMargin = SketchwareUtil.dpToPx(8);
        textLp.bottomMargin = SketchwareUtil.dpToPx(8);
        textLp.setMarginStart(SketchwareUtil.dpToPx(20));

        subheader.setLayoutParams(textLp);
        view.addView(subheader);
    }

    private void addDrawerDivider(ViewGroup view) {
        MaterialDivider divider = new MaterialDivider(getContext());
        divider.setDividerInsetEnd(SketchwareUtil.dpToPx(20));
        divider.setDividerInsetStart(SketchwareUtil.dpToPx(20));
        LayoutParams dividerLp = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dividerLp.setMargins(0, SketchwareUtil.dpToPx(8), 0, SketchwareUtil.dpToPx(8));
        divider.setLayoutParams(dividerLp);
        view.addView(divider);
    }

    private static class DrawerItem extends LinearLayout {
        private final DesignDrawerItemBinding binding;
        public DrawerItem(Context context) { this(context, null); }
        public DrawerItem(Context context, AttributeSet attrs) {
            super(context, attrs);
            binding = DesignDrawerItemBinding.inflate(LayoutInflater.from(context), this, true);
        }
        public void setContent(int iconResId, String rootTitleText, String subTitleText) {
            binding.imgIcon.setImageResource(iconResId);
            binding.tvRootTitle.setText(rootTitleText);
            binding.tvSubTitle.setText(subTitleText);
        }
        public void setOnClickListener(int id, OnClickListener listener) {
            binding.getRoot().setId(id);
            binding.getRoot().setOnClickListener(listener);
        }
    }
}
