package com.mml.adbwifidebug.utils

import android.app.Activity
import android.graphics.Color
import com.mml.adbwifidebug.ADBApplication
import com.mml.adbwifidebug.R
import io.multimoon.colorful.Colorful
import io.multimoon.colorful.ThemeColor

/**
 * 项目名称：ADBWIFIDEBUG
 * Created by Long on 2019/3/18.
 * 修改时间：2019/3/18 11:31
 */
object ThemeManager {
    val layoutMap = mapOf(
        "-1" to Pair(Color.CYAN, Color.GREEN),
        "0" to Pair(R.color.md_green_500, Color.BLUE),
        "1" to Pair(R.color.pink, Color.YELLOW),
        "2" to Pair(R.color.blue, Color.RED),
        "3" to Pair(Color.RED, Color.BLACK),
        "4" to Pair(R.color.black1, Color.BLUE)
    )
    val themeMap = mapOf(
        "-1" to Pair(ThemeColor.CYAN, ThemeColor.GREEN),
        "0" to Pair(ThemeColor.GREEN, ThemeColor.BLUE),
        "1" to Pair(ThemeColor.PINK, ThemeColor.PURPLE),
        "2" to Pair(ThemeColor.BLUE, ThemeColor.BROWN),
        "3" to Pair(ThemeColor.RED, ThemeColor.BLACK),
        "4" to Pair(ThemeColor.BLACK, ThemeColor.BLUE)
    )
    var lastTheme: String? = SP(ADBApplication.instances.applicationContext).base_theme_list
    var nowTheme: String? = lastTheme
    fun changeTheme(choice: String? = nowTheme, context: Activity) {
        lastTheme = nowTheme
        nowTheme = choice
        val editer = Colorful().edit()
        editer.setPrimaryColor(themeMap[choice]!!.first)
            .setAccentColor(themeMap[choice]!!.second)
        /**
        when (choice) {
        "-1" -> {//"默认色"
        editer.setPrimaryColor(ThemeColor.CYAN)
        .setAccentColor(ThemeColor.GREEN)
        }
        "0"-> {//"清新绿"
        editer.setPrimaryColor(ThemeColor.GREEN)
        .setAccentColor(ThemeColor.BLUE)
        }
        "1" -> {//"活力粉"
        editer.setPrimaryColor(ThemeColor.PINK)
        .setAccentColor(ThemeColor.PURPLE)
        }
        "2" -> {//"优雅蓝"
        editer.setPrimaryColor(ThemeColor.BLUE)
        .setAccentColor(ThemeColor.BROWN)
        }
        "3" -> {//奔放红
        editer.setPrimaryColor(ThemeColor.RED)
        .setAccentColor(ThemeColor.BLACK)
        }
        "4" -> {//暗夜黑
        editer.setPrimaryColor(ThemeColor.BLACK)
        .setAccentColor(ThemeColor.GREY)
        }
        else->{
        editer.setPrimaryColor(ThemeColor.CYAN)
        .setAccentColor(ThemeColor.GREEN)
        }

        }**/
        editer.setDarkTheme(false)
            .setTranslucent(false)
            .apply(context) {
                context.recreate()
            }
    }

}