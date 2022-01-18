package com.mi.layoutinspector

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

/**
 * create by niuxiaowei
 * date : 2021/7/30
 **/

class InspectPage constructor(
        context: Context,
        private var childOfContentView: View
) : FrameLayout(context) {


    private var viewInfos = mutableListOf<InspectViewInfo>()
    private val viewAttributesPopupWindowHelper =
            ViewAttributesPopupWindow {
                curShowedView = null
                curInspectItemView?.setSelecte(false)
                curInspectItemView = null
            }
    private var curShowedView: View? = null
    private var curInspectItemView: InspectItemView? = null
    private var yOfContentView = -1
    private var mPaint: Paint = Paint()

    init {
        mPaint.color = ContextCompat.getColor(context, R.color.li_color_34b1f3)
        mPaint.strokeWidth = 2.0f
        //InspectPage的点击事件拦截掉
        setOnClickListener {
            Log.i("InspectPage", "InspectPage  click")
        }
        visibility = View.GONE
    }

    private fun add(
            viewInfo: InspectViewInfo,
            parent: InspectItemView?
    ): InspectItemView {
        viewInfos.add(viewInfo)
        val isSetClick4View = !(viewInfo.view is ViewGroup && !LayoutInspector.isViewGroupShowViewAttributes)
        val view = InspectItemView(
                context,
                viewInfo.view,
                this,
                isSetClick4View,
                parent
        )
        val lp = LayoutParams(viewInfo.view.width, viewInfo.view.height)
        addView(view, lp)
        return view
    }

    private fun calYOfContentView() {
        if (yOfContentView == -1) {
            val location = IntArray(2)
            childOfContentView.getLocationOnScreen(location)
            yOfContentView = location[1]
        }
    }

    fun showInspectorView() {
        visibility = View.VISIBLE
        calYOfContentView()
        removeAllViews()
        collectInspectItemViews()
    }

    fun hideInspectorView() {
        visibility = View.GONE
        removeAllViews()
        viewInfos.clear()
    }

    fun collectInspectItemViews() {
        if (childOfContentView is ViewGroup) {
            val innerParent = add(InspectViewInfo(childOfContentView), null)
            collectInspectItemViewsForViewGroup(childOfContentView as ViewGroup, innerParent)
        } else {
            add(InspectViewInfo(childOfContentView), null)
        }
    }


    private fun collectInspectItemViewsForViewGroup(
            view: ViewGroup,
            parentInspectItemView: InspectItemView? = null
    ) {
        view.let {
            var childCount = 0
            childCount = it.childCount
            for (index in 0 until childCount) {
                val child = it.getChildAt(index)
                if (child != null && child.visibility == View.VISIBLE) {
                    if (child is ViewGroup) {
                        val innerParent = add(InspectViewInfo(child), parentInspectItemView)
                        collectInspectItemViewsForViewGroup(child, innerParent)
                    } else {
                        add(InspectViewInfo(child), parentInspectItemView)
                    }
                }
            }
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0 until childCount) {
            val inspectItemView = getChildAt(i)
            val viewInfo = viewInfos[i]
            val location = IntArray(2)
            viewInfo.view.getLocationOnScreen(location)
            val x = location[0] // view距离 屏幕左边的距离（即x轴方向）
            val y = location[1] - yOfContentView // view距离 屏幕顶边的距离（即y轴方向）
            inspectItemView.layout(x, y, x + viewInfo.view.width, y + viewInfo.view.height)
        }
    }

    override fun dispatchDraw(canvas: Canvas?) {
        for (i in 0 until childCount) {
            val inspectItemView = getChildAt(i)
            if (inspectItemView is InspectItemView) {
                inspectItemView.drawMargin(canvas, mPaint)
            }
        }
        super.dispatchDraw(canvas)
    }


    fun showViewAttributes(view: View, inspectItemView: InspectItemView) {
        curShowedView = view
        curInspectItemView = inspectItemView
        viewAttributesPopupWindowHelper.showPopupWindow(context, view, inspectItemView)
    }

    fun hideViewAttributes() {
        curShowedView = null
        viewAttributesPopupWindowHelper.hidePopupWindow()
    }

    fun curShowedView(): View? {
        return curShowedView
    }

    data class InspectViewInfo(val view: View)

}