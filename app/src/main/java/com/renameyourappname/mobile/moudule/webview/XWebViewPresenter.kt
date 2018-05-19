package com.renameyourappname.mobile.moudule.webview

import android.app.Activity
import com.renameyourappname.mobile.moudule.base.presenter.BasePresenter
import javax.inject.Inject
import com.renameyourappname.mobile.common.DataManager

class XWebViewPresenter @Inject constructor(mActivity: Activity, dataManager: DataManager) : BasePresenter<XWebViewContract.View>(mActivity, dataManager), XWebViewContract.Presenter {
}