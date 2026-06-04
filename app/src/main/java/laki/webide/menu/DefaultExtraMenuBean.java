package laki.webide.menu;

import android.net.Uri;
import android.util.Pair;

import com.besome.sketch.beans.ComponentBean;
import com.besome.sketch.editor.LogicEditorActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import a.a.a.Ss;
import a.a.a.eC;
import a.a.a.jC;
import a.a.a.uq;
import a.a.a.wq;
import mod.agus.jcoderz.editor.manage.block.makeblock.BlockMenu;
import mod.hey.studios.util.Helper;
import laki.webide.utility.CustomVariableUtil;
import laki.webide.utility.FileUtil;

public class DefaultExtraMenuBean {

    private final LogicEditorActivity logicEditor;
    private final eC projectDataManager;
    private final String sc_id;

    public DefaultExtraMenuBean(LogicEditorActivity logicEditor) {
        this.logicEditor = logicEditor;
        sc_id = logicEditor.scId;
        projectDataManager = jC.a(sc_id);
    }

    public static String getName(String menuName) {
        return switch (menuName) {
            case "image" -> "Custom Image";
            case "til_box_mode" -> "Box Mode";
            case "fabsize" -> "Fab Size";
            case "fabvisible" -> "Fab Visible";
            case "menuaction" -> "Menu Action";
            case "porterduff" -> "Porterduff Mode";
            case "transcriptmode" -> "Transcript Mode";
            case "listscrollparam", "recyclerscrollparam", "pagerscrollparam" -> "Scroll Param";
            case "gridstretchmode" -> "Stretch Mode";
            case "gravity_v" -> "Gravity Vertical";
            case "gravity_h" -> "Gravity Horizontal";
            case "gravity_t" -> "Gravity Toast";
            case "patternviewmode" -> "Pattern Mode";
            case "styleprogress" -> "Progress Style";
            case "cv_theme" -> "Theme";
            case "cv_language" -> "Language";
            case "import" -> "Import";
            default -> menuName;
        };
    }

