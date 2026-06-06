package laki.webide.core;

import android.content.Context;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CssRegistry {
    public static ArrayList<String> getMenuData(Context context, String menuName, String currentValue) {
        ArrayList<String> list = new ArrayList<>();
        switch (menuName) {
            case "var":
                return DesignDataManager.getVariablesByType(LogicEditorActivity.filename, 2);
            case "htmlId":
            case "htmlClass":
            case "htmlTag":
                String scId = (context instanceof LogicEditorActivity) ? ((LogicEditorActivity) context).scId : "";
                return DesignDataManager.getHtmlSelectors(context, scId, LogicEditorActivity.filename, menuName);
            case "display":
                return new ArrayList<>(Arrays.asList("block", "inline", "inline-block", "flex", "grid", "inline-flex", "inline-grid", "none"));
            case "position":
                return new ArrayList<>(Arrays.asList("static", "relative", "absolute", "fixed", "sticky"));
            case "overflow":
                return new ArrayList<>(Arrays.asList("visible", "hidden", "scroll", "auto"));
            case "visibility":
                return new ArrayList<>(Arrays.asList("visible", "hidden", "collapse"));
            case "side":
                return new ArrayList<>(Arrays.asList("top", "right", "bottom", "left"));
            case "unit":
                return new ArrayList<>(Arrays.asList("px", "%", "em", "rem", "vh", "vw", "auto"));
            case "dimension":
                return new ArrayList<>(Arrays.asList("width", "height"));
            case "direction":
                return new ArrayList<>(Arrays.asList("row", "row-reverse", "column", "column-reverse"));
            case "justify":
                return new ArrayList<>(Arrays.asList("flex-start", "flex-end", "center", "space-between", "space-around", "space-evenly"));
            case "align":
                return new ArrayList<>(Arrays.asList("stretch", "flex-start", "flex-end", "center", "baseline"));
            case "wrap":
                return new ArrayList<>(Arrays.asList("nowrap", "wrap", "wrap-reverse"));
            case "boxSizing":
                return new ArrayList<>(Arrays.asList("border-box", "content-box"));
            case "fontWeight":
                return new ArrayList<>(Arrays.asList("normal", "bold", "lighter", "bolder", "100", "200", "300", "400", "500", "600", "700", "800", "900"));
            case "textAlign":
                return new ArrayList<>(Arrays.asList("left", "right", "center", "justify", "start", "end"));
            case "textTransform":
                return new ArrayList<>(Arrays.asList("none", "capitalize", "uppercase", "lowercase"));
            case "textDecoration":
                return new ArrayList<>(Arrays.asList("none", "underline", "overline", "line-through"));
            case "fontStyle":
                return new ArrayList<>(Arrays.asList("normal", "italic", "oblique"));
            case "whiteSpace":
                return new ArrayList<>(Arrays.asList("normal", "nowrap", "pre", "pre-wrap", "pre-line"));
            case "borderStyle":
                return new ArrayList<>(Arrays.asList("none", "solid", "dotted", "dashed", "double", "groove", "ridge", "inset", "outset"));
            case "bgRepeat":
                return new ArrayList<>(Arrays.asList("repeat", "repeat-x", "repeat-y", "no-repeat", "round", "space"));
            case "bgSize":
                return new ArrayList<>(Arrays.asList("auto", "cover", "contain"));
            case "bgAttachment":
                return new ArrayList<>(Arrays.asList("scroll", "fixed", "local"));
            case "textOverflow":
                return new ArrayList<>(Arrays.asList("clip", "ellipsis"));
            case "wordBreak":
                return new ArrayList<>(Arrays.asList("normal", "break-all", "keep-all", "break-word"));
            case "verticalAlign":
                return new ArrayList<>(Arrays.asList("baseline", "top", "middle", "bottom", "sub", "super", "text-top", "text-bottom"));
            case "cursor":
                return new ArrayList<>(Arrays.asList("auto", "default", "pointer", "wait", "text", "move", "help", "not-allowed", "none"));
            case "pointerEvents":
                return new ArrayList<>(Arrays.asList("auto", "none", "inherit", "initial"));
            case "userSelect":
                return new ArrayList<>(Arrays.asList("auto", "none", "text", "all"));
            case "writingMode":
                return new ArrayList<>(Arrays.asList("horizontal-tb", "vertical-rl", "vertical-lr"));
            case "hyphens":
                return new ArrayList<>(Arrays.asList("none", "manual", "auto"));
            case "bgOrigin":
            case "bgClip":
                return new ArrayList<>(Arrays.asList("border-box", "padding-box", "content-box"));
            case "blendMode":
                return new ArrayList<>(Arrays.asList("normal", "multiply", "screen", "overlay", "darken", "lighten", "color-dodge", "color-burn", "hard-light", "soft-light", "difference", "exclusion", "hue", "saturation", "color", "luminosity"));
            case "objectFit":
                return new ArrayList<>(Arrays.asList("fill", "contain", "cover", "none", "scale-down"));
            case "image":
            case "webImage":
                String scIdImg = (context instanceof LogicEditorActivity) ? ((LogicEditorActivity) context).scId : "";
                return DesignDataManager.getProjectAssets(scIdImg, "image");
        }
        return list;
    }
    
    public static String getMenuTitle(String menuName) {
        if (menuName.startsWith("html")) return "Select " + menuName.substring(4);
        return "Select " + menuName.substring(0, 1).toUpperCase() + menuName.substring(1);
    }
}
