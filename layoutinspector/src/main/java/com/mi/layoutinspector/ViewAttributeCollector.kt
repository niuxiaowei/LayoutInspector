package com.mi.layoutinspector

import android.util.Log
import android.util.TypedValue.*
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * create by niuxiaowei
 * date : 21-12-28
 * 定义手机view详细信息(ViewDetail)的接口
 **/
interface IViewAttributeCollector {
    /**
     * 收集一个 viewDetail
     * @param inspectView View 界面上真正显示的view
     * @param inspectItemView 主要用来绘制inspectView的宽高布局的view
     * @return ViewAttribute
     */
    fun collectViewAttribute(inspectView: View, inspectItemView: InspectItemView): ViewAttribute? {
        return null
    }

    /**
     * 收集多个ViewDetails
     * @param inspectView View  界面上真正显示的view
     * @param inspectItemView 主要用来绘制inspectView的宽高布局的view
     * @return MutableList<ViewAttribute>?
     */
    fun collectViewAttributes(inspectView: View, inspectItemView: InspectItemView): List<ViewAttribute>? {
        return null
    }
}

private fun getUnitStr(): String {
    return if (LayoutInspector.unitsIsDP) "dp" else "px"
}

/**
 * view的大小收集器
 */
class ViewSizeCollector : IViewAttributeCollector {


    override fun collectViewAttributes(inspectView: View, inspectItemView: InspectItemView): List<ViewAttribute>? {
        val result = arrayListOf<ViewAttribute>()
        result.add(ViewAttribute("大小", "宽：${getDimensionWithUnitName(inspectView.width.toFloat())} 高：${getDimensionWithUnitName(inspectView.height.toFloat())}"))

        result.add(ViewAttribute("坐标", "x: ${getDimensionWithUnitName(inspectView.x)} y:${getDimensionWithUnitName(inspectView.y)}"))

        result.add(ViewAttribute("padding", "left: ${getDimensionWithUnitName(inspectView.paddingLeft.toFloat())} right:${getDimensionWithUnitName(inspectView.paddingRight.toFloat())} " +
                "top: ${getDimensionWithUnitName(inspectView.paddingTop.toFloat())} bottom:${getDimensionWithUnitName(inspectView.paddingBottom.toFloat())}"))

        if (inspectView.layoutParams is ViewGroup.MarginLayoutParams) {
            val marginLP = inspectView.layoutParams as ViewGroup.MarginLayoutParams
            result.add(ViewAttribute("margin", "left: ${getDimensionWithUnitName(marginLP.leftMargin.toFloat())} right:${getDimensionWithUnitName(marginLP.rightMargin.toFloat())} " +
                    "top: ${getDimensionWithUnitName(marginLP.topMargin.toFloat())} bottom:${getDimensionWithUnitName(marginLP.bottomMargin.toFloat())}"))
        }
        return result
    }

}

/**
 * view的id name和class信息收集器
 */
class ViewIdClassCollector : IViewAttributeCollector {
    override fun collectViewAttributes(inspectView: View, inspectItemView: InspectItemView): List<ViewAttribute>? {
        val result = arrayListOf<ViewAttribute>()
        try {
            val entryname: String = inspectView.resources.getResourceEntryName(inspectView.id)
            result.add(ViewAttribute("id名字", "R.id.$entryname"))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        result.add(ViewAttribute("类名", inspectView.javaClass.simpleName + ""))
        return result
    }
}



/**
 * view点击事件信息收集器
 */
class ViewClickInfoCollector : IViewAttributeCollector {
    override fun collectViewAttributes(inspectView: View, inspectItemView: InspectItemView): List<ViewAttribute>? {
        val result = arrayListOf<ViewAttribute>()
        result.add(ViewAttribute("view属性", "点击不显示view属性界面", View.OnClickListener {
            inspectItemView.isClickable = false
            inspectItemView.hideViewAttributes()
        }))
        if (inspectView.parent != null) {
            var entryname: String? = ""
            if (inspectView.parent is ViewGroup) {
                try {
                    val parent = inspectView.parent as ViewGroup
                    entryname = " (R.id." + inspectView.resources.getResourceEntryName(parent.id) + ")"
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }



            result.add(ViewAttribute("父控件", "${inspectView.parent.javaClass.simpleName}${entryname}", View.OnClickListener {
                inspectItemView.hideViewAttributes()
                inspectItemView.parentInspectItemView?.showViewAttributes()
            }))
        }
        result.add(ViewAttribute("是否设置点击事件", if (inspectView.hasOnClickListeners()) "是" else "否"))
        return result
    }
}

/**
 * view所属的layout文件已经被inflate出来的位置信息收集器
 */
class ViewLayoutInfoCollector : IViewAttributeCollector {
    override fun collectViewAttributes(inspectView: View, inspectItemView: InspectItemView): List<ViewAttribute>? {
        val result = arrayListOf<ViewAttribute>()
        val parentView = findParentWithLayoutName(inspectView)
        if (parentView != null) {
            result.add(ViewAttribute("所属布局名称", "R.layout." + parentView.getTag(LayoutInflaterProxy.TAG_KEY_LAYOUT_NAME).toString()))
            val inflateMethodInfo = parentView.getTag(LayoutInflaterProxy.TAG_KEY_INFLATE_METHOD_NAME).toString()
            val tempArr = inflateMethodInfo.split("#".toRegex()).toTypedArray()
            var classSimpleName = ""
            if (tempArr.size == 3) {
                classSimpleName = tempArr[0].substring(tempArr[0].lastIndexOf(".") + 1)
                Log.i("LayoutInspector", "inflate info: (" + classSimpleName + ".java:" + tempArr[2] + ")" + " or (" + classSimpleName + ".kt:" + tempArr[2] + ")")
            }
            result.add(ViewAttribute("布局被inflate位置", "${classSimpleName}#${tempArr[1]}#${tempArr[2]}"))
        }
        return result
    }

