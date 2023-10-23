package com.example.layoutinspector;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * create by niuxiaowei
 * date : 21-10-28
 **/
public class MyView extends LinearLayout {

    public MyView(Context context) {
        super(context);
        inflate(context, R.layout.view_myview, this);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_myview, this);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_myview, this);
    }
}
