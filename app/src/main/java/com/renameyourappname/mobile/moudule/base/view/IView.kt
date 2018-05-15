package com.renameyourappname.mobile.moudule.base.view

/**
 * Created by Kobe on 2017/12/25.
 */
interface IView {
    fun showLoading()
    fun hideLoading()
    fun showDefaultToast(msg: String)
    fun showErrorToast(msg: String)
    fun showSuccessToast(msg: String)
    fun showWarningToast(msg: String)
    fun showErrorDialog(msg: String)
    fun done()
    fun finishRefresh()
}