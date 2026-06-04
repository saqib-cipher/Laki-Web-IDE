package laki.webide.blockSystem.inputs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

/**
 * Handles connection points for other blocks (e.g., Boolean diamonds).
 */
public class SocketInput extends BaseBlockInput {
    private final Paint paint;

    public SocketInput(Context context, String type) {
        super(context, type);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0x44000000); // Darker cutout for socket
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();
        
        if (type.equals("b")) {
            // Draw diamond socket for Boolean
            Path path = new Path();
            path.moveTo(0, h / 2f);
            path.lineTo(h / 2f, 0);
            path.lineTo(w - h / 2f, 0);
            path.lineTo(w, h / 2f);
            path.lineTo(w - h / 2f, h);
            path.lineTo(h / 2f, h);
            path.close();
            canvas.drawPath(path, paint);
        } else {
            // Draw round socket for Value
            canvas.drawRoundRect(new RectF(0, 0, w, h), h / 2f, h / 2f, paint);
        }
    }

    @Override
    public void onInputClicked() {
        // Drop sockets are usually interacted via Drag & Drop, not simple clicks
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(60, 40);
    }
}
