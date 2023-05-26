package com.example.bookloveer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookloveer.R;
import com.example.bookloveer.model.Mess;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private ArrayList<Mess> messagesList;

    public MessageAdapter(ArrayList<Mess> messagesList) {
        this.messagesList = messagesList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Mess message = messagesList.get(position);

        holder.messageTextView.setText(message.getMessage());
        holder.senderTextView.setText(message.getSenderId());
        Long time = message.getTimestamp();
        holder.timestampTextView.setText(readableTime(time));
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }
    private String readableTime(long time) {
        Date date = new Date(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        return dateFormat.format(date);
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView messageTextView;
        private TextView senderTextView;
        private TextView timestampTextView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.message_textview);
            senderTextView = itemView.findViewById(R.id.sender_textview);
            timestampTextView = itemView.findViewById(R.id.timestamp_textview);
        }
    }
}
