package com.example.bookloveer.fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookloveer.R;
import com.example.bookloveer.adapter.FeedItemAdapter;
import com.example.bookloveer.model.Review;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FeedFragment extends Fragment {
    private RecyclerView rvFeed;
    private List<Review> reviewList = new ArrayList<>();
    private FeedItemAdapter reviewAdapter;

    private DatabaseReference reviewRef;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get reference to Firebase database
        reviewRef = FirebaseDatabase.getInstance("https://book-lover-8bffc-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("review");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        // Set up RecyclerView
        rvFeed = view.findViewById(R.id.rvFeed);
        reviewAdapter = new FeedItemAdapter(reviewList, getContext());
        rvFeed.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFeed.setAdapter(reviewAdapter);

        // Attach ValueEventListener to update reviews
        reviewRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reviewList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Review review = snapshot.getValue(Review.class);
                    reviewList.add(review);
                }
                Collections.reverse(reviewList); // Show most recent reviews first
                reviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });

        return view;
    }

}