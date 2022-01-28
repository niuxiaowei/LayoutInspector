package com.mi.layoutinspector.viewinfos.viewattributes

import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.mi.layoutinspector.inspector.IViewInspector
import com.mi.layoutinspector.utils.gravityToString

/**
 * Copyright (C) 2020, niuxiaowei. All rights reserved.
 * <p>
 * @author niuxiaowei
 * @date 2022/1/28.
 */
class BaseViewGroupAttributeCollector : IViewAttributeCollector {

    override fun collectViewAttributes(
        inspectedView: View,
        IViewInspector: IViewInspector
    ): List<ViewAttribute>? {
        val result = arrayListOf<ViewAttribute>()
        if (inspectedView is LinearLayout) {
            result.add(
                ViewAttribute(
                    "orientation",
                    if (inspectedView.orientation == LinearLayout.HORIZONTAL) "HORIZONTAL" else "VERTICAL"
                )
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (inspectedView.gravity != Gravity.START or Gravity.TOP) {
                    result.add(ViewAttribute("gravity", gravityToString(inspectedView.gravity)))
                }

            }

        } else if (inspectedView is RelativeLayout) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                if (inspectedView.gravity != Gravity.START or Gravity.TOP) {
                    result.add(ViewAttribute("gravity", gravityToString(inspectedView.gravity)))
                }


            }
        }
        return result
    }
}