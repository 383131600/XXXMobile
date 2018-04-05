package com.renameyourappname.mobile.moudule.base.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.widget.LinearLayout
import android.widget.TextView
import com.renameyourappname.mobile.utils.StringUtils
import com.renameyourappname.mobile.utils.WDevice
import com.renameyourappname.mobile.utils.utilShowToast

/**
 * Created by Kobe on 2017/12/25.
 */
abstract class BaseAppCompatActivity: AppCompatActivity() {
    /**
     * Log tag
     */
    protected var TAG_LOG: String? = null


    /**
     * Screen information
     */
    protected var mScreenWidth = 0
    protected var mScreenHeight = 0
    protected var mScreenDensity = 0.0f

    /**
     * context
     */
    protected var mContext: Context? = null



    private val mErrorView: LinearLayout? = null
    private val mErrorText: TextView? = null


    /**
     * overridePendingTransition mode
     */
    enum class TransitionMode {
        LEFT, RIGHT, TOP, BOTTOM, SCALE, FADE
    }
    //所有继承的子类如果复写onCreate的话不能SetcontentView,这样会覆盖基类的所有操作
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        beforeSetContentView()

        // base setup
        val extras = intent.extras
        if (null != extras) {
            getBundleExtras(extras)
        }

        mContext = this
        TAG_LOG = this.javaClass.simpleName

        BaseAppManager.addActivity(this)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        mScreenDensity = displayMetrics.density
        mScreenHeight = displayMetrics.heightPixels
        mScreenWidth = displayMetrics.widthPixels


        if (getContentViewLayoutID() != 0) {


            setContentView(getContentViewLayoutID())
        } else {
            throw IllegalArgumentException("You must return a right contentView layout resource Id")
        }
        initToolbar()
        initViews()
        initEvents()
    }

    abstract fun initToolbar()



    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)

    }

    override fun finish() {
        super.finish()
        BaseAppManager.removeActivity(this)
        //        if (toggleOverridePendingTransition()) {
        //            switch (getOverridePendingTransitionMode()) {
        //                case LEFT:
        //                    overridePendingTransition(R.anim.left_in,R.anim.left_out);
        //                    break;
        //                case RIGHT:
        //                    overridePendingTransition(R.anim.right_in,R.anim.right_out);
        //                    break;
        //                case TOP:
        //                    overridePendingTransition(R.anim.top_in,R.anim.top_out);
        //                    break;
        //                case BOTTOM:
        //                    overridePendingTransition(R.anim.bottom_in,R.anim.bottom_out);
        //                    break;
        //                case SCALE:
        //                    overridePendingTransition(R.anim.scale_in,R.anim.scale_out);
        //                    break;
        //                case FADE:
        //                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        //                    break;
        //            }
        //        }
    }

    override fun onDestroy() {
        super.onDestroy()

        WDevice.hideSoftKeyboard(this, currentFocus)
    }

    protected abstract fun beforeSetContentView()

    /**
     * get bundle data
     *
     * @param extras
     */
    protected abstract fun getBundleExtras(extras: Bundle)

    /**
     * bind layout resource file
     *
     * @return id of layout resource
     */
    protected abstract fun getContentViewLayoutID(): Int


    /**
     * init all views and add events
     */
    protected abstract fun initViews()
    protected abstract fun initEvents()

    /**
     * startActivity
     *
     * @param clazz
     */
    protected fun readyGo(clazz: Class<*>) {
        val intent = Intent(this, clazz)
        startActivity(intent)
    }

    /**
     * startActivity with bundle
     *
     * @param clazz
     * @param bundle
     */
    protected fun readyGo(clazz: Class<*>, bundle: Bundle?) {
        val intent = Intent(this, clazz)
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
        val intent = Intent(this, clazz).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    /**
     * startActivity with bundle then finish
     *
     * @param clazz
     * @param bundle
     */
    protected fun readyGoThenKill(clazz: Class<*>, bundle: Bundle?) {
        val intent = Intent(this, clazz).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        if (null != bundle) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
        finish()
    }

    /**
     * startActivityForResult
     *
     * @param clazz
     * @param requestCode
     */
    protected fun readyGoForResult(clazz: Class<*>, requestCode: Int) {
        val intent = Intent(this, clazz)
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
        val intent = Intent(this, clazz)
        if (null != bundle) {
            intent.putExtras(bundle)
        }
        startActivityForResult(intent, requestCode)
    }

    /**
     * show toast
     *
     * @param msg
     */
    protected fun showToast(msg: String) {
        if (!StringUtils.isEmpty(msg)) {
            utilShowToast(window.decorView,msg)
        }
    }
}