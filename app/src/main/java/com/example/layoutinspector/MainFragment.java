package com.example.layoutinspector;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * create by niuxiaowei
 * date : 21-8-26
 **/
public class MainFragment extends Fragment {

    private AddToContentViewFragment addToContentViewFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                addToContentViewFragment = new AddToContentViewFragment();
                fragmentTransaction.add(android.R.id.content, addToContentViewFragment).commit();
            }
        });

        view.findViewById(R.id.bottom).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "hello bottom", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onBackPressed() {
        if (addToContentViewFragment != null) {
            FragmentManager manager = getActivity().getFragmentManager();
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.remove(addToContentViewFragment).commit();
            addToContentViewFragment = null;
        } else {
            getActivity().finish();
        }
    }


}
