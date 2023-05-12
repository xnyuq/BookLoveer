package com.example.bookloveer.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookloveer.MainActivity;
import com.example.bookloveer.R;
import com.example.bookloveer.fragment.AddEditBookFragment;
import com.example.bookloveer.fragment.HomeFragment;
import com.example.bookloveer.model.Book;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class BookItemAdapter extends RecyclerView.Adapter<BookItemAdapter.BookItemViewHolder>{
    private List<Book> bookList;
    private DatabaseReference booksRef;
    private Context context;


    public BookItemAdapter(List<Book> bookList, Context context) {
        this.bookList = bookList;
        this.context = context;
        this.booksRef = FirebaseDatabase.getInstance("https://book-lover-8bffc-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("books");
    }

    @NonNull
    @Override
    public BookItemAdapter.BookItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item,parent,false);
        return new BookItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookItemAdapter.BookItemViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.bind(book);

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class BookItemViewHolder extends RecyclerView.ViewHolder{
        ImageView ivBook;
        TextView title, author, description;
        ImageView btnDelete, btnEdit, btnView;

        public BookItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBook = itemView.findViewById(R.id.book_thumbnail);
            title = itemView.findViewById(R.id.book_title);
            author = itemView.findViewById(R.id.author_name);
            description = itemView.findViewById(R.id.book_description);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnEdit = itemView.findViewById(R.id.btn_edit);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        String bookId = bookList.get(position).getId();
                        booksRef.child(bookId).removeValue();
                    }
                }
            });
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        Book book = bookList.get(position);
                        FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.homeContainer, AddEditBookFragment.newInstance(book));
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }
            });
        }
        public void bind(Book book){
            title.setText(book.getTitle());
            // set image from image url
            Glide.with(itemView.getContext())
                    .load(book.getImage())
                    .into(ivBook);
            author.setText(book.getAuthor());
            description.setText(book.getDescription());
        }
    }
}
