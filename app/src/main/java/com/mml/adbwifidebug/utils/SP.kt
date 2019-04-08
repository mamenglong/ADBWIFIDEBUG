package com.mml.adbwifidebug.utils

import android.content.Context
import com.mml.lib.SharedPreferencesUtil

/**
 * 项目名称：ADBWIFIDEBUG
 * Created by Long on 2019/3/18.
 * 修改时间：2019/3/18 17:24
 */
class SP(context: Context) : SharedPreferencesUtil(context) {
    var username by SharedPreferenceDelegates.string(defaultValue = "this is username")

    var age by SharedPreferenceDelegates.int()

    var isRoot by SharedPreferenceDelegates.boolean(defaultValue = false)
    var is_open_shortcut_switch by SharedPreferenceDelegates.boolean(defaultValue = false)

    var base_theme_list by SharedPreferenceDelegates.string(defaultValue = "-1")
}