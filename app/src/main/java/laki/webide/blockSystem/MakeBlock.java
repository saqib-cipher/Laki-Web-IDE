package laki.webide.blockSystem;

import android.view.View;
import com.besome.sketch.editor.LogicEditorActivity;
import a.a.a.Rs;
import a.a.a.Ts;
import java.util.HashMap;
import java.util.Map;

/**
 * Unified block creation utility for Laki Web IDE.
 * This class copies the logic from LogicEditorActivity.a to centralize block handling
 * and fix the "Blank Label" bug for Web-focused blocks.
 */
public class MakeBlock {

    private static final Map<String, String> WEB_SPECS = new HashMap<>();

    static {
        // Layout blocks
        WEB_SPECS.put("set_display", "display: %m.display");
        WEB_SPECS.put("set_position", "position: %m.position");
        
        // Variable blocks
        WEB_SPECS.put("css_var_two", "Variable: %s set %s");

    }

    /**
     * Retrieves a Web-friendly spec for a given OpCode.
     */
    public static String getWebSpec(String opCode) {
        return WEB_SPECS.getOrDefault(opCode, "");
    }

    /**
     * Replaces LogicEditorActivity.a(String type, String opCode)
     * Automatically looks up the correct Web Spec to avoid blank labels.
     */
    public static View a(LogicEditorActivity editor, String type, String opCode) {
        String spec = WEB_SPECS.getOrDefault(opCode, "");
        return a(editor, spec, type, opCode);
    }

    /**
     * Replaces LogicEditorActivity.a(String spec, String type, String opCode)
     * This is the core method that enables Native Drag and Snap.
     */
    public static View a(LogicEditorActivity editor, String spec, String type, String opCode) {
        // 1. Physical Creation: Uses PaletteBlock.m.a() logic
        // We pass the actual spec here so the native parser creates labels/holes
        Ts blockView = editor.m.a(spec, type, opCode);
        
        // 2. Identification: Set tag for project saving and code generation
        blockView.setTag(opCode);
        
        // 3. Native Registration: Set Clickable
        blockView.setClickable(true);
        
        // 4. PHYSICS LINK: Attaches the activity as the touch listener.
        // WITHOUT THIS, THE BLOCK WILL NOT SNAP OR DRAG.
        blockView.setOnTouchListener(editor);
        
        return blockView;
    }

    /**
     * Replaces LogicEditorActivity.a(String spec, String type, String typeName, String opCode)
     */
    public static View a(LogicEditorActivity editor, String spec, String type, String typeName, String opCode) {
        Ts blockView = editor.m.a(spec, type, typeName, opCode);
        blockView.setTag(opCode);
        blockView.setClickable(true);
        blockView.setOnTouchListener(editor);
        return blockView;
    }
}
