package com.renameyourappname.mobile.moudule.base.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.renameyourappname.mobile.R
import com.renameyourappname.mobile.application.XApplication
import com.renameyourappname.mobile.injector.component.DaggerFragmentComponent
import com.renameyourappname.mobile.injector.component.FragmentComponent
import com.renameyourappname.mobile.injector.module.FragmentModule
import com.renameyourappname.mobile.moudule.base.dialog.DialogControl
import com.renameyourappname.mobile.moudule.base.dialog.WaitDialog
import com.renameyourappname.mobile.moudule.base.presenter.BasePresenter
import com.renameyourappname.mobile.moudule.base.view.IView
import com.renameyourappname.mobile.utils.utilShowNormalDialog
import com.renameyourappname.mobile.utils.utilShowToast
import javax.inject.Inject

/**
 * Created by Kobe on 2017/12/25.
 */
abstract class BaseFragment<T:Any>: android.support.v4.app.Fragment(),DialogControl,IView {
    //==============================================================
    //                        variable area
    //==============================================================
    protected lateinit var mFragmentComponent: FragmentComponent
    private var _waitDialog: WaitDialog? = null
    @Inject
    protected lateinit var mPresenter: T
    private val _isVisible: Boolean = false
    protected lateinit var mView: View
    protected var mContext: Activity? = null

    //==============================================================
    //                        constant area
    //==============================================================
    private val TAG = "BaseFragment"


    //==============================================================
    //                        method area
    //==============================================================
    override fun onAttach(activity: Activity?) {
        if (activity is BaseActivity<*>) {
            mContext = activity
        }
        super.onAttach(activity)
    }



    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        mView = inflater.inflate(getContentViewLayoutID(), null)


        mFragmentComponent = DaggerFragmentComponent.builder()
                .fragmentModule(FragmentModule(this))
                .appComponent(XApplication.getAppComponent())
                .build()

        initInjector()

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //if (mPresenter != null) {
        (mPresenter as BasePresenter<IView>).attachView(this)

        //}

        //        mErrorView = ButterKnife.findById(getActivity(), R.id.main_base_state_item);
        //        if (null != mErrorView) {
        //            mErrorText = (TextView) mErrorView.findViewById(R.id.tv_errormsg);
        //        }
        initViews()
        initEvents()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
       // if (mPresenter != null) {

        (mPresenter as BasePresenter<IView>).detachView()
       // }

    }

    /**
     * startActivity
     *
     * @param clazz
     */
    protected fun readyGo(clazz: Class<*>) {
        val intent = Intent(mContext, clazz)
        startActivity(intent)
    }

    /**
     * startActivity with bundle
     *
     * @param clazz
     * @param bundle
     */
    protected fun readyGo(clazz: Class<*>, bundle: Bundle?) {
        val intent = Intent(mContext, clazz)
        if (null != bundle) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    /**
     * startActivity then finish
     *
     * @param clazz
     */
    protected fun readyGoThenKill(clazz: Class<*>) {
        val intent = Intent(mContext, clazz).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        mContext!!.finish()
    }

    /**
     * startActivity with bundle then finish
     *
     * @param clazz
     * @param bundle
     */
    protected fun readyGoThenKill(clazz: Class<*>, bundle: Bundle?) {
        val intent = Intent(mContext, clazz).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        if (null != bundle) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
        mContext!!.finish()
    }

    /**
     * startActivityForResult
     *
     * @param clazz
     * @param requestCode
     */
    protected fun readyGoForResult(clazz: Class<*>, requestCode: Int) {
        val intent = Intent(mContext, clazz)
        startActivityForResult(intent, requestCode)
    }

    /**
     * startActivityForResult with bundle
     *
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    protected fun readyGoForResult(clazz: Class<*>, requestCode: Int, bundle: Bundle?) {
        val intent = Intent(mContext, clazz)
        if (null != bundle) {
            intent.putExtras(bundle)
        }
        startActivityForResult(intent, requestCode)
    }

    protected fun showToast(msg: String) {
            utilShowToast(view!!.rootView,msg)
    }

    protected abstract fun initInjector()

    protected abstract fun initViews()
    protected abstract fun initEvents()


    protected abstract fun getContentViewLayoutID(): Int

    override fun showError(s: String) {
        utilShowNormalDialog(this.mContext,this.mContext!!.getString(R.string.Error_Title_Msg),s)
    }

    override fun showWaitDialog(): WaitDialog? {
        if (_isVisible) {
            if (_waitDialog == null) {
                _waitDialog = getWaitDialog(mContext)
            }
            if (_waitDialog != null) {

                _waitDialog!!.show()
            }
            return _waitDialog
        }
        return null
    }



    private fun getWaitDialog(activity: Activity?): WaitDialog? {
        var dialog: WaitDialog? = null
        try {
            dialog = WaitDialog(activity!!,R.style.dialog)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return dialog
    }

    override fun hideWaitDialog() {
        if (_isVisible && _waitDialog != null) {
            try {
                _waitDialog!!.dismiss()
                _waitDialog = null
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }
    }

    override fun showMessage(msg: String) {
        showToast(msg)
    }

    override fun showLoading() {
        showWaitDialog()
    }

    override fun hideLoading() {
        hideWaitDialog()
    }
}