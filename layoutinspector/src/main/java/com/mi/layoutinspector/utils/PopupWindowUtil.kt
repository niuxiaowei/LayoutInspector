package com.mi.layoutinspector.utils

import android.view.View
import android.widget.PopupWindow
import com.mi.layoutinspector.LayoutInspector

/**
 * Copyright (C) 2020, niuxiaowei. All rights reserved.
 * <p>
 * @author niuxiaowei
 * @date 2022/1/23.
 */
/**
 * 在使用popupwindow.showAtLocation（anchorView，xoffset, yoffset）方法显示的时候，计算xoffset, yoffset的值
 *
 * @param anchorView  在popupwindow显示的时候需要anchorView的windowtoken信息，popupwindow的大小以及具体的位置受制于decorView
 * @param popupHeight
 * @param popupWidth
 * @param decorView popupwindow的anchorView所对应的根viewAnchorView的位置
 * @param alignModeInLandscape 在横屏的时候popupwindow处于
 * @return IntArray 一个int数组，长度为2。  0：位置存放x坐标信息。  1：位置存放y坐标信息
 */
fun calculatePopWindowOffsets(
    anchorView: View,
    popupHeight: Int,
    popupWidth: Int,
    decorView: View?,
    alignModeInLandscape: PopupWindowAlignAnchorView? = PopupWindowAlignAnchorView.HORIZONTAL
): IntArray {
    val result = IntArray(2)
    val anchorViewLocation = IntArray(2)
    val decorViewLocation = IntArray(2)
    anchorView.getLocationOnScreen(anchorViewLocation)
    decorView?.getLocationOnScreen(decorViewLocation)
    val anchorHeight = anchorView.height
    val anchorWidth = anchorView.width

    if (screenIsPortrait(anchorView.context) || alignModeInLandscape == PopupWindowAlignAnchorView.VERTICAL) {
        // 竖屏的时候展示在anchor的上边或下边。判断需要在anchorView向上弹出还是向下弹出显示
        val isNeedShowUp =
            getScreenHeight() - anchorViewLocation[1] - anchorHeight < popupHeight
        result[0] =
            anchorViewLocation[0] - decorViewLocation[0] + anchorWidth / 2 - popupWidth / 2
        if (isNeedShowUp) {
            result[1] = anchorViewLocation[1] - decorViewLocation[1] - popupHeight
        } else {
            result[1] = anchorViewLocation[1] - decorViewLocation[1] + anchorHeight
        }
    } else {
        // 横屏的时候展示在anchor的左边或右边，判断需要左anchorView边还是右边弹出
        val isNeedShowRight =
            anchorViewLocation[0] + anchorWidth + popupWidth > getScreenWidth()
        if (isNeedShowRight) {
            result[0] = anchorViewLocation[0] - decorViewLocation[0] - popupWidth
        } else {
            result[0] = anchorViewLocation[0] - decorViewLocation[0] + anchorWidth
        }
        result[1] = anchorViewLocation[1] - decorViewLocation[1]
    }
    return result
}


/**
 * @return 返回popupwindow的size，size[0]:width   size[1]: height
 */
fun getPopupWindowSize(popupWindow: PopupWindow): IntArray {
    val size = IntArray(2)
    popupWindow.contentView?.apply {
        size[0] = measuredWidth
        size[1] = measuredHeight
    }
    return size
}

/**
 * 定义popupwindow显示在AnchorView的位置,VERTICAL表示popupwindow处于AnchorVie的上面或者下面，HORIZONTAL表示popupwindow处于AnchorVie的
 * 左边或者右边
 */
enum class PopupWindowAlignAnchorView {
    VERTICAL, HORIZONTAL
}