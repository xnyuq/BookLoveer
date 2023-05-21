package com.example.bookloveer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bookloveer.R;
import com.example.bookloveer.model.Chat;

import java.util.List;

public class ConvoAdapter extends RecyclerView.Adapter<ConvoAdapter.ViewHolder> {

    private List<Chat> chats;

    public ConvoAdapter(List<Chat> chats) {
        this.chats = chats;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.convo_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Chat chat = chats.get(position);

        //holder.profileImage.setImageResource(chat.getProfileImageResId());
        holder.emailText.setText(chat.getEmail());
        holder.latestMessageText.setText(chat.getLatestMessagePreview());
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImage;
        TextView emailText;
        TextView latestMessageText;

        ViewHolder(View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            emailText = itemView.findViewById(R.id.emailText);
            latestMessageText = itemView.findViewById(R.id.latestMessageText);
        }
    }
}
