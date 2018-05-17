package com.renameyourappname.mobile.moudule.main.ui

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.FragmentManager
import android.view.View
import android.widget.ImageView
import com.renameyourappname.mobile.R
import com.renameyourappname.mobile.model.bean.request.LoginRequest
import com.renameyourappname.mobile.moudule.base.ui.BaseActivity
import com.renameyourappname.mobile.moudule.main.contract.MainContract
import com.renameyourappname.mobile.moudule.main.presenter.MainPresenter
import com.mxn.soul.flowingdrawer_core.ElasticDrawer
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment
import com.yalantis.contextmenu.lib.MenuObject
import com.yalantis.contextmenu.lib.MenuParams
import kotlinx.android.synthetic.main.activity_main.*




class MainActivity : BaseActivity<MainPresenter>(), MainContract.View {

    override fun initInjector() {
        mActivityComponent.inject(this)
    }

    override fun getBundleExtras(extras: Bundle) {

    }

    override fun getContentViewLayoutID(): Int {
        return R.layout.activity_main
    }

    override fun initViews() {
        hideBackButton()
        simpleTest()

        //toolbar
        setToolbarTitle("9527")
        showMenuButton()

        //floatingactionbar
        var icon: ImageView = ImageView(this) // Create an icon
        icon.setImageDrawable(getDrawable(R.drawable.ic_svg_telephone_orange_24))

        var actionButton: FloatingActionButton = FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build()

        val rLSubBuilder = SubActionButton.Builder(this)
        val rlIcon1 = ImageView(this)
        val rlIcon2 = ImageView(this)
        val rlIcon3 = ImageView(this)
        val rlIcon4 = ImageView(this)

        rlIcon1.setImageDrawable(getDrawable(R.drawable.ic_svg_setting_orange_24))
        rlIcon2.setImageDrawable(getDrawable(R.drawable.ic_svg_setting_orange_24))
        rlIcon3.setImageDrawable(getDrawable(R.drawable.ic_svg_setting_orange_24))
        rlIcon4.setImageDrawable(getDrawable(R.drawable.ic_svg_setting_orange_24))


        val actionMenu = FloatingActionMenu.Builder(this)
                .addSubActionView(rLSubBuilder.setContentView(rlIcon1).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon2).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon3).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon4).build())
                // ...
                .attachTo(actionButton)
                .build()


        //右侧菜单
        val close = MenuObject()
        close.resource = R.drawable.ic_svg_setting_orange_24

        val send = MenuObject("Send message")
        send.resource = R.drawable.ic_svg_telephone_orange_24

        val menuObjects = arrayListOf<MenuObject>()
        menuObjects.add(close)
        menuObjects.add(send)
        menuObjects.add(send)
        menuObjects.add(send)
        menuObjects.add(send)


        val menuParams = MenuParams()
        menuParams.actionBarSize = resources.getDimension(R.dimen.abc_action_bar_default_height_material).toInt()
        menuParams.menuObjects = menuObjects
        menuParams.isClosableOutside = true    //设置点击其他区域是否关闭menu
        // set other settings to meet your needs
        var mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams)

        mTvMenu!!.setOnClickListener {
            if (supportFragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                mMenuDialogFragment.show(supportFragmentManager, ContextMenuDialogFragment.TAG)
            }
        }

        val behavior = BottomSheetBehavior.from(bottom_sheet)
        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })


        //drawer
        drawerlayout.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL)
        /*drawerlayout.setOnDrawerStateChangeListener(object : ElasticDrawer.OnDrawerStateChangeListener {
            override fun onDrawerStateChange(oldState: Int, newState: Int) {
                if (newState == ElasticDrawer.STATE_CLOSED) {
                    Log.i("MainActivity", "Drawer STATE_CLOSED")
                    showMessage("Drawer STATE_CLOSED")
                } else if (newState == ElasticDrawer.STATE_OPEN) {
                    showMessage("Drawer STATE_OPEN")
                }
            }

            override fun onDrawerSlide(openRatio: Float, offsetPixels: Int) {
                Log.i("MainActivity", "openRatio=$openRatio ,offsetPixels=$offsetPixels")
                showMessage("openRatio=$openRatio ,offsetPixels=$offsetPixels")
            }
        })*/

        //menu  添加侧拉菜单布局
        val fm: FragmentManager = supportFragmentManager
        fm.beginTransaction().add(R.id.id_container_menu, MenuListFragment()).commit()
    }

    private fun simpleTest() {
        //测试fragment
        /*val beginTransaction = getSupportFragmentManager().beginTransaction()
        val mainFragment = MainFragment()
        beginTransaction.add(R.id.fl_container, mainFragment, "1")
        beginTransaction.show(mainFragment)
        beginTransaction.commitAllowingStateLoss()

        if (mainFragment.activity != null) {
            //Log.e("titi", mPresenter.presenteTest())
        }*/
        val loginRequest: LoginRequest = LoginRequest("", "aaa666", "newword")
        mPresenter.login(loginRequest)
        // CanDialog.Builder(this).setProgressCustomView(LayoutInflater.from(this).inflate(R.layout.dialog_wait, null)).show()

        //showLoading("")  //dialog显示的时候侧拉菜单是不能出来的
    }

    override fun initEvents() {

    }

    override fun showLogin(str: String?) {
        //showLoading("")
        //showError("?????")
        tv_abc.text = str
        //WDevice.openCamera(this)
        //CanDialog.Builder(this).setView(LayoutInflater.from(this).inflate(R.layout.dialog_wait, null)).show()
        //showWaitDialog()

//        ActivityCompat.requestPermissions(this, arrayOf("Manifest.permission.CAMERA"),
//                100)
    }

    //后退键事件
    override fun onBackPressed() {
        if (drawerlayout.isMenuVisible()) {
            drawerlayout.closeMenu()
        } else {
            super.onBackPressed()
        }
    }


}