    /**
     * 查找包含LayoutInflaterProxy.TAG_KEY_LAYOUT_NAME tag的parent view
     * @param inspectorView View?
     * @return View? 找到包含LayoutInflaterProxy.TAG_KEY_LAYOUT_NAME tag的parent view 则返回，否则返回null
     */
    private fun findParentWithLayoutName(inspectorView: View?): View? {
        if (inspectorView == null) {
            return null
        }
        return if (inspectorView.getTag(LayoutInflaterProxy.TAG_KEY_LAYOUT_NAME) != null) {
            inspectorView
        } else {
            findParentWithLayoutName(inspectorView.parent as View)
        }
    }
}

class ViewBackgroundCollector : IViewAttributeCollector {
    override fun collectViewAttribute(inspectView: View, inspectItemView: InspectItemView): ViewAttribute? {
        val viewClass: Class<*> = inspectView.javaClass
        try {
            val superClass = getSuperClass(viewClass, View::class.java)
            val field = superClass?.getDeclaredField("mBackgroundResource")
            field?.isAccessible = true
            val id = field?.get(inspectView) as Int
            if (id > 0) {
                val entryname: String = inspectView.resources.getResourceEntryName(id)
                return ViewAttribute("背景", entryname + "")
            }
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return null
    }
}

fun getSuperClass(child: Class<*>, superClass: Class<*>): Class<*>? {
    return if (child == superClass) {
        child
    } else getSuperClass(child.superclass, superClass)
}

class ViewTextInfoCollector : IViewAttributeCollector {
    override fun collectViewAttributes(inspectView: View, inspectItemView: InspectItemView): List<ViewAttribute>? {
        if (inspectView is TextView) {
            val result = arrayListOf<ViewAttribute>()
            val textResource = getTextResource(inspectView)
            if (textResource != null) {
                result.add(textResource)
            }

            //文本
            result.add(ViewAttribute("文本", inspectView.text.toString(), View.OnClickListener {
                inspectItemView.hideViewAttributes()
                val dialog = CustomDialog(inspectView.context, object : CustomDialog.IOkClickListener {
                    override fun onOkClick(editMsg: String) {
                        if (editMsg.isNotEmpty()) {
                            inspectView.text = editMsg
                        }
                    }
                }, "请输入替换的文本", "${inspectView.text}")
                dialog.show()
            }))

            //textcolor属性
            var textColor = ""
            try {
                val color = inspectView.textColors.getColorForState(inspectView.drawableState, 0)
                textColor = String.format("%x", color)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            result.add(ViewAttribute("textcolor", textColor, View.OnClickListener {
                inspectItemView.hideViewAttributes()
                val dialog = CustomDialog(inspectView.context, object : CustomDialog.IOkClickListener {
                    override fun onOkClick(editMsg: String) {
                        if (editMsg.isNotEmpty()) {
                        }
                    }
                }, "请输入颜色值", "$textColor")
                dialog.show()
            }))

            //textsize
            var textsizeStr = getDimensionWithUnitName(inspectView.textSize)
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

class ChildViewCollector : IViewAttributeCollector {
    override fun collectViewAttributes(inspectView: View, inspectItemView: InspectItemView): List<ViewAttribute>? {
        val result = arrayListOf<ViewAttribute>()
        fun getEntryName(view: View): String {
            var entryname: String = ""
            try {
                entryname = " (R.id." + view.resources.getResourceEntryName(view.id) + ")"
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return entryname
        }

        inspectItemView.getChilds()?.apply {
            result.add(ViewAttribute("子控件个数",size.toString()))
        }
        var i = 0
        inspectItemView.getChilds()?.forEach { child ->

            result.add(ViewAttribute("子控件${i++}", "${child.inspectView.javaClass.simpleName}${getEntryName(child.inspectView)}", View.OnClickListener {
                inspectItemView.hideViewAttributes()
                child.showViewAttributes()
            }))
        }
        return result
    }
}