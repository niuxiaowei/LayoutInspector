package com.mi.layoutinspector.viewinfos.viewattributes

import android.view.View
import com.mi.layoutinspector.R
import com.mi.layoutinspector.inspector.IViewInspector
import com.mi.layoutinspector.replacemethod.LayoutInflaterProxy
import com.mi.layoutinspector.utils.findParentWithTag

/**
 * create by niuxiaowei
 * date : 22-1-24
 * 收集组件(activity，fragment，dialog，popupwindow)的一些信息，比如当前activity的类名，activity对应的layout信息， dialog类名，layout文件名； popupwindow类名及layout文件吗
 **/
class ComponentInfoCollector : IViewAttributeCollector {
    companion object {
        val TAG_ACTIVITY_NAME = R.id.tag_key_activity_name
        val TAG_ACTIVITY_LAYOUT_NAME = R.id.tag_key_activity_layout_name
        val TAG_COMPONENT_NAME = R.id.tag_key_component_name
        val TAG_COMPONENT_LAYOUT_NAME = R.id.tag_key_component_layout_name
    }

    override fun collectViewAttributes(inspectedView: View, IViewInspector: IViewInspector): List<ViewAttribute>? {
        val result = arrayListOf<ViewAttribute>()
        val parentView = findParentWithTag(inspectedView, TAG_ACTIVITY_NAME)
        parentView?.apply {
            getTag(TAG_ACTIVITY_NAME)?.let {
                result.add(ViewAttribute("activity", it.toString()))
            }
            getTag(TAG_ACTIVITY_LAYOUT_NAME)?.let {
                result.add(ViewAttribute("activity_layout", it.toString()))
            }
            getTag(TAG_COMPONENT_NAME)?.let {
                result.add(ViewAttribute("组件名", it.toString()))
            }
            getTag(TAG_COMPONENT_LAYOUT_NAME)?.let {
                result.add(ViewAttribute("组件layout", it.toString()))
            }
        }
        return result
    }
}