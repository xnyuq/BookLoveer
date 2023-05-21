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
import android.widget.SearchView;

import com.example.bookloveer.R;
import com.example.bookloveer.adapter.BookItemAdapter;
import com.example.bookloveer.adapter.ConvoAdapter;
import com.example.bookloveer.model.Book;
import com.example.bookloveer.model.Chat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConvoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConvoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SearchView convoSearchView;
    private List<Chat> chatList;
    private RecyclerView convoRecyclerView;
    private ConvoAdapter convoAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ConvoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConvoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConvoFragment newInstance(String param1, String param2) {
        ConvoFragment fragment = new ConvoFragment();
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
        View view = inflater.inflate(R.layout.fragment_convo, container, false);
        convoSearchView = view.findViewById(R.id.convoSearchView);
        convoRecyclerView = view.findViewById(R.id.convoRecyclerView);

        DatabaseReference convoRef = FirebaseDatabase.getInstance("https://book-lover-8bffc-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("convo");

        ValueEventListener booksListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList = new ArrayList<>();
                for (DataSnapshot chatSnapshot : dataSnapshot.getChildren()) {
                    Chat chat = chatSnapshot.getValue(Chat.class);
                    chatList.add(chat);
                }
                convoAdapter = new ConvoAdapter(chatList);
//                convoAdapter.setOnClickListener(new BookItemAdapter.OnClickListener() {
//                    @Override
//                    public void onClick(int position, Book book) {
//                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                        transaction.replace(R.id.homeContainer, new BookDetailFragment(book));
//                        transaction.addToBackStack(null);
//                        transaction.commit();
//                    }
//                });
                convoRecyclerView.setAdapter(convoAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("BooksActivity", "loadBooks:onCancelled", databaseError.toException());
            }
        };

        booksRef.addValueEventListener(booksListener);
        rvBook.setLayoutManager(new LinearLayoutManager(getActivity()));


        return view;
    }
}