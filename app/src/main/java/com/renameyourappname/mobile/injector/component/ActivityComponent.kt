package com.renameyourappname.mobile.injector.component

import android.app.Activity
import android.content.Context
import com.renameyourappname.mobile.injector.annotation.ContextLife
import com.renameyourappname.mobile.injector.annotation.PerActivity
import com.renameyourappname.mobile.injector.module.ActivityModule
import com.renameyourappname.mobile.moudule.main.ui.MainActivity
import com.renameyourappname.mobile.moudule.webview.XWebViewActivity
import dagger.Component

/**
 * Created by Kobe on 2017/12/25.
 */

//dependencies的作用是依赖某个Component,这里依赖的是AppComponent,类似继承的效果
@PerActivity
@Component(dependencies = [AppComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {
    @ContextLife("Activity")
    fun getActivityContext(): Context

    @ContextLife("Application")
    fun getApplicationContext(): Context

    fun getActivity(): Activity

    fun inject(mainActivity: MainActivity)
    fun inject(mainActivity: XWebViewActivity)

}