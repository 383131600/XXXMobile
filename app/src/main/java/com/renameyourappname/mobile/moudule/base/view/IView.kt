package com.renameyourappname.mobile.moudule.base.view

/**
 * Created by Kobe on 2017/12/25.
 */
interface IView {
    fun showLoading()
    fun hideLoading()
    fun showMessage(msg: String)
    fun showError(s: String)
}