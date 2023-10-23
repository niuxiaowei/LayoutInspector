package com.mi.layoutinspector.menu

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.util.Log
import android.view.*
import android.widget.FrameLayout
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
class InspectorMenu(private val activityInspector: ActivityInspector, private val contentView: ViewGroup ) {
    private val morePopupWindow: MorePopupWindow = MorePopupWindow()
    private var menuView: View? = null
    private val context: Context = activityInspector.activity
    private var layoutParam: FrameLayout.LayoutParams? = null

    fun onCreate() {
        if (menuView == null) {
            menuView = createMenuView()
            layoutParam = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                leftMargin = 20
                topMargin = 200
            }
            layoutParam?.let { setOnTouchListener(it) }
        }
        contentView.addView(menuView,layoutParam)
    }

    fun onDestory() {
        contentView.removeView(menuView)
    }

    private fun setOnTouchListener(layoutParam: FrameLayout.LayoutParams) {
        ViewTouchListener(
                layoutParam,
                menuView!!
        ).let {
            menuView?.setOnTouchListener(it)
            menuView?.show?.setOnTouchListener(it)
            menuView?.more?.setOnTouchListener(it)
        }
    }

    private class ViewTouchListener(
            private val wl: FrameLayout.LayoutParams,
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
            Log.i("inspectormenu","action:"+motionEvent.action+" view:"+view)
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
                        leftMargin += movedX
                        topMargin += movedY
                    }
                    rootView.layoutParams = wl
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

        view.setTag(R.layout.layoutinspector_view_inspector_ui_menu,"inspector_menu_view")

        menuView = view
        return view
    }

    companion object{
        fun isMenuView(view: View):Boolean{
            val tag = view.getTag(R.layout.layoutinspector_view_inspector_ui_menu)
            return "inspector_menu_view" == tag
        }
    }
}