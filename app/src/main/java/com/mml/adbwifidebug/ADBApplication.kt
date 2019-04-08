package com.mml.adbwifidebug

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.preference.PreferenceManager
import com.coder.zzq.smartshow.core.SmartShow
import com.mml.adbwifidebug.activity.SettingsActivity

import com.mml.adbwifidebug.utils.SP
import com.mml.adbwifidebug.utils.ThemeManager
import com.mml.android.utils.LogUtils
import io.multimoon.colorful.*


/**
 * 项目名称：ADBWIFIDEBUG
 * Created by Long on 2019/3/14.
 * 修改时间：2019/3/14 17:29
 */
class ADBApplication : Application() {
    companion object {
        lateinit var instances: ADBApplication
    }

    init {
        instances = this
    }

    override fun onCreate() {
        super.onCreate()
        LogUtils.debug(BuildConfig.DEBUG).saveSd(false)
        SmartShow.init(this)
        val defaults: Defaults = Defaults(
            primaryColor = ThemeColor.ORANGE,
            accentColor = ThemeColor.RED,
            useDarkTheme = false,
            translucent = false
        )
        initColorful(this, defaults)
        registerActivityLifecycleCallbacks(ADBActivityLifecycleCallbacks())
    }

    //由于 ActivityLifecycleCallbacks 中所有方法的调用时机都是在 Activity 对应生命周期的 Super 方法中进行的,
    // 所以在 Activity 的 onCreate 方法中使用 setContentView
    // 必须在 super.onCreate(savedInstanceState); 之前,
    // 不然在 onActivityCreated 方法中 findViewById 会发现找不到
    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private class ADBActivityLifecycleCallbacks : ActivityLifecycleCallbacks {

        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
            Colorful().apply(activity!!, override = true, baseTheme = BaseTheme.THEME_MATERIAL)
            LogUtils.i(msg = activity.localClassName)
        }

        override fun onActivityPaused(activity: Activity?) {
            LogUtils.i(msg = activity!!.localClassName)

        }

        override fun onActivityResumed(activity: Activity?) {
            when (activity) {
                !is SettingsActivity -> {
                    LogUtils.i(msg = activity!!.localClassName)
                    if (ThemeManager.lastTheme != ThemeManager.nowTheme) {
                        activity.recreate()
                        ThemeManager.lastTheme = ThemeManager.nowTheme
                    }
                }
            }
        }

        override fun onActivityStarted(activity: Activity?) {
            LogUtils.i(msg = activity!!.localClassName)
        }

        override fun onActivityDestroyed(activity: Activity?) {
            LogUtils.i(msg = activity!!.localClassName)
        }

        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            LogUtils.i(msg = activity!!.localClassName)
        }

        override fun onActivityStopped(activity: Activity?) {
            LogUtils.i(msg = activity!!.localClassName)
        }
    }
}