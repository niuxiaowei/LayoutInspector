package com.mi.layoutinspector.menu

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.mi.layoutinspector.R
import com.mi.layoutinspector.inspector.DialogInspector
import com.mi.layoutinspector.utils.distance
import com.mi.layoutinspector.utils.px2dip
import kotlinx.android.synthetic.main.layoutinspector_view_inspector_ui_menu.view.*

/**
 * Copyright (C) 2020, niuxiaowei. All rights reserved.
 * <p>
 * @author niuxiaowei
 * @date 2022/1/23.
 *
 */
class DialogInspectorMenu(context: Context, private val dialogInspector: DialogInspector) : FrameLayout(context) {

    init {
        val menuView = createMenuView()
        val layoutParams = LayoutParams(-2, -2)
        layoutParams.apply {
            leftMargin = 0
            topMargin = 10
        }
        addView(menuView, layoutParams)
    }

    companion object {
        const val MAX_CLICK_DURATION = 1000
        const val MAX_CLICK_DISTANCE = 15
    }

    private inner class OnTouchListenerImpl(private val menuRootView: View) : OnTouchListener {

        private var menuViewWidth = 0
        private var menuViewHeight = 0
        private var moveDistance: Float = 0f
        private var lastX: Int = 0
        private var lastY: Int = 0
        private var pressStartTime: Long = 0
        private var tempLastX: Int = 0
        private var tempLastY: Int = 0
        private var dialogWidth: Int = 0
        private var dialogHeight: Int = 0




        override fun onTouch(v: View?, event: MotionEvent): Boolean {
            val x = event.x.toInt()
            val y = event.y.toInt()
            if (menuViewHeight == 0) {
                menuViewHeight = menuRootView.height
                menuViewWidth = menuRootView.width
            }

            if (dialogWidth == 0) {
                dialogWidth = if (this@DialogInspectorMenu.parent is View) (this@DialogInspectorMenu.parent as View).width else 0
                dialogHeight = if (this@DialogInspectorMenu.parent is View) (this@DialogInspectorMenu.parent as View).height else 0
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
                    val lp = menuRootView.layoutParams as LayoutParams
                    val isInScreen =
                            (lp.leftMargin + offsetX > 0 && lp.leftMargin + offsetX + menuViewWidth < dialogWidth && lp.topMargin + offsetY > 0 && lp.topMargin + offsetY + menuViewHeight < dialogHeight)
                    if (isInScreen) {
                        lp.leftMargin = lp.leftMargin + offsetX
                        lp.topMargin = lp.topMargin + offsetY
                        menuRootView.layoutParams = lp
                    }
                    moveDistance += px2dip(distance(tempLastX, tempLastY, x, y))
                }
                MotionEvent.ACTION_UP -> {
                    val pressDuration = System.currentTimeMillis() - pressStartTime
                    if ((pressDuration < MAX_CLICK_DURATION && moveDistance < MAX_CLICK_DISTANCE)) {
                        v?.performClick()
                    }
                    moveDistance = 0f
                }
            }
            return true
        }
    }


    @SuppressLint("ClickableViewAccessibility", "InflateParams")
    private fun createMenuView(): View {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.layoutinspector_view_inspector_ui_menu, null)
        OnTouchListenerImpl(view).let {
            view.setOnTouchListener(it)
            view.show.setOnTouchListener(it)
            view.more.setOnTouchListener(it)
        }

        view.show.apply {
            text = "显示"
            if (dialogInspector.inspectorsShowed) {
                text = "隐藏"
            }
            setOnClickListener {
                if (dialogInspector.inspectorsShowed) {
                    dialogInspector.hideInspectors()
                    text = "显示"
                } else {
                    dialogInspector.showInspectors()
                    text = "隐藏"
                }
            }
        }
        view.more.visibility = View.GONE
        return view
    }
}

