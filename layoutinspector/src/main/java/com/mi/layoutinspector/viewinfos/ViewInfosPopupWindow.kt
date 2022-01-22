package com.mi.layoutinspector.viewinfos

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.mi.layoutinspector.LayoutInspector
import com.mi.layoutinspector.LayoutInspector.Companion.getScreenHeight
import com.mi.layoutinspector.LayoutInspector.Companion.getScreenWidth
import com.mi.layoutinspector.LayoutInspector.Companion.getViewAttributesCollectors
import com.mi.layoutinspector.R
import com.mi.layoutinspector.inspect.InspectItemView
import com.mi.layoutinspector.utils.screenIsPortrait
import com.mi.layoutinspector.viewinfos.viewattributes.IViewAttributeCollector
import com.mi.layoutinspector.viewinfos.viewattributes.ViewAttribute
import com.mi.layoutinspector.viewinfos.viewattributes.ViewAttributesAdapter
import com.mi.layoutinspector.viewinfos.viewhierarchy.HierarchyItem
import com.mi.layoutinspector.viewinfos.viewhierarchy.ViewHierarchyAdapter
import kotlinx.android.synthetic.main.layoutinspector_popupwindow_detail_view.view.*
import java.lang.Exception
import java.util.ArrayList

/**
 * Copyright (C) 2020, niuxiaowei. All rights reserved.
 * <p>
 * @author niuxiaowei
 * @date 2022/1/22.
 */
