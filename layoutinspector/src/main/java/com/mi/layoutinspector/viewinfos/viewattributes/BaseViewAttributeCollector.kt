package com.mi.layoutinspector.viewinfos.viewattributes

import android.os.Build
import android.view.View
import com.mi.layoutinspector.inspector.IViewInspector
import com.mi.layoutinspector.utils.getSuperClass

/**
 * Copyright (C) 2020, niuxiaowei. All rights reserved.
 * <p>
 * @author niuxiaowei
 * @date 2022/1/28.
 */
class BaseViewAttributeCollector : IViewAttributeCollector {

    override fun collectViewAttributes(
        inspectedView: View,
        IViewInspector: IViewInspector
    ): List<ViewAttribute>? {
        val result = arrayListOf<ViewAttribute>()
        getBackgroundAttribute(inspectedView)?.let { result.add(it) }
        result.add(ViewAttribute("isClickable", "${inspectedView.isClickable}"))
        result.add(ViewAttribute("isSelected", "${inspectedView.isSelected}"))
        result.add(ViewAttribute("isLongClickable", "${inspectedView.isLongClickable}"))
        if (inspectedView.alpha != 1F) {
            result.add(ViewAttribute("alpha", "${inspectedView.alpha}"))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (inspectedView.translationZ != 0F) {
                result.add(ViewAttribute("translationZ", "${inspectedView.translationZ}"))
            }
        }
        if (inspectedView.translationX != 0F) {
            result.add(ViewAttribute("translationX", "${inspectedView.translationX}"))
        }
        if (inspectedView.translationY != 0F) {
            result.add(ViewAttribute("translationY", "${inspectedView.translationY}"))
        }
        return result
    }

    private fun getBackgroundAttribute(inspectedView: View): ViewAttribute? {
        val viewClass: Class<*> = inspectedView.javaClass
        try {
            val superClass = getSuperClass(viewClass, View::class.java)
            val field = superClass?.getDeclaredField("mBackgroundResource")
            field?.isAccessible = true
            val id = field?.get(inspectedView) as Int
            if (id > 0) {
                val entryname: String = inspectedView.resources.getResourceEntryName(id)
                return ViewAttribute("background", entryname + "")
            }
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return null
    }
}