package com.mml.adbwifidebug.utils

import android.content.Context
import android.content.SharedPreferences
import com.mml.adbwifidebug.ADBApplication
import kotlin.reflect.KProperty

/**
 * 项目名称：ADBWIFIDEBUG
 * Created by Long on 2019/3/14.
 * 修改时间：2019/3/14 22:15
 */
class PreferenceManager<T>(private val name: String, private val default: T) {
    companion object {
        fun <T> preference(name: String, default: T) = PreferenceManager(name, default)
    }

    private val prefs: SharedPreferences by lazy {
        ADBApplication.instances.applicationContext.getSharedPreferences(
            "SP",
            Context.MODE_PRIVATE
        )
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = getSharedPreferences(name, default)

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = putSharedPreferences(name, value)


    private fun putSharedPreferences(name: String, value: T) = with(prefs.edit()) {
        when (value) {
            is Int -> putInt(name, value)
            is Float -> putFloat(name, value)
            is Long -> putLong(name, value)
            is Boolean -> putBoolean(name, value)
            is String -> putString(name, value)
            else -> throw IllegalArgumentException("SharedPreference can't be save this type")
        }.apply()
    }

    private fun getSharedPreferences(name: String, default: T): T = with(prefs) {
        val res: Any = when (default) {
            is Int -> getInt(name, default)
            is Float -> getFloat(name, default)
            is Long -> getLong(name, default)
            is Boolean -> getBoolean(name, default)
            is String -> getString(name, default)!!
            else -> throw IllegalArgumentException("SharedPreference can't be get this type")
        }
        return res as T
    }
}