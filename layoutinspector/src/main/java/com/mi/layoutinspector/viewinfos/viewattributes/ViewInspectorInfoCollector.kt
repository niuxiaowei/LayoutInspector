package com.mi.layoutinspector.viewinfos.viewattributes

import android.view.View
import com.mi.layoutinspector.inspector.IViewInspector

/**
 * create by niuxiaowei
 * date : 22-1-27
 **/
class ViewInspectorInfoCollector : IViewAttributeCollector {
    override fun collectViewAttribute(inspectedView: View, IViewInspector: IViewInspector): ViewAttribute? {
        return ViewAttribute("view检查器", "点击不显示该view的检查器", View.OnClickListener {
            IViewInspector.setClickable(false)
            IViewInspector.hideViewInfosPopupWindown()
        })
    }
}