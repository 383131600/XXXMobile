package com.renameyourappname.mobile.moudule.base.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.renameyourappname.mobile.R
import com.renameyourappname.mobile.application.XApplication
import com.renameyourappname.mobile.injector.component.ActivityComponent
import com.renameyourappname.mobile.injector.component.DaggerActivityComponent
import com.renameyourappname.mobile.injector.module.ActivityModule
import com.renameyourappname.mobile.moudule.base.dialog.DialogControl
import com.renameyourappname.mobile.moudule.base.dialog.WaitDialog
import com.renameyourappname.mobile.moudule.base.presenter.BasePresenter
import com.renameyourappname.mobile.moudule.base.view.IView
import com.renameyourappname.mobile.utils.utilShowNormalDialog
import com.tbruyelle.rxpermissions2.RxPermissions
import javax.inject.Inject


/**
 * Created by Kobe on 2017/12/25.
 */
abstract class BaseActivity<P : Any> : BaseAppCompatActivity(), DialogControl, IView {
    //==============================================================
    //                        variable area
    //==============================================================

    @Inject
    protected lateinit var mPresenter: P


    private var _isVisible: Boolean = false
    private var _waitDialog: WaitDialog? = null


    private var mToolbar: RelativeLayout? = null
    protected var mTvTitle: TextView? = null
    protected var mIvMenu: ImageView? = null
    protected var mIvBack: ImageView? = null

    protected lateinit var inputMethodManager: InputMethodManager

    protected lateinit var mActivityComponent: ActivityComponent


    //==============================================================
    //                        method area
    //==============================================================
    abstract fun initInjector()

    //所有继承的子类如果复写onCreate的话不能SetcontentView,这样会覆盖基类的所有操作
    override fun onCreate(savedInstanceState: Bundle?) {


        mActivityComponent = DaggerActivityComponent.builder()
                .activityModule(ActivityModule(this))
                .appComponent(XApplication.getAppComponent())
                .build()

        initInjector()

        super.onCreate(savedInstanceState)

        if (!isTaskRoot) {
            val intent = intent
            val action = intent.action
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action == Intent.ACTION_MAIN) {
                finish()
                return
            }
        }
        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        _isVisible = true

        checkPermission()
    }

    //隐藏软键盘
    protected fun hideSoftKeyboard() {
        if (window.attributes.softInputMode !== WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN && currentFocus != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    /**
     * 权限组检查
     * 当用户选择不再提示的时候这里也可以截获作出对应
     * 可以分开不同权限做出不同处理
     */
    private fun checkPermission() {
        RxPermissions(this).requestEach(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.SEND_SMS
        ).subscribe {
            if (!it.granted) {
                when (it.name) {
                /*Manifest.permission.WRITE_EXTERNAL_STORAGE -> showToast(getString(R.string.Miss_Permission_WRITE_EXTERNAL_STORAGE))
                Manifest.permission.READ_CALENDAR -> showToast(getString(R.string.Miss_Permission_READ_CALENDAR))
                Manifest.permission.READ_CALL_LOG -> showToast(getString(R.string.Miss_Permission_READ_CALL_LOG))
                Manifest.permission.READ_CONTACTS->showToast(getString(R.string.Miss_Permission_READ_CONTACTS))
                Manifest.permission.READ_PHONE_STATE->showToast(getString(R.string.Miss_Permission_READ_PHONE_STATE))
                Manifest.permission.READ_SMS->showToast(getString(R.string.Miss_Permission_READ_SMS))
                Manifest.permission.RECORD_AUDIO->showToast(getString(R.string.Miss_Permission_RECORD_AUDIO))
                Manifest.permission.CAMERA->showToast(getString(R.string.Miss_Permission_CAMERA))
                Manifest.permission.CALL_PHONE->showToast(getString(R.string.Miss_Permission_CALL_PHONE))
                Manifest.permission.SEND_SMS->showToast(getString(R.string.Miss_Permission_SEND_SMS))*/
                }
            }
        }
    }


    //Start________________________WaitDialog______________________________
    private fun getWaitDialog(activity: Activity): WaitDialog? {
        var dialog: WaitDialog? = null
        try {
            dialog = WaitDialog(activity, R.style.dialog)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return dialog
    }

    override fun showWaitDialog(): WaitDialog? {
        if (_isVisible) {
            if (_waitDialog == null) {
                _waitDialog = getWaitDialog(this)
            }
            if (_waitDialog != null) {
                //_waitDialog!!.setMessage(text)
                _waitDialog!!.show()
            }
            return _waitDialog
        }
        return null
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

    override fun showLoading() {
        showWaitDialog()
    }

    override fun hideLoading() {
        hideWaitDialog()
    }
    override fun showMessage(msg: String) {
        showToast(msg)
    }

    override fun showError(s: String) {
        utilShowNormalDialog(this, getString(R.string.Error_Title_Msg), s)
    }

    //________________________WaitDialog______________________________End


    //Start______________________________Toolbar_________________________________________
    //back和menu的自定义点击事件直接在子类实现,基类不作处理,back默认是finish()
    override fun initToolbar() {
        mToolbar = findViewById<RelativeLayout>(R.id.toolbar)
        if (mToolbar != null) {
            mTvTitle = mToolbar!!.findViewById<TextView>(R.id.tv_toolbar_title)
            mIvMenu = mToolbar!!.findViewById<ImageView>(R.id.iv_toolbar_menu)
            mIvBack = mToolbar!!.findViewById<ImageView>(R.id.iv_toolbar_back)
            mIvBack!!.setOnClickListener {
                finish()
            }
        }
    }

    fun showBackButton() {
        if (mIvBack != null) {
            mIvBack!!.visibility = View.VISIBLE
        }
    }

    fun showMenuButton() {
        if (mIvMenu != null) {
            mIvMenu!!.visibility = View.VISIBLE
        }
    }

    fun setToolbarTitle(msg: String) {
        if (mTvTitle != null) {
            mTvTitle!!.text = msg
        }
    }
    //______________________________Toolbar_________________________________________end


    //Start_______________________________System override______________________________

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        _isVisible = true
    }

    override fun beforeSetContentView() {
        //由于kotlin语法已经做了非空检测,这里不需要再次检测,永远是true
        (mPresenter as BasePresenter<IView>).attachView(this)
    }

    protected override fun onDestroy() {
        hideWaitDialog()
        super.onDestroy()
        (mPresenter as BasePresenter<IView>).detachView()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    //部分手机系统onBackPressed无法触发,利用这个方法兼容
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        //拦截返回键
        if (event.getKeyCode() === KeyEvent.KEYCODE_BACK) {
            //判断触摸UP事件才会进行返回事件处理
            if (event.getAction() === KeyEvent.ACTION_UP) {
                onBackPressed()
            }
            //只要是返回事件，直接返回true，表示消费掉
            return true
        }
        return super.dispatchKeyEvent(event)
    }
    //_______________________________System override________________________End

}