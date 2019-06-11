package com.example.steamaccountmanager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    FloatingActionButton floatingActionButton;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        floatingActionButton = view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment selectedFragment = null;
                selectedFragment = new AddAccountSteam();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).
                        addToBackStack(null).show(selectedFragment)
                        .detach(selectedFragment).attach(selectedFragment)
                        .commit();
            }
        });


        return view;
    }

}
