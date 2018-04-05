package com.renameyourappname.mobile.injector.module

import android.content.Context
import android.content.res.Resources
import com.renameyourappname.mobile.api.ApiService
import com.renameyourappname.mobile.application.XApplication
import com.renameyourappname.mobile.injector.annotation.ContextLife
import com.renameyourappname.mobile.utils.PrefUtils
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by Kobe on 2017/12/22.
 */
@Module
class AppModule(private val mApplication: XApplication) {


    @Provides
    @Singleton
    @ContextLife("Application")
    fun provideContext(): Context {
        return mApplication.applicationContext
    }

    @Provides
    @Singleton
    fun providePrefUtils(): PrefUtils {
        return PrefUtils(mApplication)
    }

    @Provides
    @Singleton
    fun provideDwgjApplication() = mApplication

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) = retrofit.create(ApiService::class.java)!!

    @Provides
    @Singleton
    fun provideResources(): Resources {
        return mApplication.resources
    }

}