package com.mi.layoutinspector

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.ViewGroup
import com.mi.layoutinspector.inspect.InspectPageManager
import com.mi.layoutinspector.viewinfos.viewattributes.*
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
        private val layoutInspectors = mutableListOf<LayoutInspector>()

        init {
            viewAttributesCollectors.add(ViewIdClassCollector())
            viewAttributesCollectors.add(ViewSizeCollector())
            viewAttributesCollectors.add(ViewClickInfoCollector())
            viewAttributesCollectors.add(ViewLayoutInfoCollector())
            viewAttributesCollectors.add(ViewBackgroundCollector())
            viewAttributesCollectors.add(ViewTextInfoCollector())
        }

        /**
         * 注册IViewAttributeCollector事件
         * @param viewAttributeCollector
         */
        fun regist(viewAttributeCollector: IViewAttributeCollector) {
            this.viewAttributesCollectors.add(viewAttributeCollector)
        }

        private fun findLayoutInspector(activity: Activity): LayoutInspector? {
            layoutInspectors.forEach {
                if (it.activity.equals(activity)) {
                    return it
                }
            }
            return null
        }

        fun init(application: Application) {
            this.application = application
            this.application?.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
                override fun onActivityPaused(activity: Activity) {
                }

                override fun onActivityStarted(activity: Activity) {
                }

                override fun onActivityDestroyed(activity: Activity) {
                    findLayoutInspector(activity)?.let {
                        it.destoryEnd()
                        layoutInspectors.remove(it)
                    }

                }

                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                }

                override fun onActivityStopped(activity: Activity) {
                }

                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    layoutInspectors.add(LayoutInspector(activity))
                }

                override fun onActivityResumed(activity: Activity) {
                    findLayoutInspector(activity)?.createEnd()
                }

            })
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

        fun getScreenWidth(): Int {
            if (screenWidth > 0) {
                return screenWidth
            }
            screenWidth = getDisplayMetrics().widthPixels
            return screenWidth
        }

        fun getScreenHeight(): Int {
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
        if (contentView != null) {
            return
        }
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







