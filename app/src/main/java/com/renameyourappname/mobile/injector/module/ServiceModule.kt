package com.renameyourappname.mobile.injector.module

import android.app.Service
import android.content.Context
import com.renameyourappname.mobile.injector.annotation.ContextLife
import com.renameyourappname.mobile.injector.annotation.PerService
import dagger.Module
import dagger.Provides
import javax.inject.Inject

/**
 * Created by Kobe on 2017/12/25.
 */
@Module
class ServiceModule @Inject constructor(private val mService:Service) {

    @Provides
    @PerService
    @ContextLife("Service")
    fun provideContext(): Context {
        return mService
    }
}