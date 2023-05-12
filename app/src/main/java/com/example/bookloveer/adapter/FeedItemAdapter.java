package com.example.bookloveer.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookloveer.R;
import com.example.bookloveer.fragment.BookDetailFragment;
import com.example.bookloveer.model.Book;
import com.example.bookloveer.model.Review;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class FeedItemAdapter extends RecyclerView.Adapter<FeedItemAdapter.ViewHolder> {

    private List<Review> mReviews;
    private Context mContext;
    private FirebaseUser mCurrentUser;

    public FeedItemAdapter(List<Review> reviews, Context context) {
        mReviews = reviews;
        mContext = context;
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = mReviews.get(position);

        getBook(review.getBook_id(), new BookCallback() {
            @Override
            public void onBookRetrieved(Book book, String imageUrl) {
                if (book != null) {
                    String displayName = "Anonymous";
                    if (review.getUser_id() != null) {
                        displayName = review.getUser_id();
                    }
                    holder.displayNameTextView.setText(displayName + " reviewed on " + book.getTitle());
                    holder.commentTextView.setText(review.getReview());
                    holder.ratingBar.setRating(review.getRating());
                    if (imageUrl != null)
                        Glide.with(mContext).load(imageUrl).into(holder.ivThumbnail);
                } else {
                    Log.w("FeedItemAdapter", "Book not found for review with ID " + review.getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView displayNameTextView;
        public TextView commentTextView;
        public RatingBar ratingBar;
        ImageView ivThumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            displayNameTextView = itemView.findViewById(R.id.desc);
            commentTextView = itemView.findViewById(R.id.comment);
            ratingBar = itemView.findViewById(R.id.ratingBar);

            ivThumbnail = itemView.findViewById(R.id.imageView);
            ivThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Review review = mReviews.get(position);
                        openBookDetailFragment(review.getBook_id());
                    }
                }
            });
        }
    }

    private void getBook(String bookId, BookCallback callback) {
        DatabaseReference booksRef = FirebaseDatabase.getInstance("https://book-lover-8bffc-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("books");
        ValueEventListener booksListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
                    Book book = bookSnapshot.getValue(Book.class);
                    if (book.getId().equals(bookId)) {
                        // Pass the retrieved book to the callback function
                        callback.onBookRetrieved(book, book.getImage());
                        return;
                    }
                }
                // Call the callback function with null if the book is not found
                callback.onBookRetrieved(null, null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("FeedItemAdapter", "getBook:onCancelled", databaseError.toException());
                // Call the callback function with null in case of error
                callback.onBookRetrieved(null, null);
            }
        };

        booksRef.addValueEventListener(booksListener);
    }

    public interface BookCallback {
        void onBookRetrieved(Book book, String image);
    }
    private void openBookDetailFragment(String bookId) {
        DatabaseReference booksRef = FirebaseDatabase.getInstance("https://book-lover-8bffc-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("books");

        ValueEventListener booksListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
                    Book book = bookSnapshot.getValue(Book.class);
                    if (book.getId().equals(bookId)) {
                        // Create a new instance of the BookDetailFragment and pass the selected book object
                        Fragment fragment = new BookDetailFragment(book);

                        // Replace the current fragment with the BookDetailFragment
                        FragmentTransaction transaction = ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.homeContainer, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("FeedItemAdapter", "getBook:onCancelled", databaseError.toException());
            }
        };

        booksRef.addValueEventListener(booksListener);
    }
}
