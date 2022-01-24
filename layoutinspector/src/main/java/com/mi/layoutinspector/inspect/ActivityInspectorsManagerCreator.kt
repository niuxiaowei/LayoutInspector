package com.mi.layoutinspector.inspect

import android.app.Activity
import com.mi.layoutinspector.ActivityInspector
import com.mi.layoutinspector.inspect.IViewInspectorsManagerCreator
import com.mi.layoutinspector.utils.getContentViewForActivity

/**
 * Copyright (C) 2020, niuxiaowei. All rights reserved.
 * <p>
 * @author niuxiaowei
 * @date 2022/1/22.
 */
class ActivityInspectorsManagerCreator : IViewInspectorsManagerCreator {
    override fun createViewInspectorsManager(
        any: Any,
        activityInspector: ActivityInspector
    ): ViewInspectorsManager? {
        return if (any is Activity) {
            val contentView = getContentViewForActivity(any)
            ViewInspectorsManager(any, activityInspector, contentView, any.window.decorView)
        } else null

    }

}