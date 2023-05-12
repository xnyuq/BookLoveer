package com.example.bookloveer.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bookloveer.R;
import com.example.bookloveer.adapter.ReviewItemAdapter;
import com.example.bookloveer.model.Book;
import com.example.bookloveer.model.Review;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Book book;

    public BookDetailFragment() {
        // Required empty public constructor
    }

    public BookDetailFragment(Book book) {
        this.book = book;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookDetailFragment newInstance(String param1, String param2) {
        BookDetailFragment fragment = new BookDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_detail, container, false);
        if (container != null) {
            container.removeAllViews();
        }

        // Set up views
        TextView bookTitle = view.findViewById(R.id.bookTitle);
        bookTitle.setText(book.getTitle());

        TextView bookAuthor = view.findViewById(R.id.bookAuthor);
        bookAuthor.setText("Author: " + book.getAuthor());

        TextView bookDescription = view.findViewById(R.id.bookDescription);
        bookDescription.setText(book.getDescription());

        ImageView bookThumb = view.findViewById(R.id.bookThumb);
        Glide.with(getContext()).load(book.getImage()).into(bookThumb);

        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        EditText reviewComment = view.findViewById(R.id.reviewComment);
        Button reviewSubmit = view.findViewById(R.id.reviewSubmit);
        RecyclerView reviewList = view.findViewById(R.id.reviewList);

        // Set up adapter for review list

        DatabaseReference bookReviewRef = FirebaseDatabase.getInstance("https://book-lover-8bffc-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("review");

        ValueEventListener reviewListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Review> reviews = new ArrayList<>();
                for (DataSnapshot reviewSnapshot : dataSnapshot.getChildren()) {
                    Review review = reviewSnapshot.getValue(Review.class);
                    if (review.getBook_id().equals(book.getId())) {
                        reviews.add(review);
                    }
                }
                ReviewItemAdapter reviewItemAdapter = new ReviewItemAdapter(reviews, getActivity());
                reviewList.setAdapter(reviewItemAdapter);
                // Calculate overall rating
                float totalRating = 0;
                int numReviews = 0;
                for (DataSnapshot reviewSnapshot : dataSnapshot.getChildren()) {
                    Review review = reviewSnapshot.getValue(Review.class);
                    if (review.getBook_id().equals(book.getId())) {
                        totalRating += review.getRating();
                        numReviews++;
                    }
                }
                if(numReviews > 0) {
                    float overallRating = totalRating / numReviews;

                    // Set overall rating TextView
                    TextView overallRatingTextView = view.findViewById(R.id.overallRating);
                    overallRatingTextView.setText("Overall Rating: " + overallRating);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("BooksActivity", "loadBooks:onCancelled", databaseError.toException());
            }
        };

        bookReviewRef.addValueEventListener(reviewListener);
        reviewList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set up submit button listener
        reviewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = reviewComment.getText().toString();
                float rating = ratingBar.getRating();

                String user_id = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                // Create new review object and add it to the list
                String reviewId = bookReviewRef.push().getKey();
                Review newReview = new Review(reviewId, rating, comment, user_id, book.getId());
                bookReviewRef.child(reviewId).setValue(newReview);


                // Clear comment field and reset rating bar
                reviewComment.setText("");
                ratingBar.setRating(0);
            }
        });

        return view;
    }
}
