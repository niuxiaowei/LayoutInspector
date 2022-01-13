package com.mi.layoutinspector

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.view_inspector_ui_menu.view.*
import kotlin.math.sqrt


/**
 * Copyright (C) 2020, niuxiaowei. All rights reserved.
 * <p>
 * @author niuxiaowei
 * @date 2022/1/8.
 * 承载菜单的page
 */
class InspectMenuPage(context: Context,
                      private val layoutInspector: LayoutInspector,
                      private val inspectPageManager: InspectPageManager
) : FrameLayout(context) {
    private var lastX: Int = 0
    private var lastY: Int = 100
    private var tempLastX: Int = 0
    private var tempLastY: Int = 0
    private val morePopupWindow: MorePopupWindow = MorePopupWindow()
    private var pressStartTime: Long = 0
    private val MAX_CLICK_DISTANCE = 15

    //  Max allowed duration for a"click", in milliseconds.
    private val MAX_CLICK_DURATION = 1000
    private var menuViewWidth = 0
    private var menuViewHeight = 0
    private var menuView: View? = null
    private var moveDistance: Float = 0f


    init {
        val menuView = createMenuView()
        val layoutParams = LayoutParams(-2, -2)
        layoutParams.apply {
            leftMargin = lastX
            topMargin = lastY
        }
        addView(menuView, layoutParams)
    }

    private fun startDrag(parentView: View, view: View?, event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()
        if (menuViewHeight == 0) {
            menuViewHeight = menuView?.height!!
            menuViewWidth = menuView?.width!!
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = x
                lastY = y
                pressStartTime = System.currentTimeMillis();
                //判断移动的正数距离
                moveDistance = 0f
                tempLastX = x
                tempLastY = y
            }
            MotionEvent.ACTION_MOVE -> {
                //是点击事件
                // 计算偏移量
                val offsetX = x - lastX
                val offsetY = y - lastY
                // 在当前left、top、right、bottom的基础上加上偏移量
                val lp = parentView.layoutParams as LayoutParams
                val isInScreen = (lp.leftMargin + offsetX > 0 && lp.leftMargin + offsetX + menuViewWidth < LayoutInspector.getScreenWidth() && lp.topMargin + offsetY > 0 && lp.topMargin + offsetY + menuViewHeight + 600 < LayoutInspector.getScreenHeight())
                if (isInScreen) {
                    lp.leftMargin = lp.leftMargin + offsetX
                    lp.topMargin = lp.topMargin + offsetY
                    parentView.layoutParams = lp
                }
                moveDistance += distance(tempLastX, tempLastY, x, y)
            }
            MotionEvent.ACTION_UP -> {
                val pressDuration = System.currentTimeMillis() - pressStartTime
                if ((pressDuration < MAX_CLICK_DURATION && moveDistance < MAX_CLICK_DISTANCE)) {
                    view?.performClick()
                }
                moveDistance = 0f
            }
        }
        return true
    }


    private fun distance(x1: Int, y1: Int, x2: Int, y2: Int): Float {
        val dx = x1 - x2
        val dy = y1 - y2
        val distanceInPx = sqrt(dx * dx + dy * dy.toDouble()).toFloat()
        return pxToDp(distanceInPx)
    }

    private fun pxToDp(px: Float): Float {
        return px / resources.displayMetrics.density
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun createMenuView(): View {
        val view = LayoutInflater.from(context).inflate(R.layout.view_inspector_ui_menu, null)
        view.setOnTouchListener { v, event ->
            return@setOnTouchListener startDrag(v, null, event)
        }

        view.show.apply {
            text = "显示"
            if (inspectPageManager.inspectorViewShowed) {
                text = "隐藏"
            }
            setOnTouchListener { v, event -> return@setOnTouchListener startDrag(view, v, event) }
            setOnClickListener {
                if (inspectPageManager.inspectorViewShowed) {
                    inspectPageManager.hideInspectorView()
                    text = "显示"
                } else {
                    inspectPageManager.showInspectorView()
                    text = "隐藏"
                }
            }
        }
        view.more.apply {
            setOnTouchListener { v, event -> return@setOnTouchListener startDrag(view, v, event) }

            setOnClickListener {
                inspectPageManager.hideInspectorView()
                inspectPageManager.hideShowedView()
                view.show.text = "显示"
                morePopupWindow.showPopupWindow(
                        layoutInspector,
                        layoutInspector.activity,
                        view.more
                )
            }
        }

        menuView = view
        return view
    }
}