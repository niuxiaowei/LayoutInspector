package com.mi.layoutinspector.viewinfos;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.mi.layoutinspector.inspect.InspectItemView;
import com.mi.layoutinspector.LayoutInspector;
import com.mi.layoutinspector.R;
import com.mi.layoutinspector.viewinfos.viewattributes.IViewAttributeCollector;
import com.mi.layoutinspector.viewinfos.viewattributes.ViewAttribute;

import java.util.ArrayList;
import java.util.List;


/**
 * create by niuxiaowei
 * date : 21-7-22
 * 显示view信息的popupwindow， view信息有 宽，高， id， parent，child,  view层级
 **/
public class ViewInfosPopupWindow {


    private PopupWindow realPopupWindow;
    private ViewAttributesAdapter adapter;
    private PopupWindow.OnDismissListener onDismissListener;
    private boolean isNotifyDismissEvent = true;

    private final Handler handler = new Handler();
    private static int sPopupWindowHeight = LayoutInspector.Companion.getScreenHeight() / 2;

    public ViewInfosPopupWindow(PopupWindow.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    /**
     * 因此viewinfos
     */
    public void hideViewInfos() {
        if (realPopupWindow == null) {
            return;
        }
        realPopupWindow.dismiss();
    }


    private void initRealPopupWindow(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layoutinspector_popupwindow_detail_view, null);
        RecyclerView recyslerview = view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(context);
        recyslerview.setLayoutManager(layoutmanager);
        adapter = new ViewAttributesAdapter();
        recyslerview.setAdapter(adapter);
        realPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, sPopupWindowHeight) {};
        realPopupWindow.setOutsideTouchable(true);
        realPopupWindow.setFocusable(true);
        realPopupWindow.setOnDismissListener(() -> {
            if (onDismissListener != null && isNotifyDismissEvent) {
                onDismissListener.onDismiss();
            }
        });
    }

    /**
     * 显示view信息
     * @param context
     * @param inspectedView   被检测器检测的view
     * @param inspectItemView
     */
    public void showViewInfos(Context context, View inspectedView, InspectItemView inspectItemView) {
        if (realPopupWindow == null) {
            initRealPopupWindow(context);
        }

        //设置数据
        adapter.setDatas(createViewAttributes(inspectedView, inspectItemView));

        handler.postDelayed(() -> {
            int[] size = getPopupWindowSize();
            //隐藏再显示
            isNotifyDismissEvent = false;
            realPopupWindow.dismiss();
            isNotifyDismissEvent = true;
            int[] popupWindowPos = calculatePopWindowPos(inspectedView, size[1], size[0]);
            realPopupWindow.showAtLocation(inspectedView, Gravity.TOP, popupWindowPos[0], popupWindowPos[1]);
            realPopupWindow.getContentView().setVisibility(View.VISIBLE);
        }, 100);

        //以下代码主要是解决popupwindow显示在anchorView位置不对的问题，主要原因是set了数据后，这时候直接获取popupwindow的高度获取不到正确值导致的，因此先把正确值获取到后，在延迟显示
        realPopupWindow.getContentView().setVisibility(View.INVISIBLE);
        //先随便给一个0 0 的坐标主要为了获取正确的高度
        realPopupWindow.showAtLocation(inspectedView, Gravity.TOP | Gravity.START, 0, 0);
    }

    /**
     * @return 返回popupwindow的size，size[0]:width   size[1]: height
     */
    private int[] getPopupWindowSize() {
        int[] size = new int[2];
        size[0] = realPopupWindow.getContentView().getMeasuredWidth();
        size[1] = realPopupWindow.getContentView().getMeasuredHeight();
        return size;
    }

    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     *
     * @param anchorView   呼出window的view
     * @param windowHeight
     * @param windowWidth
     * @return window显示的左上角的xOff, yOff坐标
     */
    private static int[] calculatePopWindowPos(final View anchorView, final int windowHeight, final int windowWidth) {
        final int[] windowPos = new int[2];
        final int[] anchorLoc = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        final int anchorWidth = anchorView.getWidth();
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (LayoutInspector.Companion.getScreenHeight() - anchorLoc[1] - anchorHeight < windowHeight);
        if (isNeedShowUp) {
            windowPos[0] = anchorLoc[0] + anchorWidth / 2 - windowWidth / 2;
            windowPos[1] = anchorLoc[1] - windowHeight;
        } else {
            windowPos[0] = anchorLoc[0] + anchorWidth / 2 - windowWidth / 2;
            windowPos[1] = anchorLoc[1] + anchorHeight;
        }
        return windowPos;
    }

    private List<ViewAttribute> createViewAttributes(View inspectView, InspectItemView inspectItemView) {
        List<ViewAttribute> viewAttributes = new ArrayList<>();
        List<IViewAttributeCollector> collectors = LayoutInspector.Companion.getViewAttributesCollectors();
        for (int i = 0; i < collectors.size(); i++) {
            IViewAttributeCollector viewDetailCollector = collectors.get(i);
            ViewAttribute viewAttribute = viewDetailCollector.collectViewAttribute(inspectView, inspectItemView);
            if (viewAttribute != null) {
                viewAttributes.add(viewAttribute);
            }
            List<ViewAttribute> collectViewAttributes = viewDetailCollector.collectViewAttributes(inspectView, inspectItemView);
            if (collectViewAttributes != null && collectViewAttributes.size() > 0) {
                viewAttributes.addAll(collectViewAttributes);
            }
        }
        return viewAttributes;
    }

}



