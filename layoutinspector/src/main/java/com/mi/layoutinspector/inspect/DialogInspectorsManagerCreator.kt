package com.mi.layoutinspector.inspect

import android.app.Activity
import android.app.Dialog
import com.mi.layoutinspector.ActivityInspector
import com.mi.layoutinspector.utils.getContentViewForActivity
import com.mi.layoutinspector.utils.getContentViewForDialog

/**
 * Copyright (C) 2020, niuxiaowei. All rights reserved.
 * <p>
 * @author niuxiaowei
 * @date 2022/1/22.
 */
class DialogInspectorsManagerCreator : IViewInspectorsManagerCreator {
    override fun createViewInspectorsManager(
        any: Any,
        activityInspector: ActivityInspector
    ): ViewInspectorsManager? {
        return if (any is Dialog) {
            val contentView = getContentViewForDialog(any)
            ViewInspectorsManager(
                activityInspector.activity,
                activityInspector,
                contentView,
                any.window?.decorView
            )
        } else null
    }
}