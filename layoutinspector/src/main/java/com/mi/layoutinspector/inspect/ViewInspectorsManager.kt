package com.mi.layoutinspector.inspect

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.mi.layoutinspector.ActivityInspector
import com.mi.layoutinspector.menu.InspectorMenu

/**
 * Copyright (C) 2020, niuxiaowei. All rights reserved.
 * <p>
 * @author niuxiaowei
 * @date 2022/1/8.
 * ViewInspectorsManager管理ViewInspectors。其中ViewInspectors对应于contentView的一个子view，它主要是对该子view及它包含的所有子view进行
 * 映射，在ViewInspectors中把这些view的边界等信息绘制出来，ViewInspectors会被添加到contentView中，并且位于它映射的子view的上层.
 * InspectMenuPage是一个菜单（包含显示，更多功能），它会被添加到contentView中，并且位于最顶层。
 *
 * @param contentView 对应activity的android.R.id.content  或者Dialog的android.R.id.content 或者PopupWindow的PopupDecorView
 *
 */
class ViewInspectorsManager(
    context: Context,
    private val activityInspector: ActivityInspector,
    private val contentView: ViewGroup?,
    private val decorView: View?
) {
    private val ViewInspectors = mutableListOf<ViewInspectors>()

    init {
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
            for (i in 0 until childCount) {
                val child = getChildAt(i)

                if (preChild != null) {
                    if (child !is ViewInspectors) {
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

                //最后一个不是ViewInspectors
                if (i == childCount - 1) {
                    if (child !is ViewInspectors) {
                        if (childViewAndIndexWrappers == null) {
                            childViewAndIndexWrappers = mutableListOf()
                        }
                        childViewAndIndexWrappers.apply {
                            add(ChildViewAndIndexWrapper(child!!, size + i + 1))
                        }
                    }
                }
            }
            childViewAndIndexWrappers?.forEach { (childView, index) ->
                val ViewInspectors = ViewInspectors(context, childView, decorView,activityInspector)
                this@ViewInspectorsManager.ViewInspectors.add(ViewInspectors)
                val lp = FrameLayout.LayoutParams(childView.layoutParams)
                addView(ViewInspectors, index, lp)
            }
        }
    }

    private fun tryRemoveErrViewInspectors() {
        contentView?.apply {
            var preView: View? = null
            var removeIndexs: MutableList<Int>? = null
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                //若是ViewInspectors，则验证它的preView是否是非ViewInspectors,是则该ViewInspectors是正确的
                if (child is ViewInspectors) {
                    if (preView == null || preView is ViewInspectors) {
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

    fun hideInspectorViews() {
        ViewInspectors.forEach {
            it.hideInspectorViews()
        }
    }

    fun showInspectorViews() {
        ViewInspectors.forEach {
            it.showInspectorViews()
        }
    }

    fun hideShowedView() {
        ViewInspectors.forEach {
            it.hideViewInfosPopupWindow()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ViewInspectorsManager) return false

        if (contentView != other.contentView) return false

        return true
    }

    override fun hashCode(): Int {
        return contentView.hashCode()
    }


}