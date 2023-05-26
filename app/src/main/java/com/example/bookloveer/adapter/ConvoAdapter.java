package com.example.bookloveer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookloveer.R;
import com.example.bookloveer.model.Book;
import com.example.bookloveer.model.Chat;
import com.example.bookloveer.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ConvoAdapter extends RecyclerView.Adapter<ConvoAdapter.ConvoViewHolder> implements Filterable {

    private List<Chat> chatList;
    private List<Chat> filteredList;
    private OnItemClickListener mListener;

    public ConvoAdapter(List<Chat> chatList) {
        this.chatList = chatList;
        this.filteredList = new ArrayList<>(chatList);
    }

    @NonNull
    @Override
    public ConvoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.convo_item, parent, false);
        return new ConvoViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ConvoViewHolder holder, int position) {
        Chat currentChat = filteredList.get(position);
        holder.emailTextView.setText(currentChat.getEmail());
        holder.previewTextView.setText("");
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void setOnClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class ConvoViewHolder extends RecyclerView.ViewHolder {
        public TextView emailTextView;
        public TextView previewTextView;

        public ConvoViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            emailTextView = itemView.findViewById(R.id.emailText);
            previewTextView = itemView.findViewById(R.id.latestMessageText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchText = constraint.toString().toLowerCase(Locale.getDefault());
                filteredList = new ArrayList<>();
                if (searchText.isEmpty()) {
                    filteredList.addAll(chatList);
                } else {
                    for (Chat chat : chatList) {
                        if (chat.getEmail().toLowerCase(Locale.getDefault()).contains(searchText)) {
                            filteredList.add(chat);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<Chat>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
