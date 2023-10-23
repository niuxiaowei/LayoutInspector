package com.example.layoutinspector;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.mi.layoutinspector.widget.CustomDialog;

import org.jetbrains.annotations.NotNull;


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

    @RequiresApi(api = Build.VERSION_CODES.M)
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

        view.findViewById(R.id.show_toast).setOnClickListener(v -> Toast.makeText(getContext(), "hello bottom", Toast.LENGTH_LONG).show());

        view.findViewById(R.id.show_dialog).setOnClickListener(v -> {
            Dialog dialog = new CustomDialog(getContext(), new CustomDialog.IOkClickListener() {
                @Override
                public void onOkClick(@NotNull String editMsg) {
                    Toast.makeText(getContext(), "ok click", Toast.LENGTH_LONG).show();
                }
            }, "我是一个对话框", "我设置了点击事件，点我");
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    Log.i("layoutinspector", "dialog dismiss");
                }
            });
            dialog.show();
        });


        view.findViewById(R.id.show_pupupwindow).setOnClickListener(v -> {
            showPopupWindow(v);
        });

        view.findViewById(R.id.show_pupupwindow1).setOnClickListener(v -> {
            showPopupWindow1(v);
        });

        view.findViewById(R.id.show_pupupwindow2).setOnClickListener(v -> {
            showPopupWindow2(v);
        });

        view.findViewById(R.id.show_pupupwindow3).setOnClickListener(v -> {
            showPopupWindow3(v);
        });

        view.findViewById(R.id.click3).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getContext(), "设置了长按事件的view", Toast.LENGTH_LONG).show();
                return true;
            }
        });

        ((ImageView) view.findViewById(R.id.image)).setImageResource(R.drawable.ic_launcher_background);
    }

    private void showPopupWindow(View anchor) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.popup_window, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(anchor);
    }

    private void showPopupWindow1(View anchor) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.popup_window, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(anchor, 20, 20);
    }

    private void showPopupWindow2(View anchor) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.popup_window, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(anchor, 30, 20, Gravity.LEFT);
    }

    private void showPopupWindow3(View anchor) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.popup_window, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(anchor, Gravity.LEFT, 100, 100);
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
