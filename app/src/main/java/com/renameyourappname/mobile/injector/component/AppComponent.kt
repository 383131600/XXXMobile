package com.renameyourappname.mobile.injector.component

import android.content.Context
import android.content.res.Resources
import com.renameyourappname.mobile.injector.annotation.ContextLife
import com.renameyourappname.mobile.injector.module.AppModule
import com.renameyourappname.mobile.injector.module.ConstantModule
import com.renameyourappname.mobile.injector.module.RetrofitModule
import com.renameyourappname.mobile.common.DataManager
import com.renameyourappname.mobile.utils.PrefUtils
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Kobe on 2017/12/22.
 */
@Singleton
@Component(modules = [AppModule::class,ConstantModule::class, RetrofitModule::class])
interface AppComponent {
    @ContextLife("Application")
    fun getContext(): Context

    fun getPrefUtils(): PrefUtils

    //不直接在AppModule中实例化DataManager,只需要DataManager中的构造参数注入所需参数即可在AppModule动态生成文件中生成这个方法
    //不是单例
    fun getDataManager(): DataManager

    fun getResources(): Resources
}