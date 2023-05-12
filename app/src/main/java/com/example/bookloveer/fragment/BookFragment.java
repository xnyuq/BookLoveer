package com.example.bookloveer.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookloveer.R;
import com.example.bookloveer.adapter.BookItemAdapter;
import com.example.bookloveer.model.Book;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookFragment extends Fragment {

    RecyclerView rvBook;
    FloatingActionButton fabAddBook;


    public BookFragment() {
        // Required empty public constructor
    }
    public static BookFragment newInstance(String param1, String param2) {
        BookFragment fragment = new BookFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_book, container, false);
        if (container != null) {
            container.removeAllViews();
        }
        fabAddBook = view.findViewById(R.id.fab_add_book);
        rvBook = view.findViewById(R.id.rvBook);
        List<Book> bookList = new ArrayList<Book>();

        DatabaseReference booksRef = FirebaseDatabase.getInstance("https://book-lover-8bffc-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("books");
        ValueEventListener booksListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Book> bookList = new ArrayList<>();
                for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
                    Book book = bookSnapshot.getValue(Book.class);
                    bookList.add(book);
                }
                BookItemAdapter bookItemAdapter = new BookItemAdapter(bookList, getActivity());
                rvBook.setAdapter(bookItemAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("BooksActivity", "loadBooks:onCancelled", databaseError.toException());
            }
        };

        booksRef.addValueEventListener(booksListener);
        rvBook.setLayoutManager(new LinearLayoutManager(getActivity()));

        fabAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.homeContainer, new AddEditBookFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }

}