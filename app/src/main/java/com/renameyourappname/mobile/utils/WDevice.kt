package com.renameyourappname.mobile.utils

import android.R
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
import android.content.*
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Point
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.PowerManager
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Display
import android.view.View
import android.view.ViewConfiguration
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import java.io.File
import java.lang.reflect.Field
import java.text.NumberFormat

/**
 * Created by Kobe on 2017/12/25.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
object WDevice {
    // 手机网络类型
    val NETTYPE_WIFI = 0x01
    val NETTYPE_CMWAP = 0x02
    val NETTYPE_CMNET = 0x03

    var GTE_HC: Boolean = false
    var GTE_ICS: Boolean = false
    var PRE_HC: Boolean = false
    private var _hasBigScreen: Boolean? = null
    private var _hasCamera: Boolean? = null
    private var _isTablet: Boolean? = null
    private var _loadFactor: Int? = null

    private val _pageSize = -1
    var displayDensity = 0.0f

    //在实例初始化期间，初始化块按照它们出现在类体中的顺序执行，与属性初始化器交织在一起
    init
    {
        GTE_ICS = Build.VERSION.SDK_INT >= 14
        GTE_HC = Build.VERSION.SDK_INT >= 11
        PRE_HC = Build.VERSION.SDK_INT >= 11
    }


    fun dpToPixel(ctx: Context, dp: Float): Float {
        return dp * (getDisplayMetrics(ctx).densityDpi / 160f)
    }

    fun getDefaultLoadFactor(ctx: Context): Int {
        if (_loadFactor == null) {
            val integer = Integer.valueOf(0xf and ctx.resources.configuration.screenLayout)
            _loadFactor = integer
            _loadFactor = Integer.valueOf(Math.max(integer!!.toInt(), 1))
        }
        return _loadFactor!!.toInt()
    }

    fun getDensity(ctx: Context): Float {
        if (displayDensity.toDouble() == 0.0)
            displayDensity = getDisplayMetrics(ctx).density
        return displayDensity
    }

    fun getDisplayMetrics(ctx: Context): DisplayMetrics {
        val displaymetrics = DisplayMetrics()
        (ctx.getSystemService(
                Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(
                displaymetrics)
        return displaymetrics
    }

    fun getScreenHeight(ctx: Context): Float {
        return getDisplayMetrics(ctx).heightPixels.toFloat()
    }

    fun getScreenWidth(ctx: Context): Float {
        return getDisplayMetrics(ctx).widthPixels.toFloat()
    }

    fun getRealScreenSize(activity: Activity): IntArray {
        val size = IntArray(2)
        var screenWidth: Int
        var screenHeight: Int
        val w = activity.windowManager
        val d = w.defaultDisplay
        val metrics = DisplayMetrics()
        d.getMetrics(metrics)
        // since SDK_INT = 1;
        screenWidth = metrics.widthPixels
        screenHeight = metrics.heightPixels
        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT in 14..16)
            try {
                screenWidth = Display::class.java.getMethod("getRawWidth")
                        .invoke(d) as Int
                screenHeight = Display::class.java
                        .getMethod("getRawHeight").invoke(d) as Int
            } catch (ignored: Exception) {
            }

        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 17)
            try {
                val realSize = Point()
                Display::class.java.getMethod("getRealSize", Point::class.java).invoke(d,
                        realSize)
                screenWidth = realSize.x
                screenHeight = realSize.y
            } catch (ignored: Exception) {
            }

        size[0] = screenWidth
        size[1] = screenHeight
        return size
    }

    fun getStatusBarHeight(ctx: Context): Int {
        var c: Class<*>?
        var obj: Any?
        var field: Field?
        var x: Int
        try {
            c = Class.forName("com.android.internal.R\$dimen")
            obj = c!!.newInstance()
            field = c.getField("status_bar_height")
            x = Integer.parseInt(field!!.get(obj).toString())
            return ctx.resources.getDimensionPixelSize(x)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return 0
    }


    fun hasBigScreen(ctx: Context): Boolean {
        var flag = true
        if (_hasBigScreen == null) {
            val flag1: Boolean = if (0xf and ctx.resources.configuration.screenLayout >= 3)
                flag
            else
                false
            val boolean1 = java.lang.Boolean.valueOf(flag1)
            _hasBigScreen = boolean1
            if (!boolean1!!) {
                if (getDensity(ctx) <= 1.5f)
                    flag = false
                _hasBigScreen = java.lang.Boolean.valueOf(flag)
            }
        }
        return _hasBigScreen!!
    }

    fun hasCamera(ctx: Context): Boolean {
        if (_hasCamera == null) {
            val pckMgr = ctx.packageManager
            val flag = pckMgr
                    .hasSystemFeature("android.hardware.camera.front")
            val flag1 = pckMgr.hasSystemFeature("android.hardware.camera")
            val flag2: Boolean
            flag2 = flag || flag1
            _hasCamera = java.lang.Boolean.valueOf(flag2)
        }
        return _hasCamera!!
    }

    fun hasHardwareMenuKey(context: Context): Boolean {
        var flag: Boolean
        flag = when {
            PRE_HC -> true
            GTE_ICS -> ViewConfiguration.get(context).hasPermanentMenuKey()
            else -> false
        }
        return flag
    }

    @SuppressLint("MissingPermission")
    fun hasInternet(ctx: Context): Boolean {
        return (ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo != null
    }

    fun gotoGoogleMarket(activity: Activity, pck: String): Boolean {
        return try {
            val intent = Intent()
            intent.`package` = "com.android.vending"
            intent.action = Intent.ACTION_VIEW
            intent.data = Uri.parse("market://details?id=" + pck)
            activity.startActivity(intent)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

    }

    fun isPackageExist(ctx: Context, pckName: String): Boolean {
        try {
            val pckInfo = ctx.packageManager
                    .getPackageInfo(pckName, 0)
            if (pckInfo != null)
                return true
        } catch (e: PackageManager.NameNotFoundException) {

        }

        return false
    }

    fun hideAnimatedView(view: View?) {
        if (PRE_HC && view != null)
            view.setPadding(view.width, 0, 0, 0)
    }

    fun hideSoftKeyboard(ctx: Context, view: View?) {
        if (view == null)
            return
        (ctx.getSystemService(
                Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                view.windowToken, 0)
    }

    fun isLandscape(ctx: Context): Boolean {
        return ctx.resources.configuration.orientation == 2
    }

    fun isPortrait(ctx: Context): Boolean {
        var flag = true
        if (ctx.resources.configuration.orientation != 1)
            flag = false
        return flag
    }

    fun isTablet(ctx: Context): Boolean {
        if (_isTablet == null) {
            val flag: Boolean = 0xf and ctx.resources
                    .configuration.screenLayout >= 3
            _isTablet = java.lang.Boolean.valueOf(flag)
        }
        return _isTablet!!
    }

    fun pixelsToDp(ctx: Context, f: Float): Float {
        return f / (getDisplayMetrics(ctx).densityDpi / 160f)
    }

    fun showAnimatedView(view: View?) {
        if (PRE_HC && view != null)
            view.setPadding(0, 0, 0, 0)
    }

    fun showSoftKeyboard(dialog: Dialog) {
        dialog.window!!.setSoftInputMode(4)
    }

    fun showSoftKeyboard(ctx: Context, view: View) {
        (ctx.getSystemService(
                Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(view,
                InputMethodManager.SHOW_FORCED)
    }

    fun toogleSoftKeyboard(ctx: Context) {
        (ctx.getSystemService(
                Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(0,
                InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun isSdcardReady(): Boolean {
        return Environment.MEDIA_MOUNTED == Environment
                .getExternalStorageState()
    }

    fun getCurCountryLan(ctx: Context): String {
        return (ctx.resources.configuration.locale
                .language
                + "-"
                + ctx.resources.configuration.locale
                .country)
    }

    fun isZhCN(ctx: Context): Boolean {
        val lang = ctx.resources
                .configuration.locale.country
        return lang.equals("CN", ignoreCase = true)
    }

    fun percent(p1: Double, p2: Double): String {
        val str: String
        val p3 = p1 / p2
        val nf = NumberFormat.getPercentInstance()
        nf.minimumFractionDigits = 2
        str = nf.format(p3)
        return str
    }

    fun percent2(p1: Double, p2: Double): String {
        val str: String
        val p3 = p1 / p2
        val nf = NumberFormat.getPercentInstance()
        nf.minimumFractionDigits = 0
        str = nf.format(p3)
        return str
    }

    fun gotoMarket(context: Context, pck: String) {
        if (!isHaveMarket(context)) {
            //	    AppContext.showToast("你手机中没有安装应用市场！");
            return
        }
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse("market://details?id=" + pck)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

    fun isHaveMarket(context: Context): Boolean {
        val intent = Intent()
        intent.action = "android.intent.action.MAIN"
        intent.addCategory("android.intent.category.APP_MARKET")
        val pm = context.packageManager
        val infos = pm.queryIntentActivities(intent, 0)
        return infos.size > 0
    }

    fun openAppInMarket(context: Context?) {
        if (context != null) {
            val pckName = context.packageName
            try {
                gotoMarket(context, pckName)
            } catch (ex: Exception) {
                try {
                    val otherMarketUri = "http://market.android.com/details?id=" + pckName
                    val intent = Intent(Intent.ACTION_VIEW,
                            Uri.parse(otherMarketUri))
                    context.startActivity(intent)
                } catch (e: Exception) {

                }

            }

        }
    }

    fun setFullScreen(activity: Activity) {
        val params = activity.window
                .attributes
        params.flags = params.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
        activity.window.attributes = params
        activity.window.addFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    fun cancelFullScreen(activity: Activity) {
        val params = activity.window
                .attributes
        params.flags = params.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
        activity.window.attributes = params
        activity.window.clearFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    fun getPackageInfo(ctx: Context, pckName: String): PackageInfo? {
        try {
            return ctx.packageManager
                    .getPackageInfo(pckName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
        }

        return null
    }

    fun getVersionCode(ctx: Context): Int {
        var versionCode: Int
        versionCode = try {
            ctx.packageManager
                    .getPackageInfo(ctx.packageName,
                            0).versionCode
        } catch (ex: PackageManager.NameNotFoundException) {
            0
        }

        return versionCode
    }

    fun getVersionCode(ctx: Context, packageName: String): Int {
        var versionCode: Int
        versionCode = try {
            ctx.packageManager
                    .getPackageInfo(packageName, 0).versionCode
        } catch (ex: PackageManager.NameNotFoundException) {
            0
        }

        return versionCode
    }

    fun getVersionName(ctx: Context): String {
        var name: String
        name = try {
            ctx.packageManager
                    .getPackageInfo(ctx.packageName,
                            0).versionName
        } catch (ex: PackageManager.NameNotFoundException) {
            ""
        }

        return name
    }

    fun isScreenOn(ctx: Context): Boolean {
        val pm = ctx.getSystemService(Context.POWER_SERVICE) as PowerManager
        return pm.isScreenOn
    }

    fun installAPK(context: Context, file: File?) {
        if (file == null || !file.exists())
            return
        val intent = Intent()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive")
        context.startActivity(intent)
    }

    fun getInstallApkIntent(file: File): Intent {
        val intent = Intent()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive")
        return intent
    }

    fun openDial(context: Context, number: String) {
        val uri = Uri.parse("tel:" + number)
        val it = Intent(Intent.ACTION_DIAL, uri)
        context.startActivity(it)
    }

    fun openSMS(context: Context, smsBody: String, tel: String) {
        val uri = Uri.parse("smsto:" + tel)
        val it = Intent(Intent.ACTION_SENDTO, uri)
        it.putExtra("sms_body", smsBody)
        context.startActivity(it)
    }

    fun openDail(context: Context) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun openSendMsg(context: Context) {
        val uri = Uri.parse("smsto:")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    @SuppressLint("WrongConstant")
    fun openCamera(context: Context) {
        val intent = Intent() // 调用照相机
        intent.action = "android.media.action.STILL_IMAGE_CAMERA"
        intent.flags = 0x34c40000
        context.startActivity(intent)
    }

    @SuppressLint("MissingPermission")
    fun getIMEI(ctx: Context): String {
        val tel = ctx.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tel.deviceId
    }

    fun getPhoneType(): String {
        return Build.MODEL
    }

    fun openApp(context: Context, packageName: String) {
        var mainIntent = context.packageManager
                .getLaunchIntentForPackage(packageName)
        if (mainIntent == null) {
            mainIntent = Intent(packageName)
        } else {

        }
        context.startActivity(mainIntent)
    }

    fun openAppActivity(context: Context, packageName: String,
                        activityName: String): Boolean {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val cn = ComponentName(packageName, activityName)
        intent.component = cn
        return try {
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            false
        }

    }

    @SuppressLint("MissingPermission")
    fun isWifiOpen(ctx: Context): Boolean {
        var isWifiConnect = false
        val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // check the networkInfos numbers
        val networkInfos = cm.allNetworkInfo
        for (i in networkInfos.indices) {
            if (networkInfos[i].state == NetworkInfo.State.CONNECTED) {
                if (networkInfos[i].type == ConnectivityManager.TYPE_MOBILE) {
                    isWifiConnect = false
                }
                if (networkInfos[i].type == ConnectivityManager.TYPE_WIFI) {
                    isWifiConnect = true
                }
            }
        }
        return isWifiConnect
    }

    fun uninstallApk(context: Context, packageName: String) {
        if (isPackageExist(context, packageName)) {
            val packageURI = Uri.parse("package:" + packageName)
            val uninstallIntent = Intent(Intent.ACTION_DELETE,
                    packageURI)
            context.startActivity(uninstallIntent)
        }
    }

    fun copyTextToBoard(ctx: Context, string: String) {
        if (TextUtils.isEmpty(string))
            return
        val clip = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clip.text = string
        //	AppContext.showToast(R.string.copy_success);
    }

    /**
     * 发送邮件
     *
     * @param context
     * @param subject
     * 主题
     * @param content
     * 内容
     * @param emails
     * 邮件地址
     */
    fun sendEmail(context: Context, subject: String,
                  content: String, vararg emails: String) {
        try {
            val intent = Intent(Intent.ACTION_SEND)
            // 模拟器
            // intent.setType("text/plain");
            intent.type = "message/rfc822" // 真机
            intent.putExtra(Intent.EXTRA_EMAIL, emails)
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.putExtra(Intent.EXTRA_TEXT, content)
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }

    }

    fun getStatuBarHeight(ctx: Context): Int {
        var c: Class<*>?
        var obj: Any?
        var field: Field?
        var x: Int
        var sbar = 38// 默认为38，貌似大部分是这样的
        try {
            c = Class.forName("com.android.internal.R\$dimen")
            obj = c!!.newInstance()
            field = c.getField("status_bar_height")
            x = Integer.parseInt(field!!.get(obj).toString())
            sbar = ctx.resources
                    .getDimensionPixelSize(x)

        } catch (e1: Exception) {
            e1.printStackTrace()
        }

        return sbar
    }

    fun getActionBarHeight(context: Context): Int {
        var actionBarHeight = 0
        val tv = TypedValue()
        if (context.theme.resolveAttribute(R.attr.actionBarSize,
                tv, true))
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
                    context.resources.displayMetrics)

        if (actionBarHeight == 0 && context.theme.resolveAttribute(R.attr.actionBarSize,
                tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
                    context.resources.displayMetrics)
        }

        return actionBarHeight
    }

    fun hasStatusBar(activity: Activity): Boolean {
        val attrs = activity.window.attributes
        return attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN != WindowManager.LayoutParams.FLAG_FULLSCREEN
    }

    /**
     * 调用系统安装了的应用分享
     *
     * @param context
     * @param title
     * @param url
     */
    fun showSystemShareOption(context: Activity,
                              title: String, url: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享：" + title)
        intent.putExtra(Intent.EXTRA_TEXT, title + " " + url)
        context.startActivity(Intent.createChooser(intent, "选择分享"))
    }

    @SuppressLint("MissingPermission")
            /**
     * 获取当前网络类型
     *
     * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
     */
    fun getNetworkType(ctx: Context): Int {
        var netType = 0
        val connectivityManager = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo ?: return netType
        val nType = networkInfo.type
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            val extraInfo = networkInfo.extraInfo
            if (!StringUtils.isEmpty(extraInfo)) {
                netType = if (extraInfo.toLowerCase() == "cmnet") {
                    NETTYPE_CMNET
                } else {
                    NETTYPE_CMWAP
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI
        }
        return netType
    }
}