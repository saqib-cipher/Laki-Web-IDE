package laki.webide.blockSystem.inputs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Handles dropdown menus (e.g., %m.htmlId, %m.view).
 */
public class MenuInput extends BaseBlockInput {
    private final String menuName;
    private final Paint paint;
    private final Paint textPaint;

    public MenuInput(Context context, String menuName) {
        super(context, "m");
        this.menuName = menuName;
        
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0x33000000); // Semi-transparent overlay for dropdowns
        
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(0xFFFFFFFF);
        textPaint.setTextSize(24f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), 4f, 4f, paint);
        String display = (value == null || value.isEmpty()) ? "select" : value;
        canvas.drawText(display, 10, getHeight() / 2f + 8f, textPaint);
    }

    @Override
    public void onInputClicked() {
        // Logic to trigger SelectorMenuHandler for menuName (e.g., htmlClass)
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(120, 40);
    }
}
