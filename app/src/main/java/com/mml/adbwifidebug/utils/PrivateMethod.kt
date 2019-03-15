package com.mml.adbwifidebug.utils

import android.app.Activity
import android.view.KeyEvent
import android.view.View
import com.coder.zzq.smartshow.toast.SmartToast
import com.google.android.material.snackbar.Snackbar
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import android.R
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat.LAYOUT_DIRECTION_LTR
import androidx.core.view.ViewCompat.LAYOUT_DIRECTION_RTL
import com.google.android.material.internal.ContextUtils.getActivity
import com.mml.adbwifidebug.ADBApplication


/**
 * 项目名称：ADBWIFIDEBUG
 * Created by Long on 2019/3/15.
 * 修改时间：2019/3/15 11:53
 */
private var exitTime: Long = 0//全局计时

object Common {
    fun setTitleCenter(toolbar: Toolbar) {
        val titleText = TextView(ADBApplication.instances)
        titleText.setTextColor(ContextCompat.getColor(ADBApplication.instances, R.color.white))
        titleText.text = toolbar.title
        titleText.textSize =20f
        titleText.gravity = Gravity.CENTER
        val layoutParams = Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = Gravity.CENTER
        titleText.layoutParams = layoutParams
        toolbar.addView(titleText)
    }

    fun setToolbarTitleCenter(toolbar: Toolbar) {
        val title = "title"
        val originalTitle = toolbar.title
        toolbar.title = title
        for (i in 0 until toolbar.childCount) {
            val view = toolbar.getChildAt(i)
            if (view is TextView) {
                if (title == view.text) {
                    view.gravity = Gravity.CENTER
                    val params =
                        Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.MATCH_PARENT)
                    params.gravity = Gravity.CENTER
                    view.layoutParams = params
                    break
                }
            }
            toolbar.title = originalTitle
        }
    }

    fun doubleClick(keyCode: Int, event: KeyEvent, v: View, activity: Activity) {
        if (!(keyCode != KeyEvent.KEYCODE_BACK || event.action != KeyEvent.ACTION_DOWN)) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Snackbar.make(v, "再按一次退出程序！(๑ت๑)", Snackbar.LENGTH_SHORT)
                    .show()
                exitTime = System.currentTimeMillis()
            } else {
                SmartToast.complete("欢迎下次再来！(๑`･︶･´๑)")
                activity.finish()
            }
        }
    }
}

