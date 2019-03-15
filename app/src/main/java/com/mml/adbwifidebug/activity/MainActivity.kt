package com.mml.adbwifidebug.activity

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.amulyakhare.textdrawable.TextDrawable
import com.coder.zzq.smartshow.toast.SmartToast
import com.mml.adbwifidebug.R
import com.mml.adbwifidebug.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*


class MainActivity : AppCompatActivity() {
    companion object {
        val TAG: String = "MainActivity"
    }

    var isOpen = false
    var isRoot = false
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        super.onCreate(savedInstanceState)
        initFunctions()
        initView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    /**
     * 返回true是为了阻止这个事件被传播的更远，或者false来表明你并没有控制这个点击事件，它可以继续传播下去
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event != null) {
            Common.doubleClick(keyCode, event, ll_tips_content, this@MainActivity)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mu_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateWifiState() {
        if (WIFIManager.getInstance(application)!!.isWifiConnect()) {
            val ip = WIFIManager.getInstance(application)!!.getWifiIpAddress()
            tv_ip_address.text = "adb connect $ip:5555"
        } else {
            tv_ip_address.text = "wifi未连接"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initFunctions() {
        TimeManager(tv_now_time).start()
        updateWifiState()
        isRoot = ADBManager.RootUtil.isDeviceRooted
        if (isRoot) {
            tv_is_root.text = "是否Root:$isRoot"
            if (!PreferenceManager.getDefaultSharedPreferences(applicationContext).getBoolean("isRoot", false))
                SmartToast.success("设备已root，可以使用本工具，请在接下来的授权窗口授权！")
            ADBManager.getInstance(packageCodePath)!!.getRootPermission()
            PreferenceManager.getDefaultSharedPreferences(applicationContext).edit {
                putBoolean("isRoot", isRoot)
            }
        } else {
            SmartToast.fail("非常抱歉，你的设备尚未root，将无法使用此程序所提供的功能！")
        }
    }

    private val drawable = TextDrawable.builder()
        .beginConfig()
        .textColor(Color.BLACK)
        .useFont(Typeface.DEFAULT)
        .fontSize(50) /* size in px */
        .bold()
        .toUpperCase()
        .endConfig()!!

    @SuppressLint("ResourceAsColor")
    private fun initView() {
        btn_open_adb.setImageDrawable(
            drawable.buildRect(
                resources.getString(R.string.string_btn_open_abd_open)
                , Color.GRAY)
        )
        btn_open_adb.background=resources.getDrawable(R.drawable.shape_btn_open)
        btn_open_adb.setOnClickListener {
            updateWifiState()
            isOpen = !isOpen
            if (isOpen) {
                if (ADBManager.getInstance(packageCodePath)!!.execOpenADB()) {
                    tv_adb_status.text = resources.getString(R.string.string_tv_adb_status_opened)
                    btn_open_adb.setImageDrawable(
                        drawable.buildRect(
                            resources.getString(R.string.string_btn_open_abd_close)
                            , Color.GREEN)
                    )
                    btn_open_adb.background=resources.getDrawable(R.drawable.shape_btn_close)
                    //btn_open_adb.text=resources.getString(R.string.string_btn_open_abd_close)
                }
            } else {
                if (ADBManager.getInstance(packageCodePath)!!.execCloseADB()) {
                    tv_adb_status.text = resources.getString(R.string.string_tv_adb_status_closed)
                    btn_open_adb.setImageDrawable(
                        drawable.buildRect(
                            resources.getString(R.string.string_btn_open_abd_open)
                            , Color.GRAY)
                    )
                    btn_open_adb.background=resources.getDrawable(R.drawable.shape_btn_open)
                    // btn_open_adb.text = resources.getString(R.string.string_btn_open_abd_open)
                }
            }
        }
        btn_copy_ip_address.setOnClickListener {
            ClipboardManagers.getInstance(applicationContext)!!.copy(tv_ip_address.text.toString())
            SmartToast.info("命令已复制：${tv_ip_address.text}")
        }
    }


}
