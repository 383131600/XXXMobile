package com.renameyourappname.mobile.moudule.main.ui

import com.renameyourappname.mobile.R
import com.renameyourappname.mobile.moudule.base.ui.BaseFragment
import com.renameyourappname.mobile.moudule.main.contract.MainContract
import com.renameyourappname.mobile.moudule.main.presenter.MainPresenter

/**
 * Created by Kobe on 2017/12/26.
 */
class MainFragment:BaseFragment<MainPresenter>(),MainContract.View {


    override fun showLogin(str: String?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initInjector() {
        mFragmentComponent.inject(this)
    }

    override fun initViews() {
       // Log.e("cici", mPresenter.presenteTest()+"${activity}")
        //showToast("1111111111111111")
        //showError("??????????")
    }

    override fun initEvents() {
    }

    override fun getContentViewLayoutID(): Int {
        return R.layout.fragment_home
    }

}