class ViewInfosPopupWindow(
    private val decorView: View?,
    private val onDismissListener: PopupWindow.OnDismissListener?
) {
    private var realPopupWindow: PopupWindow? = null

    private var viewAttributesAdapter: ViewAttributesAdapter? = null

    private var viewHierarchyAdapter: ViewHierarchyAdapter? = null

    /**
     * 因此viewinfos
     */
    fun hideViewInfos() {
        if (realPopupWindow == null) {
            return
        }
        realPopupWindow!!.dismiss()
    }

    private fun getPopupWindowHeight(context: Context): Int {
        return if (screenIsPortrait(context)) {
            (getScreenHeight() * 0.45).toInt()
        } else {
            (getScreenHeight() * 0.9).toInt()
        }
    }


    private fun initRealPopupWindow(context: Context) {
        val view: View = LayoutInflater.from(context)
            .inflate(R.layout.layoutinspector_popupwindow_detail_view, null)
        initViewAttributeViews(view, context)
        initViewHierarchyViews(view, context)
        realPopupWindow = PopupWindow(
            view, LinearLayout.LayoutParams.WRAP_CONTENT,
            getPopupWindowHeight(context)
        ).apply {
            isOutsideTouchable = true
            isFocusable = true
            setOnDismissListener {
                onDismissListener?.onDismiss()
            }
        }

        view.tab_view_attribute.performClick()
    }

    private fun initViewAttributeViews(rootView: View, context: Context) {
        rootView.apply {
            tab_view_attribute.apply {
                setOnClickListener {
                    rootView.view_hierarchy_recycler_view.visibility = View.GONE
                    rootView.view_attribute_recycler_view.visibility = View.VISIBLE
                    rootView.tab_view_hierarchy.isSelected = false
                    isSelected = true
                }
            }

            view_attribute_recycler_view.apply {
                layoutManager = LinearLayoutManager(context)
                viewAttributesAdapter = ViewAttributesAdapter()
                adapter = viewAttributesAdapter
            }
        }
    }

    private fun initViewHierarchyViews(rootView: View, context: Context) {
        rootView.apply {
            tab_view_hierarchy.apply {
                setOnClickListener {
                    rootView.view_hierarchy_recycler_view.visibility = View.VISIBLE
                    rootView.view_attribute_recycler_view.visibility = View.GONE
                    rootView.tab_view_attribute.isSelected = false
                    isSelected = true
                }
            }
            view_hierarchy_recycler_view.apply {
                layoutManager = LinearLayoutManager(context)
                viewHierarchyAdapter = ViewHierarchyAdapter()
                adapter = viewHierarchyAdapter

            }
        }
    }

    /**
     * 显示view信息
     *
     * @param context
     * @param inspectedView   被检测器检测的view
     * @param inspectItemView
     */
    fun showViewInfos(
        context: Context, inspectedView: View,
        inspectItemView: InspectItemView
    ) {
        if (realPopupWindow == null) {
            initRealPopupWindow(context)
        }
        realPopupWindow!!.height = getPopupWindowHeight(context)

        //设置数据
        viewAttributesAdapter!!.setDatas(collectViewAttributes(inspectedView, inspectItemView))
        viewHierarchyAdapter!!.setDatas(collectHierarchyItems(inspectItemView))
        viewHierarchyAdapter!!.setInspectItemView(inspectItemView)
        val size = getPopupWindowSize()
        val popupWindowPos = calculatePopWindowPos(inspectedView, size[1], size[0])
        realPopupWindow!!.showAtLocation(
            inspectItemView,
            Gravity.LEFT or Gravity.TOP,
            popupWindowPos[0],
            popupWindowPos[1]
        )
    }

    /**
     * @return 返回popupwindow的size，size[0]:width   size[1]: height
     */
    private fun getPopupWindowSize(): IntArray {
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
    private fun calculatePopWindowPos(
        anchorView: View,
        popupHeight: Int,
        popupWidth: Int
    ): IntArray {
        val result = IntArray(2)
        val anchorViewLocation = IntArray(2)
        val decorViewLocation = IntArray(2)
        anchorView.getLocationOnScreen(anchorViewLocation)
        decorView?.getLocationOnScreen(decorViewLocation)
        val anchorHeight = anchorView.height
        val anchorWidth = anchorView.width
        if (screenIsPortrait(anchorView.context)) {
            // 竖屏的时候展示在anchor的上边或下边。判断需要在anchorView向上弹出还是向下弹出显示
            val isNeedShowUp =
                getScreenHeight() - anchorViewLocation[1] - anchorHeight < popupHeight
            result[0] =
                anchorViewLocation[0] - decorViewLocation[0] + anchorWidth / 2 - popupWidth / 2
            if (isNeedShowUp) {
                result[1] = anchorViewLocation[1] - decorViewLocation[1] - popupHeight
            } else {
                result[1] = anchorViewLocation[1] - decorViewLocation[1] + anchorHeight
            }
        } else {
            // 横屏的时候展示在anchor的左边或右边，判断需要左anchorView边还是右边弹出
            val isNeedShowRight =
                anchorViewLocation[0] + anchorWidth + popupWidth > getScreenWidth()
            if (isNeedShowRight) {
                result[0] = anchorViewLocation[0] - decorViewLocation[0] - popupWidth
            } else {
                result[0] = anchorViewLocation[0] - decorViewLocation[0] + anchorWidth
            }
            result[1] = anchorViewLocation[1] - decorViewLocation[1]
        }
        return result
    }

    private fun collectViewAttributes(
        inspectedView: View,
        inspectItemView: InspectItemView
    ): List<ViewAttribute>? {
        val viewAttributes: MutableList<ViewAttribute> = ArrayList()
        val collectors = getViewAttributesCollectors()
        for (i in collectors.indices) {
            val viewDetailCollector = collectors[i]
            val viewAttribute = viewDetailCollector.collectViewAttribute(
                inspectedView,
                inspectItemView
            )
            if (viewAttribute != null) {
                viewAttributes.add(viewAttribute)
            }
            val collectViewAttributes =
                viewDetailCollector.collectViewAttributes(inspectedView, inspectItemView)
            if (collectViewAttributes != null && collectViewAttributes.size > 0) {
                viewAttributes.addAll(collectViewAttributes)
            }
        }
        return viewAttributes
    }

    private fun collectChilds(
        hierarchyItems: MutableList<HierarchyItem>, parent: InspectItemView,
        parentHierarchy: HierarchyItem
    ) {
        if (parent.childs() != null) {
            for (j in parent.childs()!!.indices) {
                val child = parent.childs()!![j] as InspectItemView
                val id = getId(child.inspectedView())
                hierarchyItems.add(
                    HierarchyItem(
                        child.inspectedView().javaClass.simpleName + id,
                        child,
                        parentHierarchy,
                        false
                    )
                )
            }
        }
    }

    /**
     * 只收集当前inspectItemView.inspectedView的子控件和它的兄弟控件，剩下的只收集 它的父级控件  （有的界面很复杂的话，显示的界面会非常多，因此不会收集那么多）
     *
     * @param inspectItemView
     * @return
     */
    private fun collectHierarchyItems(inspectItemView: InspectItemView): List<HierarchyItem>? {
        val result: MutableList<HierarchyItem> = ArrayList()
        var parent = inspectItemView.parent() as InspectItemView?
        var parentHierarchy: HierarchyItem? = null
        if (parent != null && parent.childs() != null) {
            //收集兄弟控件
            parentHierarchy = HierarchyItem(
                parent.inspectedView().javaClass.simpleName + getId(parent.inspectedView()),
                parent,
                null,
                false
            )
            for (i in parent.childs()!!.indices) {
                val brother = parent.childs()!![i] as InspectItemView
                val brotherHierarchy = HierarchyItem(
                    brother.inspectedView().javaClass.simpleName + getId(brother.inspectedView()),
                    brother,
                    parentHierarchy,
                    brother == inspectItemView
                )
                result.add(brotherHierarchy)
                val isCurView = brother == inspectItemView
                if (isCurView) {
                    //收集当前控件的子控件
                    collectChilds(result, inspectItemView, brotherHierarchy)
                }
            }
            result.add(0, parentHierarchy)
        } else {
            parentHierarchy = HierarchyItem(
                inspectItemView.inspectedView().javaClass.simpleName + getId(inspectItemView.inspectedView()),
                inspectItemView,
                null,
                true
            )
            result.add(parentHierarchy)
            //收集当前控件的子控件
            collectChilds(result, inspectItemView, parentHierarchy)
        }

        //收集爷爷辈的控件
        var preItem: HierarchyItem = parentHierarchy
        if (parent != null) {
            parent = parent.parent() as InspectItemView?
        }
        while (parent != null) {
            val curItem = HierarchyItem(
                parent.inspectedView().javaClass.simpleName + getId(parent.inspectedView()),
                parent,
                null,
                false
            )
            preItem.parent = curItem
            preItem = curItem
            result.add(0, curItem)
            parent = parent.parent() as InspectItemView?
        }

        //add blank count
        val addBlankCount = 4
        for (i in result.indices) {
            val hierarchyItem = result[i]
            if (hierarchyItem.parent != null) {
                hierarchyItem.blankCount = hierarchyItem.parent!!.blankCount + addBlankCount
            }
        }
        return result
    }

    private fun getId(view: View): String {
        try {
            val entryname = view.resources.getResourceEntryName(view.id)
            return "(R.id.$entryname)"
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}