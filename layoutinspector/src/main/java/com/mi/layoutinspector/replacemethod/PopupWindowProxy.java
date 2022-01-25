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
    public static void showAsDropDown(PopupWindow popupWindow, View anchor) {
        Log.i("replacemethod", "popupWindow#showAsDropDown(anchor) replaced.  popupWindow:" + popupWindow);
        popupWindow.showAsDropDown(anchor);
        LayoutInspector.INSTANCE.startInspect(popupWindow);
    }

    public static void showAsDropDown(PopupWindow popupWindow, View anchor, int xoff, int yoff) {
        Log.i("replacemethod", "popupWindow#showAsDropDown(anchor,xoff,yoff) replaced.  popupWindow:" + popupWindow);
        popupWindow.showAsDropDown(anchor, xoff, yoff);
        LayoutInspector.INSTANCE.startInspect(popupWindow);
    }

    public static void showAsDropDown(PopupWindow popupWindow, View anchor, int xoff, int yoff, int gravity) {
        Log.i("replacemethod", "popupWindow#showAsDropDown(anchor,xoff,yoff,gravity) replaced.  popupWindow:" + popupWindow);
        popupWindow.showAsDropDown(anchor, xoff, yoff, gravity);
        LayoutInspector.INSTANCE.startInspect(popupWindow);
    }

    public static void showAtLocation(PopupWindow popupWindow, View parent, int gravity, int x, int y) {
        Log.i("replacemethod", "popupWindow#showAtLocation(parent,gravity,x,y) replaced.  popupWindow:" + popupWindow);
        popupWindow.showAtLocation(parent, gravity, x, y);
        LayoutInspector.INSTANCE.startInspect(popupWindow);
    }
}
