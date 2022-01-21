package com.mi.layoutinspector.menu

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.mi.layoutinspector.LayoutInspector
import com.mi.layoutinspector.LayoutInspector.Companion.getScreenHeight
import com.mi.layoutinspector.LayoutInspector.Companion.getScreenWidth
import com.mi.layoutinspector.LayoutInspector.Companion.isViewGroupShowViewAttributes
import com.mi.layoutinspector.R
import com.mi.layoutinspector.utils.screenIsPortrait
import kotlinx.android.synthetic.main.layoutinspector_popupwindow_more_view.view.*

/**
 * create by niuxiaowei
 * date : 2021/7/30
 **/
class MorePopupWindow {
    private var realPopupWindow: PopupWindow? = null
    private var contentView: View? = null
    private var layoutInspector: LayoutInspector? = null

    @SuppressLint("SetTextI18n")
    fun showPopupWindow(layoutInspector: LayoutInspector, context: Context, anchor: View) {
        this.layoutInspector = layoutInspector
        if (realPopupWindow == null) {
            contentView = LayoutInflater.from(context).inflate(R.layout.layoutinspector_popupwindow_more_view, null)
            realPopupWindow = object : PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT) {}
            (realPopupWindow as PopupWindow).apply {
                isOutsideTouchable = true
                isFocusable = true
            }
            initUnitsView()
            initViewGroupShowViews()
        }
        contentView?.apply {
            activity_name.text = "当前Activity: ${layoutInspector.activityName}"
            layout_id.text = "当前Activity的layout：R.layout.${layoutInspector.contentViewIdName}"
        }
        setDpPxRadioState()
        setResponseCLickState()
        initFragmentsView()
        val size = getPopupWindowSize()
        val popupWindowPos = calculatePopWindowPos(anchor, size!![1], size!![0])
        realPopupWindow!!.showAtLocation(anchor, Gravity.LEFT or Gravity.TOP, popupWindowPos!![0], popupWindowPos[1])
    }

    /**
     * @return 返回popupwindow的size，size[0]:width   size[1]: height
     */
    private fun getPopupWindowSize(): IntArray? {
        val size = IntArray(2)
        size[0] = realPopupWindow!!.contentView.measuredWidth
        size[1] = realPopupWindow!!.contentView.measuredHeight
        return size
    }

    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     *
     * @param anchorView  呼出window的view
     * @param popupHeight
     * @param popupWidth
     * @return window显示的左上角的xOff, yOff坐标
     */
    private fun calculatePopWindowPos(anchorView: View, popupHeight: Int, popupWidth: Int): IntArray? {
        val result = IntArray(2)
        val anchorLoc = IntArray(2)
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc)
        val anchorHeight = anchorView.height
        val anchorWidth = anchorView.width
        // 判断需要在anchorView向上弹出还是向下弹出显示
        val isNeedShowUp = getScreenHeight() - anchorLoc[1] - anchorHeight < popupHeight
        result[0] = anchorLoc[0] + anchorWidth / 2 - popupWidth / 2
        if (isNeedShowUp) {
            result[1] = anchorLoc[1] - popupHeight
        } else {
            result[1] = anchorLoc[1] + anchorHeight
        }
        return result
    }

    private fun initUnitsView() {
        contentView?.dp_px_group?.setOnCheckedChangeListener { radioGroup, id ->
            when (id) {
                R.id.dp -> LayoutInspector.unitsIsDP = true
                R.id.px -> LayoutInspector.unitsIsDP = false
            }
        }
    }

    private fun initFragmentsView() {
        var fragmentsStr = ""
        var index = 0
        layoutInspector?.fragments?.getFragmentClassNames()?.forEach {
            if (index++ == 0) {
                fragmentsStr += "当前fragment  $it \n"
            } else {
                fragmentsStr += "              $it \n"
            }
        }
        contentView?.fragments?.text = fragmentsStr
    }

    private fun initViewGroupShowViews() {
        contentView?.viewgroup_show_group?.setOnCheckedChangeListener { radioGroup, id ->
            when (id) {
                R.id.yes -> isViewGroupShowViewAttributes = true
                R.id.no -> isViewGroupShowViewAttributes = false
            }
        }
    }

    private fun setDpPxRadioState() {
        contentView?.apply {
            if (LayoutInspector.unitsIsDP) {
                dp.isChecked = true
                px.isChecked = false
            } else {
                dp.isChecked = false
                px.isChecked = true
            }
        }

    }

    private fun setResponseCLickState() {
        contentView?.apply {
            if (isViewGroupShowViewAttributes) {
                yes.isChecked = true
                no?.isChecked = false
            } else {
                yes.isChecked = false
                no.isChecked = true
            }
        }
    }
}