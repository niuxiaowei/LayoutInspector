package com.mi.layoutinspector.inspector

import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.mi.layoutinspector.utils.getLayoutName
import com.mi.layoutinspector.viewinfos.viewattributes.ComponentInfoCollector
import com.mi.layoutinspector.viewinfos.viewattributes.IViewAttributeCollector
import com.mi.layoutinspector.viewinfos.viewattributes.ViewAttribute

/**
 * create by niuxiaowei
 * date : 22-1-24
 * PopupWindow对应的检查器
 **/
class PopupWindowInspector(private val popupWindow: PopupWindow, activityInspector: ActivityInspector) : ComponentInspector() {
    init {
        val contentView = popupWindow.contentView.parent
        if (contentView is ViewGroup) {
            //activity的decorview作为popupwindow的decorview
            setContentDecorView(contentView, activityInspector.activity.window.decorView)
        }
    }

    override fun addTagsForView(view: View) {
        super.addTagsForView(view)
        view.apply {
            setTag(ComponentInfoCollector.TAG_COMPONENT_NAME, popupWindow.javaClass.simpleName)
            setTag(ComponentInfoCollector.TAG_COMPONENT_LAYOUT_NAME, getLayoutName(if (contentView?.childCount!! > 0) contentView?.getChildAt(0) else null))
        }
    }

}