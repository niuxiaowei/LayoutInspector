package com.mi.layoutinspector.replacemethod;

import android.util.Log;
import android.view.View;

import com.mi.layoutinspector.R;

/**
 * create by niuxiaowei
 * date : 22-1-27
 * view的onClick事件方法替换类
 **/
public class OnClickListenerProxy {

    public static void setOnClickListener(View view, View.OnClickListener clickListener, Object[] objects) {
        view.setOnClickListener(new ClickListenerWrapper(clickListener, objects));
        String className = objects[0] + "";
        String classSimpleName = className.substring(className.lastIndexOf(".") + 1);
        view.setTag(R.id.tag_key_click, classSimpleName + "#" + objects[1] + "#" + objects[3] + "行");
    }

    public static class ClickListenerWrapper implements View.OnClickListener {
        private View.OnClickListener listener;
        private Object[] params;

        public ClickListenerWrapper(View.OnClickListener listener, Object[] objects) {
            this.listener = listener;
            params = objects;
        }

        @Override
        public void onClick(View v) {
            String className = params[0] + "";
            String classSimpleName = className.substring(className.lastIndexOf(".") + 1);
            Log.i("LayoutInspector", "click info: (" + classSimpleName + ".java:" + params[3] + ")" + " or (" + classSimpleName + ".kt:" + params[3] + ")" + "   view:" + v + "  clickListener:" + listener);
            if (listener != null) {
                listener.onClick(v);
            }
        }
    }

    public static void setOnLongClickListener(View view, View.OnLongClickListener onLongClickListener, Object[] objects) {
        String className = objects[0] + "";
        String classSimpleName = className.substring(className.lastIndexOf(".") + 1);
        view.setTag(R.id.tag_key_long_click, classSimpleName + "#" + objects[1] + "#" + objects[3] + "行");
        view.setOnLongClickListener(new OnLongClickListenerWrapper(onLongClickListener, objects));
    }

    public static class OnLongClickListenerWrapper implements View.OnLongClickListener {
        private View.OnLongClickListener onLongClickListener;
        private Object[] params;

        public OnLongClickListenerWrapper(View.OnLongClickListener onLongClickListener, Object[] objects) {
            this.onLongClickListener = onLongClickListener;
            params = objects;
        }

        @Override
        public boolean onLongClick(View v) {
            String className = params[0] + "";
            String classSimpleName = className.substring(className.lastIndexOf(".") + 1);
            Log.i("LayoutInspector", "onLongClick info: (" + classSimpleName + ".java:" + params[3] + ")" + " or (" + classSimpleName + ".kt:" + params[3] + ")" + "   view:" + v + "  clickListener:" + onLongClickListener);
            return onLongClickListener.onLongClick(v);
        }
    }
}
