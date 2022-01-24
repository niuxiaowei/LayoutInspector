package com.mi.layoutinspector.utils

import android.content.Context
import android.os.Build
import android.view.View

/**
 * create by niuxiaowei
 * date : 22-1-24
 **/

fun idToString(id: Int, context: Context): String? {
    return if (id > 0) {
        "R.${context.resources.getResourceTypeName(id)}.${context.resources.getResourceEntryName(id)}"
    } else {
        null
    }
}

fun getLayoutName(view: View?): String? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        try {
            val layoutId = view?.sourceLayoutResId
            if (layoutId != null) {
                if (layoutId > 0) {
                    return idToString(layoutId, view.context)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return null
}