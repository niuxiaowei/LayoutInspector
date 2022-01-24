package com.mi.layoutinspector.inspect

import android.app.Activity
import android.app.Dialog
import android.widget.PopupWindow

/**
 * Copyright (C) 2020, niuxiaowei. All rights reserved.
 * <p>
 * @author niuxiaowei
 * @date 2022/1/22.
 */
object InspectorsManagerCreators {

    private val creators =
        mapOf(
            Activity::class.java.name to ActivityInspectorsManagerCreator(),
            Dialog::class.java.name to DialogInspectorsManagerCreator(),
            PopupWindow::class.java.name to PopupWindowInspectorsManagerCreator()
        )


    fun of(any: Any): IViewInspectorsManagerCreator? {
        return if (any is Activity) {
            creators[Activity::class.java.name]
        } else if (any is Dialog) {
            creators[Dialog::class.java.name]
        } else if (any is PopupWindow) {
            creators[PopupWindow::class.java.name]
        } else null
    }
}