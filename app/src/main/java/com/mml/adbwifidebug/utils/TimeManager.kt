package com.mml.adbwifidebug.utils

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.os.Parcel
import android.os.Parcelable
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

/**
 * 项目名称：ADBWIFIDEBUG
 * Created by Long on 2019/3/14.
 * 修改时间：2019/3/14 21:31
 */
class TimeManager() : Thread()  {
    private lateinit var tvDate: TextView
    private val msgKey1 = 22

    constructor(tvDate: TextView) : this() {
        this.tvDate = tvDate
    }

    override fun run() {
        do {
            try {
                Thread.sleep(1000)
                val msg = Message()
                msg.what = msgKey1
                mHandler.sendMessage(msg)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        } while (true)
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private val mHandler = Handler {
        when (it.what) {
            msgKey1 -> {

                tvDate.text = "${getDate()} ${getTime()} ${getWeek()}"
                true
            }
            else -> {
                true
            }
        }

    }

    /**
     * 获取今天星期几
     * @return
     */
    private fun getWeek(): String {
        var cal: Calendar = Calendar.getInstance()
        return when (cal.get(Calendar.DAY_OF_WEEK)) {
            1 -> "周日"
            2 -> "周一"
            3 -> "周二"
            4 -> "周三"
            5 -> "周四"
            6 -> "周五"
            7 -> "周六"
            else ->
                ""
        }

    }

    /**
     * 获取今天日期
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    private fun getDate(): String {
         val format2 = SimpleDateFormat("yyyy-MM-dd")
        return format2.format(Calendar.getInstance().time)
        }
    /**
     * 获取时间
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    private fun getTime(): String {
        val sdf = SimpleDateFormat("HH:mm:ss")
        return   sdf.format(Date())
    }

}