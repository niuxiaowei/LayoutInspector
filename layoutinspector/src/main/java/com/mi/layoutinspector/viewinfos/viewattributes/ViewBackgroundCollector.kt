package com.mi.layoutinspector.viewinfos.viewattributes

import android.view.View
import com.mi.layoutinspector.inspect.IViewInspector
import com.mi.layoutinspector.utils.getSuperClass

/**
 * create by niuxiaowei
 * date : 22-1-18
 **/
class ViewBackgroundCollector : IViewAttributeCollector {
    override fun collectViewAttribute(inspectedView: View, IViewInspector: IViewInspector): ViewAttribute? {
        val viewClass: Class<*> = inspectedView.javaClass
        try {
            val superClass = getSuperClass(viewClass, View::class.java)
            val field = superClass?.getDeclaredField("mBackgroundResource")
            field?.isAccessible = true
            val id = field?.get(inspectedView) as Int
            if (id > 0) {
                val entryname: String = inspectedView.resources.getResourceEntryName(id)
                return ViewAttribute("背景", entryname + "")
            }
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return null
    }
}