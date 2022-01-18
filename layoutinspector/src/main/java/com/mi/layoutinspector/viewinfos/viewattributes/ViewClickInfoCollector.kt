package com.mi.layoutinspector.viewinfos.viewattributes

import android.view.View
import android.view.ViewGroup
import com.mi.layoutinspector.inspect.ViewInspector

/**
 * create by niuxiaowei
 * date : 22-1-18
 * view点击事件信息收集器
 **/
class ViewClickInfoCollector : IViewAttributeCollector {
    override fun collectViewAttributes(inspectedView: View, viewInspector: ViewInspector): List<ViewAttribute>? {
        val result = arrayListOf<ViewAttribute>()
        result.add(ViewAttribute("view属性", "点击不显示view属性界面", View.OnClickListener {
            viewInspector.setClickable(false)
            viewInspector.hideViewInfosPopupWindown()
        }))
        if (inspectedView.parent != null) {
            var entryname: String? = ""
            if (inspectedView.parent is ViewGroup) {
                try {
                    val parent = inspectedView.parent as ViewGroup
                    entryname = " (R.id." + inspectedView.resources.getResourceEntryName(parent.id) + ")"
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }



            result.add(ViewAttribute("父控件", "${inspectedView.parent.javaClass.simpleName}${entryname}", View.OnClickListener {
                viewInspector.hideViewInfosPopupWindown()
                viewInspector.parent()?.showViewInfosPopupWindow()
            }))
        }
        result.add(ViewAttribute("是否设置点击事件", if (inspectedView.hasOnClickListeners()) "是" else "否"))
        return result
    }
}