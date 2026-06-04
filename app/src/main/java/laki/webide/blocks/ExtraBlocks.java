package laki.webide.blocks;

import android.util.Pair;

import com.besome.sketch.editor.LogicEditorActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import a.a.a.eC;
import a.a.a.jC;
import mod.hilal.saif.activities.tools.ConfigActivity;
import laki.webide.events.HTMLHead;

public class ExtraBlocks {

    private final String eventName;
    private final String javaName;
    private final LogicEditorActivity logicEditor;
    private final eC projectDataManager;

    public ExtraBlocks(LogicEditorActivity logicEditor) {
        eventName = logicEditor.eventName;
        this.logicEditor = logicEditor;
        javaName = logicEditor.M.getJavaName();
        projectDataManager = jC.a(logicEditor.scId);
    }

    public static void extraBlocks(ArrayList<HashMap<String, Object>> arrayList) {
        arrayList.add(addBlock("caseStrAnd", " ", "", "case %s:", "#e1a92a", "case %s and"));
        arrayList.add(addBlock("caseNumAnd", " ", "", "case ((int)%s):", "#e1a92a", "case %d and"));
        arrayList.add(addBlock("continue", "f", "", "continue;", "#e1a92a", "continue"));
        arrayList.add(addBlock("isEmpty", "b", "", "%s.isEmpty()", "#e1a92a", "%s isEmpty"));
        arrayList.add(addBlock("fileutilcopydir", " ", "", "FileUtil.copyDir(%1$s, %2$s);", "#a1887f", "copy dir path %s to path %s"));
        arrayList.add(addBlock("instanceOfOperator", "b", "", "%1$s instanceof %2$s", "#e1a92a", "%s instanceOf %s"));
        arrayList.add(addBlock("checkViewVisibility", "b", "", "%s.getVisibility() == View.%s", "#4a6cd4", "visibility of %m.view equals %m.visible"));
        arrayList.add(addBlock("intentHasExtra", "b", "", "getIntent().hasExtra(%s)", "#2ca5e2", "Activity hasExtra key %s"));
        arrayList.add(addBlock("intentSetType", " ", "", "%s.setType(%s);", "#2ca5e2", "%m.intent setType %s"));
        arrayList.add(addBlock("intentRemoveExtra", " ", "", "%s.removeExtra(%s);", "#2ca5e2", "%m.intent removeExtra key %s"));
        arrayList.add(addBlock("fileContainsData", "b", "", "%1$s.contains(%2$s)", "#2ca5e2", "%m.file contains %s"));
        arrayList.add(addBlock("viewGetChildAt", "v", "View", "%1$s.getChildAt(%2$s)", "#4a6cd4", "%m.view getChildAt %d"));
        arrayList.add(addBlock("strParseInteger", "d", "", "Integer.parseInt(%s)", "#5cb722", "parse int %s"));
        arrayList.add(addBlock("stringSubSingle", "s", "", "%1$s.substring(%2$s)", "#5cb722", "%s subString %d"));
        arrayList.add(addBlock("webviewGetProgress", "d", "", "%1$s.getProgress()", "#4a6cd4", "%m.webview getProgress"));
        arrayList.add(addBlock("menuItemSetVisible", " ", "", "%1$s.setVisible(%2$s);", "#4a6cd4", "%m.MenuItem setVisible %b"));
        arrayList.add(addBlock("menuItemSetEnabled", " ", "", "%1$s.setEnabled(%2$s);", "#4a6cd4", "%m.MenuItem setEnabled %b"));
        arrayList.add(addBlock("menuFindItem", "v", "MenuItem", "menu.findItem(%s)", "#4a6cd4", "MenuItem findItem %s.inputOnly"));
        arrayList.add(addBlock("listAddAll", " ", "", "%1$s.addAll(%2$s);", "#cc5b22", "%m.listStr addAll from %m.list"));
        // File Blocks
        arrayList.add(addBlock("fileCanExecute", "b", "", "%s.canExecute()", "#a1887f", "%m.File canExecute"));
        arrayList.add(addBlock("fileCanRead", "b", "", "%s.canRead()", "#a1887f", "%m.File canRead"));
        arrayList.add(addBlock("fileCanWrite", "b", "", "%s.canWrite()", "#a1887f", "%m.File canWrite"));
        arrayList.add(addBlock("fileGetName", "s", "", "%s.getName()", "#a1887f", "%m.File getName"));
        arrayList.add(addBlock("fileGetParent", "s", "", "%s.getParent()", "#a1887f", "%m.File getParent"));
        arrayList.add(addBlock("fileGetPath", "s", "", "%s.getPath()", "#a1887f", "%m.File getPath"));
        arrayList.add(addBlock("fileIsHidden", "b", "", "%s.isHidden()", "#a1887f", "%m.File isHidden"));
        // Basically Command Block
        arrayList.add(addBlock("addPermission", " ", "", "", "#493F5A", "Permission Command Block: add %m.Permission"));
        arrayList.add(addBlock("removePermission", " ", "", "", "#493F5A", "Permission Command Block: remove %m.Permission"));
        arrayList.add(addBlock("addCustomVariable", " ", "", "", "#493F5A", "Custom Variable Block: add variable %s"));
        arrayList.add(addBlock("addInitializer", " ", "", "", "#493F5A", "Initializer Block: add initializer %s"));
        //OtpView Blocks
        arrayList.add(addBlock("otpViewSetFieldCount", " ", "", "%1$s.setFieldCount(%2$s);", "#4a6cd4", "%m.otpview setFieldCount %d"));
        arrayList.add(addBlock("otpViewSetOTPText", " ", "", "%1$s.setOTPText(%2$s);", "#4a6cd4", "%m.otpview setOTPText %s"));
        arrayList.add(addBlock("otpViewGetOTPText", "s", "", "%1$s.getOTPText()", "#4a6cd4", "%m.otpview getOTPText"));
        arrayList.add(addBlock("otpViewSetOTPListener", "c", "", "%1$s.setOTPListener(new OTPListener() {\r\npublic void onOTPEntered(String _otp) {\r\n%2$s\r\n}\r\n});", "#4a6cd4", "%m.otpview onOTPEntered -> _otp"));
        //SignInButton Blocks
        arrayList.add(addBlock("signInButtonSetColorScheme", " ", "", "%1$s.setColorScheme(SignInButton.%2$s);", "#4a6cd4", "%m.signinbutton setColorScheme %m.SignButtonColor"));
        arrayList.add(addBlock("signInButtonSetSize", " ", "", "%1$s.setSize(SignInButton.%2$s);", "#4a6cd4", "%m.signinbutton setSize %m.SignButtonSize"));
        //Admob's AdView Blocks
        arrayList.add(addBlock("bannerAdViewLoadAd", " ", "", "{\r\nAdRequest adRequest = new AdRequest.Builder().build();\r\n%1$s.loadAd(adRequest);\r\n}", "#4a6cd4", "%m.adview load"));
        //RewardedVideoAd
        arrayList.add(addBlock("rewardedVideoAdLoad", " ", "", "RewardedAd.load(%2$s.this, _reward_ad_unit_id, new AdRequest.Builder().build(), _%1$s_rewarded_ad_load_callback);", "#2ca5e2", "%m.videoad load in %m.activity"));
        arrayList.add(addBlock("rewardedVideoAdShow", " ", "", "%1s.show(%2$s.this, _%1$s_on_user_earned_reward_listener);", "#2ca5e2", "%m.videoad show in %m.activity"));
        
        // --- HTML Head Blocks ---
        arrayList.add(addWebBlock("web_head_title", " ", "", "<title>%s</title>", "#009688", "Set Page Title: %s"));
        arrayList.add(addWebBlock("web_head_charset", " ", "", "<meta charset=\"%s\">", "#009688", "Set Charset: %s"));
        arrayList.add(addWebBlock("web_head_viewport", " ", "", "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">", "#009688", "Add Responsive Viewport"));
        arrayList.add(addWebBlock("web_head_description", " ", "", "<meta name=\"description\" content=\"%s\">", "#009688", "Set Meta Description: %s"));
        arrayList.add(addWebBlock("web_head_css", " ", "", "<link rel=\"stylesheet\" href=\"%s\">", "#009688", "Link External CSS: %s"));
        arrayList.add(addWebBlock("web_head_script", " ", "", "<script src=\"%s\"></script>", "#009688", "Link External JS: %s"));

        // --- CSS Extend Blocks ---
        // CSS Selector
        arrayList.add(addCSSBlock("web_css_sel_custom", "c", "", "%1$s {\n  %2$s\n}", "#FF9800", "Style Selector %s: { %s }"));
        arrayList.add(addCSSBlock("web_css_sel_body", "c", "", "body {\n  %s\n}", "#FF9800", "Body: { %s }"));
        arrayList.add(addCSSBlock("web_css_sel_id", "c", "", "#%1$s {\n  %2$s\n}", "#FF9800", "ID #%s: { %s }"));
        arrayList.add(addCSSBlock("web_css_sel_class", "c", "", ".%1$s {\n  %2$s\n}", "#FF9800", "Class .%s: { %s }"));

        // Layout
        arrayList.add(addCSSBlock("web_css_lay_width", " ", "", "width: %s;", "#FF9800", "width: %s"));
        arrayList.add(addCSSBlock("web_css_lay_height", " ", "", "height: %s;", "#FF9800", "height: %s"));
        arrayList.add(addCSSBlock("web_css_lay_display", " ", "", "display: %s;", "#FF9800", "display: %s"));
        arrayList.add(addCSSBlock("web_css_lay_position", " ", "", "position: %s;", "#FF9800", "position: %s"));
        arrayList.add(addCSSBlock("web_css_lay_top", " ", "", "top: %s;", "#FF9800", "top: %s"));
        arrayList.add(addCSSBlock("web_css_lay_left", " ", "", "left: %s;", "#FF9800", "left: %s"));

        // Spacing
        arrayList.add(addCSSBlock("web_css_spa_margin", " ", "", "margin: %s;", "#FF9800", "margin: %s"));
        arrayList.add(addCSSBlock("web_css_spa_padding", " ", "", "padding: %s;", "#FF9800", "padding: %s"));

        // Text Edit
        arrayList.add(addCSSBlock("web_css_txt_color", " ", "", "color: %s;", "#FF9800", "color: %s"));
        arrayList.add(addCSSBlock("web_css_txt_size", " ", "", "font-size: %s;", "#FF9800", "font-size: %s"));
        arrayList.add(addCSSBlock("web_css_txt_align", " ", "", "text-align: %s;", "#FF9800", "text-align: %s"));
        arrayList.add(addCSSBlock("web_css_txt_family", " ", "", "font-family: %s;", "#FF9800", "font-family: %s"));
        arrayList.add(addCSSBlock("web_css_txt_weight", " ", "", "font-weight: %s;", "#FF9800", "font-weight: %s"));

        // Background
        arrayList.add(addCSSBlock("web_css_bg_color", " ", "", "background-color: %s;", "#FF9800", "background-color: %s"));
        arrayList.add(addCSSBlock("web_css_bg_image", " ", "", "background-image: url('%s');", "#FF9800", "background-image: url(%s)"));
        arrayList.add(addCSSBlock("web_css_bg_repeat", " ", "", "background-repeat: %s;", "#FF9800", "background-repeat: %s"));

        // Animation
        arrayList.add(addCSSBlock("web_css_ani_transition", " ", "", "transition: %s;", "#FF9800", "transition: %s"));
        arrayList.add(addCSSBlock("web_css_ani_animation", " ", "", "animation: %s;", "#FF9800", "animation: %s"));

        // Responsive
        arrayList.add(addCSSBlock("web_css_res_media", "c", "", "@media %1$s {\n  %2$s\n}", "#FF9800", "@media %s { %s }"));

        // Effect
        arrayList.add(addCSSBlock("web_css_eff_opacity", " ", "", "opacity: %s;", "#FF9800", "opacity: %s"));
        arrayList.add(addCSSBlock("web_css_eff_radius", " ", "", "border-radius: %s;", "#FF9800", "border-radius: %s"));
        arrayList.add(addCSSBlock("web_css_eff_border", " ", "", "border: %s;", "#FF9800", "border: %s"));
        arrayList.add(addCSSBlock("web_css_eff_shadow", " ", "", "box-shadow: %s;", "#FF9800", "box-shadow: %s"));
    }

    /**
     * @return A CSS-specific block as HashMap&lt;String, Object&gt;.
     */
    private static HashMap<String, Object> addCSSBlock(String name, String type, String typeName, String code, String color, String spec) {
        HashMap<String, Object> map = addBlock(name, type, typeName, code, color, spec);
        map.put("palette", "89"); // Unique palette ID for CSS Blocks
        return map;
    }

    /**
     * @return A web-specific block as HashMap&lt;String, Object&gt;.
     */
    private static HashMap<String, Object> addWebBlock(String name, String type, String typeName, String code, String color, String spec) {
        HashMap<String, Object> map = addBlock(name, type, typeName, code, color, spec);
        map.put("palette", "88"); // Unique palette ID for Web Blocks
        return map;
    }

    /**
     * @return A block as HashMap&lt;String, Object&gt;.
     */
    private static HashMap<String, Object> addBlock(String name, String type, String typeName, String code, String color, String spec) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("type", type);
        map.put("typeName", typeName);
        map.put("code", code);
        map.put("color", color);
        map.put("palette", "-1");
        map.put("spec", spec);
        return map;
    }

    /**
     * @return A block as HashMap&lt;String, Object&gt; with spec2 (<code>e</code> type blocks)
     */
    private static HashMap<String, Object> addBlock(String name, String type, String typeName, String code, String color, String spec, String spec2) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("type", type);
        map.put("typeName", typeName);
        map.put("code", code);
        map.put("color", color);
        map.put("palette", "-1");
        map.put("spec", spec);
        map.put("spec2", spec2);
        return map;
    }

