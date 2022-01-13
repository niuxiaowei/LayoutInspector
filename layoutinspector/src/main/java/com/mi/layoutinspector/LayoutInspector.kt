package com.mi.layoutinspector

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.ViewGroup
import java.lang.IllegalArgumentException


/**
 * create by niuxiaowei
 * date : 21-7-16
 **/
class LayoutInspector(val activity: Activity, var contentViewId: Int? = 0) {

    private var contentView: ViewGroup? = null
    var contentViewIdName: String? = null
    var activityName: String? = null
    private var inspectPageManager: InspectPageManager? = null

    companion object {
        //ViewGroup是否显示 view的属性界面
        var isViewGroupShowViewAttributes = true
        private val TAG = "LayoutInspector"
        private var viewAttributesCollectors = arrayListOf<IViewAttributeCollector>()
        private var application: Application? = null

        //当前的单位是否是dp
        var unitsIsDP = false
        private var screenWidth: Int = 0
        private var screenHeight: Int = 0
        private val childViewCollector  = ChildViewCollector()

        init {
            viewAttributesCollectors.add(ViewIdClassCollector())
            viewAttributesCollectors.add(ViewSizeCollector())
            viewAttributesCollectors.add(ViewClickInfoCollector())
            viewAttributesCollectors.add(ViewLayoutInfoCollector())
            viewAttributesCollectors.add(ViewBackgroundCollector())
            viewAttributesCollectors.add(ViewTextInfoCollector())
            viewAttributesCollectors.add(childViewCollector)
        }

        /**
         * 进行初始化
         * @param application Application
         * @param collectors Array<out IViewDetailCollector> viewDetail收集器
         */
        fun install(application: Application, vararg collectors: IViewAttributeCollector) {
            this.application = application
            if (collectors != null && collectors.isNotEmpty()) {
                this.viewAttributesCollectors.addAll(collectors)
            }
            //吧childViewCollector放在最后
            viewAttributesCollectors.remove(childViewCollector)
            viewAttributesCollectors.add(childViewCollector)
        }

        fun getContext(): Context {
            if (application == null) {
                throw IllegalArgumentException("must invoke LayoutInspector.Companion.install method ")
            }
            return application as Application
        }

        fun getDisplayMetrics(): DisplayMetrics {
            return getContext().resources.displayMetrics
        }

        fun getScreenWidth():Int{
            if (screenWidth > 0) {
                return screenWidth
            }
            screenWidth = getDisplayMetrics().widthPixels
            return screenWidth
        }

        fun getScreenHeight():Int{
            if (screenHeight > 0) {
                return screenHeight
            }
            screenHeight = getDisplayMetrics().heightPixels
            return screenHeight
        }

        fun getViewAttributesCollectors(): List<IViewAttributeCollector> {
            return viewAttributesCollectors
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    fun createEnd() {
        contentView = activity.window.decorView.findViewById(android.R.id.content)
        inspectPageManager = InspectPageManager(activity, this, contentView as ViewGroup)
        createActivityInfo()
    }

    private fun createActivityInfo() {
        //初始化一些数据
        try {
            contentViewIdName = contentViewId?.let { activity.resources.getResourceEntryName(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        activityName = activity.javaClass.simpleName
    }


    fun destoryEnd() {

    }

}







