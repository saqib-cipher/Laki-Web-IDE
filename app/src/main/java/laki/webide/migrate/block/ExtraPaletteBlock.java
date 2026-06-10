package laki.webide.migrate.block;

import static laki.webide.utility.ThemeUtils.getColor;
import static laki.webide.utility.ThemeUtils.isDarkThemeEnabled;

import android.util.Pair;

import androidx.annotation.ColorInt;

import com.besome.sketch.beans.ComponentBean;
import com.besome.sketch.beans.ProjectFileBean;
import com.besome.sketch.beans.ViewBean;
import com.besome.sketch.editor.LogicEditorActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import a.a.a.Ox;
import a.a.a.jC;
import a.a.a.jq;
import a.a.a.kq;
import mod.agus.jcoderz.beans.ViewBeans;
import mod.hey.studios.editor.view.IdGenerator;
import mod.hey.studios.moreblock.ReturnMoreblockManager;
import mod.hilal.saif.activities.tools.ConfigActivity;
import mod.hilal.saif.blocks.BlocksHandler;
import mod.pranav.viewbinding.ViewBindingBuilder;
import laki.webide.R;
import laki.webide.activities.resourceseditor.components.utils.StringsEditorManager;
import laki.webide.blocks.ExtraBlocks;
import laki.webide.control.logic.LogicClickListener;
import laki.webide.utility.CustomVariableUtil;
import laki.webide.utility.FileResConfig;
import laki.webide.utility.FileUtil;
import laki.webide.utility.SketchwareUtil;
import laki.webide.blockSystem.core.BlockPaletteManager;

public class ExtraPaletteBlock {

    private final String eventName;
    private final String javaName;
    private final String xmlName;
    private final String sc_id;

    private final LogicClickListener clickListener;
    private final ExtraBlocks extraBlocks;
    private final FileResConfig frc;
    private final HashMap<String, Object> mapSave = new HashMap<>();
    private final ProjectFileBean projectFile;
    private final Boolean isViewBindingEnabled;
    public LogicEditorActivity logicEditor;

    public ExtraPaletteBlock(LogicEditorActivity logicEditorActivity, Boolean isViewBindingEnabled) {
        logicEditor = logicEditorActivity;
        eventName = logicEditorActivity.eventName;

        projectFile = logicEditor.M;
        javaName = projectFile.getJavaName();
        xmlName = projectFile.getXmlName();
        sc_id = logicEditor.scId;
        this.isViewBindingEnabled = isViewBindingEnabled;

        frc = new FileResConfig(sc_id);
        extraBlocks = new ExtraBlocks(logicEditor);
        clickListener = new LogicClickListener(logicEditor);
    }

    private boolean isWidgetUsed(String str) {
        if (ConfigActivity.isSettingEnabled(ConfigActivity.SETTING_SHOW_EVERY_SINGLE_BLOCK)) {
            return true;
        }

        if (mapSave.containsKey(str)) {
            Object strValueInMapSave = mapSave.get(str);
            if (strValueInMapSave instanceof Boolean) {
                return (boolean) strValueInMapSave;
            } else {
                return false;
            }
        }
        if (eventName.equals("onBindCustomView")) {
            var eC = jC.a(sc_id);
            var view = eC.c(xmlName, logicEditor.id);
            if (view == null) {
                // in case the View's in a Drawer
                view = eC.c("_drawer_" + xmlName, logicEditor.id);
            }
            String customView = view.customView;
            if (customView != null && !customView.isEmpty()) {
                for (ViewBean viewBean : jC.a(sc_id).d(ProjectFileBean.getXmlName(customView))) {
                    if (viewBean.getClassInfo().a(str)) {
                        mapSave.put(str, true);
                        return true;
                    }
                }
            }
        } else if (jC.a(sc_id).y(xmlName, str)) {
            mapSave.put(str, true);
            return true;
        }
        mapSave.put(str, false);
        return false;
    }

    /*
     * ExtraPaletteBlock#f(Ss) moved to mod.w3wide.menu.ExtraMenuBean#defineMenuSelector(Ss)
     * for better block menu selections and to add new stuff easily.
     */

