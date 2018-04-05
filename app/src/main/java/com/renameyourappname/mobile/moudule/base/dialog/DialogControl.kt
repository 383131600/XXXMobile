package com.renameyourappname.mobile.moudule.base.dialog

/**
 * Created by Kobe on 2017/12/25.
 */
interface DialogControl {
    fun hideWaitDialog()

    fun showWaitDialog(): WaitDialog?

}