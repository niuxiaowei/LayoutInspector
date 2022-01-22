package com.mi.layoutinspector.utils

import android.app.Activity
import android.app.Dialog
import android.content.ContextWrapper
import android.view.ViewGroup

/**
 * Copyright (C) 2020, niuxiaowei. All rights reserved.
 * <p>
 * @author niuxiaowei
 * @date 2022/1/22.
 */

fun getActivityFromDialog(dialog: Dialog): Activity? {

    val context = dialog.context
    return if (context is ContextWrapper && context.baseContext is Activity) {
        context.baseContext as Activity
    } else {
        null
    }
}

fun getContentViewForActivity(activity: Activity): ViewGroup {
    return activity.window.decorView.findViewById(android.R.id.content)
}

fun getContentViewForDialog(dialog: Dialog): ViewGroup? {
    return dialog.window?.decorView?.findViewById(android.R.id.content)
}