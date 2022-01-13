package com.mi.layoutinspector

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

/**
 * Copyright (C) 2020, niuxiaowei. All rights reserved.
 * <p>
 * @author niuxiaowei
 * @date 2022/1/8.
 *
 */
class InspectPageManager(context: Context,
                         private val layoutInspector: LayoutInspector,
                         private val contentView: ViewGroup
) {
    var inspectorViewShowed = false
    private val inspectMenuPage = InspectMenuPage(context, layoutInspector, this)
    private val inspectPages = mutableListOf<InspectPage>()

    init {
        contentView.apply {
            viewTreeObserver.addOnGlobalLayoutListener {
                tryRemoveErrInspectPages()
                tryAddInspectPages()
            }
        }
    }

    private data class ChildViewAndIndexWrapper(val childView: View, val index: Int)

    private fun tryAddInspectPages() {
        contentView.apply {
            //需要配对的child view
            var preChild: View? = null
            var childViewAndIndexWrappers: MutableList<ChildViewAndIndexWrapper>? = null
            var isAddMenuPage = false
            for (i in 0 until childCount) {
                val child = getChildAt(i)

                if (preChild != null) {
                    if (child !is InspectPage) {
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

                //最后一个不是InspectPage
                if (i == childCount - 1) {
                    if (child !is InspectPage && child !is InspectMenuPage) {
                        if (childViewAndIndexWrappers == null) {
                            childViewAndIndexWrappers = mutableListOf()
                        }
                        childViewAndIndexWrappers.apply {
                            add(ChildViewAndIndexWrapper(child!!, size + i + 1))
                        }
                    }
                    //最后一个不是InspectMenuPage，则去添加
                    if (child !is InspectMenuPage) {
                        isAddMenuPage = true
                    }
                }
            }
            childViewAndIndexWrappers?.forEach { (childView, index) ->
                val inspectPage = InspectPage(context, childView)
                inspectPages.add(inspectPage)
                val lp = FrameLayout.LayoutParams(childView.layoutParams)
                addView(inspectPage, index, lp)
            }
            if (isAddMenuPage) {
                addView(inspectMenuPage)
            }

        }
    }

    private fun tryRemoveErrInspectPages() {
        contentView.apply {
            var preView: View? = null
            var removeIndexs: MutableList<Int>? = null
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                //InspectMenuPage不在最后一个，则认为是有问题的
                if (child is InspectMenuPage && i != childCount - 1) {
                    if (removeIndexs == null) {
                        removeIndexs = mutableListOf()
                    }
                    removeIndexs.add(i)
                    continue
                }
                //若是InspectPage，则验证它的preView是否是非Inspectpage,是则该Inspectpage是正确的
                if (child is InspectPage) {
                    if (preView == null || preView is InspectPage) {
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

    fun hideInspectorView() {
        inspectPages.forEach {
            it.hideInspectorView()
        }
        inspectorViewShowed = false
    }

    fun showInspectorView() {
        inspectPages.forEach {
            it.showInspectorView()
        }
        inspectorViewShowed = true
    }

    fun hideShowedView() {
        inspectPages.forEach {
            it.hideViewAttributes()
        }
    }
}