package com.example.filterimage;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.filterimage.adapter.ColorAdapter;
import com.example.filterimage.adapter.FontAdapter;
import com.example.filterimage.interfaces.AddTextFragmentListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.lang.reflect.Type;

/**
 * A simple {@link Fragment} subclass.
 */
public class TextAddFragment extends BottomSheetDialogFragment implements ColorAdapter.ColorAdapterListener, FontAdapter.FontAdapterClickListener {

    int colorSelected = Color.parseColor("#000000"); //Default color

    static TextAddFragment instance;
    AddTextFragmentListener listener;
    EditText edt_add_text;
    RecyclerView recycler_color,recycler_font;
    Button btn_done;

    Typeface typefaceSelected = Typeface.DEFAULT;

    public void setListener(AddTextFragmentListener listener) {
        this.listener = listener;
    }

    public static TextAddFragment getInstance(){
        if(instance == null)
            instance = new TextAddFragment();
        return instance;
    }

    public TextAddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_text_add, container, false);
        edt_add_text = view.findViewById(R.id.edt_add_text);
        recycler_color = view.findViewById(R.id.recyler_color);
        recycler_font = view.findViewById(R.id.recyler_font);
        btn_done = view.findViewById(R.id.btn_add_text);
        recycler_color.setHasFixedSize(true);
        recycler_color.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        recycler_font.setHasFixedSize(true);
        recycler_font.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));


        ColorAdapter adapter = new ColorAdapter(getContext(),this);
        recycler_color.setAdapter(adapter);

        FontAdapter fontAdapter = new FontAdapter(getContext(),this);
        recycler_font.setAdapter(fontAdapter);

        //Event
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAddTextButtonClick(typefaceSelected,edt_add_text.getText().toString(),colorSelected);
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onColorSelected(int color) {
        colorSelected = color; //set color when user select
    }

    @Override
    public void onFontSelected(String fontName) {
        typefaceSelected =Typeface.createFromAsset(getContext().getAssets(),new StringBuilder("fonts/")
                .append(fontName).toString());
    }
}
