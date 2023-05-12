package com.example.bookloveer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookloveer.R;
import com.example.bookloveer.model.Review;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ReviewItemAdapter extends RecyclerView.Adapter<ReviewItemAdapter.ViewHolder> {

    private List<Review> mReviews;
    private Context mContext;
    private FirebaseUser mCurrentUser;

    public ReviewItemAdapter(List<Review> reviews, Context context) {
        mReviews = reviews;
        mContext = context;
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = mReviews.get(position);

        // Set user display name
        String displayName = "Anonymous";

        if (review.getUser_id().equals(mCurrentUser.getUid())) {
            displayName = "You";
        }
        else if (review.getUser_id() != null) {
            displayName = review.getUser_id();
        }
        holder.displayNameTextView.setText(displayName);

        // Set rating and comment
        holder.commentTextView.setText(review.getReview());
        holder.ratingBar.setRating(review.getRating());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView displayNameTextView;
        public TextView commentTextView;
        public RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            displayNameTextView = itemView.findViewById(R.id.displayNameTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}
