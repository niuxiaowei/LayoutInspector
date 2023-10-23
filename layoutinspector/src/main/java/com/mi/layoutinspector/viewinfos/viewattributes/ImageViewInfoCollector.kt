package com.mi.layoutinspector.viewinfos.viewattributes

import android.view.View
import android.widget.ImageView
import com.mi.layoutinspector.R
import com.mi.layoutinspector.inspector.IViewInspector

class ImageViewInfoCollector : IViewAttributeCollector {

    override fun collectViewAttributes(
        inspectedView: View,
        IViewInspector: IViewInspector
    ): List<ViewAttribute>? {
        if (inspectedView is ImageView) {
            val result = arrayListOf<ViewAttribute>()
            val a = 0
            inspectedView.getTag(R.id.tag_key_iv_setImageResource)?.apply {
                result.add(ViewAttribute("ImageView.setImageResource",toString()))
            }
            return result
        }

        return null
    }
}