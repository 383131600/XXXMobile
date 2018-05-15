package com.renameyourappname.mobile.application

import android.app.Application
import com.orhanobut.logger.DiskLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
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
        initLogger()
    }

    private fun initLogger() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .tag("Bong_Crown")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build()

        Logger.addLogAdapter(DiskLogAdapter(formatStrategy))//这个adapter会把日志存入本地
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