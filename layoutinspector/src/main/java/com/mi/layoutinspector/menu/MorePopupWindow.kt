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
    private var dpPxGroup: RadioGroup? = null
    private var dpRadio: RadioButton? = null
    private var pxRadio: RadioButton? = null

    private var viewGroupShowViewAttributesGroup: RadioGroup? = null
    private var viewGroupShowViewAttributesYesRadio: RadioButton? = null
    private var viewGroupShowViewAttributesNoRadio: RadioButton? = null

    @SuppressLint("SetTextI18n")
    fun showPopupWindow(layoutInspector: LayoutInspector, context: Context, anchor: View) {
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
        realPopupWindow!!.showAsDropDown(anchor, 2, 10, Gravity.START)
    }

    private fun initUnitsView() {
        dpPxGroup = contentView?.findViewById(R.id.dp_px_group)
        dpRadio = contentView?.findViewById(R.id.dp)
        pxRadio = contentView?.findViewById(R.id.px)
        dpPxGroup?.setOnCheckedChangeListener { radioGroup, id ->
            when (id) {
                R.id.dp -> LayoutInspector.unitsIsDP = true
                R.id.px -> LayoutInspector.unitsIsDP = false
            }
        }
    }

    private fun initViewGroupShowViews() {
        viewGroupShowViewAttributesGroup = contentView?.findViewById(R.id.viewgroup_show_group)
        viewGroupShowViewAttributesYesRadio = contentView?.findViewById(R.id.yes)
        viewGroupShowViewAttributesNoRadio = contentView?.findViewById(R.id.no)
        viewGroupShowViewAttributesGroup?.setOnCheckedChangeListener { radioGroup, id ->
            when (id) {
                R.id.yes -> isViewGroupShowViewAttributes = true
                R.id.no -> isViewGroupShowViewAttributes = false
            }
        }
    }

    private fun setDpPxRadioState() {
        if (LayoutInspector.unitsIsDP) {
            dpRadio?.isChecked = true
            pxRadio?.isChecked = false
        } else {
            dpRadio?.isChecked = false
            pxRadio?.isChecked = true
        }
    }

    private fun setResponseCLickState() {
        if (isViewGroupShowViewAttributes) {
            viewGroupShowViewAttributesYesRadio?.isChecked = true
            viewGroupShowViewAttributesNoRadio?.isChecked = false
        } else {
            viewGroupShowViewAttributesYesRadio?.isChecked = false
            viewGroupShowViewAttributesNoRadio?.isChecked = true
        }
    }
}