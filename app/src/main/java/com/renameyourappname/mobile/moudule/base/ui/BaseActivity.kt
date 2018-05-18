package com.renameyourappname.mobile.moudule.base.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.Fragment
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.MediaStoreSignature
import com.canyinghao.candialog.CanDialog
import com.luozm.captcha.Captcha
import com.renameyourappname.mobile.R
import com.renameyourappname.mobile.application.XApplication
import com.renameyourappname.mobile.common.Constant
import com.renameyourappname.mobile.common.XRuntimeExcepttion
import com.renameyourappname.mobile.injector.component.ActivityComponent
import com.renameyourappname.mobile.injector.component.DaggerActivityComponent
import com.renameyourappname.mobile.injector.module.ActivityModule
import com.renameyourappname.mobile.moudule.base.dialog.DialogControl
import com.renameyourappname.mobile.moudule.base.dialog.WaitDialog
import com.renameyourappname.mobile.moudule.base.presenter.BasePresenter
import com.renameyourappname.mobile.moudule.base.view.IView
import com.renameyourappname.mobile.utils.*
import com.tbruyelle.rxpermissions2.RxPermissions
import java.util.*
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
    protected var mTvMenu: TextView? = null
    protected var mTvBack: TextView? = null

    protected lateinit var inputMethodManager: InputMethodManager

    protected lateinit var mActivityComponent: ActivityComponent

    private var currentFragment: Fragment? = null

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
        if (window.attributes.softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN && currentFocus != null) {
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


    /**
     * 显示Fragment
     *
     * @param fragment
     */
    protected fun showFragment(fragment: Fragment, position: Int) {

        if (setFragmentContainerResId() == -1) {
            throw XRuntimeExcepttion("Fragment container ResId is null!! You have to override setFragmentContainerResId() function and set your ResId")
        }

        val fm = supportFragmentManager

        val transaction = fm.beginTransaction()
        //Fragment添加
        if (!fragment.isAdded) {
            //            fragment.setArguments(bundle);
            transaction.add(setFragmentContainerResId(), fragment, position.toString() + "")
        }
        if (currentFragment == null) {
            currentFragment = fragment
        }
        //通过tag进行过渡动画滑动判断
        if (Integer.parseInt(currentFragment!!.tag) >= Integer.parseInt(fragment.tag)) {
            transaction.setCustomAnimations(R.anim.fragment_push_left_in, R.anim.fragment_push_right_out)
        } else {
            transaction.setCustomAnimations(R.anim.fragment_push_right_in, R.anim.fragment_push_left_out)
        }

        transaction.hide(currentFragment).show(fragment)
        transaction.commit()
        currentFragment = fragment
    }
    /**
     * 设置Fragment的外层布局容器
     */
    override fun setFragmentContainerResId(): Int {
        return -1
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
        try {

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
        }catch (e:Exception){

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



    override fun showDefaultToast(msg: String) {
        globalUtilShowDefaultToast(window.decorView, msg)
    }

    override fun showErrorToast(msg: String) {
        globalUtilShowErrorToast(window.decorView, msg)
    }

    override fun showSuccessToast(msg: String) {
        globalUtilShowSuccessToast(window.decorView, msg)
    }

    override fun showWarningToast(msg: String) {
        globalUtilShowWarningToast(window.decorView, msg)
    }

    override fun showErrorDialog(msg: String) {
        globalUtilShowNormalDialog(this, getString(R.string.Error_Title_Msg), msg)
    }

    //________________________WaitDialog______________________________End


    //Start______________________________Toolbar_________________________________________
    //back和menu的自定义点击事件直接在子类实现,基类不作处理,back默认是finish()
    override fun initToolbar() {
        mToolbar = findViewById<RelativeLayout>(R.id.toolbar)
        if (mToolbar != null) {
            mTvTitle = mToolbar!!.findViewById<TextView>(R.id.tv_toolbar_title)
            mTvMenu = mToolbar!!.findViewById<TextView>(R.id.iv_toolbar_menu)
            mTvBack = mToolbar!!.findViewById<TextView>(R.id.iv_toolbar_back)
            mTvBack!!.setOnClickListener {
                finish()
            }
        }
    }
    fun hideToolbar() {
        if (mToolbar != null) {
            mToolbar!!.visibility = View.GONE
        }
    }

    fun showToolbar() {
        if (mToolbar != null) {
            mToolbar!!.visibility = View.VISIBLE
        }
    }

    fun setBackButtonText(text: String) {
        if (mTvBack != null) {
            mTvBack!!.text = text
        }
    }

    fun setBackButtonDrawable(drawable: Drawable) {
        if (mTvBack != null) {
            mTvBack!!.setCompoundDrawablesWithIntrinsicBounds(drawable,
                    null, null, null)
        }
    }

    fun setMenuButtonText(text: String) {
        if (mTvMenu !== null) {
            mTvMenu!!.text = text
        }
    }

    fun setMenuButtonDrawable(drawable: Drawable) {
        if (mTvMenu != null) {
            mTvMenu!!.setCompoundDrawablesWithIntrinsicBounds(drawable,
                    null, null, null)
        }
    }

    fun hideBackButton() {
        if (mTvBack != null) {
            mTvBack!!.visibility = View.GONE
        }
    }

    fun showMenuButton() {
        if (mTvMenu != null) {
            mTvMenu!!.visibility = View.VISIBLE
        }
    }

    fun hideMenuButton() {
        if (mTvMenu != null) {
            mTvMenu!!.visibility = View.GONE
        }
    }

    fun setToolbarTitle(msg: String) {
        if (mTvTitle != null) {
            mTvTitle!!.text = msg
        }
    }
    //______________________________Toolbar_________________________________________end

    //Start______________________________VCodeDialog_________________________________________
    private var captcha: Captcha? = null
    private var canDialog: CanDialog? = null


    private fun initCaptcha() {
        //只能通过inflate的方式把captcha放进CanDialog,直接new的话无法显示
        captcha = LayoutInflater.from(this).inflate(R.layout.view_captcha, null) as Captcha?

        captcha!!.setCaptchaListener(object : Captcha.CaptchaListener {
            override fun onFailed(failCount: Int): String {
                hideVCode()
                showWarningToast(getString(R.string.Msg_VCode_Failed))
                //verifyFailed()
                return getString(R.string.Msg_VCode_Failed)
            }

            override fun onAccess(time: Long): String {
                hideVCode()
                showSuccessToast(getString(R.string.Msg_VCode_Access))
                verifySuccess()
                return getString(R.string.Msg_VCode_Access)
            }

            override fun onMaxFailed(): String {
                return ""
            }

        })
    }

    //验证成功
    override fun verifySuccess() {
        //在子类作具体实现,不作抽象函数,用的地方少
    }

    /*  //验证失败
      override fun verifyFailed() {
          //在子类作具体实现,不作抽象函数,用的地方少
      }
  */

    /**
     * 开启滑动验证
     */
    fun showVCode() {

        //需要每次都将Captcha重新设置,否则会报子视图已有父视图的错,这是由于二次打开dialog导致的bug
        initCaptcha()

        canDialog = CanDialog.Builder(this).setView(captcha).create()
        setRandomVerifyPhoto()
        canDialog!!.show()
    }

    /**
     * 关闭滑动验证
     */
    private fun hideVCode() {
        canDialog!!.dismiss()
    }

    /**
     * 设置随机验证图片
     */
    private fun setRandomVerifyPhoto() {
        val options = RequestOptions()
        //改变缓存的key,达到每次加载都是新图片的效果
        options.signature(MediaStoreSignature("no_cache", Date().time + Random().nextLong(), 0))
        Glide.with(mContext!!)
                .asBitmap()
                .load(Constant.URL_RANDOM_PICTURE)
                .apply(options)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        if (resource != null) {
                            captcha!!.setBitmap(resource)
                        }
                    }
                })
    }
    //______________________________VCodeDialog_________________________________________End

    override fun done() {
        //这里空实现,有需要的子类再使用,有时候presenter这边需要结束activity或者完成某些操作
    }

    override fun finishRefresh() {
        //这里空实现,有需要的子类再使用,某些页面使用了下拉刷新,但是网络请求出现问题的时候也需要将其关闭
    }


    override fun initData() {
        //这里空实现,有需要的子类再使用,这个位于initView和event的后面
    }


    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        //直接注释这行可以简单解决崩溃重启后fragment重叠的问题
        //super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        _isVisible = true
    }

    override fun beforeSetContentView() {
        //由于kotlin语法已经做了非空检测,这里不需要再次检测,永远是true
        (mPresenter as BasePresenter<IView>).attachView(this)
    }

    protected override fun onDestroy() {
        InputMethodManagerLeakFixUtil.fixInputMethodManagerLeak(this)
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

    override fun onBackPressed() {
        super.onBackPressed()
        hideSoftKeyboard()
    }
    //_______________________________System override________________________End

}