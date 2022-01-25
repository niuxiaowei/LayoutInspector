package com.mi.layoutinspector

import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.PopupWindow
import com.mi.layoutinspector.inspector.ActivityInspector
import com.mi.layoutinspector.utils.getActivityFromDialog
import com.mi.layoutinspector.viewinfos.viewattributes.*
import java.lang.IllegalArgumentException


/**
 * create by niuxiaowei
 * date : 21-7-16
 **/
class LayoutInspector() {

    companion object {
        //ViewGroup是否显示 view的属性界面
        var isViewGroupShowViewAttributes = true
        private var viewAttributesCollectors = arrayListOf<IViewAttributeCollector>()
        private var application: Application? = null

        //当前的单位是否是dp
        var unitsIsDP = false
        private var screenWidth: Int = 0
        private var screenHeight: Int = 0
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
        fun regist(viewAttributeCollector: IViewAttributeCollector) {
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
                }

                override fun onActivityStarted(activity: Activity) {
                }

                override fun onActivityDestroyed(activity: Activity) {
                    findActivityInspector(activity)?.let {
                        it.onDestory()
                        activityInspectors.remove(it)
                    }

                }

                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                }

                override fun onActivityStopped(activity: Activity) {
                }

                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    activityInspectors.add(ActivityInspector(activity).apply { onCreate() })
                }

                override fun onActivityResumed(activity: Activity) {
                    findActivityInspector(activity)?.onResume()
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
            return getDisplayMetrics().widthPixels
        }

        fun getScreenHeight(): Int {
            return getDisplayMetrics().heightPixels
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


}