    public boolean e(String str, String str2) {
        return switch (str) {
            case "collapsingtoolbar" ->
                    jC.a(sc_id).g(xmlName, ViewBeans.VIEW_TYPE_LAYOUT_COLLAPSINGTOOLBARLAYOUT, str2);
            case "cloudmessage" ->
                    jC.a(sc_id).d(javaName, ComponentBean.COMPONENT_TYPE_FIREBASE_CLOUD_MESSAGE, str2);
            case "datepicker" ->
                    jC.a(sc_id).g(xmlName, ViewBeans.VIEW_TYPE_WIDGET_DATEPICKER, str2);
            case "customVar" -> jC.a(sc_id).f(xmlName, 5, str2);
            case "timepicker" ->
                    jC.a(sc_id).g(xmlName, ViewBeans.VIEW_TYPE_WIDGET_TIMEPICKER, str2);
            case "swiperefreshlayout" ->
                    jC.a(sc_id).g(xmlName, ViewBeans.VIEW_TYPE_LAYOUT_SWIPEREFRESHLAYOUT, str2);
            default -> true;
        };
    }

    /**
     * @see ReturnMoreblockManager#listMoreblocks(Iterator, LogicEditorActivity)
     */
    private void moreBlocks() {
        ReturnMoreblockManager.listMoreblocks(jC.a(sc_id).i(javaName).iterator(), logicEditor);
    }
    private void variables() {
        ArrayList<String> booleanVariables = jC.a(sc_id).e(javaName, 0);
        for (int i = 0; i < booleanVariables.size(); i++) {
            if (i == 0) logicEditor.a("Boolean", getTitleBgColor());

            logicEditor.a(booleanVariables.get(i), "b", "getVar").setTag(booleanVariables.get(i));
        }

        ArrayList<String> numberVariables = jC.a(sc_id).e(javaName, 1);
        for (int i = 0; i < numberVariables.size(); i++) {
            if (i == 0) logicEditor.a("Number", getTitleBgColor());

            logicEditor.a(numberVariables.get(i), "d", "getVar").setTag(numberVariables.get(i));
        }

        ArrayList<String> stringVariables = jC.a(sc_id).e(javaName, 2);
        for (int i = 0; i < stringVariables.size(); i++) {
            if (i == 0) logicEditor.a("String", getTitleBgColor());

            logicEditor.a(stringVariables.get(i), "s", "getVar").setTag(stringVariables.get(i));
        }

        ArrayList<String> mapVariables = jC.a(sc_id).e(javaName, 3);
        for (int i = 0; i < mapVariables.size(); i++) {
            if (i == 0) logicEditor.a("Map", getTitleBgColor());

            logicEditor.a(mapVariables.get(i), "a", "getVar").setTag(mapVariables.get(i));
        }

        ArrayList<String> customVariables = jC.a(sc_id).e(javaName, 5);
        for (int i = 0; i < customVariables.size(); i++) {
            if (i == 0) logicEditor.a("Custom Variable", getTitleBgColor());

            String[] split = customVariables.get(i).split(" ");
            if (split.length > 1) {
                logicEditor.a(split[1], "v", split[0], "getVar").setTag(customVariables.get(i));
            } else {
                SketchwareUtil.toastError("Found invalid data of Custom Variable #" + (i + 1) + ": \"" + customVariables.get(i) + "\"");
            }
        }

        ArrayList<String> customVariables2 = jC.a(sc_id).e(javaName, 6);
        for (int i = 0; i < customVariables2.size(); i++) {
            if (i == 0) logicEditor.a("Custom Variable", getTitleBgColor());

            String variable = customVariables2.get(i);
            String variableType = CustomVariableUtil.getVariableType(variable);
            String variableName = CustomVariableUtil.getVariableName(variable);
            if (variableType != null && variableName != null) {
                String type = switch (variableType) {
                    case "boolean", "Boolean" -> "b";
                    case "String" -> "s";
                    case "double", "Double", "int", "Integer", "float", "Float", "long", "Long",
                         "short", "Short" -> "d";
                    default -> "v";
                };
                logicEditor.a(variableName, type, variableType, "getVar").setTag(variable);
            } else {
                logicEditor.a("Invalid: " + variable, getColor(logicEditor, R.attr.colorError));
            }
        }
        BlocksHandler.primaryBlocksA(
                logicEditor,
                extraBlocks.isVariableUsed(0),
                extraBlocks.isVariableUsed(1),
                extraBlocks.isVariableUsed(2),
                extraBlocks.isVariableUsed(3)
        );
        blockCustomViews();
        blockDrawer();
        extraBlocks.eventBlocks();
        blockComponents();
    }

    private void blockComponents() {
        ArrayList<ComponentBean> components = jC.a(sc_id).e(javaName);
        for (int i = 0, componentsSize = components.size(); i < componentsSize; i++) {
            ComponentBean component = components.get(i);

            if (i == 0) {
                logicEditor.a("Components", getTitleBgColor());
            }

            if (component.type != 27) {
                logicEditor.a(component.componentId, "p", ComponentBean.getComponentTypeName(component.type), "getVar").setTag(component.componentId);
            }
        }
    }

