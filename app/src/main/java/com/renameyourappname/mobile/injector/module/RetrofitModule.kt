package com.renameyourappname.mobile.injector.module

import com.renameyourappname.mobile.api.UriConstant
import com.renameyourappname.mobile.application.XApplication
import com.renameyourappname.mobile.common.Constant
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by Kobe on 2017/12/25.
 */
@Module
class RetrofitModule {

    @Provides
    @Singleton
    fun provideRetrofit()= Retrofit.Builder()
            .baseUrl(UriConstant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(genericClient())
            .build()!!

    /**
     * 自定义ok的实例,拦截返回时的请求头保存登录Token
     */
    private fun genericClient():OkHttpClient{
        return OkHttpClient.Builder().addInterceptor {
            var response:Response=it.proceed(it.request())
            if (!response.header("new_token").isNullOrBlank()){
                XApplication.getAppComponent().getPrefUtils()[Constant.TOKEN] = response.header("new_token")!!
            }
             response
        }.build()
    }
}