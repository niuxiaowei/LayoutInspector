package com.mi.layoutinspector.menu

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.view.*
import com.mi.layoutinspector.inspector.ActivityInspector
import com.mi.layoutinspector.R
import com.mi.layoutinspector.utils.distance
import com.mi.layoutinspector.utils.px2dip
import kotlinx.android.synthetic.main.layoutinspector_view_inspector_ui_menu.view.*


/**
 * Copyright (C) 2020, niuxiaowei. All rights reserved.
 * <p>
 * @author niuxiaowei
 * @date 2022/1/8.
 */
class InspectorMenu(private val activityInspector: ActivityInspector) {
    private val morePopupWindow: MorePopupWindow = MorePopupWindow()
    private var menuView: View? = null
    private val context: Context = activityInspector.activity

    fun onCreate() {
        menuView = createMenuView()
        createLayoutParam().let {
            setOnTouchListener(it)
            activityInspector.activity.windowManager.addView(menuView, it)
        }
    }

    private fun setOnTouchListener(layoutParam: WindowManager.LayoutParams) {
        ViewTouchListener(
                layoutParam,
                activityInspector.activity.windowManager,
                menuView!!
        ).let {
            menuView?.setOnTouchListener(it)
            menuView?.show?.setOnTouchListener(it)
            menuView?.more?.setOnTouchListener(it)
        }
    }

    private fun createLayoutParam(): WindowManager.LayoutParams {
        return WindowManager.LayoutParams().apply {
            //设置大小 自适应
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            flags =
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
            x = 0
            y = 200
            gravity = Gravity.LEFT or Gravity.TOP
            format = PixelFormat.TRANSPARENT
        }
    }

    private class ViewTouchListener(
            private val wl: WindowManager.LayoutParams,
            private val windowManager: WindowManager,
            private val rootView: View
    ) : View.OnTouchListener {
        private var x = 0
        private var y = 0
        private var pressStartTime = 0L
        private var moveDistance = 0f

        private companion object {
            const val MAX_CLICK_DURATION = 1000
            const val MAX_CLICK_DISTANCE = 15
        }

        override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    x = motionEvent.rawX.toInt()
                    y = motionEvent.rawY.toInt()
                    pressStartTime = System.currentTimeMillis();
                    //判断移动的正数距离
                    moveDistance = 0f
                }
                MotionEvent.ACTION_MOVE -> {
                    val nowX = motionEvent.rawX.toInt()
                    val nowY = motionEvent.rawY.toInt()
                    moveDistance += px2dip(distance(nowX, nowY, x, y))
                    val movedX = nowX - x
                    val movedY = nowY - y
                    x = nowX
                    y = nowY
                    wl.apply {
                        x += movedX
                        y += movedY
                    }
                    //更新悬浮球控件位置
                    windowManager.updateViewLayout(rootView, wl)
                }
                MotionEvent.ACTION_UP -> {
                    val pressDuration = System.currentTimeMillis() - pressStartTime
                    if ((pressDuration < MAX_CLICK_DURATION && moveDistance < MAX_CLICK_DISTANCE)) {
                        view.performClick()
                    }
                    moveDistance = 0f
                }
            }
            return true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun createMenuView(): View {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.layoutinspector_view_inspector_ui_menu, null)
        view.show.apply {
            text = "显示"
            if (activityInspector.inspectorsShowed) {
                text = "隐藏"
            }
            setOnClickListener {
                if (activityInspector.inspectorsShowed) {
                    activityInspector.hideInspectors()
                    text = "显示"
                } else {
                    activityInspector.showInspectors()
                    text = "隐藏"
                }
            }
        }
        view.more.apply {

            setOnClickListener {
                activityInspector.hideInspectors()
                view.show.text = "显示"
                morePopupWindow.showPopupWindow(
                        activityInspector,
                        activityInspector.activity,
                        view.more
                )
            }
        }

        menuView = view
        return view
    }

    fun onDestory() {
        activityInspector.activity.windowManager.removeView(menuView)
    }
}