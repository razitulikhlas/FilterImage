package com.example.filterimage.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filterimage.R;

import java.util.ArrayList;
import java.util.List;

public class FontAdapter extends RecyclerView.Adapter<FontAdapter.ViewHolder> {

    Context context;
    FontAdapterClickListener listener;
    List<String> fontList;

    int row_selected = -1;

    public FontAdapter(Context context, FontAdapterClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.fontList = loadFontList();
    }

    private List<String> loadFontList() {
        List<String> result = new ArrayList<>();
        result.add("Poppins-Black.ttf");
        result.add("Poppins-BlackItalic.ttf");

        return result;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.font_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(row_selected == position)
            holder.img_check.setVisibility(View.VISIBLE);
        else
            holder.img_check.setVisibility(View.INVISIBLE);

        Typeface typeface = Typeface.createFromAsset(context.getAssets(),new StringBuilder("fonts/")
        .append(fontList.get(position)).toString());

        holder.txt_font_name.setText(fontList.get(position));
        holder.txt_font_demo.setTypeface(typeface);

    }

    @Override
    public int getItemCount() {
        return fontList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_font_name,txt_font_demo;
        ImageView img_check;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_font_name = itemView.findViewById(R.id.txt_font_name);
            txt_font_demo = itemView.findViewById(R.id.txt_font_demo);
            img_check = itemView.findViewById(R.id.img_check);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onFontSelected(fontList.get(getAdapterPosition()));
                    row_selected = getAdapterPosition();
                    notifyDataSetChanged();

                }
            });
        }
    }

    public interface FontAdapterClickListener{
        public void onFontSelected(String fontName);
    }
}
