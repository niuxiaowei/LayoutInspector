package com.mi.layoutinspector.viewinfos.viewattributes

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.mi.layoutinspector.*
import com.mi.layoutinspector.inspector.IViewInspector
import com.mi.layoutinspector.utils.dp2px
import com.mi.layoutinspector.utils.getDimension
import com.mi.layoutinspector.utils.getDimensionWithUnitName
import com.mi.layoutinspector.utils.getUnitStr
import com.mi.layoutinspector.widget.CustomDialog

/**
 * create by niuxiaowei
 * date : 22-1-18
 *  * view的大小收集器
 **/
class ViewSizeCollector : IViewAttributeCollector {


    override fun collectViewAttributes(inspectedView: View, IViewInspector: IViewInspector): List<ViewAttribute>? {
        val result = arrayListOf<ViewAttribute>()
        result.add(ViewAttribute("宽", "${getDimensionWithUnitName(inspectedView.width.toFloat())} "))
        result.add(ViewAttribute("高", "${getDimensionWithUnitName(inspectedView.height.toFloat())} "))
        inspectedView.layoutParams?.let { layoutParams ->
            layoutParamsToStr(layoutParams.width)?.let {
                result.add(ViewAttribute("layout_width", it))
            }
            layoutParamsToStr(layoutParams.height)?.let {
                result.add(ViewAttribute("layout_width", it))
            }
        }

        val location = IntArray(2)
        inspectedView.getLocationOnScreen(location)
        result.add(ViewAttribute("坐标", "x: ${getDimensionWithUnitName(location[0].toFloat())} y:${getDimensionWithUnitName(location[1].toFloat())}"))

        result.add(ViewAttribute("paddingLeft", getDimensionWithUnitName(inspectedView.paddingLeft.toFloat()), createClickListener(IViewInspector, inspectedView, getDimension(inspectedView.paddingLeft.toFloat()).toString(), OptType.PADDING_LEFT)))
        result.add(ViewAttribute("paddingRight", getDimensionWithUnitName(inspectedView.paddingRight.toFloat()), createClickListener(IViewInspector, inspectedView, getDimension(inspectedView.paddingRight.toFloat()).toString(), OptType.PADDING_RIGHT)))
        result.add(ViewAttribute("paddingBottom", getDimensionWithUnitName(inspectedView.paddingBottom.toFloat()), createClickListener(IViewInspector, inspectedView, getDimension(inspectedView.paddingBottom.toFloat()).toString(), OptType.PADDING_BOTTOM)))
        result.add(ViewAttribute("paddingTop", getDimensionWithUnitName(inspectedView.paddingTop.toFloat()), createClickListener(IViewInspector, inspectedView, getDimension(inspectedView.paddingTop.toFloat()).toString(), OptType.PADDING_TOP)))

        if (inspectedView.layoutParams is ViewGroup.MarginLayoutParams) {
            val marginLP = inspectedView.layoutParams as ViewGroup.MarginLayoutParams
            result.add(ViewAttribute("leftMargin", getDimensionWithUnitName(marginLP.leftMargin.toFloat()), createClickListener(IViewInspector, inspectedView, getDimension(marginLP.leftMargin.toFloat()).toString(), OptType.MARGIN_LEFT)))
            result.add(ViewAttribute("rightMargin", getDimensionWithUnitName(marginLP.rightMargin.toFloat()), createClickListener(IViewInspector, inspectedView, getDimension(marginLP.rightMargin.toFloat()).toString(), OptType.MARGIN_RIGHT)))
            result.add(ViewAttribute("topMargin", getDimensionWithUnitName(marginLP.topMargin.toFloat()), createClickListener(IViewInspector, inspectedView, getDimension(marginLP.topMargin.toFloat()).toString(), OptType.MARGIN_TOP)))
            result.add(ViewAttribute("bottomMargin", getDimensionWithUnitName(marginLP.bottomMargin.toFloat()), createClickListener(IViewInspector, inspectedView, getDimension(marginLP.bottomMargin.toFloat()).toString(), OptType.MARGIN_BOTTOM)))
        }



        return result
    }

    private fun layoutParamsToStr(value: Int): String? {
        return when (value) {
            WRAP_CONTENT -> "wrap_content"
            MATCH_PARENT -> "match_parent"
            else -> null
        }
    }

    private fun createClickListener(IViewInspector: IViewInspector, inspectedView: View, hintMsg: String, optType: OptType): View.OnClickListener {

        return View.OnClickListener {
            IViewInspector.hideViewInfosPopupWindown()
            var title = when (optType) {
                OptType.PADDING_BOTTOM -> "修改padding bottom(数字)"
                OptType.PADDING_TOP -> "修改padding top(数字)"
                OptType.PADDING_LEFT -> "修改padding left(数字)"
                OptType.PADDING_RIGHT -> "修改padding right(数字)"
                OptType.MARGIN_BOTTOM -> "修改margin bottom(数字)"
                OptType.MARGIN_TOP -> "修改margin top(数字)"
                OptType.MARGIN_LEFT -> "修改margin left(数字)"
                OptType.MARGIN_RIGHT -> "修改margin right(数字)"
            }
            title += "单位${getUnitStr()}"
            val dialog = CustomDialog(inspectedView.context, object : CustomDialog.IOkClickListener {
                override fun onOkClick(editMsg: String) {
                    if (editMsg.isNotEmpty()) {
                        try {
                            var value = editMsg.toInt()
                            if (LayoutInspector.unitsIsDP) {
                                value = dp2px(value.toFloat()).toInt()
                            }
                            when (optType) {
                                OptType.PADDING_BOTTOM -> inspectedView.apply { setPadding(paddingLeft, paddingTop, paddingRight, value) }
                                OptType.PADDING_TOP -> inspectedView.apply { setPadding(paddingLeft, value, paddingRight, paddingBottom) }
                                OptType.PADDING_LEFT -> inspectedView.apply { setPadding(value, paddingTop, paddingRight, paddingBottom) }
                                OptType.PADDING_RIGHT -> inspectedView.apply { setPadding(paddingLeft, paddingTop, value, paddingBottom) }
                            }
                            changeMargin(optType, value, inspectedView)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }, title, hintMsg)
            dialog.show()
        }
    }

    private fun changeMargin(optType: OptType, value: Int, inspectedView: View) {
        val marginLP = inspectedView.layoutParams as ViewGroup.MarginLayoutParams
        when (optType) {
            OptType.MARGIN_BOTTOM -> marginLP.bottomMargin = value
            OptType.MARGIN_TOP -> marginLP.topMargin = value
            OptType.MARGIN_LEFT -> marginLP.leftMargin = value
            OptType.MARGIN_RIGHT -> marginLP.rightMargin = value
            else -> return
        }
        inspectedView.layoutParams = marginLP
    }

    private enum class OptType {
        PADDING_LEFT, PADDING_RIGHT, PADDING_TOP, PADDING_BOTTOM, MARGIN_LEFT, MARGIN_RIGHT, MARGIN_TOP, MARGIN_BOTTOM
    }

}