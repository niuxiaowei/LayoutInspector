package com.mi.layoutinspector.inspector

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.mi.layoutinspector.Fragments
import com.mi.layoutinspector.menu.InspectorMenu
import com.mi.layoutinspector.utils.getContentViewForActivity
import com.mi.layoutinspector.utils.getLayoutName

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
    val fragments = Fragments(activity)
    private var isInit = false
    private val componentInspectors: MutableList<ComponentInspector> by lazy { mutableListOf<ComponentInspector>() }
    private var menu: InspectorMenu? = null

    fun onCreate() {
        fragments.registerFragmentLifecycle()
    }

    fun createComponentInspector(any: Any) {
        ComponentInspectorFactory.createComponentInspector(any, this)?.let {
            componentInspectors.add(it)
        }
    }

    override fun viewIsMenu(view: View): Boolean {
        return InspectorMenu.isMenuView(view)
    }

    fun onResume() {
        if (isInit) {
            return
        }
        isInit = true
        val contentView = getContentViewForActivity(activity)
        setContentDecorView(contentView, activity.window.decorView)
        createActivityInfo(contentView)
        menu = InspectorMenu(this,contentView).apply { onCreate() }
    }

    fun onStart() {
    }

    internal fun removeInspector(removed: ComponentInspector) {
        componentInspectors.remove(removed)
        Log.i("LayoutInspector", "removeInspector  componentInspectors count:${componentInspectors.size}   removed:${removed}")
    }

    private fun createActivityInfo(contentView: ViewGroup) {
        contentLayoutName = getLayoutName(contentView.getChildAt(0))
        activityName = activity.javaClass.simpleName
    }

    override fun hideInspectors() {
        super.hideInspectors()
        componentInspectors.forEach { it.hideInspectors() }
    }

    override fun showInspectors() {
        super.showInspectors()
        componentInspectors.forEach { it.showInspectors() }
    }


    override fun onDestory() {
        fragments.unRegisterFragmentLifecycle()
        menu?.onDestory()
        componentInspectors.forEach { it.onDestory() }
        componentInspectors.clear()
    }
}

