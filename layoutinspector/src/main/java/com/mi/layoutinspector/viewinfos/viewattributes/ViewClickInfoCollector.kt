package com.mi.layoutinspector.viewinfos.viewattributes

import android.os.Build
import android.support.annotation.RequiresApi
import android.view.View
import com.mi.layoutinspector.R
import com.mi.layoutinspector.inspector.IViewInspector

/**
 * create by niuxiaowei
 * date : 22-1-18
 * view点击事件信息收集器
 **/
class ViewClickInfoCollector : IViewAttributeCollector {

    companion object{
        val TAG_CLICK_INTO = R.id.tag_key_click
        val TAG_LONG_CLICK_INTO = R.id.tag_key_long_click
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun collectViewAttributes(inspectedView: View, IViewInspector: IViewInspector): List<ViewAttribute>? {
        val result = arrayListOf<ViewAttribute>()
        result.add(ViewAttribute("是否设置onClickListener", if (inspectedView.hasOnClickListeners()) "是" else "否"))
        inspectedView.getTag(TAG_CLICK_INTO)?.let {
            result.add(ViewAttribute("onClickListener位置", it.toString()))
        }
        inspectedView.getTag(TAG_LONG_CLICK_INTO)?.let {
            result.add(ViewAttribute("是否设置onLongClickListener", "是"))
            result.add(ViewAttribute("onLongClickListener位置", it.toString()))
        }
        return result
    }
}