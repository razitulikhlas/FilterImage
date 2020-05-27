package com.example.filterimage.interfaces;

public interface BrushFragmentListener {
    void onBrushSizeChangeListener(float size);
    void onBrushOpacityChangeListener(int opacity);
    void onBrushColorChangeListener(int color);
    void onBrushStateChangeListener(boolean isEraser);
}
