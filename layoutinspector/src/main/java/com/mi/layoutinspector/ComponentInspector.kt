package com.mi.layoutinspector

import android.app.Activity
import com.mi.layoutinspector.menu.InspectorMenu

/**
 * Copyright (C) 2020, niuxiaowei. All rights reserved.
 * <p>
 * @author niuxiaowei
 * @date 2022/1/23.
 * 组件检查器，定义一些基础的接口
 */
abstract class ComponentInspector {
    var inspectorViewShowed: Boolean = false
    protected open var menu: InspectorMenu? = null
    abstract fun hideInspectorViews()
    abstract fun showInspectorViews()
    abstract fun hideShowedView()
}