package com.mi.layoutinspector.inspect

import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.mi.layoutinspector.ActivityInspector

/**
 * Copyright (C) 2020, niuxiaowei. All rights reserved.
 * <p>
 * @author niuxiaowei
 * @date 2022/1/22.
 */
class PopupWindowInspectorsManagerCreator : IViewInspectorsManagerCreator {
    override fun createViewInspectorsManager(
        any: Any,
        activityInspector: ActivityInspector
    ): ViewInspectorsManager? {
        if (any is PopupWindow) {
            val contentView = any.contentView.parent
            if (contentView is ViewGroup) {
                return ViewInspectorsManager(
                    activityInspector.activity,
                    activityInspector,
                    contentView ,
                    contentView
                )
            }
        }
        return null
    }
}