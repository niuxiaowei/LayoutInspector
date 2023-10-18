package com.mi.layoutinspector.inspector

import android.app.Dialog
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.PopupWindow
import com.mi.layoutinspector.menu.InspectorMenu
import com.mi.layoutinspector.viewinfos.viewattributes.ComponentInfoCollector

/**
 * Copyright (C) 2020, niuxiaowei. All rights reserved.
 * <p>
 * @author niuxiaowei
 * @date 2022/1/23.
 * 组件检查器，定义一些基础的接口
 *
 * pageInspectors管理PageInspector。其中PageInspector对应于contentView的一个子view，它主要是对该子view及它包含的所有子view进行
 * 映射，在ViewInspectors中把这些view的边界等信息绘制出来，PageInspector会被添加到contentView中，并且位于它映射的子view的上层.*
 * @param contentView 对应activity的android.R.id.content  或者Dialog的android.R.id.content 或者PopupWindow的PopupDecorView
 * @param decorView 对应activity的decorView  或者Dialog的decorView 或者PopupWindow的PopupDecorView
 *
 */
abstract class ComponentInspector() : IInspector {
    var inspectorsShowed: Boolean = false

    private val pageInspectors = mutableListOf<PageInspector>()

    protected var contentView: ViewGroup? = null
    protected var decorView: View? = null

    var activityName: String? = null
    var contentLayoutName: String? = null

    protected fun setContentDecorView(contentView: ViewGroup?, decorView: View) {
        this.contentView = contentView
        this.decorView = decorView
        contentView?.apply {
            viewTreeObserver.addOnGlobalLayoutListener {
                tryRemoveErrViewInspectors()
                tryAddViewInspectors()
            }
        }
    }

    private data class ChildViewAndIndexWrapper(val childView: View, val index: Int)

    private fun tryAddViewInspectors() {
        contentView?.apply {
            //需要配对的child view
            var preChild: View? = null
            var childViewAndIndexWrappers: MutableList<ChildViewAndIndexWrapper>? = null
            var isAddMenuView = false
            for (i in 0 until childCount) {
                val child = getChildAt(i)

                if (preChild != null) {
                    if (child !is PageInspector) {
                        if (childViewAndIndexWrappers == null) {
                            childViewAndIndexWrappers = mutableListOf()
                        }
                        childViewAndIndexWrappers.apply {
                            add(ChildViewAndIndexWrapper(preChild!!, size + i))
                        }
                        preChild = child
                    } else {
                        preChild = null
                    }

                } else {
                    preChild = child
                }

                //最后一个不是PageInspector
                if (i == childCount - 1) {
                    if (child !is PageInspector && !viewIsMenu(child)) {
                        if (childViewAndIndexWrappers == null) {
                            childViewAndIndexWrappers = mutableListOf()
                        }
                        childViewAndIndexWrappers.apply {
                            add(ChildViewAndIndexWrapper(child!!, size + i + 1))
                        }
                    }
                    //最后一个不是InspectMenuPage，则去添加
                    if (!viewIsMenu(child)) {
                        isAddMenuView = true
                    }
                }
            }
            childViewAndIndexWrappers?.forEach { (childView, index) ->
                val pageInspector = PageInspector(context, childView, decorView,hasDialogMenu())
                addTagsForView(childView)
                pageInspectors.add(pageInspector)
                val lp = FrameLayout.LayoutParams(childView.layoutParams)
                addView(pageInspector, index, lp)
            }
            if (isAddMenuView) {
                addMenu(this)
            }
        }
    }

    protected open fun addTagsForView(view: View) {
        view.apply {
            setTag(ComponentInfoCollector.TAG_ACTIVITY_NAME, activityName)
            setTag(ComponentInfoCollector.TAG_ACTIVITY_LAYOUT_NAME, contentLayoutName)
        }
    }

    protected open fun hasDialogMenu(): Boolean {
        return false
    }

    protected open fun viewIsMenu(view: View): Boolean {
        return false
    }

    protected open fun addMenu(contentView: ViewGroup) {

    }

    private fun tryRemoveErrViewInspectors() {
        contentView?.apply {
            var preView: View? = null
            var removeIndexs: MutableList<Int>? = null
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                //菜单不在最后一个，则认为是有问题的
                if (viewIsMenu(child) && i != childCount - 1) {
                    if (removeIndexs == null) {
                        removeIndexs = mutableListOf()
                    }
                    removeIndexs.add(i)
                    continue
                }
                //若是PageInspector，则验证它的preView是否是非PageInspector，否则尝试移除当前的 PageInspector
                if (child is PageInspector) {
                    if (preView == null || preView is PageInspector) {
                        if (removeIndexs == null) {
                            removeIndexs = mutableListOf()
                        }
                        removeIndexs.add(i)
                    }
                }
                preView = child
            }
            removeIndexs?.forEach {
                removeViewAt(it)
            }
        }
    }

    override fun hideInspectors() {
        pageInspectors.forEach {
            it.hideInspectors()
            it.hideViewInfosPopupWindow()
        }
        inspectorsShowed = false
    }

    override fun showInspectors() {
        pageInspectors.forEach {
            it.showInspectors()
            it.hideViewInfosPopupWindow()
        }
        inspectorsShowed = true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ComponentInspector

        if (contentView != other.contentView) return false
        if (decorView != other.decorView) return false

        return true
    }

    override fun hashCode(): Int {
        var result = contentView?.hashCode() ?: 0
        result = 31 * result + (decorView?.hashCode() ?: 0)
        return result
    }

    open fun onDestory() {
        pageInspectors.clear()
    }
}

internal object ComponentInspectorFactory {
    fun createComponentInspector(any: Any, activityInspector: ActivityInspector): ComponentInspector? {
        return if (any is Dialog) {
            DialogInspector(any, activityInspector)
        } else if (any is PopupWindow) {
            PopupWindowInspector(any, activityInspector)
        } else {
            null
        }
    }
}