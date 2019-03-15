package com.mml.adbwifidebug.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation
import com.mml.adbwifidebug.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity(), Animation.AnimationListener {
    override fun onAnimationEnd(animation: Animation?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAnimationStart(animation: Animation?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAnimationRepeat(animation: Animation?) {
        //   TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initStartAnim()
        val intent = Intent(this, MainActivity::class.java)
        window.decorView.postDelayed({
            startActivity(intent)
            finish()
        }, (ANIM_TIME * 2).toLong())
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

}
