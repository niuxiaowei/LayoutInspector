package com.mi.layoutinspector.replacemethod;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mi.layoutinspector.LayoutInspector;
import com.mi.layoutinspector.R;


/**
 * create by niuxiaowei
 * date : 21-8-23
 * 对LayoutInflater的inflate方法进行替换
 **/
public class LayoutInflaterProxy {

    public static final int TAG_KEY_LAYOUT_NAME = R.id.tag_key_layout_name;
    public static final int TAG_KEY_INFLATE_METHOD_NAME = R.id.tag_key_inflate_method;

    public static View inflate(LayoutInflater layoutInflater, int resource, ViewGroup root, Object[] objects) {
        Log.i("replacemethod", "layoutInflater#inflate(resource, root) replaced.  res:" + resource);
        View view = layoutInflater.inflate(resource, root);
        setTagInfoForView(view, resource, objects, LayoutInspector.Companion.getContext().getResources());
        return view;
    }

    public static View inflate(Context context, int resource, ViewGroup root, Object[] objects) {
        Log.i("replacemethod", "View#inflate(context,resource, root) replaced.  res:" + resource);
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

//        Log.i("replacemethod", "layoutname:" + layoutName + "  inflatermethodname:" + objects);

    }

    public static View inflate(LayoutInflater layoutInflater, int resource, ViewGroup root, boolean attachToRoot, Object[] objects) {
        Log.i("replacemethod", "layoutInflater#inflate(resource,root,attachToRoot) replaced.  res:" + resource);
        View view = layoutInflater.inflate(resource, root, attachToRoot);
        setTagInfoForView(view, resource, objects, LayoutInspector.Companion.getContext().getResources());
        return view;
    }

}
