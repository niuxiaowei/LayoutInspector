package com.mi.layoutinspector.viewinfos.viewattributes

import android.view.View
import android.widget.TextView
import com.mi.layoutinspector.inspect.ViewInspector
import com.mi.layoutinspector.getDimensionWithUnitName
import com.mi.layoutinspector.getSuperClass
import com.mi.layoutinspector.widget.CustomDialog

/**
 * create by niuxiaowei
 * date : 22-1-18
 **/
class ViewTextInfoCollector : IViewAttributeCollector {
    override fun collectViewAttributes(inspectedView: View, viewInspector: ViewInspector): List<ViewAttribute>? {
        if (inspectedView is TextView) {
            val result = arrayListOf<ViewAttribute>()
            val textResource = getTextResource(inspectedView)
            if (textResource != null) {
                result.add(textResource)
            }

            //文本
            result.add(ViewAttribute("文本", inspectedView.text.toString(), View.OnClickListener {
                viewInspector.hideViewInfosPopupWindown()
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
                viewInspector.hideViewInfosPopupWindown()
                val dialog = CustomDialog(inspectedView.context, object : CustomDialog.IOkClickListener {
                    override fun onOkClick(editMsg: String) {
                        if (editMsg.isNotEmpty()) {
                        }
                    }
                }, "请输入颜色值", "$textColor")
                dialog.show()
            }))

            //textsize
            var textsizeStr = getDimensionWithUnitName(inspectedView.textSize)
            result.add(ViewAttribute("textsize", textsizeStr))
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