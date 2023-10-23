package com.mi.layoutinspector.viewinfos

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.mi.layoutinspector.LayoutInspector.getViewAttributesCollectors
import com.mi.layoutinspector.R
import com.mi.layoutinspector.inspector.ViewInspector
import com.mi.layoutinspector.utils.*
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
    private val decorView: View,
    private val onDismissListener: PopupWindow.OnDismissListener?,
    private val isDialogMenu: Boolean = false
) {
    private val realPopupWindow: PopupWindow by lazy {
        val context = decorView.context
        val view: View = LayoutInflater.from(context)
                .inflate(R.layout.layoutinspector_popupwindow_detail_view, null)
        initViewAttributeViews(view, context)
        initViewHierarchyViews(view, context)
        view.tab_view_attribute.performClick()
        PopupWindow(
                view, getPopupWindowWidth(context),
                getPopupWindowHeight(context)
        ).apply {
            isOutsideTouchable = true
            isFocusable = true
            setOnDismissListener {
                onDismissListener?.onDismiss()
            }
        }
    }

    private var viewAttributesAdapter: ViewAttributesAdapter? = null

    private var viewHierarchyAdapter: ViewHierarchyAdapter? = null

    /**
     * 因此viewinfos
     */
    fun hideViewInfos() {
        realPopupWindow.dismiss()
    }

    private fun getPopupWindowHeight(context: Context): Int {
        if (isDialogMenu) {
            return context.resources.getDimension(R.dimen.layout_inspector_popup_height).toInt()
        }
        return if (screenIsPortrait(context)) {
            (decorView.height * if (isPad(context)) 0.45 else 0.8).toInt()
        } else {
            (decorView.height * if (isPad(context)) 0.45 else 0.8).toInt()
        }
    }


    private fun getPopupWindowWidth(context: Context): Int {
        if (isDialogMenu) {
            return context.resources.getDimension(R.dimen.layout_inspector_popup_width).toInt()
        }
        return if (screenIsPortrait(context)) {
            (decorView.width * if (isPad(context)) 0.5 else 0.8).toInt()
        } else {
            (decorView.height * if (isPad(context)) 0.5 else 0.8).toInt()
        }
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
                layoutManager =
                    LinearLayoutManager(context)
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
                layoutManager =
                    LinearLayoutManager(context)
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
     * @param viewInspector
     */
    fun showViewInfos(
            context: Context, inspectedView: View,
            viewInspector: ViewInspector
    ) {
        realPopupWindow.height = getPopupWindowHeight(context)
        realPopupWindow.height = getPopupWindowWidth(context)

        //设置数据
        viewAttributesAdapter?.setDatas(collectViewAttributes(inspectedView, viewInspector))
        viewHierarchyAdapter?.setDatas(collectHierarchyItems(viewInspector))
        viewHierarchyAdapter?.setInspectItemView(viewInspector)


        realPopupWindow.let {
            val size = getPopupWindowSize(it)
            if (isDialogMenu) {
                it.showAsDropDown(decorView)
            } else {
                val offsets = calculatePopWindowOffsets(inspectedView, size[1], size[0], decorView)
                it.showAtLocation(
                    decorView,
                    Gravity.LEFT or Gravity.TOP,
                    offsets[0],
                    offsets[1]
                )
            }
        }
    }


    private fun collectViewAttributes(
            inspectedView: View,
            viewInspector: ViewInspector
    ): List<ViewAttribute>? {
        val viewAttributes: MutableList<ViewAttribute> = ArrayList()
        val collectors = getViewAttributesCollectors()
        for (i in collectors.indices) {
            val viewDetailCollector = collectors[i]
            val viewAttribute = viewDetailCollector.collectViewAttribute(
                    inspectedView,
                    viewInspector
            )
            if (viewAttribute != null) {
                viewAttributes.add(viewAttribute)
            }
            val collectViewAttributes =
                    viewDetailCollector.collectViewAttributes(inspectedView, viewInspector)
            if (collectViewAttributes != null && collectViewAttributes.isNotEmpty()) {
                viewAttributes.addAll(collectViewAttributes)
            }
        }
        return viewAttributes
    }

    private fun collectChilds(
            hierarchyItems: MutableList<HierarchyItem>, parent: ViewInspector,
            parentHierarchy: HierarchyItem
    ) {
        if (parent.childs() != null) {
            for (j in parent.childs()!!.indices) {
                val child = parent.childs()!![j] as ViewInspector
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
     * @param viewInspector
     * @return
     */
    private fun collectHierarchyItems(viewInspector: ViewInspector): List<HierarchyItem>? {
        val result: MutableList<HierarchyItem> = ArrayList()
        var parent = viewInspector.parent() as ViewInspector?
        var parentHierarchy: HierarchyItem? = null
        if (parent?.childs() != null) {
            //收集兄弟控件
            parentHierarchy = HierarchyItem(
                    parent.inspectedView().javaClass.simpleName + getId(parent.inspectedView()),
                    parent,
                    null,
                    false
            )
            for (i in parent.childs()!!.indices) {
                val brother = parent.childs()!![i] as ViewInspector
                val brotherHierarchy = HierarchyItem(
                        brother.inspectedView().javaClass.simpleName + getId(brother.inspectedView()),
                        brother,
                        parentHierarchy,
                        brother == viewInspector
                )
                result.add(brotherHierarchy)
                val isCurView = brother == viewInspector
                if (isCurView) {
                    //收集当前控件的子控件
                    collectChilds(result, viewInspector, brotherHierarchy)
                }
            }
            result.add(0, parentHierarchy)
        } else {
            parentHierarchy = HierarchyItem(
                    viewInspector.inspectedView().javaClass.simpleName + getId(viewInspector.inspectedView()),
                    viewInspector,
                    null,
                    true
            )
            result.add(parentHierarchy)
            //收集当前控件的子控件
            collectChilds(result, viewInspector, parentHierarchy)
        }

        //收集爷爷辈的控件
        var preItem: HierarchyItem = parentHierarchy
        if (parent != null) {
            parent = parent.parent() as ViewInspector?
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
            parent = parent.parent() as ViewInspector?
        }

        //add blank count
        val addBlankCount = 2
        for (i in result.indices) {
            val hierarchyItem = result[i]
            if (hierarchyItem.parent != null) {
                hierarchyItem.blankCount = hierarchyItem.parent!!.blankCount + addBlankCount
            }
        }
        return result
    }

    private fun getId(view: View): String? {
        idToString(view)?.let { return "($it)" }
        return ""
    }
}