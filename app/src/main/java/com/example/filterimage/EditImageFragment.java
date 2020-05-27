package com.example.filterimage;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.example.filterimage.interfaces.EditImageFragmentListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditImageFragment extends BottomSheetDialogFragment implements SeekBar.OnSeekBarChangeListener {

    private EditImageFragmentListener listener;
    private SeekBar seekBar_brightness,seekBar_contrast,seekBar_saturation;

    static EditImageFragment instance;

//    public static EditImageFragment getInstance(){
//        if (instance == null)
//            instance = new EditImageFragment();
//        return instance;
//    }

    public static EditImageFragment getInstance(){
        if(instance == null)
            instance = new EditImageFragment();
        return instance;
    }

    public void setListener(EditImageFragmentListener listener) {
        this.listener = listener;
    }

    public EditImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_edit_image, container, false);
        seekBar_brightness = view.findViewById(R.id.seekbar_brightness);
        seekBar_contrast = view.findViewById(R.id.seekbar_constrants);
        seekBar_saturation = view.findViewById(R.id.seekbar_saturation);

        seekBar_brightness.setMax(200);
        seekBar_brightness.setProgress(100);

        seekBar_contrast.setMax(20);
        seekBar_contrast.setProgress(0);

        seekBar_saturation.setMax(30);
        seekBar_saturation.setProgress(10);



        seekBar_saturation.setOnSeekBarChangeListener(this);
        seekBar_contrast.setOnSeekBarChangeListener(this);
        seekBar_brightness.setOnSeekBarChangeListener(this);


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(listener != null){
            if(seekBar.getId() == R.id.seekbar_brightness){
                listener.onBrightnessChange(progress -100);
            }else if(seekBar.getId() == R.id.seekbar_constrants){
                progress+=10;
                float value = .10f*progress;
                listener.onContrastChange(value);
            }else if(seekBar.getId() == R.id.seekbar_saturation){
                float value = .10f*progress;
                listener.onSaturationChange(value);
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if(listener != null)
            listener.onEditStarted();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
            if(listener != null)
                listener.onEditCompleted();;
    }

    public void resetControls(){
        seekBar_brightness.setProgress(100);
        seekBar_contrast.setProgress(0);
        seekBar_saturation.setProgress(16);
    }


}