    public Pair<String, ArrayList<String>> getMenu(Ss menu) {
        var javaName = logicEditor.M.getJavaName();
        var menuName = menu.getMenuName();
        ArrayList<String> menus = new ArrayList<>();
        String title;
        Pair<String, String[]> menuPair = BlockMenu.getMenu(menuName);
        title = menuPair.first;
        menus = new ArrayList<>(Arrays.asList(menuPair.second));
        for (String s : projectDataManager.e(javaName, 5)) {
            Matcher matcher2 = Pattern.compile("^(\\w+)[\\s]+(\\w+)").matcher(s);
            while (matcher2.find()) {
                if (menuName.equals(matcher2.group(1))) {
                    title = "Select a " + matcher2.group(1) + " Variable";
                    menus.add(matcher2.group(2));
                }
            }
        }
        for (String variable : projectDataManager.e(javaName, 6)) {
            String variableType = CustomVariableUtil.getVariableType(variable);
            String variableName = CustomVariableUtil.getVariableName(variable);
            if (menuName.equals(variableType)) {
                title = "Select a " + variableType + " Variable";
                menus.add(variableName);
            }
        }
        for (ComponentBean componentBean : projectDataManager.e(javaName)) {
            if (componentBean.type > 36
                    && menuName.equals(ComponentBean.getComponentTypeName(componentBean.type))) {
                title = "Select a " + ComponentBean.getComponentTypeName(componentBean.type);
                menus.add(componentBean.componentId);
            }
        }
        switch (menuName) {
            case "LayoutParam" -> {
                title = "Select layout params";
                menus.addAll(Helper.createStringList("MATCH_PARENT", "WRAP_CONTENT"));
            }
            case "Command" -> {
                title = "Select command";
                menus.addAll(
                        Helper.createStringList(
                                "insert",
                                "add",
                                "replace",
                                "find-replace",
                                "find-replace-first",
                                "find-replace-all"));
            }
            // This is meant to be a built-in menu including the cases below, but Aldi implemented it as a file, which is why, in some cases, certain menus appear empty.
            //start

            case "til_box_mode" -> {
                title = "Select box mode";
                menus.addAll(Arrays.asList(uq.TIL_BOX_MODE));
            }
            case "fabsize" -> {
                title = "Select fab size";
                menus.addAll(Arrays.asList(uq.FAB_SIZE));
            }
            case "fabvisible" -> {
                title = "Select fab visibility";
                menus.addAll(Arrays.asList(uq.FAB_VISIBLE));
            }
            case "menuaction" -> {
                title = "Select menu action";
                menus.addAll(Arrays.asList(uq.MENU_ACTION));
            }
            case "porterduff" -> {
                title = "Select porterduff mode";
                menus.addAll(Arrays.asList(uq.PORTER_DUFF));
            }
            case "transcriptmode" -> {
                title = "Select transcript mode";
                menus.addAll(Arrays.asList(uq.TRANSCRIPT_MODE));
            }
            // idk, but it seems this isn't used anywhere, yet it was included in the menu file.
            case "listscrollparam" -> {
                title = "Select scroll param";
                menus.addAll(Arrays.asList(uq.LIST_SCROLL_STATES));
            }
            // same with listscrollparam
            case "recyclerscrollparam", "pagerscrollparam" -> {
                title = "Select scroll param";
                menus.addAll(Arrays.asList(uq.RECYCLER_SCROLL_STATES));
            }
            case "gridstretchmode" -> {
                title = "Select stretch mode";
                menus.addAll(Arrays.asList(uq.GRID_STRETCH_MODE));
            }
            case "gravity_v" -> {
                title = "Select gravity vertical";
                menus.addAll(Arrays.asList(uq.GRAVITY_VERTICAL));
            }
            case "gravity_h" -> {
                title = "Select gravity horizontal";
                menus.addAll(Arrays.asList(uq.GRAVITY_HORIZONTAL));
            }
            case "gravity_t" -> {
                title = "Select gravity toast";
                menus.addAll(Arrays.asList(uq.GRAVITY_TOAST));
            }
            case "patternviewmode" -> {
                title = "Select patternview mode";
                menus.addAll(Arrays.asList(uq.PATTERNVIEW_MODE));
            }
            case "styleprogress" -> {
                title = "Select progress style";
                menus.addAll(Arrays.asList(uq.PROGRESS_STYLE));
            }
            case "cv_theme" -> {
                title = "Select theme";
                menus.addAll(Arrays.asList(uq.CODEVIEW_THEME));
            }
            case "cv_language" -> {
                title = "Select language";
                menus.addAll(Arrays.asList(uq.CODEVIEW_LANGUAGE));
            }
            case "import" -> {
                title = "Select language";
                menus.addAll(Arrays.asList(uq.IMPORT_CLASS_PATH));
            }
            case "display" -> {
                title = "Select display mode";
                menus.addAll(Arrays.asList("block", "inline", "inline-block", "flex", "grid", "none"));
            }
            case "position" -> {
                title = "Select position mode";
                menus.addAll(Arrays.asList("static", "relative", "absolute", "fixed", "sticky"));
            }
            case "overflow" -> {
                title = "Select overflow mode";
                menus.addAll(Arrays.asList("visible", "hidden", "scroll", "auto"));
            }
            case "visibility" -> {
                title = "Select visibility mode";
                menus.addAll(Arrays.asList("visible", "hidden", "collapse"));
            }
            case "flex-direction" -> {
                title = "Select flex direction";
                menus.addAll(Arrays.asList("row", "row-reverse", "column", "column-reverse"));
            }
            case "flexbox" -> {
                title = "Select flex mode";
                menus.addAll(Arrays.asList("flex", "inline-flex"));
            }
            case "grid" -> {
                title = "Select grid mode";
                menus.addAll(Arrays.asList("grid", "inline-grid"));
            }
            case "cssVar" -> {
                title = "Select CSS Variable";
                try {
                    // Force Plural 'settings' and ensure projectName is correct
                    java.util.HashMap<String, Object> projectDetails = a.a.a.lC.b(sc_id);
                    String projectName = a.a.a.yB.c(projectDetails, "my_ws_name");
                    String path = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() 
                        + "/.lakiwebsites/simple/" + projectName + "/settings/variable.json";
                    
                    if (FileUtil.isExistFile(path)) {
                        String content = FileUtil.readFile(path);
                        ArrayList<String> varList = new com.google.gson.Gson().fromJson(content, 
                            new com.google.gson.reflect.TypeToken<ArrayList<String>>(){}.getType());
                        if (varList != null) {
                            for (String v : varList) {
                                if (!menus.contains(v)) menus.add(v);
                            }
                        }
                    }
                    
                    // Always include memory-based variables to ensure display
                    for (String v : projectDataManager.e(javaName, 2)) {
                        if (!menus.contains(v)) menus.add(v);
                    }
                } catch (Exception e) {
                    menus.addAll(projectDataManager.e(javaName, 2));
                }
            }
            case "textAlign" -> {
                title = "Select text-align";
                menus.addAll(Arrays.asList("left", "center", "right", "justify"));
            }
            case "fontWeight" -> {
                title = "Select font-weight";
                menus.addAll(Arrays.asList("normal", "bold", "lighter", "bolder", "100", "200", "300", "400","500", "600", "700", "800","900"));
            }
            case "borderStyle" -> {
                title = "Select border style";
                menus.addAll(Arrays.asList("none", "hidden", "dotted", "dashed", "solid", "double", "groove", "ridge", "inset", "outset"));
            }
            case "borderSide" -> {
                title = "Select border side";
                menus.addAll(Arrays.asList("top", "bottom", "left", "right"));
            }
            case "corner" -> {
                title = "Select corner";
                menus.addAll(Arrays.asList("top-left", "top-right", "bottom-left", "bottom-right"));
            }
            case "bgSize" -> {
                title = "Select background size";
                menus.addAll(Arrays.asList("auto", "cover", "contain", "initial", "inherit"));
            }
            case "bgPos" -> {
                title = "Select background position";
                menus.addAll(Arrays.asList("center", "top", "bottom", "left", "right", "left top", "left bottom", "right top", "right bottom"));
            }
            case "bgRepeat" -> {
                title = "Select background repeat";
                menus.addAll(Arrays.asList("repeat", "no-repeat", "repeat-x", "repeat-y", "round", "space"));
            }
            case "bgClip" -> {
                title = "Select background clip";
                menus.addAll(Arrays.asList("border-box", "padding-box", "content-box", "text"));
            }
            case "bgBlend" -> {
                title = "Select background blend mode";
                menus.addAll(Arrays.asList("normal", "multiply", "screen", "overlay", "darken", "lighten", "color-dodge", "color-burn", "hard-light", "soft-light", "difference", "exclusion", "hue", "saturation", "color", "luminosity"));
            }
            case "cursor" -> {
                title = "Select cursor type";
                menus.addAll(Arrays.asList("default", "pointer", "move", "text", "wait", "help", "progress", "not-allowed", "grab", "grabbing", "zoom-in", "zoom-out", "none"));
            }
            case "filterType" -> {
                title = "Select filter function";
                menus.addAll(Arrays.asList("blur", "brightness", "contrast", "grayscale", "hue-rotate", "invert", "opacity", "saturate", "sepia"));
            }
            case "timing" -> {
                title = "Select timing function";
                menus.addAll(Arrays.asList("linear", "ease", "ease-in", "ease-out", "ease-in-out", "step-start", "step-end"));
            }
            case "animDirection" -> {
                title = "Select animation direction";
                menus.addAll(Arrays.asList("normal", "reverse", "alternate", "alternate-reverse"));
            }
            case "animFill" -> {
                title = "Select fill mode";
                menus.addAll(Arrays.asList("none", "forwards", "backwards", "both"));
            }
            case "mediaCondition" -> {
                title = "Select media condition";
                menus.addAll(Arrays.asList("max-width", "min-width", "max-height", "min-height", "aspect-ratio"));
            }
            case "mediaOrientation" -> {
                title = "Select orientation";
                menus.addAll(Arrays.asList("portrait", "landscape"));
            }
            case "webImage" -> {
                title = "Select project image";
                try {
                    java.util.HashMap<String, Object> projectDetails = a.a.a.lC.b(sc_id);
                    String projectName = a.a.a.yB.c(projectDetails, "my_ws_name");
                    String imgPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() 
                        + "/.lakiwebsites/simple/" + projectName + "/assets/images";
                    
                    java.io.File folder = new java.io.File(imgPath);
                    if (folder.exists() && folder.isDirectory()) {
                        java.io.File[] files = folder.listFiles();
                        if (files != null) {
                            for (java.io.File f : files) {
                                String name = f.getName();
                                // Add common image formats
                                if (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".svg") || name.endsWith(".webp")) {
                                    menus.add(name);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    menus.add("--error-scanning-assets");
                }
            }
            //end
        }
        return new Pair<>(title, menus);
    }

    private String getPath(String sc_id, String name) {
        return wq.b(sc_id) + "/files/resource/" + name + "/";
    }

    private String getFilename(String filePath, String filenameExtensionToCutOff) {
        String lastPathSegment = Uri.parse(filePath).getLastPathSegment();
        return lastPathSegment.substring(0, lastPathSegment.indexOf(filenameExtensionToCutOff));
    }
}
