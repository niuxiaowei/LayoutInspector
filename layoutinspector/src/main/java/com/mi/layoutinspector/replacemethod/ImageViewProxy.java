package com.mi.layoutinspector.replacemethod;

import android.widget.ImageView;

import com.mi.layoutinspector.R;

public class ImageViewProxy {

    public static void setImageResource(ImageView imageView, int resId, Object[] objects) {
        imageView.setImageResource(resId);
        imageView.setTag(R.id.tag_key_iv_setImageResource, objects[0]+"#"+objects[1]+"#"+objects[3]+"行");

    }
}
