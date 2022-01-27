package com.mi.layoutinspector.viewinfos.viewattributes

import android.view.View

/**
 * create by niuxiaowei
 * date : 2021/7/30
 * 代表view的属性，比如宽 高，padding
 * @param name 显示在ViewAttributesPopupWindow 中的名字
 * @param value 显示在ViewAttributesPopupWindow 中的vlue
 * @param onClickListener 显示在ViewAttributesPopupWindow 是否设置点击事件
 **/
data class ViewAttribute(val name: String, val value: String, var onClickListener: View.OnClickListener? = null)