    public boolean isVariableUsed(int varId) {
        ArrayList<Pair<Integer, String>> arrayList = projectDataManager.k(javaName);
        ArrayList<Integer> variableList = new ArrayList<>();
        for (Pair<Integer, String> intStrPair : arrayList) {
            variableList.add(intStrPair.first);
        }
        return variableList.contains(varId);
    }

    public boolean isListUsed(int listId) {
        ArrayList<Pair<Integer, String>> arrayList = projectDataManager.j(javaName);
        ArrayList<Integer> listVar = new ArrayList<>();
        for (Pair<Integer, String> intStrPair : arrayList) {
            listVar.add(intStrPair.first);
        }
        return listVar.contains(listId);
    }

    public boolean isComponentUsed(int componentId) {
        return projectDataManager.f(javaName, componentId) || ConfigActivity.isSettingEnabled(ConfigActivity.SETTING_SHOW_EVERY_SINGLE_BLOCK);
    }

    public boolean isCustomVarUsed(String variable) {
        if (ConfigActivity.isSettingEnabled(ConfigActivity.SETTING_SHOW_EVERY_SINGLE_BLOCK)) {
            return true;
        }

        ArrayList<String> arrayList = new ArrayList<>();
        for (String variableName : projectDataManager.e(javaName, 5)) {
            Matcher matcher = Pattern.compile("^(\\w+)[\\s]+(\\w+)").matcher(variableName);
            while (matcher.find()) {
                arrayList.add(matcher.group(1));
            }
        }
        return arrayList.contains(variable);
    }

