package com.mml.adbwifidebug.utils

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

/**
 * 项目名称：ADBWIFIDEBUG
 * Created by Long on 2019/3/14.
 * 修改时间：2019/3/14 21:09
 */
class ClipboardManagers constructor(private val context: Context) {

    companion object {
        private val TAG = "ClipboardManager"
        @SuppressLint("StaticFieldLeak")
        @Volatile
        var instance: ClipboardManagers? = null

        fun getInstance(mContext: Context): ClipboardManagers? {
            if (instance == null) {
                synchronized(ClipboardManagers::class) {
                    if (instance == null) {
                        instance =
                            ClipboardManagers(mContext)
                    }
                }
            }
            return instance
        }
    }

    @SuppressLint("ServiceCast")
    fun copy(string: String) {
        // 获取系统剪贴板
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

// 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）
        val clipData: ClipData = ClipData.newPlainText("ip地址", string)

// 把数据集设置（复制）到剪贴板
        clipboard.primaryClip=clipData
    }
}
