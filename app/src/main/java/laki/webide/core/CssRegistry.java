package laki.webide.core;

import android.content.Context;
import java.util.*;

public class CssRegistry {
    private static final Map<String, List<String>> DATA = new HashMap<>();

    static {
        // --- DATA DEFINITIONS (Easy to read and edit) ---
        define("display").options("block", "inline", "inline-block", "flex", "grid", "inline-flex", "inline-grid", "none");
        define("position").options("static", "relative", "absolute", "fixed", "sticky");
        define("overflow").options("visible", "hidden", "scroll", "auto");
        define("visibility").options("visible", "hidden", "collapse");
        define("side").options("top", "right", "bottom", "left");
        define("unit").options("px", "%", "em", "rem", "vh", "vw", "auto");
        define("dimension").options("width", "height");
        define("direction").options("row", "row-reverse", "column", "column-reverse");
        define("justify").options("flex-start", "flex-end", "center", "space-between", "space-around", "space-evenly");
        define("align").options("stretch", "flex-start", "flex-end", "center", "baseline");
        define("wrap").options("nowrap", "wrap", "wrap-reverse");
        define("boxSizing").options("border-box", "content-box");
        define("fontWeight").options("normal", "bold", "lighter", "bolder", "100", "200", "300", "400", "500", "600", "700", "800", "900");
        define("textAlign").options("left", "right", "center", "justify", "start", "end");
        define("textTransform").options("none", "capitalize", "uppercase", "lowercase");
        define("textDecoration").options("none", "underline", "overline", "line-through");
        define("fontStyle").options("normal", "italic", "oblique");
        define("whiteSpace").options("normal", "nowrap", "pre", "pre-wrap", "pre-line");
        define("borderStyle").options("none", "solid", "dotted", "dashed", "double", "groove", "ridge", "inset", "outset");
        define("bgRepeat").options("repeat", "repeat-x", "repeat-y", "no-repeat", "round", "space");
        define("bgSize").options("auto", "cover", "contain");
        define("bgAttachment").options("scroll", "fixed", "local");
        define("textOverflow").options("clip", "ellipsis");
        define("wordBreak").options("normal", "break-all", "keep-all", "break-word");
        define("verticalAlign").options("baseline", "top", "middle", "bottom", "sub", "super", "text-top", "text-bottom");
        define("cursor").options("auto", "default", "pointer", "wait", "text", "move", "help", "not-allowed", "none");
        define("pointerEvents").options("auto", "none", "inherit", "initial");
        define("userSelect").options("auto", "none", "text", "all");
        define("writingMode").options("horizontal-tb", "vertical-rl", "vertical-lr");
        define("hyphens").options("none", "manual", "auto");
        define("bgOrigin").options("border-box", "padding-box", "content-box");
        define("bgClip").options("border-box", "padding-box", "content-box");
        define("blendMode").options("normal", "multiply", "screen", "overlay", "darken", "lighten", "color-dodge", "color-burn", "hard-light", "soft-light", "difference", "exclusion", "hue", "saturation", "color", "luminosity");
        define("objectFit").options("fill", "contain", "cover", "none", "scale-down");
        define("pseudoState").options("hover", "focus", "active", "visited", "disabled", "enabled", "checked", "focus-within", "focus-visible");
        define("easing").options("linear", "ease", "ease-in", "ease-out", "ease-in-out", "step-start", "step-end");
        define("transform").options("rotate(45deg)", "scale(1.5)", "translate(10px, 10px)", "skew(10deg)", "none");
        define("filter").options("blur(5px)", "brightness(0.5)", "contrast(200%)", "grayscale(100%)", "hue-rotate(90deg)", "invert(100%)", "opacity(50%)", "saturate(30%)", "sepia(100%)", "none");
    }

    private static DataBuilder define(String target) {
        return new DataBuilder(target);
    }

    private static class DataBuilder {
        private final String target;
        DataBuilder(String target) { this.target = target; }
        void options(String... opts) { DATA.put(target, Arrays.asList(opts)); }
    }

    public static ArrayList<String> getMenuData(Context context, String menuName, String currentValue) {
        if (DATA.containsKey(menuName)) {
            return new ArrayList<>(DATA.get(menuName));
        }

        if (menuName.equals("var")) {
            return DesignDataManager.getVariablesByType(LogicEditorActivity.filename, 2);
        }

        String scId = com.besome.sketch.design.DesignActivity.sc_id;
        if (context instanceof LogicEditorActivity) {
            scId = ((LogicEditorActivity) context).scId;
        } else if (context instanceof android.content.ContextWrapper) {
            android.content.Context base = ((android.content.ContextWrapper) context).getBaseContext();
            if (base instanceof LogicEditorActivity) scId = ((LogicEditorActivity) base).scId;
        }

        if (menuName.startsWith("html")) {
            return DesignDataManager.getHtmlSelectors(context, scId, LogicEditorActivity.filename, menuName);
        } else if (menuName.equals("image") || menuName.equals("webImage")) {
            return DesignDataManager.getProjectAssets(scId, "image");
        }

        return new ArrayList<>();
    }

    public static String getMenuTitle(String menuName) {
        if (menuName.startsWith("html")) return "Select " + menuName.substring(4);
        return "Select " + menuName.substring(0, 1).toUpperCase() + menuName.substring(1);
    }
}
