package laki.webide.blockSystem;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

/**
 * The Centralized Drawing System for Laki Web IDE.
 * Replaces the obfuscated rendering logic from the important.jar.
 */
public class UniversalBlockRenderer {

    private final Paint blockPaint;
    private final Paint borderPaint;

    public UniversalBlockRenderer() {
        blockPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        blockPaint.setStyle(Paint.Style.FILL);

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(2f);
        borderPaint.setColor(0x33000000); // Slight dark border
    }

    /**
     * Draws the block geometry based on its shape type.
     */
    public void drawBlock(Canvas canvas, BlockDrawingData data) {
        blockPaint.setColor(data.color);
        Path path = data.backgroundPath;
        path.reset();

        float finalW = (float) data.width;
        float finalH = (float) data.height;
        float finalScale = data.scale;
        float finalR = BlockDrawingData.BASE_CORNER_RADIUS * finalScale;
        float finalNW = BlockDrawingData.BASE_NOTCH_WIDTH * finalScale;
        float finalNH = BlockDrawingData.BASE_NOTCH_HEIGHT * finalScale;
        boolean finalIsFinal = data.isFinal;

        switch (data.shape) {
            case COMMAND -> renderCommandShape(path, finalW, finalH, finalR, finalNW, finalNH, finalIsFinal, finalScale);
            case CONTAINER -> renderContainerShape(path, finalW, finalH, finalR, finalNW, finalNH, (float)data.mouth1Height, finalIsFinal, finalScale);
            case MULTI_CONTAINER -> renderMultiContainerShape(path, finalW, finalH, finalR, finalNW, finalNH, (float)data.mouth1Height, (float)data.mouth2Height, finalIsFinal, finalScale);
            case VALUE -> renderValueShape(path, finalW, finalH, finalR);
            case BOOLEAN -> renderBooleanShape(path, finalW, finalH);
            case HEAD -> renderHeadShape(path, finalW, finalH, finalR, finalNW, finalNH, finalScale);
            case WRAPPER -> renderWrapperShape(path, finalW, finalH, finalR);
        }

        canvas.drawPath(path, blockPaint);
        canvas.drawPath(path, borderPaint);
    }

    private void renderCommandShape(Path p, float w, float h, float r, float nw, float nh, boolean isFinal, float s) {
        p.moveTo(0, r);
        p.lineTo(10f * s, 0);
        p.lineTo((10f + nw / 2f) * s, nh / 2f);
        p.lineTo((10f + nw) * s, 0);
        p.lineTo(w - r, 0);
        p.arcTo(new RectF(w - 2f * r, 0, w, 2f * r), -90, 90);
        p.lineTo(w, h - r);
        p.arcTo(new RectF(w - 2f * r, h - 2f * r, w, h), 0, 90);
        if (isFinal) {
            p.lineTo(0, h);
        } else {
            p.lineTo((10f + nw) * s, h);
            p.lineTo((10f + nw / 2f) * s, h + nh / 2f);
            p.lineTo(10f * s, h);
            p.lineTo(r, h);
        }
        p.arcTo(new RectF(0, h - 2f * r, 2f * r, h), 90, 90);
        p.close();
    }

    private void renderContainerShape(Path p, float w, float h, float r, float nw, float nh, float m1, boolean isFinal, float s) {
        float topH = 40f * s;
        p.moveTo(0, r);
        p.lineTo(10f * s, 0);
        p.lineTo((10f + nw / 2f) * s, nh / 2f);
        p.lineTo((10f + nw) * s, 0);
        p.lineTo(w - r, 0);
        p.arcTo(new RectF(w - 2f * r, 0, w, 2f * r), -90, 90);
        p.lineTo(w, topH);
        p.lineTo((25f + nw) * s, topH);
        p.lineTo((25f + nw / 2f) * s, topH + nh / 2f);
        p.lineTo(25f * s, topH);
        p.lineTo(25f * s, topH + m1);
        p.lineTo((25f + nw / 2f) * s, topH + m1 + nh / 2f);
        p.lineTo((25f + nw) * s, topH + m1);
        p.lineTo(w, topH + m1);
        p.lineTo(w, h - r);
        p.arcTo(new RectF(w - 2f * r, h - 2f * r, w, h), 0, 90);
        if (isFinal) {
            p.lineTo(0, h);
        } else {
            p.lineTo((10f + nw) * s, h);
            p.lineTo((10f + nw / 2f) * s, h + nh / 2f);
            p.lineTo(10f * s, h);
            p.lineTo(r, h);
        }
        p.arcTo(new RectF(0, h - 2f * r, 2f * r, h), 90, 90);
        p.close();
    }

