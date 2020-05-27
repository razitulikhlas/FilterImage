package com.example.filterimage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filterimage.R;

import java.util.List;

import io.github.rockerhieu.emojicon.EmojiconTextView;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.ViewHolder> {
    Context context;
    List<String> emojiList;
    EmojiAdapterListener listener;

    public EmojiAdapter(Context context, List<String> emojiList, EmojiAdapterListener listener) {
        this.context = context;
        this.emojiList = emojiList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.emoji_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.emojiconTextView.setText(emojiList.get(position));
    }

    @Override
    public int getItemCount() {
        return emojiList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EmojiconTextView emojiconTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            emojiconTextView = itemView.findViewById(R.id.emoji_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onEmojiItemSelected(emojiList.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface EmojiAdapterListener{
        void onEmojiItemSelected(String emoji);
    }
}
