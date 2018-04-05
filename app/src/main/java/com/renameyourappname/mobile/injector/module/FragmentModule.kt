package com.renameyourappname.mobile.injector.module

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import com.renameyourappname.mobile.injector.annotation.ContextLife
import com.renameyourappname.mobile.injector.annotation.PerFragment
import dagger.Module
import dagger.Provides
import javax.inject.Inject

/**
 * Created by Kobe on 2017/12/25.
 */
@Module
class FragmentModule @Inject constructor(private val mFragment: Fragment) {

    @Provides
    @PerFragment
    @ContextLife("Activity")
    fun provideContext(): Context? {
        return mFragment.activity
    }

    @Provides
    @PerFragment
    fun provideActivity(): Activity {
        return mFragment.activity!!
    }

    @Provides
    @PerFragment
    fun provideFragment(): Fragment {
        return mFragment
    }
}