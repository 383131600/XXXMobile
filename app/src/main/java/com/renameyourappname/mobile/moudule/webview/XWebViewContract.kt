package com.renameyourappname.mobile.moudule.webview


import com.renameyourappname.mobile.moudule.base.presenter.IPresenter
import com.renameyourappname.mobile.moudule.base.view.IView

interface XWebViewContract {
    interface View: IView
    interface Presenter: IPresenter<View>
}