    private void blockCustomViews() {
        if (eventName.equals("onBindCustomView")) {
            String viewId = logicEditor.id;
            var eC = jC.a(sc_id);
            ViewBean viewBean = eC.c(xmlName, viewId);
            if (viewBean == null) {
                // Event is of a Drawer View
                viewBean = eC.c("_drawer_" + xmlName, viewId);
            }
            String viewBeanCustomView = viewBean.customView;
            if (viewBeanCustomView != null && !viewBeanCustomView.isEmpty()) {
                ArrayList<ViewBean> customViews = jC.a(sc_id).d(ProjectFileBean.getXmlName(viewBeanCustomView));
                for (int i = 0, customViewsSize = customViews.size(); i < customViewsSize; i++) {
                    ViewBean customView = customViews.get(i);

                    if (i == 0) {
                        logicEditor.a("Custom Views", getTitleBgColor());
                    }

                    if (!customView.convert.equals("include")) {
                        String typeName = customView.convert.isEmpty() ? ViewBean.getViewTypeName(customView.type) : IdGenerator.getLastPath(customView.convert);
                        String resultId = isViewBindingEnabled ? "binding." + ViewBindingBuilder.generateParameterFromId(customView.id) : customView.id;
                        logicEditor.a(resultId, "v", typeName, "getVar").setTag(resultId);
                    }
                }
            }
            logicEditor.a(" ", "notifyDataSetChanged");
            logicEditor.a("c", "viewOnClick");
            logicEditor.a("c", "viewOnLongClick");
            logicEditor.a("c", "checkboxOnChecked");
            logicEditor.a("b", "checkboxIsChecked");
            return;
        }
        ArrayList<ViewBean> views = jC.a(sc_id).d(xmlName);
        for (int i = 0, viewsSize = views.size(); i < viewsSize; i++) {
            ViewBean view = views.get(i);
            Set<String> toNotAdd = new Ox(new jq(), projectFile).readAttributesToReplace(view);

            if (i == 0) {
                logicEditor.a("Views", getTitleBgColor());
            }

            if (!view.convert.equals("include")) {
                if (!toNotAdd.contains("android:id")) {
                    String typeName = view.convert.isEmpty() ? ViewBean.getViewTypeName(view.type) : IdGenerator.getLastPath(view.convert);
                    logicEditor.a(isViewBindingEnabled ? "binding." + ViewBindingBuilder.generateParameterFromId(view.id) : view.id, "v", typeName, "getVar").setTag(isViewBindingEnabled ? "binding." + ViewBindingBuilder.generateParameterFromId(view.id) : view.id);
                }
            }
        }
    }

    private void blockDrawer() {
        if (projectFile.hasActivityOption(ProjectFileBean.OPTION_ACTIVITY_DRAWER)) {
            ArrayList<ViewBean> drawerViews = jC.a(sc_id).d(projectFile.getDrawerXmlName());
            if (drawerViews != null) {
                for (int i = 0, drawerViewsSize = drawerViews.size(); i < drawerViewsSize; i++) {
                    ViewBean drawerView = drawerViews.get(i);

                    if (i == 0) {
                        logicEditor.a("Drawer Views", getTitleBgColor());
                    }

                    if (!drawerView.convert.equals("include")) {
                        String id = "_drawer_" + drawerView.id;
                        String typeName = drawerView.convert.isEmpty() ? ViewBean.getViewTypeName(drawerView.type) : IdGenerator.getLastPath(drawerView.convert);
                        logicEditor.a(isViewBindingEnabled ? "binding.drawer." + ViewBindingBuilder.generateParameterFromId(drawerView.id) : id, "v", typeName, "getVar").setTag(id);
                    }
                }
            }
        }
    }

    private void list() {
        for (Pair<Integer, String> list : jC.a(sc_id).j(javaName)) {
            int type = list.first;
            String name = list.second;

            switch (type) {
                case 1, 2, 3 -> logicEditor.a(name, "l", kq.a(type), "getVar").setTag(name);
                default -> {
                    String variableName = CustomVariableUtil.getVariableName(name);
                    if (variableName != null) {
                        logicEditor.a(variableName, "l", "List", "getVar").setTag(name);
                    } else {
                        logicEditor.a("Invalid: " + name, getColor(logicEditor, R.attr.colorError));
                    }
                }
            }
        }

        BlocksHandler.primaryBlocksB(
                logicEditor,
                extraBlocks.isListUsed(1),
                extraBlocks.isListUsed(2),
                extraBlocks.isListUsed(3)
        );
    }

