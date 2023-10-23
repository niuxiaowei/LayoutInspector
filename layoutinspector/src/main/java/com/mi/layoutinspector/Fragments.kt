package com.mi.layoutinspector

import android.app.Activity
import androidx.lifecycle.ReportFragment
import android.content.Context
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

/**
 * create by niuxiaowei
 * date : 22-1-20
 **/
class Fragments(val activity: Activity) {
    private var supportFragments: MutableList<Fragment>? = null
    private var fragments: MutableList<android.app.Fragment>? = null
    private var supportFragmentLifecycle: FragmentManager.FragmentLifecycleCallbacks? = null
    private var fragmentLifecycle: android.app.FragmentManager.FragmentLifecycleCallbacks? = null

    fun registerFragmentLifecycle() {
        if (activity is FragmentActivity) {
            supportFragmentLifecycle = object : FragmentManager.FragmentLifecycleCallbacks() {

                override fun onFragmentAttached(
                    fm: FragmentManager,
                    f: Fragment,
                    context: Context
                ) {
                    if (supportFragments == null) {
                        supportFragments = mutableListOf()
                    }
                    supportFragments?.add(0, f)
                }

                override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
                    super.onFragmentDetached(fm, f)
                    supportFragments?.remove(f)
                }
            }
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(supportFragmentLifecycle as FragmentManager.FragmentLifecycleCallbacks, true)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            fragmentLifecycle = object : android.app.FragmentManager.FragmentLifecycleCallbacks() {

                override fun onFragmentAttached(
                        fm: android.app.FragmentManager?,
                        f: android.app.Fragment?,
                        context: Context?
                ) {
                    if (isReportFragment(f)) {
                        return
                    }
                    if (fragments == null) {
                        fragments = mutableListOf()
                    }
                    if (f != null) {
                        fragments?.add(0, f)
                    }
                }

                override fun onFragmentDetached(fm: android.app.FragmentManager?, f: android.app.Fragment?) {
                    super.onFragmentDetached(fm, f)
                    fragments?.remove(f)
                }
            }

            activity.fragmentManager.registerFragmentLifecycleCallbacks(fragmentLifecycle, true)
        }
    }

    private fun isReportFragment(any: Any?): Boolean {
        return any?.javaClass == ReportFragment::class.java
    }

    fun unRegisterFragmentLifecycle() {
        if (activity is FragmentActivity) {
            supportFragmentLifecycle?.let {
                activity.supportFragmentManager.unregisterFragmentLifecycleCallbacks(it)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            (fragmentLifecycle as? android.app.FragmentManager.FragmentLifecycleCallbacks)?.let {
                activity.fragmentManager.unregisterFragmentLifecycleCallbacks(it)
            }
        }
    }

    /**
     * 获取fragment的类名
     * @return MutableList<String>?
     */
    fun getFragmentClassNames(): MutableList<String>? {
        var result: MutableList<String>? = null
        supportFragments?.apply {
            filter { !it.isHidden }.forEach {
                if (result == null) {
                    result = mutableListOf()
                }
                result?.add(it.javaClass.name)
            }
            filter { it.isHidden }.forEach {
                if (result == null) {
                    result = mutableListOf()
                }
                result?.add(it.javaClass.name)
            }
        }


        fragments?.apply {
            filter { !it.isHidden }?.forEach {
                if (result == null) {
                    result = mutableListOf()
                }
                result?.add(it.javaClass.name)
            }
            filter { it.isHidden }?.forEach {
                if (result == null) {
                    result = mutableListOf()
                }
                result?.add(it.javaClass.name)
            }
        }

        return result
    }
}