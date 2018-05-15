package com.renameyourappname.mobile.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import cn.qqtheme.framework.picker.OptionPicker
import cn.qqtheme.framework.widget.WheelView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.MediaStoreSignature
import com.canyinghao.candialog.CanDialog
import com.canyinghao.candialog.CanDialogInterface
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.data.Type
import com.renameyourappname.mobile.R
import com.renameyourappname.mobile.application.XApplication
import com.trycatch.mysnackbar.Prompt
import com.trycatch.mysnackbar.TSnackbar
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.DecimalFormat
import java.util.*

/**
 * 全局工具函数,不需要类的外壳
 * __________________________________________________
 */

/**
 * 网络请求的处理线程设置
 */
/**
 * 网络请求的处理线程设置
 */
fun <T> Observable<T>.applySchedulers(): Observable<T> {
    return subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

/**
 * Toast显示,这里用Snackbar代替,可以避免权限没开导致Toast不显示的问题
 * 这里用的样式是从顶部弹出
 * Activity需要window.decorView
 * Fragment需要view!!.rootView
 * 正确的view才能使Snackbar从顶部开始弹出
 */
fun globalUtilShowDefaultToast(view: View, msg: String) {
    if (!StringUtils.isEmpty(msg)) {
        TSnackbar
                .make(view, msg, TSnackbar.LENGTH_LONG, TSnackbar.APPEAR_FROM_TOP_TO_DOWN)
                .setActionTextColor(view.context.resources.getColor(R.color.primary_material_dark))
                .setMessageTextColor(view.context.resources.getColor(R.color.primary_material_dark))
                .setBackgroundColor(view.context.resources.getColor(R.color.module_white))
                .show()
    }
}

fun globalUtilShowErrorToast(view: View, msg: String) {
    if (!StringUtils.isEmpty(msg)) {
        TSnackbar.make(view, msg, TSnackbar.LENGTH_LONG, TSnackbar.APPEAR_FROM_TOP_TO_DOWN).setPromptThemBackground(Prompt.ERROR).show()
    }
}

fun globalUtilShowSuccessToast(view: View, msg: String) {
    if (!StringUtils.isEmpty(msg)) {
        TSnackbar.make(view, msg, TSnackbar.LENGTH_LONG, TSnackbar.APPEAR_FROM_TOP_TO_DOWN).setPromptThemBackground(Prompt.SUCCESS).show()
    }
}

fun globalUtilShowWarningToast(view: View, msg: String) {
    if (!StringUtils.isEmpty(msg)) {
        TSnackbar.make(view, msg, TSnackbar.LENGTH_LONG, TSnackbar.APPEAR_FROM_TOP_TO_DOWN).setPromptThemBackground(Prompt.WARNING).show()
    }
}


/**
 * 简单的信息提示Dialog,这里为了统一Activity和Fragment中的一般使用
 * 需要其他类型的直接定义使用
 */
fun globalUtilShowNormalDialog(mContext: Activity?, title: String, msg: String): CanDialog {
    return globalUtilShowNormalDialog(mContext,title,msg,null)
}

fun globalUtilShowNormalDialog(mContext: Activity?, title: String, msg: String,listener: CanDialogInterface.OnClickListener?): CanDialog {
    return CanDialog
            .Builder(mContext)
            .setTitle(title)
            .setMessage(msg)
            .setCircularRevealAnimator(CanDialog.CircularRevealStatus.TOP_LEFT)
            .setPositiveButton(mContext!!.getString(R.string.Msg_Enter), true, listener)
            .show()
}

fun globalUtilShowCancelDialog(mContext: Activity?, title: String, msg: String,enterListener: CanDialogInterface.OnClickListener?): CanDialog {
    return globalUtilShowCancelDialog(mContext,title,msg,enterListener,null)
}

fun globalUtilShowCancelDialog(mContext: Activity?, title: String, msg: String, enterListener: CanDialogInterface.OnClickListener?, cancelListener: CanDialogInterface.OnClickListener?): CanDialog {
    return CanDialog
            .Builder(mContext)
            .setTitle(title)
            .setMessage(msg)
            .setCircularRevealAnimator(CanDialog.CircularRevealStatus.TOP_LEFT)
            .setPositiveButton(mContext!!.getString(R.string.Msg_Enter), true, enterListener)
            .setNegativeButton(mContext!!.getString(R.string.Msg_Cancel), true, cancelListener)
            .show()
}


fun globalUtilDefaultGlide(context: Context, url: Any?, imageView: ImageView, listener: RequestListener<Drawable>?) {
    /*
    val options = RequestOptions()
    options.fitCenter()
    options.placeholder()
    options.errorPlaceholder()
    */
    Glide
            .with(context)
            .load(url)
            //.apply(options)   //v4版本的使用方法
            .listener(listener)
            .into(imageView)
}


fun globalUtilDefaultGlide(context: Context, url: Any?, imageView: ImageView){
    globalUtilDefaultGlide(context,url,imageView,null)
}


/**
 * 通过改变缓存的key达到每次打开都刷新的效果
 */
fun globalUtilNoCacheGlide(context: Context, url: Any?, imageView: ImageView) {
    val options = RequestOptions()

    options.signature(MediaStoreSignature("no_cache", Date().time, 0))
    Glide
            .with(context)
            .load(url)
            .apply(options)   //v4版本的使用方法
            .into(imageView)
}

/**
 * 返回的只是builder,要调用build(),如果要实现回调则先设置setCallBack()后再build
 */
fun globalUtilCreateDefaultDatePicker(context: Context): TimePickerDialog.Builder? {
    val maxTime = 10L * 365 * 1000 * 60 * 60 * 24L //10年,必须显式表明是Long类型,否则系统认为是Int的时界面会出问题
    return TimePickerDialog.Builder()
            //.setCallBack(this)  //回调事件直接在创建的地方实现
            .setCancelStringId(context.getString(R.string.Msg_Cancel))
            .setSureStringId(context.getString(R.string.Msg_Enter))
            .setTitleStringId("选择时间")
            .setYearText("年")
            .setMonthText("月")
            .setDayText("日")
            .setHourText("时")
            .setMinuteText("分")
            .setCyclic(false)
            .setMinMillseconds(System.currentTimeMillis())
            .setMaxMillseconds(System.currentTimeMillis() + maxTime)
            .setCurrentMillseconds(System.currentTimeMillis())
            .setThemeColor(context.resources.getColor(R.color.timepicker_dialog_bg))
            .setType(Type.ALL)
            .setWheelItemTextNormalColor(context.resources.getColor(R.color.timetimepicker_default_text_color))
            .setWheelItemTextSelectorColor(context.resources.getColor(R.color.timepicker_toolbar_bg))
            .setWheelItemTextSize(12)

}

/**
 * 返回条目选择器,需要监听回调就实现setOnOptionPickListener
 */
fun globalUtilCreateDefaultOptionPicker(activity: Activity, array: Array<String>): OptionPicker {

    var optionPicker = OptionPicker(activity, array)
    optionPicker.setCanceledOnTouchOutside(false)
    optionPicker.setDividerRatio(WheelView.DividerConfig.FILL)
    optionPicker.setShadowColor(Color.RED, 40)
    optionPicker.selectedIndex = 1
    optionPicker.setCycleDisable(true)
    optionPicker.setTextSize(12)
    return optionPicker
}


//判断空值和设置值,可设置默认值
fun globalUtilSetText(tv: TextView, text:String?, defaultText:String){
    if (text!=null&&StringUtils.isNotEmpty(text)){
        tv.text=text
    }else{
        tv.text=defaultText
    }
}
//判断空值和设置值,默认值为空
fun globalUtilSetText(tv: TextView, text:String?){
    globalUtilSetText(tv,text,"")
}

//格式化金额,为了避免用户设置其他的系统语言导致 . 变成  ,
//这里统一用Locale.CHINA保证格式统一
fun globalUtilDecimalFormat(s:String): String {
    return DecimalFormat("#0.00").format(s.toDouble())
    //return String.format(Locale.CHINA,"%.2f",s)
}

//复制文本到剪切板
fun globalUtilClipText(content:String){
    val cm = XApplication.sAppComponent.getContext().getSystemService(Context.CLIPBOARD_SERVICE)as ClipboardManager
    cm.text=content
    // 将文本内容放到系统剪贴板里。
    ClipData.newPlainText(content, content)
}