package com.mi.layoutinspector.utils

import android.view.View

/**
 * create by niuxiaowei
 * date : 22-1-24
 **/

/**
 * 查找包含LayoutInflaterProxy.TAG_KEY_LAYOUT_NAME tag的parent view
 * @param inspectorView View?
 * @return View? 找到包含LayoutInflaterProxy.TAG_KEY_LAYOUT_NAME tag的parent view 则返回，否则返回null
 */
fun findParentWithTag(inspectorView: View?, tagId: Int): View? {
    if (inspectorView == null || inspectorView.parent !is View) {
        return null
    }
    return if (inspectorView.getTag(tagId) != null) {
        inspectorView
    } else {
        findParentWithTag(inspectorView.parent as View, tagId)
    }
}