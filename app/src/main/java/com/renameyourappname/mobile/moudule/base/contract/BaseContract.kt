package com.renameyourappname.mobile.moudule.base.contract

import com.renameyourappname.mobile.moudule.base.presenter.IPresenter
import com.renameyourappname.mobile.moudule.base.view.IView

/**
 * Created by Kobe on 2017/12/25.
 */
interface BaseContract {
    interface View : IView
    interface Presenter : IPresenter<View>
}