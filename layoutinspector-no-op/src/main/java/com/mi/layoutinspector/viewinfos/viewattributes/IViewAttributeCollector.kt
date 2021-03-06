package com.mi.layoutinspector.viewinfos.viewattributes

import android.view.View
import com.mi.layoutinspector.inspector.IViewInspector

/**
 * create by niuxiaowei
 * date : 21-12-28
 * 定义手机view详细信息(ViewDetail)的接口
 **/
interface IViewAttributeCollector {
    /**
     * 收集一个 viewDetail
     * @param inspectedView View 被收集属性的view
     * @param IViewInspector 具体的view检测器
     * @return ViewAttribute
     */
    fun collectViewAttribute(inspectedView: View, IViewInspector: IViewInspector): ViewAttribute? {
        return null
    }

    /**
     * 收集多个ViewDetails
     * @param inspectedView View 被收集属性的view
     * @param IViewInspector 具体的view检测器
     * @return MutableList<ViewAttribute>?
     */
    fun collectViewAttributes(inspectedView: View, IViewInspector: IViewInspector): List<ViewAttribute>? {
        return null
    }
}