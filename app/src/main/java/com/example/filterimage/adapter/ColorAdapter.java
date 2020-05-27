package com.example.filterimage.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filterimage.R;

import java.util.ArrayList;
import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {

    Context context;
    List<Integer> colorList;
    ColorAdapterListener listener;

    public ColorAdapter(Context context, ColorAdapterListener listener) {
        this.context = context;
        this.colorList = getColorList();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.color_item,parent,false);
        return new ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        holder.color_section.setCardBackgroundColor(colorList.get(position));
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    public class ColorViewHolder extends RecyclerView.ViewHolder{

        CardView color_section;

        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);
            color_section = itemView.findViewById(R.id.color_section);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onColorSelected(colorList.get(getAdapterPosition()));
                }
            });
        }
    }

    private List<Integer> getColorList() {
        List<Integer> colorList = new ArrayList<>();

        colorList.add(Color.parseColor("#956389"));
        colorList.add(Color.parseColor("#049853"));
        colorList.add(Color.parseColor("#023984"));
        colorList.add(Color.parseColor("#678549"));
        colorList.add(Color.parseColor("#565784"));
        colorList.add(Color.parseColor("#758475"));
        colorList.add(Color.parseColor("#046b00"));
        colorList.add(Color.parseColor("#f54000"));
        colorList.add(Color.parseColor("#8b0101"));
        colorList.add(Color.parseColor("#37595c"));
        colorList.add(Color.parseColor("#942d56"));
        colorList.add(Color.parseColor("#339ca8"));
        colorList.add(Color.parseColor("#2d946b"));
        colorList.add(Color.parseColor("#fde5b8"));

        colorList.add(Color.parseColor("#c1cefa"));
        colorList.add(Color.parseColor("#fad1c1"));
        colorList.add(Color.parseColor("#f69f9e"));
        colorList.add(Color.parseColor("#ffffff"));
        colorList.add(Color.parseColor("#00ff00"));
        colorList.add(Color.parseColor("#cd6ae8"));
        colorList.add(Color.parseColor("#ff00ff"));
        colorList.add(Color.parseColor("#0000cd"));
        colorList.add(Color.parseColor("#008080"));
        colorList.add(Color.parseColor("#eaa0a4"));
        colorList.add(Color.parseColor("#f4c2c2"));
        colorList.add(Color.parseColor("#fdd2cd"));


        colorList.add(Color.parseColor("#f69f9e"));
        colorList.add(Color.parseColor("#faedc1"));
        colorList.add(Color.parseColor("#c1cefa"));
        colorList.add(Color.parseColor("#fad1c1"));
        colorList.add(Color.parseColor("#fad1c1"));
        colorList.add(Color.parseColor("#c1cefa"));
        colorList.add(Color.parseColor("#faedc1"));
        colorList.add(Color.parseColor("#9b461f"));
        colorList.add(Color.parseColor("#d89f48"));
        colorList.add(Color.parseColor("#9d8c44"));
        colorList.add(Color.parseColor("#0b4fad"));
        colorList.add(Color.parseColor("#eaa0a4"));
        colorList.add(Color.parseColor("#fdd2cd"));
        colorList.add(Color.parseColor("#f69f9e"));



        return  colorList;
    }

    public interface ColorAdapterListener{
        void onColorSelected(int color);
    }
}
