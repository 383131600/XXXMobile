package com.renameyourappname.mobile.application

import android.app.Application
import com.renameyourappname.mobile.injector.component.AppComponent
import com.renameyourappname.mobile.injector.component.DaggerAppComponent
import com.renameyourappname.mobile.injector.module.AppModule

/**
 * Created by Kobe on 2017/12/21.
 */
class XApplication:Application() {


    override fun onCreate() {
        super.onCreate()
        initComponent()
    }

    private fun initComponent() {
        sAppComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()

    }

    companion object {
        lateinit var sAppComponent: AppComponent

        fun getAppComponent(): AppComponent {
            return sAppComponent
        }
    }


}