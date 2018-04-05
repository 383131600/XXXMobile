package com.renameyourappname.mobile.injector.component

import android.app.Activity
import android.content.Context
import com.renameyourappname.mobile.injector.annotation.ContextLife
import com.renameyourappname.mobile.injector.annotation.PerFragment
import com.renameyourappname.mobile.injector.module.FragmentModule
import com.renameyourappname.mobile.moudule.main.ui.MainFragment
import dagger.Component

/**
 * Created by Kobe on 2017/12/25.
 */
@PerFragment
@Component(modules = [FragmentModule::class], dependencies = [AppComponent::class])
interface FragmentComponent {
    @ContextLife("Application")
    fun getContext(): Context?

    @ContextLife("Activity")
    fun getActivityContext(): Context?

    fun getActivity(): Activity?
    fun inject(mainFragment: MainFragment)
}