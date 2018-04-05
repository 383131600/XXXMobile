package com.renameyourappname.mobile.moudule.base.listener

interface PermissionsResultListener {
    //成功
    fun onSuccessful(grantResults: IntArray)

    //失败
    fun onFailure()
}