    public void setBlock(int paletteId, int paletteColor) {
        // Remove previous palette's blocks
        logicEditor.m.a();


        if (eventName.equals("Import")) {
            if (paletteId == 3) {
                logicEditor.a(" ", "addSourceDirectly");
            } else {
                logicEditor.a("Enter the path without import & semicolon", paletteColor);
                logicEditor.a(" ", "customImport");
                logicEditor.a(" ", "customImport2");
            }
            return;
        }

        switch (paletteId) {
            case -1:
                String filePath = FileUtil.getExternalStorageDir().concat("/.lakiwebsites/data/").concat(sc_id.concat("/files/resource/values/strings.xml"));
                ArrayList<HashMap<String, Object>> StringsListMap = new ArrayList<>();
                StringsEditorManager stringsEditorManager = new StringsEditorManager();
                stringsEditorManager.convertXmlStringsToListMap(FileUtil.readFileIfExist(filePath), StringsListMap);

                logicEditor.b("Open Resources editor", "openResourcesEditor");

                logicEditor.a("s", "getResString");
                logicEditor.a("Saved Res Strings :", paletteColor);
                if (!stringsEditorManager.isXmlStringsExist(StringsListMap, "app_name")) {
                    logicEditor.a("app_name", "s", "getResStr").setTag("S98ZCSapp_name");
                }

                for (int i = 0; i < StringsListMap.size(); i++) {
                    String key = StringsListMap.get(i).get("key").toString();
                    logicEditor.a(key, "s", "getResStr").setTag("S98ZCS" + key);
                }
                return;
            case 0:
                if (laki.webide.events.ExtCSS.isMatch(logicEditor.id)) {
                    logicEditor.b("Add variable", "variableAdd");
                    logicEditor.b("Remove variable", "variableRemove", clickListener);
                    variables();
                    // Modular blocks will be injected automatically below
                    break;
                }
                return;

            case 1:
                if (laki.webide.events.ExtCSS.isMatch(logicEditor.id)) {
                    logicEditor.a("CSS Selectors", paletteColor);
                    // Modular blocks will be injected automatically below
                    break;
                }
                logicEditor.b("Add list", "listAdd");
                logicEditor.b("Add custom List", "listAddCustom", clickListener);
                logicEditor.b("Remove list", "listRemove", clickListener);
                list();
                return;

            case 2:
                if (laki.webide.events.ExtCSS.isMatch(logicEditor.id)) {
                    logicEditor.a("Layout", paletteColor);
                    // Modular blocks will be injected automatically below
                    break;
                }
                BlocksHandler.primaryBlocksC(logicEditor);
                return;

            case 3:
                if (laki.webide.events.ExtCSS.isMatch(logicEditor.id)) {
                    logicEditor.a("Spacing", paletteColor);
                    // Modular blocks will be injected automatically below
                    break;
                }
                BlocksHandler.primaryBlocksD(logicEditor);
                return;

            case 4:
                if (laki.webide.events.ExtCSS.isMatch(logicEditor.id)) {
                    logicEditor.a("Text Edit", paletteColor);
                    // Modular blocks will be injected automatically below
                    break;
                }
                logicEditor.a("d", "mathLog");
                logicEditor.a("d", "mathLog10");
                logicEditor.a("d", "mathToRadian");
                logicEditor.a("d", "mathToDegree");
                return;

            case 5:
                if (laki.webide.events.ExtCSS.isMatch(logicEditor.id)) {
                    logicEditor.a("Background", paletteColor);
                    // Modular blocks will be injected automatically below
                    break;
                }
                logicEditor.a(" ", "setBitmapFileColorFilter");
                logicEditor.a(" ", "setBitmapFileBrightness");
                logicEditor.a(" ", "setBitmapFileContrast");
                logicEditor.a("d", "getJpegRotate");
                return;

            case 6:
                if (laki.webide.events.ExtCSS.isMatch(logicEditor.id)) {
                    logicEditor.a("Border & shadow", paletteColor);
                    break;
                }

                logicEditor.a(" ", "setStrokeView");
                logicEditor.a(" ", "setCornerRadiusView");
                logicEditor.a(" ", "setGradientBackground");
                logicEditor.a(" ", "setRadiusAndStrokeView");
                return;

            case 7:
                if (laki.webide.events.ExtCSS.isMatch(logicEditor.id)) {
                    logicEditor.a("Effects", paletteColor);
                    break;
                }
                logicEditor.b("Add component", "componentAdd");
                logicEditor.a(" ", "changeStatebarColour");
                logicEditor.a(" ", "LightStatusBar");
                logicEditor.a(" ", "showKeyboard");
                
                return;
            case 8:
                if (laki.webide.events.ExtCSS.isMatch(logicEditor.id)) {
                    logicEditor.a("Animation", paletteColor);
                    break;
                }
                logicEditor.b("Add component", "componentAdd");
                logicEditor.a(" ", "changeStatebarColour");
                logicEditor.a(" ", "LightStatusBar");
                logicEditor.a(" ", "showKeyboard");

                return;
            case 9:
                if (laki.webide.events.ExtCSS.isMatch(logicEditor.id)) {
                    logicEditor.a("Responsive", paletteColor);
                    break;
                }
                logicEditor.b("Add component", "componentAdd");
                logicEditor.a(" ", "changeStatebarColour");
                logicEditor.a(" ", "LightStatusBar");
                logicEditor.a(" ", "showKeyboard");

                return;

            case 10:
                logicEditor.b("Create", "blockAdd");
                logicEditor.b("Import From Collection", "blockImport");
                if (ConfigActivity.isSettingEnabled(ConfigActivity.SETTING_SHOW_BUILT_IN_BLOCKS)) {
                    logicEditor.a(" ", "customToast");
                    logicEditor.a(" ", "customToastWithIcon");
                }
                moreBlocks();
                if (ConfigActivity.isSettingEnabled(ConfigActivity.SETTING_SHOW_BUILT_IN_BLOCKS)) {
                    logicEditor.a("Command Blocks", getTitleBgColor());
                    logicEditor.a("c", "CommandBlockJava");
                    logicEditor.addDeprecatedBlock("Deprecated: Use XML Command Manager", "c", "CommandBlockXML");
                    logicEditor.a("Permission Command Blocks", getTitleBgColor());
                    logicEditor.a(" ", "addPermission");
                    logicEditor.a(" ", "removePermission");
                    logicEditor.a("Other Command Blocks", getTitleBgColor());
                    logicEditor.a(" ", "addCustomVariable");
                    logicEditor.a(" ", "addInitializer");
                    return;
                }
                return;

            default:
                int paletteIndex = -1, paletteBlocks = 0;
                ArrayList<HashMap<String, Object>> extraBlockData = ExtraBlockFile.getExtraBlockData();
                for (int i = 0, extraBlockDataSize = extraBlockData.size(); i < extraBlockDataSize; i++) {
                    HashMap<String, Object> map = extraBlockData.get(i);

                    Object palette = map.get("palette");
                    if (palette instanceof String paletteString) {

                        if (paletteString.equals(String.valueOf(paletteId))) {
                            if (paletteIndex == -1) paletteIndex = Integer.parseInt(paletteString);
                            paletteBlocks++;

                            Object type = map.get("type");
                            if (type instanceof String typeString) {

                                if (typeString.equals("h")) {
                                    Object spec = map.get("spec");
                                    if (spec instanceof String specString) {
                                        logicEditor.a(specString, getTitleBgColor());
                                    } else {
                                        SketchwareUtil.toastError("Custom Block #" + paletteBlocks +
                                                " of current palette has an invalid spec data type");
                                    }
                                } else {
                                    Object name = map.get("name");
                                    if (name instanceof String nameString) {

                                        Object typeName = map.get("typeName");
                                        if (typeName instanceof String typeNameString) {

                                            logicEditor.a("", typeString, typeNameString, nameString);
                                        } else {
                                            logicEditor.a("", typeString, "", nameString);
                                        }
                                    } else {
                                        SketchwareUtil.toastError("Custom Block #" + paletteBlocks +
                                                " of current palette has an invalid name data type");
                                    }
                                }
                            } else {
                                SketchwareUtil.toastError("Custom Block #" + paletteBlocks +
                                        " of current palette has an invalid block type data type");
                            }
                        }
                    } else {
                        SketchwareUtil.toastError("Custom Block #" + paletteBlocks +
                                " of current palette has an invalid block palette data type");
                    }
                }
                break;
        }

        // --- DECENTRALIZED BLOCK INJECTION ---
        // Automatically loads blocks defined in separate modular files
        BlockPaletteManager.injectBlocks(paletteId, paletteColor, logicEditor);
    }

    private @ColorInt int getTitleBgColor() {
        return getColor(logicEditor, isDarkThemeEnabled(logicEditor) ? R.attr.colorSurfaceContainerHigh : R.attr.colorSurfaceInverse);
    }
}
