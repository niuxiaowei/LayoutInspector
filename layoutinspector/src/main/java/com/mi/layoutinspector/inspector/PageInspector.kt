package com.mi.layoutinspector.inspector

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.ContextCompat
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.PopupWindow
import com.mi.layoutinspector.LayoutInspector
import com.mi.layoutinspector.R
import com.mi.layoutinspector.utils.screenIsPortrait
import com.mi.layoutinspector.viewinfos.ViewInfosPopupWindow

/**
 * create by niuxiaowei
 * date : 2021/7/30
 * 它与DeorView中android.R.id.content的子view是一一对应关系。它包含多个{@see ViewInspector}
 **/

@SuppressLint("ViewConstructor")
class PageInspector constructor(
        context: Context,
        private var childOfContentView: View,
        decorView: View?,
        isDialogMenu: Boolean = false
) : FrameLayout(context), IInspector {

    private val viewInfosPopupWindow = ViewInfosPopupWindow(decorView!!, null, isDialogMenu)
    private var curInspectedView: View? = null
    private var curViewInspector: ViewInspector? = null
    private var offsetY = -1
    private var offsetX = -1
    private var mPaint: Paint = Paint()

    private var preScreenOrientation = screenIsPortrait(context)

    init {
        mPaint.color = ContextCompat.getColor(context, R.color.li_color_34b1f3)
        mPaint.strokeWidth = 2.0f
        //InspectPage的点击事件拦截掉
        setOnClickListener {
            Log.i("LayoutInspector", "InspectPage  click")
        }
        visibility = View.GONE
    }

    private fun add(
            view: View,
            parent: ViewInspector?
    ): ViewInspector {
        val isSetClick4View =
                !(view is ViewGroup && !LayoutInspector.isViewGroupShowViewInspector)
        val view = ViewInspector(
                context,
                view,
                this,
                isSetClick4View,
                parent
        )
        val lp = LayoutParams(view.width, view.height)
        addView(view, lp)
        return view
    }

    private fun calOffset() {
        val location = IntArray(2)
        childOfContentView.getLocationOnScreen(location)
        offsetY = location[1]
        offsetX = location[0]
    }

    override fun hideInspectors() {
        visibility = View.GONE
        removeAllViews()
    }

    private fun setSize() {
        var resetLayoutParams = false
        val lp = layoutParams.apply {
            if (width != childOfContentView.width) {
                width = childOfContentView.width
                resetLayoutParams = true
            }
            if (height != childOfContentView.height) {
                height = childOfContentView.height
                resetLayoutParams = true
            }
        }
        if (resetLayoutParams) {
            layoutParams = lp
        }
    }

    override fun showInspectors() {
        setSize()
        visibility = View.VISIBLE
        calOffset()
        removeAllViews()
        collectViewInspectors()
    }

    private fun collectViewInspectors() {
        if (childOfContentView is ViewGroup) {
            val parent = add(childOfContentView, null)
            collectViewInspectorsForViewGroup(childOfContentView as ViewGroup, parent)
        } else {
            add(childOfContentView, null)
        }
    }


    private fun collectViewInspectorsForViewGroup(
            view: ViewGroup,
            parent: ViewInspector? = null
    ) {
        view.let {
            var childCount = 0
            childCount = it.childCount
            for (index in 0 until childCount) {
                val child = it.getChildAt(index)
                if (child != null && child.visibility != View.GONE) {
                    if (child is ViewGroup) {
                        val innerParent = add(child, parent)
                        collectViewInspectorsForViewGroup(child, innerParent)
                    } else {
                        add(child, parent)
                    }
                }
            }
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0 until childCount) {
            val viewInspector = getChildAt(i) as ViewInspector
            val location = IntArray(2)
            viewInspector.inspectedView().getLocationOnScreen(location)
            val x = location[0] - offsetX// view距离 屏幕左边的距离（即x轴方向）
            val y = location[1] - offsetY // view距离 屏幕顶边的距离（即y轴方向）
            viewInspector.apply {
                layout(x, y, x + viewInspector.inspectedView().width, y + viewInspector.inspectedView().height)
                isOutOfScreen =
                        x * y < 0 || x >= this@PageInspector.measuredWidth || y >= this@PageInspector.measuredHeight
            }
        }
    }

    override fun dispatchDraw(canvas: Canvas?) {
        if (LayoutInspector.isShowViewMargin) {
            for (i in 0 until childCount) {
                val inspectItemView = getChildAt(i)
                if (inspectItemView is ViewInspector) {
                    inspectItemView.drawMargin(canvas, mPaint)
                }
            }
        }
        super.dispatchDraw(canvas)
    }


    fun showViewInfosPopupWindow(view: View, viewInspector: ViewInspector) {
        curInspectedView = view
        curViewInspector = viewInspector
        viewInfosPopupWindow.showViewInfos(context, view, viewInspector)
    }

    fun hideViewInfosPopupWindow() {
        curViewInspector?.setSelecte(false)
        curInspectedView = null
        viewInfosPopupWindow.hideViewInfos()
        curViewInspector = null
    }

    fun curInspectedView(): View? {
        return curInspectedView
    }

}