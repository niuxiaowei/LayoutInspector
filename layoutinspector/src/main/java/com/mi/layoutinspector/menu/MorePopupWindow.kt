package com.mi.layoutinspector.menu

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RadioButton
import android.widget.RadioGroup
import com.mi.layoutinspector.LayoutInspector
import com.mi.layoutinspector.LayoutInspector.Companion.isViewGroupShowViewAttributes
import com.mi.layoutinspector.R
import kotlinx.android.synthetic.main.layoutinspector_popupwindow_more_view.view.*

/**
 * create by niuxiaowei
 * date : 2021/7/30
 **/
class MorePopupWindow {
    private var realPopupWindow: PopupWindow? = null
    private var contentView: View? = null
    private var layoutInspector:LayoutInspector? = null

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
        realPopupWindow!!.showAsDropDown(anchor, 2, 10, Gravity.START)
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