package com.renameyourappname.mobile.moudule.main.contract

import com.renameyourappname.mobile.model.bean.request.LoginRequest
import com.renameyourappname.mobile.moudule.base.presenter.IPresenter
import com.renameyourappname.mobile.moudule.base.view.IView

/**
 * Created by Kobe on 2017/12/26.
 */
interface MainContract {
    interface View : IView {
        fun showLogin(str:String?)
    }

    interface Presenter : IPresenter<View> {
        fun login(request: LoginRequest)
    }

}