    private void renderMultiContainerShape(Path p, float w, float h, float r, float nw, float nh, float m1, float m2, boolean isFinal, float s) {
        float topH = 40f * s;
        float midH = 40f * s;
        p.moveTo(0, r);
        p.lineTo(10f*s, 0); p.lineTo((10f+nw/2f)*s, nh/2f); p.lineTo((10f+nw)*s, 0);
        p.lineTo(w-r, 0); p.arcTo(new RectF(w-2f*r, 0, w, 2f*r), -90, 90);
        p.lineTo(w, topH);
        p.lineTo((25f+nw)*s, topH); p.lineTo((25f+nw/2f)*s, topH+nh/2f); p.lineTo(25f*s, topH);
        p.lineTo(25f*s, topH + m1);
        p.lineTo((25f+nw/2f)*s, topH+m1+nh/2f); p.lineTo((25f+nw)*s, topH+m1);
        p.lineTo(w, topH + m1);
        p.lineTo(w, topH + m1 + midH);
        p.lineTo((25f+nw)*s, topH + m1 + midH); p.lineTo((25f+nw/2f)*s, topH+m1+midH+nh/2f); p.lineTo(25f*s, topH+m1+midH);
        p.lineTo(25f*s, topH + m1 + midH + m2);
        p.lineTo((25f+nw/2f)*s, topH+m1+midH+m2+nh/2f); p.lineTo((25f+nw)*s, topH+m1+midH+m2);
        p.lineTo(w, topH + m1 + midH + m2);
        p.lineTo(w, h - r);
        p.arcTo(new RectF(w-2f*r, h-2f*r, w, h), 0, 90);
        if (isFinal) {
            p.lineTo(0, h);
        } else {
            p.lineTo((10f + nw)*s, h); p.lineTo((10f+nw/2f)*s, h+nh/2f); p.lineTo(10f*s, h); p.lineTo(r, h);
        }
        p.arcTo(new RectF(0, h-2f*r, 2f*r, h), 90, 90);
        p.close();
    }

    private void renderValueShape(Path p, float w, float h, float r) {
        p.addRoundRect(new RectF(0, 0, w, h), h / 2f, h / 2f, Path.Direction.CW);
    }

    private void renderBooleanShape(Path p, float w, float h) {
        float side = h / 2f;
        p.moveTo(0, side);
        p.lineTo(side, 0);
        p.lineTo(w - side, 0);
        p.lineTo(w, side);
        p.lineTo(w - side, h);
        p.lineTo(side, h);
        p.close();
    }

    private void renderHeadShape(Path p, float w, float h, float r, float nw, float nh, float s) {
        p.moveTo(0, h - r);
        p.lineTo(0, 2f * r);
        p.arcTo(new RectF(0, 0, w, 4f * r), 180, 180);
        p.lineTo(w, h - r);
        p.arcTo(new RectF(w - 2f * r, h - 2f * r, w, h), 0, 90);
        p.lineTo((10f + nw) * s, h);
        p.lineTo((10f + nw / 2f) * s, h + nh / 2f);
        p.lineTo(10f * s, h);
        p.lineTo(r, h);
        p.arcTo(new RectF(0, h - 2f * r, 2f * r, h), 90, 90);
        p.close();
    }

    private void renderWrapperShape(Path p, float w, float h, float r) {
        p.addRoundRect(new RectF(0, 0, w, h), r, r, Path.Direction.CW);
    }
}
