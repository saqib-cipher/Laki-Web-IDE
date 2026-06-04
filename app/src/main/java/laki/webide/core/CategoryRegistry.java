package laki.webide.core;

import java.util.HashMap;
import java.util.Map;

public class CategoryRegistry {
    private static final Map<String, Integer> CATEGORY_COLORS = new HashMap<>();

    static {
        CATEGORY_COLORS.put("VARIABLE", 0xFFFF9800); // Orange (Standard Variable color)
        CATEGORY_COLORS.put("SELECTOR", 0xFFE91E63); // Pink
        CATEGORY_COLORS.put("LAYOUT", 0xFF2196F3);   // Blue
        CATEGORY_COLORS.put("TEXT", 0xFF4CAF50);     // Green
        CATEGORY_COLORS.put("BORDER", 0xFF795548);   // Brown
        CATEGORY_COLORS.put("BACKGROUND", 0xFF9C27B0); // Purple
        CATEGORY_COLORS.put("FLEX", 0xFF00BCD4);     // Cyan
        CATEGORY_COLORS.put("OTHER", 0xFF607D8B);    // Blue Gray
    }

    public static int getColor(String category) {
        if (category == null) return CATEGORY_COLORS.get("OTHER");
        Integer color = CATEGORY_COLORS.get(category.toUpperCase());
        return color != null ? color : CATEGORY_COLORS.get("OTHER");
    }
}
