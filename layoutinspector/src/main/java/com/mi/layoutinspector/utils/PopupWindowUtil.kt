package com.mi.layoutinspector.utils

import android.view.View
import android.widget.PopupWindow
import com.mi.layoutinspector.LayoutInspector
import com.mi.layoutinspector.R

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
    decorView: View
): IntArray {
    val result = IntArray(2)
    val anchorViewLocation = IntArray(2)
    anchorView.getLocationInWindow(anchorViewLocation)

    val popMarginTop = decorView.context.resources.getDimensionPixelSize(R.dimen.inspector_pop_margin_top)

    //显示在 anchorView的下面
    if (anchorViewLocation[1] + anchorView.height + popupHeight <= decorView.height) {
        result[0] = ((anchorViewLocation[0] + anchorView.width / 2) - popupWidth/2)
        result[1] = (anchorViewLocation[1] + anchorView.height)
    } else if (anchorViewLocation[1] >= popupHeight + popMarginTop) {
        //显示在 anchorView的上面
        result[0] = ((anchorViewLocation[0] + anchorView.width / 2) - popupWidth/2)
        result[1] = anchorViewLocation[1] - popupHeight
    } else if (anchorViewLocation[0] >= popupWidth) {
        //显示在left
        result[0] = (anchorViewLocation[0] - popupWidth)
        result[1] = anchorViewLocation[1] + popMarginTop
    } else if (anchorViewLocation[0] + anchorView.width + popupWidth <= decorView.width) {
        //显示在right
        result[0] = anchorViewLocation[0] + anchorView.width
        result[1] = anchorViewLocation[1] + popMarginTop
    } else {
        //显示在中间
        result[0] = ((anchorViewLocation[0] + anchorView.width / 2) - popupWidth/2)
        result[1] = anchorViewLocation[1] + popMarginTop
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