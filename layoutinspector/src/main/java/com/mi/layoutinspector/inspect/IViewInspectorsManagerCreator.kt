package com.mi.layoutinspector.inspect

import com.mi.layoutinspector.ActivityInspector

/**
 * Copyright (C) 2020, niuxiaowei. All rights reserved.
 * <p>
 * @author niuxiaowei
 * @date 2022/1/22.
 * 定义create ViewInspectorsManager的接口
 */
interface IViewInspectorsManagerCreator {
    fun createViewInspectorsManager(
        any: Any,
        activityInspector: ActivityInspector
    ): ViewInspectorsManager?
}