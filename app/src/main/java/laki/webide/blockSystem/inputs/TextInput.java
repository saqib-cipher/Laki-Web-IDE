package laki.webide.blockSystem.inputs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Handles editable text and numeric inputs within a block.
 */
public class TextInput extends BaseBlockInput {
    private final Paint paint;
    private final Paint textPaint;

    public TextInput(Context context, String type) {
        super(context, type);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xFFFFFFFF);
        paint.setStyle(Paint.Style.FILL);
        
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(0xFF000000);
        textPaint.setTextSize(24f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rect = new RectF(0, 0, getWidth(), getHeight());
        // String inputs are rectangular, Number inputs are more rounded in Sketchware
        float radius = type.equals("d") ? getHeight() / 2f : 4f;
        canvas.drawRoundRect(rect, radius, radius, paint);
        
        if (value != null && !value.isEmpty()) {
            canvas.drawText(value, 10, getHeight() / 2f + 8f, textPaint);
        }
    }

    @Override
    public void onInputClicked() {
        // Logic to show keyboard dialog
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Basic measurement based on text width or minimum size
        setMeasuredDimension(80, 40);
    }
}
