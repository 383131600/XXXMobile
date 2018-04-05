package com.renameyourappname.mobile.moudule.base.presenter

import android.app.Activity
import com.renameyourappname.mobile.common.DataManager
import com.renameyourappname.mobile.model.LoginModel
import com.renameyourappname.mobile.moudule.base.view.IView

/**
 * Created by Kobe on 2017/12/25.
 */
abstract class BasePresenter<V:IView>: IPresenter<V>{
    protected lateinit var mActivity: Activity
    protected var mView: V? = null
    protected lateinit var dataManager: DataManager

    constructor(mActivity: Activity, dataManager: DataManager) {
        this.mActivity = mActivity
        this.dataManager = dataManager
    }

    /**
     * 获取登录Token
     */
    protected fun getToken():String?{
        return dataManager.create(LoginModel::class.java).getToken()
    }

    override fun attachView(iView: V) {
        this.mView = iView
    }

    override fun detachView() {
        this.mView = null
    }
}