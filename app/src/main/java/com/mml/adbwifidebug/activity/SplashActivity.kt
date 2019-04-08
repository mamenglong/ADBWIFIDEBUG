package com.mml.adbwifidebug.activity

import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.mml.adbwifidebug.BuildConfig
import com.mml.adbwifidebug.utils.SP
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity(), Animation.AnimationListener {
    private lateinit var mShortcutManager: ShortcutManager
    private val preferences by lazy { SP(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.mml.adbwifidebug.R.layout.activity_splash)
        initStartAnim()
        if (preferences.is_open_shortcut_switch)
            setupShortcuts()
        else
            deleteShortcuts()

        window.decorView.postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, (ANIM_TIME * 2).toLong())
    }

    /**
     * 动态创建
     */
    private fun setupShortcuts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            mShortcutManager = getSystemService(ShortcutManager::class.java)
            val infos: ArrayList<ShortcutInfo> = ArrayList()
            val info1 =
                newShortcut(MainActivity::class.java, "openDebug", "打开调试")
            infos.add(info1)
            val info2 =
                newShortcut(SettingsActivity::class.java, "openSetting", "打开设置")
            infos.add(info2)
            mShortcutManager.dynamicShortcuts = infos
        } else {
            if (BuildConfig.DEBUG) {
                Log.i("Shortcut", "版本号：${Build.VERSION.SDK_INT}")
            }
        }
    }

    /***
     * 新建shortcut
     */
    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun <T> newShortcut(targetClass: Class<T>, id: String, label: String): ShortcutInfo {
        val intent = Intent(this, targetClass)
        intent.putExtra("isShortcut", true)
        intent.action = Intent.ACTION_VIEW
        return ShortcutInfo.Builder(this, id)
            .setShortLabel(label)
            .setLongLabel(label)
            .setIcon(Icon.createWithResource(this, com.mml.adbwifidebug.R.drawable.ic_logo))
            .setIntent(intent)
            .build()
    }

    /**
     * 动态删除
     */
    private fun deleteShortcuts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val mShortcutManager = getSystemService(ShortcutManager::class.java)

            /********* 移除弹出列表图标  */
            // 所有动态创建图标
            val dShortcuts = mShortcutManager.dynamicShortcuts
            if (dShortcuts.size > 0) {
                val ids1: ArrayList<String> = ArrayList()
                for (info in dShortcuts) {
                    ids1.add(info.id)
                }

                // 禁用所有的快捷方式
                mShortcutManager.disableShortcuts(ids1, "已禁用")
                mShortcutManager.removeDynamicShortcuts(ids1)

                /********* 移除拖出来的桌面快捷图标  */
                // 放在桌面的图标
                val pShortcuts = mShortcutManager.pinnedShortcuts

                val ids2 = ArrayList<String>()
                for (info in pShortcuts) {
                    ids2.add(info.id)
                }
                mShortcutManager.disableShortcuts(ids2, "已禁用")
                mShortcutManager.removeAllDynamicShortcuts()
            }
        }
    }

    private val ANIM_TIME = 1000

    /**
     * 启动动画
     */
    private fun initStartAnim() {
        // 渐变展示启动屏
        val aa = AlphaAnimation(0.4f, 1.0f)
        aa.duration = (ANIM_TIME * 2).toLong()
        aa.setAnimationListener(this)
        iv_launcher_bg.startAnimation(aa)

        val sa = ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        sa.duration = ANIM_TIME.toLong()
        iv_launcher_icon.startAnimation(sa)

        val ra = RotateAnimation(180f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        ra.duration = ANIM_TIME.toLong()
        iv_launcher_name.startAnimation(ra)
    }

    override fun onAnimationEnd(animation: Animation?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAnimationStart(animation: Animation?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAnimationRepeat(animation: Animation?) {
        //   TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
