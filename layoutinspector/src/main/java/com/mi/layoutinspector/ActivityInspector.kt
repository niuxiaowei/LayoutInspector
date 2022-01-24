package com.mi.layoutinspector

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.os.Build
import android.view.ViewGroup
import com.mi.layoutinspector.inspect.InspectorsManagerCreators
import com.mi.layoutinspector.inspect.ViewInspectorsManager
import com.mi.layoutinspector.menu.InspectorMenu
import com.mi.layoutinspector.utils.getContentViewForActivity
import com.mi.layoutinspector.utils.getContentViewForDialog

/**
 * Copyright (C) 2020, niuxiaowei. All rights reserved.
 * <p>
 * @author niuxiaowei
 * @date 2022/1/22.
 * 与Activity是一一对应关系，它包含多个ViewInspectorsManager,不同种类的view会
 * 创建对应的ViewInspectorsManager，比如Activity的DecorView的contentview会创建自己的；
 * 当前activity的Dialog的DecorView的contentView会创建自己的；
 * 当前activity的PopupWindow会创建自己的
 * */
class ActivityInspector(val activity: Activity) : ComponentInspector() {
    var contentViewIdName: String? = null
    var activityName: String? = null
    private val viewInspectorsManagers: MutableList<ViewInspectorsManager> = mutableListOf()
    val fragments = Fragments(activity)
    private var inspectPageManagerOfActivityInit = false
    private var dialogInspectors: MutableList<DialogInspector>? = null

    init {
        onActivityCreate()
    }

    private fun onActivityCreate() {
        fragments.registerFragmentLifecycle()
    }


    fun startInspect(any: Any) {
        InspectorsManagerCreators.of(any)?.createViewInspectorsManager(any, this)?.let {
            viewInspectorsManagers.add(it)
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    fun init() {
        if (inspectPageManagerOfActivityInit) {
            return
        }
        inspectPageManagerOfActivityInit = true
        InspectorsManagerCreators.of(activity)?.createViewInspectorsManager(activity, this)?.let {
            viewInspectorsManagers.add(it)
        }
        val contentView = getContentViewForActivity(activity)
        createActivityInfo(contentView)
        menu = InspectorMenu(this).apply { onCreate() }
    }

    private fun createActivityInfo(contentView: ViewGroup) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                val layoutId = contentView.getChildAt(0)?.sourceLayoutResId
                if (layoutId != null) {
                    if (layoutId > 0) {
                        contentViewIdName = activity.resources.getResourceEntryName(layoutId)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        activityName = activity.javaClass.simpleName
    }

    override fun hideInspectorViews() {
        viewInspectorsManagers.forEach {
            it.hideInspectorViews()
        }
        inspectorViewShowed = false
    }

    override fun showInspectorViews() {
        viewInspectorsManagers.forEach {
            it.showInspectorViews()
        }
        inspectorViewShowed = true
    }

    override fun hideShowedView() {
        viewInspectorsManagers.forEach {
            it.hideShowedView()
        }
    }


    fun onActivityDestory() {
        fragments.unRegisterFragmentLifecycle()
        menu?.onDestory()
    }
}

