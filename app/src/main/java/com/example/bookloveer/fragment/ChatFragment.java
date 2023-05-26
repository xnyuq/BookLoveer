package com.example.bookloveer.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bookloveer.R;
import com.example.bookloveer.adapter.MessageAdapter;
import com.example.bookloveer.model.Mess;
import com.example.bookloveer.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private User component;
    private ArrayList<Mess> messageList;
    private MessageAdapter adapter;

    public ChatFragment() {
        // Required empty public constructor
    }
    public ChatFragment(User component) {
        this.component = component;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
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
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        TextView header = view.findViewById(R.id.header_textview);
        RecyclerView recyclerView = view.findViewById(R.id.messages_recyclerview);
        EditText messageEditText = view.findViewById(R.id.message_edittext);
        Button sendButton = view.findViewById(R.id.send_button);

        header.setText(component.getEmail());

        messageList = new ArrayList<>();
        adapter = new MessageAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // listen for new messages
        FirebaseDatabase.getInstance("https://book-lover-8bffc-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("messages")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                messageList.clear();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Mess message = dataSnapshot.getValue(Mess.class);
                                    if (message.getSenderId().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()) && message.getReceiverId().equals(component.getEmail())
                                            || message.getSenderId().equals(component.getEmail()) && message.getReceiverId().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                                        messageList.add(message);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get current user email

                Mess message = new Mess();
                message.setMessage(messageEditText.getText().toString());
                message.setSenderId(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                message.setReceiverId(component.getEmail());
                message.setTimestamp(System.currentTimeMillis());
                // save message to firebase

                FirebaseDatabase.getInstance("https://book-lover-8bffc-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("messages")
                        .push()
                        .setValue(message);
                messageEditText.setText("");
            }
        });


        return view;
    }
}