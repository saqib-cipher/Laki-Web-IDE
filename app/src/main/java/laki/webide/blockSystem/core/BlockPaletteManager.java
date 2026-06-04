package laki.webide.blockSystem.core;

import android.view.View;

import com.besome.sketch.editor.LogicEditorActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import a.a.a.Rs;

/**
 * Manages the registration and injection of modular blocks into the Logic Editor palette.
 */
public class BlockPaletteManager {

    private static final Map<Integer, ArrayList<ModularBlockDefinition>> registry = new HashMap<>();

    /**
     * Registers a new modular block definition.
     */
    public static void register(ModularBlockDefinition block) {
        int paletteId = block.getTargetPaletteId();
        if (!registry.containsKey(paletteId)) {
            registry.put(paletteId, new ArrayList<>());
        }
        registry.get(paletteId).add(block);
    }

    /**
     * Injects all registered modular blocks for a specific palette ID into the editor.
     */
    public static void injectBlocks(int paletteId, int paletteColor, LogicEditorActivity editor) {
        ArrayList<ModularBlockDefinition> blocks = registry.get(paletteId);
        if (blocks != null) {
            for (ModularBlockDefinition block : blocks) {
                // Use the enhanced native registration pipeline to enable drag/snap AND labels
                editor.a(block.getSpec(), block.getTypeCode(), block.getOpCode());
            }
        }
    }
    
    /**
     * Finds a block definition by its OpCode.
     */
    public static ModularBlockDefinition getBlockByOpCode(String opCode) {
        for (ArrayList<ModularBlockDefinition> list : registry.values()) {
            for (ModularBlockDefinition def : list) {
                if (def.getOpCode().equals(opCode)) return def;
            }
        }
        return null;
    }
}
