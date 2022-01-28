package com.mi.layoutinspector.viewinfos.viewattributes

import android.os.Build
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.mi.layoutinspector.inspector.IViewInspector
import com.mi.layoutinspector.utils.gravityToString

/**
 * Copyright (C) 2020, niuxiaowei. All rights reserved.
 * <p>
 * @author niuxiaowei
 * @date 2022/1/28.
 */
class RecyclerViewAttributeCollector : IViewAttributeCollector {
    override fun collectViewAttributes(
        inspectedView: View,
        IViewInspector: IViewInspector
    ): List<ViewAttribute>? {
        if (inspectedView is RecyclerView) {

            val result = arrayListOf<ViewAttribute>()
            result.add(ViewAttribute("adapter", inspectedView.adapter.javaClass.simpleName))
            inspectedView.layoutManager?.let {
                result.add(ViewAttribute("layoutManager", it.javaClass.simpleName))
            }
            return result
        }
        return null
    }
}