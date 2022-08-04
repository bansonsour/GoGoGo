package com.zcshou.client

/**
 * 项目名称：GoGoGo
 * 创建人：BenC Zhang zhangzhihua@yy.com
 * 类描述：TODO(这里用一句话描述这个方法的作用)
 * 创建时间：2022/8/4 10:07
 * @version  V1.0
 */
import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
/**
 * Created by deh001 on 2018/5/23
 */

object RuntimeInfo {

    @JvmField
    var sProcessName: String? = ""
    @JvmField
    var sPackageName = ""
    @JvmStatic
    lateinit var sAppContext: Context
    @JvmField
    var sIsDebuggable = false
    @JvmField
    var sIsMainProcess = true
    @JvmField
    var sVersion: String? = ""

    /**
     * 保存进程名
     *
     * @param processName
     **/
    fun processName(processName: String): RuntimeInfo = apply { sProcessName = processName }

    /**
     * 保存包名
     *
     * @param packageName
     * */
    fun packageName(packageName: String): RuntimeInfo = apply { sPackageName = packageName }

    /**
     * 保存application对象
     *
     * @param context
     * */
    fun appContext(context: Context): RuntimeInfo = apply { sAppContext = context }

    /**
     * 保存当前是否调试模式
     * @param debug
     * **/
    fun isDebuggable(debug: Boolean): RuntimeInfo = apply { sIsDebuggable = debug }

    /**
     * 保存当前进程是否UI进程
     * @param mainProcess
     * **/
    fun isMainProcess(mainProcess: Boolean): RuntimeInfo = apply { sIsMainProcess = mainProcess }

    /**
     * 保存当前进程版本号
     */
    fun version(ver: String): RuntimeInfo = apply { sVersion = ver }
}