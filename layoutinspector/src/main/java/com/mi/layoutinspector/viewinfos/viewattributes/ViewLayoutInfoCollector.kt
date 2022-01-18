package com.mi.layoutinspector.viewinfos.viewattributes

import android.util.Log
import android.view.View
import com.mi.layoutinspector.LayoutInflaterProxy
import com.mi.layoutinspector.inspect.ViewInspector

/**
 * create by niuxiaowei
 * date : 22-1-18
 * view所属的layout文件已经被inflate出来的位置信息收集器
 **/
class ViewLayoutInfoCollector : IViewAttributeCollector {
    override fun collectViewAttributes(inspectedView: View, viewInspector: ViewInspector): List<ViewAttribute>? {
        val result = arrayListOf<ViewAttribute>()
        val parentView = findParentWithLayoutName(inspectedView)
        if (parentView != null) {
            result.add(ViewAttribute("所属布局名称", "R.layout." + parentView.getTag(LayoutInflaterProxy.TAG_KEY_LAYOUT_NAME).toString()))
            val inflateMethodInfo = parentView.getTag(LayoutInflaterProxy.TAG_KEY_INFLATE_METHOD_NAME).toString()
            val tempArr = inflateMethodInfo.split("#".toRegex()).toTypedArray()
            var classSimpleName = ""
            if (tempArr.size == 3) {
                classSimpleName = tempArr[0].substring(tempArr[0].lastIndexOf(".") + 1)
                Log.i("LayoutInspector", "inflate info: (" + classSimpleName + ".java:" + tempArr[2] + ")" + " or (" + classSimpleName + ".kt:" + tempArr[2] + ")")
            }
            result.add(ViewAttribute("布局被inflate位置", "${classSimpleName}#${tempArr[1]}#${tempArr[2]}"))
        }
        return result
    }

    /**
     * 查找包含LayoutInflaterProxy.TAG_KEY_LAYOUT_NAME tag的parent view
     * @param inspectorView View?
     * @return View? 找到包含LayoutInflaterProxy.TAG_KEY_LAYOUT_NAME tag的parent view 则返回，否则返回null
     */
    private fun findParentWithLayoutName(inspectorView: View?): View? {
        if (inspectorView == null) {
            return null
        }
        return if (inspectorView.getTag(LayoutInflaterProxy.TAG_KEY_LAYOUT_NAME) != null) {
            inspectorView
        } else {
            findParentWithLayoutName(inspectorView.parent as View)
        }
    }
}