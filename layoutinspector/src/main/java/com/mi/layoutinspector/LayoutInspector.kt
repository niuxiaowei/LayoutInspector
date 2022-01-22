package com.mi.layoutinspector

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.util.DisplayMetrics
import android.view.ViewGroup
import com.mi.layoutinspector.inspect.InspectPageManager
import com.mi.layoutinspector.utils.getActivityFromDialog
import com.mi.layoutinspector.utils.getContentViewForActivity
import com.mi.layoutinspector.utils.getContentViewForDialog
import com.mi.layoutinspector.viewinfos.viewattributes.*
import java.lang.IllegalArgumentException


/**
 * create by niuxiaowei
 * date : 21-7-16
 * 一个Activity对应一个LayoutInspector
 **/
class LayoutInspector(val activity: Activity) {

    var contentViewIdName: String? = null
    var activityName: String? = null
    private val inspectPageManagers: MutableList<InspectPageManager> = mutableListOf()
    val fragments = Fragments(activity)
    private var inspectPageManagerOfActivityInit = false

    init {
        onActivityCreate()
    }

    companion object {
        //ViewGroup是否显示 view的属性界面
        var isViewGroupShowViewAttributes = true
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
            this.application?.registerActivityLifecycleCallbacks(object :
                Application.ActivityLifecycleCallbacks {
                override fun onActivityPaused(activity: Activity) {
                }

                override fun onActivityStarted(activity: Activity) {
                }

                override fun onActivityDestroyed(activity: Activity) {
                    findLayoutInspector(activity)?.let {
                        it.onActivityDestory()
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
                    findLayoutInspector(activity)?.init()
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
        fun inspectDialog(dialog: Dialog) {
            val activity = getActivityFromDialog(dialog)
            activity?.let {
                findLayoutInspector(it)?.startInspectDialog(dialog)
            }
        }

        fun stopInspectDialog(dialog: Dialog){

        }
    }

    private fun onActivityCreate() {
        fragments.registerFragmentLifecycle()
    }


    private fun startInspectDialog(dialog: Dialog){
        getContentViewForDialog(dialog)?.let{
            inspectPageManagers.add(InspectPageManager(activity, this, it,dialog.window?.decorView))
        }
    }



    @SuppressLint("ClickableViewAccessibility")
    fun init() {
        if (inspectPageManagerOfActivityInit) {
            return
        }
        inspectPageManagerOfActivityInit = true
        val contentView = getContentViewForActivity(activity)
        inspectPageManagers.add(InspectPageManager(activity, this, contentView,activity.window.decorView))
        createActivityInfo(contentView)
    }

    private fun createActivityInfo(contentView: ViewGroup) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                val layoutId = contentView.getChildAt(0)?.sourceLayoutResId
                if (layoutId != null) {
                    if (layoutId > 0) {
                        contentViewIdName = activity.resources.getResourceEntryName(layoutId)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        activityName = activity.javaClass.simpleName
    }


    private fun onActivityDestory() {
        fragments.unRegisterFragmentLifecycle()
    }

}







