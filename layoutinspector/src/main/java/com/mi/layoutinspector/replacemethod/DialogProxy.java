package com.mi.layoutinspector.replacemethod;

import android.app.Dialog;
import android.util.Log;

import com.mi.layoutinspector.LayoutInspector;
import com.mi.layoutinspector.widget.CustomDialog;

/**
 * Copyright (C) 2020, niuxiaowei. All rights reserved.
 * <p>
 *
 * @author niuxiaowei
 * @date 2022/1/22.
 * 对Dialog的方法进行替换
 */
public class DialogProxy {

    public static void show(Dialog dialog) {
        Log.i("replacemethod", "dialog#show() replaced.  dialog:" + dialog);
        dialog.show();
        LayoutInspector.Companion.startInspect(dialog);
    }

    public static void dismiss(Dialog dialog) {
        Log.i("replacemethod", "dialog#dismiss() replaced.  dialog:" + dialog);
        dialog.dismiss();
    }
}
