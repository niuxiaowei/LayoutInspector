package com.mi.layoutinspector.utils

import android.content.Context
import com.mi.layoutinspector.LayoutInspector

/**
 * create by niuxiaowei
 * date : 22-1-27
 **/

private const val SP_FILE_NAME = "layout_inspector_sp_name"

fun saveBoolean(key: String, value: Boolean) {
    val sp = LayoutInspector.getContext().getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE)
    sp.edit().putBoolean(key, value).apply()
}

fun getBoolean(key: String, default: Boolean): Boolean {
    val sp = LayoutInspector.getContext().getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE)
    return sp.getBoolean(key, default)
}