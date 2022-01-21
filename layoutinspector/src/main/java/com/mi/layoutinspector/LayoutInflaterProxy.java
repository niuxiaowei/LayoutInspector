package com.mi.layoutinspector;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mi.layoutinspector.R;


/**
 * create by niuxiaowei
 * date : 21-8-23
 **/
public class LayoutInflaterProxy {

    public static final int TAG_KEY_LAYOUT_NAME = R.id.tag_key_layout_name;
    public static final int TAG_KEY_INFLATE_METHOD_NAME = R.id.tag_key_inflate_method;

    public static View inflate(LayoutInflater layoutInflater, int resource, ViewGroup root, Object[] objects) {
        View view = layoutInflater.inflate(resource, root);
        setTagInfoForView(view, resource, objects, LayoutInspector.Companion.getContext().getResources());
        return view;
    }

    public static View inflate(Context context, int resource, ViewGroup root, Object[] objects) {
        View view = View.inflate(context,resource, root);
        setTagInfoForView(view, resource, objects, LayoutInspector.Companion.getContext().getResources());
        return view;
    }

    private static void setTagInfoForView(View inflatedView, int resource,  Object[] objects, Resources resources) {
        if (inflatedView == null) {
            return;
        }
        String layoutName = "";
        try {
            layoutName = resources.getResourceEntryName(resource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        inflatedView.setTag(TAG_KEY_LAYOUT_NAME, layoutName);
        inflatedView.setTag(TAG_KEY_INFLATE_METHOD_NAME, objects[0]+"#"+objects[1]+"#"+objects[3]+"行");

        Log.i("LayoutInflaterImpl", "layoutname:" + layoutName + "  inflatermethodname:" + objects);

    }

    public static View inflate(LayoutInflater layoutInflater, int resource, ViewGroup root, boolean attachToRoot, Object[] objects) {
        Log.i("LayoutInflaterImpl", "inflate3 res:" + resource);
        View view = layoutInflater.inflate(resource, root, attachToRoot);
        setTagInfoForView(view, resource, objects, LayoutInspector.Companion.getContext().getResources());
        return view;
    }

    public static void show(Dialog dialog) {
        Log.i("LayoutInflaterImpl", "Dialog show:" + dialog);
        dialog.show();
    }

}
