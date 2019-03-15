package com.mml.adbwifidebug

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.media.browse.MediaBrowser
import android.os.Bundle
import android.view.View
import com.coder.zzq.smartshow.core.SmartShow
import com.coder.zzq.smartshow.core.lifecycle.ActivityLifecycleCallback
import com.mml.adbwifidebug.activity.AboutActivity
import android.os.Build
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.mml.adbwifidebug.activity.MainActivity
import com.mml.adbwifidebug.utils.Common


/**
 * 项目名称：ADBWIFICONNECT
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
        SmartShow.init(this)
        registerActivityLifecycleCallbacks(ADBActivityLifecycleCallbacks())
    }

    //由于 ActivityLifecycleCallbacks 中所有方法的调用时机都是在 Activity 对应生命周期的 Super 方法中进行的,
    // 所以在 Activity 的 onCreate 方法中使用 setContentView
    // 必须在 super.onCreate(savedInstanceState); 之前,
    // 不然在 onActivityCreated 方法中 findViewById 会发现找不到
    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private class ADBActivityLifecycleCallbacks : ActivityLifecycleCallbacks {

        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
            val toolbar: Toolbar? =activity?.findViewById(R.id.toolbar)
            if ( toolbar!= null) {//找到 Toolbar 并且替换 Actionbar
                when (activity) {
                    is AboutActivity -> {
                        activity.setSupportActionBar(toolbar)
                        activity.supportActionBar!!.setHomeButtonEnabled(true)
                        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                        Common.setToolbarTitleCenter(toolbar)
                    }
                    is MainActivity -> {
                        activity.setSupportActionBar(toolbar)
                        Common.setTitleCenter(toolbar)
                        activity.supportActionBar!!.setDisplayShowTitleEnabled(false)
                    }
                    else -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            activity.setActionBar(toolbar as android.widget.Toolbar)
                            activity.actionBar.setDisplayShowTitleEnabled(true)
                        }
                    }
                }//when
            }//if
        }

        override fun onActivityPaused(activity: Activity?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onActivityResumed(activity: Activity?) {
            // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onActivityStarted(activity: Activity?) {
            // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onActivityDestroyed(activity: Activity?) {
            // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
          //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onActivityStopped(activity: Activity?) {
            //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}