    public void eventBlocks() {
        if (logicEditor.id.equals(HTMLHead.EVENT_ID)) {
            logicEditor.a("Head of HTML", 0xff009688);
            logicEditor.a(" ", "web_head_title");
            logicEditor.a(" ", "web_head_charset");
            logicEditor.a(" ", "web_head_viewport");
            logicEditor.a(" ", "web_head_description");
            logicEditor.a(" ", "web_head_css");
            logicEditor.a(" ", "web_head_script");
        }

        if (eventName.equals("onCreateOptionsMenu")) {
            logicEditor.a("Menu Item", 0xff555555);
            logicEditor.a(" ", "menuItemSetVisible");
            logicEditor.a(" ", "menuItemSetEnabled");
            logicEditor.a("v", "menuFindItem");
        }
    }

    public void fileBlocks() {
        if (isCustomVarUsed("File")) {
            logicEditor.a("File Blocks", 0xff555555);
            logicEditor.a("b", "fileCanExecute");
            logicEditor.a("b", "fileCanRead");
            logicEditor.a("b", "fileCanWrite");
            logicEditor.a("s", "fileGetName");
            logicEditor.a("s", "fileGetParent");
            logicEditor.a("s", "fileGetPath");
            logicEditor.a("b", "fileIsHidden");
        }
    }
}
