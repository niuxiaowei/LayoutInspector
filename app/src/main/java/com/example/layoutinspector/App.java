package com.example.layoutinspector;

import android.app.AlertDialog;
import android.app.Application;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mi.layoutinspector.LayoutInspector;
import com.mi.layoutinspector.viewinfos.viewattributes.IViewAttributeCollector;
import com.mi.layoutinspector.viewinfos.viewattributes.ViewAttribute;
import com.mi.layoutinspector.inspect.ViewInspector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * create by niuxiaowei
 * date : 21-8-24
 **/
public class App extends Application {

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        LayoutInspector.Companion.regist(new IViewAttributeCollector() {
            @Nullable
            @Override
            public List<ViewAttribute> collectViewAttributes(@NotNull View inspectView, @NotNull ViewInspector viewInspector) {
                return null;
            }

            @Nullable
            @Override
            public ViewAttribute collectViewAttribute(@NotNull View inspectView, @NotNull ViewInspector viewInspector) {
                if (inspectView instanceof TextView) {
                    ViewAttribute viewAttribute = new ViewAttribute("修改TextView内容", "点击进行修改", v -> {
                        viewInspector.hideViewInfosPopupWindown();
                        TextView textView = (TextView) inspectView;
                        final EditText editText = new EditText(inspectView.getContext());
                        AlertDialog.Builder inputDialog =
                                new AlertDialog.Builder(inspectView.getContext());
                        inputDialog.setTitle("输入内容").setView(editText);
                        inputDialog.setPositiveButton("确定修改",
                                (dialog, which) -> {
                                    String msg = editText.getText().toString();
                                    if (!TextUtils.isEmpty(msg)) {
                                        textView.setText(msg);
                                    }
                                }).show();
                    });
                    return viewAttribute;
                }
                return null;

            }
        });
    }
}
