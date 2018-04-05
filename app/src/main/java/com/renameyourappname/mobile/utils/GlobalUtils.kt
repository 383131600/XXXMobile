package com.renameyourappname.mobile.utils

import android.app.Activity
import android.view.View
import com.canyinghao.candialog.CanDialog
import com.renameyourappname.mobile.R
import com.trycatch.mysnackbar.TSnackbar
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 全局工具函数,不需要类的外壳
 * __________________________________________________
 */

/**
 * 网络请求的处理线程设置
 */
fun <T> Observable<T>.applySchedulers(): Observable<T> {
    return subscribeOn(Schedulers.io()).
            unsubscribeOn(Schedulers.io()).
            observeOn(AndroidSchedulers.mainThread())
}

/**
 * Toast显示,这里用Snackbar代替,可以避免权限没开导致Toast不显示的问题
 * 这里用的样式是从顶部弹出
 * Activity需要window.decorView
 * Fragment需要view!!.rootView
 * 正确的view才能使Snackbar从顶部开始弹出
 */
fun utilShowToast(view: View, msg: String) {
    if (!StringUtils.isEmpty(msg)) {
        TSnackbar.make(view, msg, TSnackbar.LENGTH_LONG, TSnackbar.APPEAR_FROM_TOP_TO_DOWN).show()
    }
}

/**
 * 简单的信息提示Dialog,这里为了统一Activity和Fragment中的一般使用
 * 需要其他类型的直接定义使用
 */
fun utilShowNormalDialog(mContext: Activity?, title: String,msg: String) {

    CanDialog
            .Builder(mContext)
            .setTitle(title)
            .setMessage(msg)
            .setCircularRevealAnimator(CanDialog.CircularRevealStatus.TOP_LEFT)
            .setPositiveButton(mContext!!.getString(R.string.Enter_Msg), true, null)
            .show()
}

/*
除了可以全局直接调用方法还可以这样设置扩展函数来使用
如  "abc".test(123)
fun String.test(duration: Int): String {
    return ""
}*/
