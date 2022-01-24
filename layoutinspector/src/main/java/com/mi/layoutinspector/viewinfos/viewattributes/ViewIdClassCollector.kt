package com.mi.layoutinspector.viewinfos.viewattributes

import android.view.View
import com.mi.layoutinspector.utils.getId
import com.mi.layoutinspector.inspector.IViewInspector

/**
 * create by niuxiaowei
 * date : 22-1-18
 *  * view的id name和class信息收集器

 **/
class ViewIdClassCollector : IViewAttributeCollector {
    override fun collectViewAttributes(inspectedView: View, IViewInspector: IViewInspector): List<ViewAttribute>? {
        val result = arrayListOf<ViewAttribute>()
        getId(inspectedView)?.let {
            result.add(ViewAttribute("id名字", it))
        }
        result.add(ViewAttribute("类名", inspectedView.javaClass.simpleName + ""))
        return result
    }
}