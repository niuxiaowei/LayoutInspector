package com.mi.layoutinspector.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import com.mi.layoutinspector.LayoutInspector

/**
 * create by niuxiaowei
 * date : 22-1-20
 **/
/**
 * 屏幕是否竖屏
 * @param activity Activity
 * @return Boolean
 */
fun screenIsPortrait(context: Context): Boolean {
    return context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
}

fun getScreenWidth(): Int {
    return LayoutInspector.getDisplayMetrics().widthPixels
}

fun getScreenHeight(): Int {
    return LayoutInspector.getDisplayMetrics().heightPixels
}