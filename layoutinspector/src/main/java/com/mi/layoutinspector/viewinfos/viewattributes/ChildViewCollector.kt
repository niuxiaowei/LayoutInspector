package com.mi.layoutinspector.viewinfos.viewattributes

import android.view.View
import com.mi.layoutinspector.inspect.ViewInspector

/**
 * create by niuxiaowei
 * date : 22-1-18
 **/
class ChildViewCollector : IViewAttributeCollector {
    override fun collectViewAttributes(inspectedView: View, viewInspector: ViewInspector): List<ViewAttribute>? {
        val result = arrayListOf<ViewAttribute>()
        fun getEntryName(view: View): String {
            var entryname: String = ""
            try {
                entryname = " (R.id." + view.resources.getResourceEntryName(view.id) + ")"
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return entryname
        }

        viewInspector.childs()?.apply {
            result.add(ViewAttribute("子控件个数", size.toString()))
        }
        var i = 0
        viewInspector.childs()?.forEach { child ->

            result.add(ViewAttribute("子控件${i++}", "${child.inspectedView().javaClass.simpleName}${getEntryName(child.inspectedView())}", View.OnClickListener {
                viewInspector.hideViewInfosPopupWindown()
                child.showViewInfosPopupWindow()
            }))
        }
        return result
    }
}