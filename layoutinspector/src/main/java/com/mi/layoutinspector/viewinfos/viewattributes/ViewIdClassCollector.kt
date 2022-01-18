package com.mi.layoutinspector.viewinfos.viewattributes

import android.view.View
import com.mi.layoutinspector.inspect.ViewInspector

/**
 * create by niuxiaowei
 * date : 22-1-18
 *  * view的id name和class信息收集器

 **/
class ViewIdClassCollector : IViewAttributeCollector {
    override fun collectViewAttributes(inspectedView: View, viewInspector: ViewInspector): List<ViewAttribute>? {
        val result = arrayListOf<ViewAttribute>()
        try {
            val entryname: String = inspectedView.resources.getResourceEntryName(inspectedView.id)
            result.add(ViewAttribute("id名字", "R.id.$entryname"))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        result.add(ViewAttribute("类名", inspectedView.javaClass.simpleName + ""))
        return result
    }
}