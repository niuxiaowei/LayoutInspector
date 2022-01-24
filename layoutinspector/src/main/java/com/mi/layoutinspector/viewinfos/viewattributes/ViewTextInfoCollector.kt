package com.mi.layoutinspector.viewinfos.viewattributes

import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.mi.layoutinspector.*
import com.mi.layoutinspector.inspector.IViewInspector
import com.mi.layoutinspector.utils.*
import com.mi.layoutinspector.widget.CustomDialog

/**
 * create by niuxiaowei
 * date : 22-1-18
 **/
class ViewTextInfoCollector : IViewAttributeCollector {
    override fun collectViewAttributes(inspectedView: View, IViewInspector: IViewInspector): List<ViewAttribute>? {
        if (inspectedView is TextView) {
            val result = arrayListOf<ViewAttribute>()
            val textResource = getTextResource(inspectedView)
            if (textResource != null) {
                result.add(textResource)
            }

            //文本
            result.add(ViewAttribute("文本", inspectedView.text.toString(), View.OnClickListener {
                IViewInspector.hideViewInfosPopupWindown()
                val dialog = CustomDialog(inspectedView.context, object : CustomDialog.IOkClickListener {
                    override fun onOkClick(editMsg: String) {
                        if (editMsg.isNotEmpty()) {
                            inspectedView.text = editMsg
                        }
                    }
                }, "请输入替换的文本", "${inspectedView.text}")
                dialog.show()
            }))

            //textcolor属性
            var textColor = ""
            try {
                val color = inspectedView.textColors.getColorForState(inspectedView.drawableState, 0)
                textColor = String.format("%x", color)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            result.add(ViewAttribute("textcolor", textColor, View.OnClickListener {
                IViewInspector.hideViewInfosPopupWindown()
                val dialog = CustomDialog(inspectedView.context, object : CustomDialog.IOkClickListener {
                    override fun onOkClick(editMsg: String) {
                        if (editMsg.isNotEmpty()) {
                            try {
                                inspectedView.setTextColor(Color.parseColor("#${editMsg.trim()}"))
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }, "修改TextView颜色（16进制）", "$textColor")
                dialog.show()
            }))

            //textsize
            var textsizeStr = getDimensionWithUnitName(inspectedView.textSize)
            result.add(ViewAttribute("textsize", textsizeStr, View.OnClickListener {
                IViewInspector.hideViewInfosPopupWindown()
                val dialog = CustomDialog(inspectedView.context, object : CustomDialog.IOkClickListener {
                    override fun onOkClick(editMsg: String) {
                        if (editMsg.isNotEmpty()) {
                            try {
                                var value = editMsg.toFloat()
                                if (LayoutInspector.unitsIsDP) {
                                    value = dp2px(value)
                                }
                                inspectedView.setTextSize(TypedValue.COMPLEX_UNIT_PX, value)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }, "修改textView字体大小(单位${getUnitStr()})", getDimension(inspectedView.textSize).toString())
                dialog.show()
            }))
            return result
        }
        return null
    }

    private fun getTextResource(inspectorView: View): ViewAttribute? {
        val viewClass: Class<*> = inspectorView.javaClass
        try {
            val superClass = getSuperClass(viewClass, TextView::class.java)
            val field = superClass?.getField("mTextId")
            field?.isAccessible = true
            val id = field?.get(inspectorView) as Int
            if (id > 0) {
                val entryname: String = inspectorView.getResources().getResourceEntryName(id)
                return ViewAttribute("文本res", "R.string.$entryname")
            }
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return null
    }
}