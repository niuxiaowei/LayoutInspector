package com.mi.layoutinspector.menu

import android.view.View

/**
 * Copyright (C) 2020, niuxiaowei. All rights reserved.
 * <p>
 * @author niuxiaowei
 * @date 2022/1/23.
 */
interface IInspectorMenu {
    fun onCreate(){

    }

    abstract fun createMenuView():View

}