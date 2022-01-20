package com.mi.layoutinspector

import android.util.TypedValue
import android.view.View

/**
 * Copyright (C) 2020, niuxiaowei. All rights reserved.
 * <p>
 * @author niuxiaowei
 * @date 2022/1/9.
 */

fun getDimension(value: Float): Int {
    return if (!LayoutInspector.unitsIsDP) {
        value.toInt()
    } else {
        px2dip(value).toInt()
    }
}

fun getDimensionWithUnitName(value: Float): String {
    val dimension = getDimension(value)
    return if (LayoutInspector.unitsIsDP) {
        "${dimension}dp"
    } else {
        "${dimension}px"
    }
}

fun getUnitStr(): String {
    return if (LayoutInspector.unitsIsDP) {
        "dp"
    } else {
        "px"
    }
}


fun dp2px(value: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, LayoutInspector.getDisplayMetrics());
}

fun px2dip(value: Float): Float {

    return value / LayoutInspector.getDisplayMetrics().density + 0.5f;
}

fun getSuperClass(child: Class<*>, superClass: Class<*>): Class<*>? {
    return if (child == superClass) {
        child
    } else getSuperClass(child.superclass, superClass)
}

fun getId(view: View): String? {
    try {
        val entryname: String = view.resources.getResourceEntryName(view.id)
        return "R.id.$entryname"
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}


