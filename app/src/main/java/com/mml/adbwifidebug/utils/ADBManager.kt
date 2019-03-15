package com.mml.adbwifidebug.utils

import android.util.Log
import com.coder.zzq.smartshow.toast.SmartToast
import java.io.*


/**
 * 项目名称：ADBWIFICONNECT
 * Created by Long on 2019/3/14.
 * 修改时间：2019/3/14 15:38
 */
class ADBManager private constructor(private val pkgCodePath: String) {
    companion object {
        private val TAG = "ADBManager"
        @Volatile
        var instance: ADBManager? = null

        fun getInstance(pkgCodePath: String): ADBManager? {
            if (instance == null) {
                synchronized(ADBManager::class) {
                    if (instance == null) {
                        instance =
                            ADBManager(pkgCodePath)
                    }
                }
            }
            return instance
        }
    }

    fun execOpenADB(): Boolean {
        return if (getRootPermission()) {
            execRootCmdSilent(
                "setprop service.adb.tcp.port 5555\n" +
                        "stop adbd\n" +
                        "start adbd"
            )
            true
        } else {
            SmartToast.error("抱歉，当前设备未root，不能使用此功能!")
            false
        }
    }

    fun execCloseADB(): Boolean {
        return try {
            execRootCmdSilent(
                "stop adbd"
            )
            true
        } catch (EE: Exception) {
            SmartToast.fail(EE.message)
            false
        }

    }

    /**
     * // 执行命令但不关注结果输出
     * @param cmd 命令
     */

    private fun execRootCmdSilent(cmd: String): Int {
        var result = -1
        var dos: DataOutputStream? = null

        try {
            val p = Runtime.getRuntime().exec("su")
            dos = DataOutputStream(p.outputStream)

            Log.i(TAG, cmd)
            dos.writeBytes(cmd + "\n")
            dos.flush()
            dos.writeBytes("exit\n")
            dos.flush()
            p.waitFor()
            result = p.exitValue()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (dos != null) {
                try {
                    dos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return result
    }

    /**
     *
     * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
     * @return 应用程序是/否获取Root权限
     */

     fun getRootPermission(): Boolean {
        var process: Process? = null
        var os: DataOutputStream? = null
        try {
            val cmd = "chmod 777 $pkgCodePath"
            process = Runtime.getRuntime().exec("su") //切换到root帐号
            os = DataOutputStream(process!!.outputStream)
            os.writeBytes(cmd + "\n")
            os.writeBytes("exit\n")
            os.flush()
            process.waitFor()
        } catch (e: Exception) {
            return false
        } finally {
            try {
                os?.close()
                process!!.destroy()
            } catch (e: Exception) {
            }
        }
        return true
    }

    /**
     *  // 执行命令并且输出结果
     * @param cmd 命令
     */
    fun execRootCmd(cmd: String): String {
        var result = ""
        var dos: DataOutputStream? = null
        var dis: DataInputStream? = null

        try {
            val p = Runtime.getRuntime().exec("su")// 经过Root处理的android系统即有su命令
            dos = DataOutputStream(p.outputStream)
            dis = DataInputStream(p.inputStream)

            Log.i(TAG, cmd)
            dos.writeBytes(cmd + "\n")
            dos.flush()
            dos.writeBytes("exit\n")
            dos.flush()
            result=dis.readBytes().toString()
            p.waitFor()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (dos != null) {
                try {
                    dos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            if (dis != null) {
                try {
                    dis!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return result
    }

    object RootUtil {
        val isDeviceRooted: Boolean
            get() = checkRootMethod1() || checkRootMethod2() || checkRootMethod3()

        private fun checkRootMethod1(): Boolean {
            val buildTags = android.os.Build.TAGS
            return buildTags != null && buildTags.contains("test-keys")
        }

        private fun checkRootMethod2(): Boolean {
            val paths = arrayOf(
                "/system/app/Superuser.apk",
                "/sbin/su",
                "/system/bin/su",
                "/system/xbin/su",
                "/data/local/xbin/su",
                "/data/local/bin/su",
                "/system/sd/xbin/su",
                "/system/bin/failsafe/su",
                "/data/local/su",
                "/su/bin/su"
            )
            for (path in paths) {
                if (File(path).exists()) return true
            }
            return false
        }

        private fun checkRootMethod3(): Boolean {
            var process: Process? = null
            try {
                process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
                val `in` = BufferedReader(InputStreamReader(process!!.inputStream))
                return `in`.readLine() != null
            } catch (t: Throwable) {
                return false
            } finally {
                process?.destroy()
            }
        }
    }
}
