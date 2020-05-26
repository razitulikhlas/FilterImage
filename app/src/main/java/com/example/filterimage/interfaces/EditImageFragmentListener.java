package com.example.filterimage.interfaces;

public interface EditImageFragmentListener {
    void onBrightnessChange(int brightness);
    void onSaturationChange(float saturation);
    void onContrastChange(float contrast);
    void onEditStarted();
    void onEditCompleted();
}
