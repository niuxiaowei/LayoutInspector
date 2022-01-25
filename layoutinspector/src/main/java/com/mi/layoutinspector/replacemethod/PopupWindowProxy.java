package com.mi.layoutinspector.replacemethod;

import android.util.Log;
import android.view.View;
import android.widget.PopupWindow;

import com.mi.layoutinspector.LayoutInspector;

/**
 * Copyright (C) 2020, niuxiaowei. All rights reserved.
 * <p>
 *
 * @author niuxiaowei
 * @date 2022/1/22.
 * 对PopupWindow 方法进行替换
 */
public class PopupWindowProxy {
    public static void showAsDropDown(PopupWindow popupWindow, View anchor){
        Log.i("replacemethod", "popupWindow#showAsDropDown(anchor) replaced.  popupWindow:" + popupWindow);
        popupWindow.showAsDropDown(anchor);
        LayoutInspector.INSTANCE.startInspect(popupWindow);
    }
}
