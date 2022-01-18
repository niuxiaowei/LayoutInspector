package com.mi.layoutinspector

import android.content.Context
import android.util.DisplayMetrics

/**
 * Copyright (C) 2020, niuxiaowei. All rights reserved.
 * <p>
 * @author niuxiaowei
 * @date 2022/1/9.
 */

fun getDimension(value: Float): Float {
    return if (!LayoutInspector.unitsIsDP) {
        value
    } else {
        px2dip(value)
    }
}

fun getDimensionWithUnitName(value: Float): String {
    val dimension = getDimension(value)
    return if (LayoutInspector.unitsIsDP) {
        "${dimension.toInt()}dp"
    } else {
        "${dimension.toInt()}px"
    }
}

fun px2dip(value: Float): Float {

    return value / LayoutInspector.getDisplayMetrics().density + 0.5f;
}

fun getSuperClass(child: Class<*>, superClass: Class<*>): Class<*>? {
    return if (child == superClass) {
        child
    } else getSuperClass(child.superclass, superClass)
}


