package com.example.filterimage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.filterimage.adapter.FrameAdapter;
import com.example.filterimage.interfaces.AddFrameListener;
import com.example.filterimage.interfaces.AddTextFragmentListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class FrameFragment extends BottomSheetDialogFragment implements FrameAdapter.FrameAdapterListener {
    static FrameFragment instance;
    RecyclerView recycler_frame;
    Button btn_add_frame;

    int frame_selected = -1;
    AddFrameListener listener;

    public void setListener(AddFrameListener listener) {
        this.listener = listener;
    }

    public static FrameFragment getInstance(){
        if(instance == null)
            instance = new FrameFragment();
        return instance;
    }


    public FrameFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_frame, container, false);
        recycler_frame = view.findViewById(R.id.recycler_frame);
        btn_add_frame = view.findViewById(R.id.btn_add_frame);

        recycler_frame.setHasFixedSize(true);
        recycler_frame.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recycler_frame.setAdapter(new FrameAdapter(getContext(),this));

        btn_add_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAddFrame(frame_selected);
            }
        });
        return view;
    }

    @Override
    public void onFrameSelected(int frame) {
     frame_selected = frame;
    }
}
