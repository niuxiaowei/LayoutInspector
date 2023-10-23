package com.example.layoutinspector;

import android.app.Fragment;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * create by niuxiaowei
 * date : 21-8-26
 * 通过                 fragmentTransaction.add(android.R.id.content, addToContentViewFragment).commit(); 这种方式添加fragment
 **/
public class AddToContentViewFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, null);
        return view;
    }
}
