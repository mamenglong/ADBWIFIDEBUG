package com.mml.adbwifidebug.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.preference.*
import android.text.TextUtils
import android.view.MenuItem
import com.coder.zzq.smartshow.toast.SmartToast
import com.mml.adbwifidebug.BuildConfig
import com.mml.adbwifidebug.R
import com.mml.adbwifidebug.utils.SP
import com.mml.adbwifidebug.utils.ThemeManager
import com.mml.android.utils.LogUtils

/**
 * A [PreferenceActivity] that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 *
 * See [Android Design: Settings](http://developer.android.com/design/patterns/settings.html)
 * for design guidelines and the [Settings API Guide](http://developer.android.com/guide/topics/ui/settings.html)
 * for more information on developing a Settings UI.
 */
class SettingsActivity : AppCompatPreferenceActivity(), Preference.OnPreferenceClickListener,
    Preference.OnPreferenceChangeListener {
    val preferences by lazy { SP(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.pref_general)
        setupActionBar()
        instances = this

        val isOpenShortcutSwitch = findPreference("is_open_shortcut_switch")
        isOpenShortcutSwitch.onPreferenceChangeListener = this
        val baseThemeList = findPreference("base_theme_list")
//            baseThemeList.onPreferenceClickListener = this
        baseThemeList.onPreferenceChangeListener = this
        this.onPreferenceClick(baseThemeList)
        val developer = findPreference("developer")
        this.onPreferenceClick(developer)
        val version = findPreference("version")
        this.onPreferenceClick(version)
    }

    /**
     * Set up the [android.app.ActionBar], if the API is available.
     */
    private fun setupActionBar() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
    }

    /**
     * 1 先调用onPreferenceClick()方法，如果该方法返回true，则不再调用onPreferenceTreeClick方法 ；
     * 如果onPreferenceClick方法返回false，则继续调用onPreferenceTreeClick方法。
     * 2 onPreferenceChange的方法独立与其他两种方法的运行。也就是说，它总是会运行。
     * 点击某个Preference控件后，会先回调onPreferenceChange()方法，
     * 即是否保存值，然后再回调onPreferenceClick以及onPreferenceTreeClick()方法，
     * 因此在onPreferenceClick/onPreferenceTreeClick
     * 方法中我们得到的控件值就是最新的Preference控件值。
     *
     * onPreferenceChange 当Preference的元素值发送改变时，触发该事件。
     *  返回值：true  代表将新值写入sharedPreference文件中。
     *  false 则不将新值写入sharedPreference文件
     */
    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        LogUtils.i(tag = TAG, msg = preference!!.key)
        when (preference.key) {
            "is_open_shortcut_switch" -> {
                SmartToast.info("已更改设置，重启应用生效！")
            }
            "base_theme_list" -> {
                ThemeManager.changeTheme(newValue as String, instances)
                bindPreferenceSummaryToValue(preference, newValue)
            }
            "developer" -> {
                bindPreferenceSummaryToValue(preference, newValue!!)
            }
            "version" -> {
                bindPreferenceSummaryToValue(preference, newValue!!)
            }

            else -> {

            }
        }
        return true
    }

    /**
     * 当点击控件时触发发生
     */
    override fun onPreferenceClick(preference: Preference?): Boolean {
        when (preference!!.key) {
            "is_open_shortcut_switch" -> {
                val choice = getPreferenceBoolean(preference)
                LogUtils.i(tag = TAG, msg = " setOnPreferenceClickListener000$choice")

            }
            "base_theme_list" -> {
                val choice: String = getPreferenceString(preference)
                LogUtils.i(tag = TAG, msg = choice)
                bindPreferenceSummaryToValue(preference, choice)
            }
            "developer" -> {
                val value: String = getPreferenceString(preference)
                LogUtils.i(tag = TAG, msg = value)
                bindPreferenceSummaryToValue(preference, value)
            }
            "version" -> {
                val value: String = BuildConfig.VERSION_NAME//getPreferenceString(preference)
                LogUtils.i(tag = TAG, msg = value)
                bindPreferenceSummaryToValue(preference, value)
            }
        }
        return false
    }

    /**
     * {@inheritDoc}
     */
    override fun onIsMultiPane(): Boolean {
        return isXLargeTablet(this)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            android.R.id.home -> {
                finish()
//                startActivity(Intent(this, MainActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instances: Activity
        const val TAG: String = "SettingActivity"

        fun bindPreferenceSummaryToValue(preference: Preference, value: Any) {
            val stringValue = value.toString()

            if (preference is ListPreference) {
                LogUtils.i(tag = TAG, msg = preference.key)
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                val index = preference.findIndexOfValue(stringValue)

                // Set the summary to reflect the new value.
                preference.setSummary(
                    if (index >= 0)
                        preference.entries[index]
                    else
                        null
                )
            } else if (preference is RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary("silent")
                    //preference.setSummary(R.string.pref_ringtone_silent)

                } else {
                    val ringtone = RingtoneManager.getRingtone(
                        preference.getContext(), Uri.parse(stringValue)
                    )

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null)
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        val name = ringtone.getTitle(preference.getContext())
                        preference.setSummary(name)
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.summary = stringValue
            }
        }

        /**
         * Helper method to determine if the device has an extra-large screen. For
         * example, 10" tablets are extra-large.
         */
        private fun isXLargeTablet(context: Context): Boolean {
            return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_XLARGE
        }

        fun getPreferenceString(preference: Preference) = PreferenceManager
            .getDefaultSharedPreferences(preference.context)
            .getString(preference.key, "")!!

        fun getPreferenceBoolean(preference: Preference) = PreferenceManager
            .getDefaultSharedPreferences(preference.context)
            .getBoolean(preference.key, false)
    }


}

