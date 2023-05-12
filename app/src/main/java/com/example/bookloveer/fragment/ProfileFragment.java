package com.example.bookloveer.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bookloveer.R;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button btnLogout;
    TextView tvHello;

    public ProfileFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        btnLogout = view.findViewById(R.id.btnLogout);
        tvHello = view.findViewById(R.id.tvHello);
        tvHello.setText("Hello, " + mAuth.getCurrentUser().getEmail());

        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Fragment loginFragment = new LoginFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.homeContainer, loginFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }
}