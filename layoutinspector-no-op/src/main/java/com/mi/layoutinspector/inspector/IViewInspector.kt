package com.mi.layoutinspector.inspector

import android.view.View

/**
 * create by niuxiaowei
 * date : 22-1-18
 * 代表对某一个view进行 检查的检查器
 **/
interface IViewInspector {
    /**
     * 隐藏 展示view属性的界面
     */
    fun hideViewInfosPopupWindown()

    /**
     * 显示 展示view属性的界面
     */
    fun showViewInfosPopupWindow()

    /**
     * 设置当前的检查器 是否可以单机
     * @param clickable Boolean
     */
    fun setClickable(clickable: Boolean)

    /**
     * 获取父检查器
     * @return IViewInspector
     */
    fun parent(): IViewInspector?

    /**
     * 获取被检查的view
     * @return View
     */
    fun inspectedView(): View

    /**
     * 添加子检查器
     * @param IViewInspector IViewInspector
     */
    fun addChild(IViewInspector: IViewInspector)

    /**
     * 获取子检查器
     * @return MutableList<ViewInspector>?
     */
    fun childs(): MutableList<IViewInspector>?
}