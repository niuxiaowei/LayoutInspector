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
import com.mi.layoutinspector.utils.getBoolean
import com.mi.layoutinspector.utils.saveBoolean
import com.mi.layoutinspector.viewinfos.viewattributes.*


/**
 * create by niuxiaowei
 * date : 21-7-16
 **/
object LayoutInspector {

    private var viewAttributesCollectors = arrayListOf<IViewAttributeCollector>()
    private var application: Application? = null
    private val activityInspectors = mutableListOf<ActivityInspector>()

    private const val KEY_SHOW_PADDING = "key_show_padding"
    private const val KEY_SHOW_MARGIN = "key_show_margin"
    private const val KEY_UNITS_DP = "key_units_is_dp"
    private const val KEY_VIEW_GROUP_SHOW_INSPECTOR = "KEY_VIEW_GROUP_SHOW_INSPECTOR"

    //ViewGroup是否显示 view的检测器界面
    var isViewGroupShowViewInspector = true
        set(value) {
            field = value
            saveBoolean(KEY_VIEW_GROUP_SHOW_INSPECTOR, value)
        }
    //当前的单位是否是dp
    var unitsIsDP = false
        set(value) {
            field = value
            saveBoolean(KEY_UNITS_DP, value)
        }

    //是否显示view的padding margin
    var isShowViewPadding = true
        set(value) {
            field = value
            saveBoolean(KEY_SHOW_PADDING, value)
        }
    var isShowViewMargin = true
        set(value) {
            field = value
            saveBoolean(KEY_SHOW_MARGIN, value)
        }

    init {
        viewAttributesCollectors.add(ViewIdClassCollector())
        viewAttributesCollectors.add(ViewInspectorInfoCollector())
        viewAttributesCollectors.add(ViewLayoutInfoCollector())
        viewAttributesCollectors.add(ViewClickInfoCollector())
        viewAttributesCollectors.add(ViewSizeCollector())
        viewAttributesCollectors.add(ViewBackgroundCollector())
        viewAttributesCollectors.add(ViewTextInfoCollector())
        viewAttributesCollectors.add(ComponentInfoCollector())
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

        isViewGroupShowViewInspector = getBoolean(KEY_VIEW_GROUP_SHOW_INSPECTOR, true)
        unitsIsDP = getBoolean(KEY_UNITS_DP, false)
        isShowViewPadding = getBoolean(KEY_SHOW_PADDING, true)
        isShowViewMargin = getBoolean(KEY_SHOW_MARGIN, true)
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







