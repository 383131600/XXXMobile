package com.renameyourappname.mobile.moudule.base.presenter

import com.renameyourappname.mobile.moudule.base.view.IView

/**
 * Created by Kobe on 2017/12/25.
 */
interface IPresenter<V:IView> {

    fun attachView(iView: V)
    fun detachView()

}