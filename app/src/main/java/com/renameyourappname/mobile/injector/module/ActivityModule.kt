package com.renameyourappname.mobile.injector.module

import android.app.Activity
import android.content.Context
import com.renameyourappname.mobile.injector.annotation.ContextLife
import com.renameyourappname.mobile.injector.annotation.PerActivity
import dagger.Module
import dagger.Provides
import javax.inject.Inject

/**
 * Created by Kobe on 2017/12/25.
 */
@Module
class ActivityModule @Inject constructor(private val mActivity:Activity) {

    @Provides
    @PerActivity
    @ContextLife("Activity")
    fun provideContext(): Context {
        return mActivity
    }

    @Provides
    @PerActivity
    fun provideActivity(): Activity {
        return mActivity
    }
}