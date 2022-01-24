package com.mi.layoutinspector.viewinfos.viewhierarchy

import com.mi.layoutinspector.inspect.ViewInspector

/**
 * create by niuxiaowei
 * date : 22-1-18
 * @param viewDesc view所属的类名，id
 * @param viewInspector
 * @param blankCount 为了界面显示效果 需要增加几个空格
 * @param isSelected 是否是被选择的
 * @param isBrotherhood 是否是兄弟关系
 **/
data class HierarchyItem(val viewDesc: String, val viewInspector: ViewInspector?, var parent: HierarchyItem? = null, var isSelected: Boolean = false) {
    var blankCount: Int = 0

}