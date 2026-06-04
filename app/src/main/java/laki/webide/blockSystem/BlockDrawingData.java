package laki.webide.blockSystem;

import android.graphics.Path;

/**
 * Metadata used by the Rendering Engine to physically draw the block.
 * Stores dimensions, paths, and notch locations.
 */
public class BlockDrawingData {
    public int width;
    public int height;
    public int color;
    public BlockShape shape;
    
    // Scale factor for different screen densities
    public float scale = 1.0f;
    
    // Dynamic height of the inner areas (mouths)
    public int mouth1Height = 40;
    public int mouth2Height = 40;
    
    // Whether this is a terminal block (no bottom notch)
    public boolean isFinal = false;
    
    // Geometry paths for the background
    public Path backgroundPath = new Path();
    
    // Standard notch dimensions (Base values before scaling)
    public static final float BASE_NOTCH_WIDTH = 20f;
    public static final float BASE_NOTCH_HEIGHT = 10f;
    public static final float BASE_CORNER_RADIUS = 8f;

    public BlockDrawingData(BlockShape shape, int color, float scale) {
        this.shape = shape;
        this.color = color;
        this.scale = scale;
        this.mouth1Height = (int) (40 * scale);
        this.mouth2Height = (int) (40 * scale);
    }
}
