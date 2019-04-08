package com.mml.adbwifidebug.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.amulyakhare.textdrawable.TextDrawable
import com.coder.zzq.smartshow.toast.SmartToast
import com.mml.adbwifidebug.R
import com.mml.adbwifidebug.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import java.lang.reflect.Method


class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG: String = "MainActivity"
    }

    private val preferences by lazy { SP(this) }
    var isOpen = false//默认关闭需要让打开
    var isRoot = false
    var isShortcut = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        isShortcut = intent.getBooleanExtra("isShortcut", false)
        LogUtil.i(TAG, "isShortcut:$isShortcut")
        setSupportActionBar(toolbar)
        Common.setTitleCenter(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        initFunctions()
        initView()
        if (isShortcut) {
            window.decorView.postDelayed({
                btn_open_adb.performClick()
            }, 1000)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (menu != null) {
            if (menu.javaClass.simpleName == "MenuBuilder") {
                val m: Method = menu.javaClass.getDeclaredMethod(
                    "setOptionalIconsVisible", Boolean::class.java
                )
                m.isAccessible = true;
                m.invoke(menu, true)
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (!isShortcut)
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
            R.id.mu_setting -> {
                startActivity(Intent(this, SettingsActivity::class.java))
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
        tv_is_root.text = "是否Root:$isRoot"
        if (isRoot) {
            if (!preferences.isRoot) {
                SmartToast.success("设备已root，可以使用本工具，请在接下来的授权窗口授权！")
                preferences.isRoot = isRoot
            }
            //  ADBManager.getInstance(packageCodePath)!!.getRootPermission()
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
                , ThemeManager.layoutMap[preferences.base_theme_list]!!.first
            )
        )
        btn_open_adb.background = resources.getDrawable(R.drawable.shape_btn_open)
        btn_open_adb.setOnClickListener {
            var (PrimaryColor, AccentColor) = ThemeManager.layoutMap[preferences.base_theme_list]!!
            updateWifiState()
            Log.i(TAG, "isRoot:$isRoot:isOpen:$isOpen:isShortcut:$isShortcut")
            when (isRoot) {
                true -> {
                    if (!isOpen) {
                        if (ADBManager.getInstance(packageCodePath)!!.execOpenADB()) {
                            tv_adb_status.text = resources.getString(R.string.string_tv_adb_status_opened)
                            btn_open_adb.setImageDrawable(
                                drawable.buildRect(
                                    resources.getString(R.string.string_btn_open_abd_close)
                                    , Color.RED
                                )
                            )
                            btn_open_adb.background = resources.getDrawable(R.drawable.shape_btn_close)
                            isOpen = !isOpen
                            //btn_open_adb.text=resources.getString(R.string.string_btn_open_abd_close)
                        }
                    } else {
                        if (ADBManager.getInstance(packageCodePath)!!.execCloseADB()) {
                            tv_adb_status.text = resources.getString(R.string.string_tv_adb_status_closed)
                            btn_open_adb.setImageDrawable(
                                drawable.buildRect(
                                    resources.getString(R.string.string_btn_open_abd_open)
                                    , PrimaryColor
                                )
                            )
                            btn_open_adb.background = resources.getDrawable(R.drawable.shape_btn_open)
                            isOpen = !isOpen
                            // btn_open_adb.text = resources.getString(R.string.string_btn_open_abd_open)
                        }
                    }
                }
                false -> {
                    SmartToast.error("非常抱歉，你的设备尚未root，将无法使用此程序所提供的功能！")
                }
            }

        }
        btn_copy_ip_address.setOnClickListener {
            ClipboardManagers.getInstance(applicationContext)!!.copy(tv_ip_address.text.toString())
            SmartToast.info("命令已复制：${tv_ip_address.text}")
        }
    }


}
