package com.renameyourappname.mobile.moudule.main.presenter

import android.app.Activity
import com.renameyourappname.mobile.common.DataManager
import com.renameyourappname.mobile.model.LoginModel
import com.renameyourappname.mobile.model.bean.request.LoginRequest
import com.renameyourappname.mobile.moudule.base.contract.BaseContract
import com.renameyourappname.mobile.moudule.base.presenter.BasePresenter
import com.renameyourappname.mobile.moudule.base.presenter.IPresenter
import com.renameyourappname.mobile.moudule.base.view.IView
import com.renameyourappname.mobile.moudule.main.contract.MainContract
import com.renameyourappname.mobile.utils.applySchedulers
import javax.inject.Inject

/**
 * Created by Kobe on 2017/12/26.
 */
class  MainPresenter @Inject constructor(mActivity:Activity,dataManager: DataManager): BasePresenter<MainContract.View>(mActivity,dataManager),MainContract.Presenter {
    override fun login(request: LoginRequest) {
        //mView!!.showLoading("")
        //dataManager.create(LoginModel::class.java).login(request)
        dataManager.request().login(request.access_token,request.username,request.password).applySchedulers().subscribe {
            val loginModel=dataManager.create(LoginModel::class.java)
            loginModel.setToken(it.data)

            mView!!.showLogin(loginModel.getToken())
        }
    }

}