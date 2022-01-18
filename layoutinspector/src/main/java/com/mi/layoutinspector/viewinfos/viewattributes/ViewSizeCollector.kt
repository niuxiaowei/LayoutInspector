package com.mi.layoutinspector.viewinfos.viewattributes

import android.view.View
import android.view.ViewGroup
import com.mi.layoutinspector.inspect.ViewInspector
import com.mi.layoutinspector.getDimensionWithUnitName

/**
 * create by niuxiaowei
 * date : 22-1-18
 *  * view的大小收集器
 **/
class ViewSizeCollector : IViewAttributeCollector {


    override fun collectViewAttributes(inspectedView: View, viewInspector: ViewInspector): List<ViewAttribute>? {
        val result = arrayListOf<ViewAttribute>()
        result.add(ViewAttribute("大小", "宽：${getDimensionWithUnitName(inspectedView.width.toFloat())} 高：${getDimensionWithUnitName(inspectedView.height.toFloat())}"))

        result.add(ViewAttribute("坐标", "x: ${getDimensionWithUnitName(inspectedView.x)} y:${getDimensionWithUnitName(inspectedView.y)}"))

        result.add(ViewAttribute("padding", "left: ${getDimensionWithUnitName(inspectedView.paddingLeft.toFloat())} right:${getDimensionWithUnitName(inspectedView.paddingRight.toFloat())} " +
                "top: ${getDimensionWithUnitName(inspectedView.paddingTop.toFloat())} bottom:${getDimensionWithUnitName(inspectedView.paddingBottom.toFloat())}"))

        if (inspectedView.layoutParams is ViewGroup.MarginLayoutParams) {
            val marginLP = inspectedView.layoutParams as ViewGroup.MarginLayoutParams
            result.add(ViewAttribute("margin", "left: ${getDimensionWithUnitName(marginLP.leftMargin.toFloat())} right:${getDimensionWithUnitName(marginLP.rightMargin.toFloat())} " +
                    "top: ${getDimensionWithUnitName(marginLP.topMargin.toFloat())} bottom:${getDimensionWithUnitName(marginLP.bottomMargin.toFloat())}"))
        }
        return result
    }

}