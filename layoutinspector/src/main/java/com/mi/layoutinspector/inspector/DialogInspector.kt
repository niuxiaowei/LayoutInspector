package com.mi.layoutinspector.inspector

import android.app.Dialog
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.mi.layoutinspector.inspector.ActivityInspector
import com.mi.layoutinspector.inspector.ComponentInspector
import com.mi.layoutinspector.menu.DialogInspectorMenu
import com.mi.layoutinspector.utils.getContentViewForDialog
import com.mi.layoutinspector.utils.getLayoutName
import com.mi.layoutinspector.viewinfos.viewattributes.ComponentInfoCollector.Companion.TAG_COMPONENT_LAYOUT_NAME
import com.mi.layoutinspector.viewinfos.viewattributes.ComponentInfoCollector.Companion.TAG_COMPONENT_NAME

/**
 * Copyright (C) 2020, niuxiaowei. All rights reserved.
 * <p>
 * @author niuxiaowei
 * @date 2022/1/23.
 * 对dialog的检测器
 */
class DialogInspector(private val dialog: Dialog, activityInspector: ActivityInspector) : ComponentInspector() {

    private val menu = DialogInspectorMenu(dialog.context, this)

    init {
        setContentDecorView(getContentViewForDialog(dialog), dialog.window?.decorView!!)
        activityName = activityInspector.activityName
        contentLayoutName = activityInspector.contentLayoutName

        dialog.window!!.decorView.viewTreeObserver.addOnWindowAttachListener(object : ViewTreeObserver.OnWindowAttachListener {
            override fun onWindowDetached() {
                activityInspector.removeInspector(this@DialogInspector)
            }

            override fun onWindowAttached() {
            }

        })
    }

    override fun viewIsMenu(view: View): Boolean {
        return view is DialogInspectorMenu
    }

    override fun addMenu(contentView: ViewGroup) {
        contentView.addView(menu)
    }

    override fun removeMenu(contentView: ViewGroup) {
        contentView.removeView(menu)
    }

    override fun hasDialogMenu(): Boolean {
        return true
    }


    override fun addTagsForView(view: View) {
        super.addTagsForView(view)
        view.apply {
            setTag(TAG_COMPONENT_NAME, dialog.javaClass.simpleName)
            setTag(TAG_COMPONENT_LAYOUT_NAME, getLayoutName(if (contentView?.childCount!! > 0) contentView?.getChildAt(0) else null))
        }
    }


}