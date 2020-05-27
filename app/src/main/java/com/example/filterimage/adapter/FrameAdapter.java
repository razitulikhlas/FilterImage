package com.example.filterimage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filterimage.R;

import java.util.ArrayList;
import java.util.List;

public class FrameAdapter extends RecyclerView.Adapter<FrameAdapter.ViewHolder> {

    Context context;
    List<Integer> frameList;
    FrameAdapterListener listener;

    int row_selected = -1;

    public FrameAdapter(Context context, FrameAdapterListener listener) {
        this.context = context;
        this.frameList = getFrameList();
        this.listener = listener;
    }

    private List<Integer> getFrameList() {
        List<Integer>  result = new ArrayList<>();
        result.add(R.drawable.card_1_resize);
        result.add(R.drawable.card_2_resize);
        result.add(R.drawable.card_3_resize);
        result.add(R.drawable.card_4_resize);
        result.add(R.drawable.card_5_resize);
        result.add(R.drawable.card_6_resize);
        result.add(R.drawable.card_7_resize);
        result.add(R.drawable.card_8_resize);
        result.add(R.drawable.card_9_resize);
        result.add(R.drawable.card_10_resize);

        return result;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.frame_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(row_selected == position)
            holder.img_check.setVisibility(View.VISIBLE);
        else
            holder.img_check.setVisibility(View.INVISIBLE);


        holder.img_frame.setImageResource(frameList.get(position));
    }

    @Override
    public int getItemCount() {
        return frameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_check,img_frame;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_check = itemView.findViewById(R.id.img_check);
            img_frame = itemView.findViewById(R.id.img_frame);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onFrameSelected(frameList.get(getAdapterPosition()));
                    row_selected = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }

    public interface FrameAdapterListener{
        void onFrameSelected(int frame);
    }

}
