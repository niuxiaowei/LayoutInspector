package com.mi.layoutinspector

import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.PopupWindow
import com.mi.layoutinspector.inspector.ActivityInspector
import com.mi.layoutinspector.utils.getActivityFromDialog
import com.mi.layoutinspector.viewinfos.viewattributes.*


/**
 * create by niuxiaowei
 * date : 21-7-16
 **/
object LayoutInspector {

    //ViewGroup是否显示 view的属性界面
    var isViewGroupShowViewAttributes = true
    private var viewAttributesCollectors = arrayListOf<IViewAttributeCollector>()
    private var application: Application? = null

    //当前的单位是否是dp
    var unitsIsDP = false
    //是否显示view的padding margin
    var isShowViewPadding = true
    var isShowViewMargin = true
    private val activityInspectors = mutableListOf<ActivityInspector>()

    init {
        viewAttributesCollectors.add(ViewIdClassCollector())
        viewAttributesCollectors.add(ComponentInfoCollector())
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
    fun register(viewAttributeCollector: IViewAttributeCollector) {
        this.viewAttributesCollectors.add(viewAttributeCollector)
    }

    private fun findActivityInspector(activity: Activity): ActivityInspector? {
        activityInspectors.forEach {
            if (it.activity == activity) {
                return it
            }
        }
        return null
    }

    fun init(application: Application) {
        this.application = application
        this.application?.registerActivityLifecycleCallbacks(object :
                Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {
                Log.i("LayoutInspector", " onActivityPaused  $activity")
            }

            override fun onActivityStarted(activity: Activity) {
                findActivityInspector(activity)?.onStart()
                Log.i("LayoutInspector", " onActivityStarted  $activity")

            }

            override fun onActivityDestroyed(activity: Activity) {
                findActivityInspector(activity)?.let {
                    it.onDestory()
                    activityInspectors.remove(it)
                }
                Log.i("LayoutInspector", " onActivityDestroyed  $activity")

            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                Log.i("LayoutInspector", " onActivitySaveInstanceState  $activity")

            }

            override fun onActivityStopped(activity: Activity) {
                Log.i("LayoutInspector", " onActivityStopped  $activity")

            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activityInspectors.add(ActivityInspector(activity).apply { onCreate() })
                Log.i("LayoutInspector", " onActivityCreated  $activity")

            }

            override fun onActivityResumed(activity: Activity) {
                findActivityInspector(activity)?.onResume()
                Log.i("LayoutInspector", " onActivityResumed  $activity")

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

    fun getViewAttributesCollectors(): List<IViewAttributeCollector> {
        return viewAttributesCollectors
    }

    /**
     * 检测Dialog
     */
    fun startInspect(any: Any) {
        getActivity(any)?.let {
            findActivityInspector(it)?.createComponentInspector(any)
        }
    }

    private fun getActivity(any: Any): Activity? {
        if (any is Dialog) {
            return getActivityFromDialog(any)
        } else if (any is PopupWindow) {
            val context = any.contentView.context
            if (context is Activity) {
                return context
            }
        }
        return null
    }


}







