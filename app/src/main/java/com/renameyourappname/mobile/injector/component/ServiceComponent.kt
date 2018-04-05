package com.renameyourappname.mobile.injector.component

import android.content.Context
import com.renameyourappname.mobile.injector.annotation.ContextLife
import com.renameyourappname.mobile.injector.annotation.PerService
import com.renameyourappname.mobile.injector.module.ServiceModule
import com.renameyourappname.mobile.common.DataManager
import dagger.Component

/**
 * Created by Kobe on 2017/12/25.
 */
@PerService
@Component(dependencies = [AppComponent::class], modules = [ServiceModule::class])
interface ServiceComponent {
    @ContextLife("Service")
    fun getServiceContext(): Context

    @ContextLife("Application")
    fun getApplicationContext(): Context

    fun getDataManager(): DataManager
}