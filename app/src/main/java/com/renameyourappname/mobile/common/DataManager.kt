package com.renameyourappname.mobile.common

import com.renameyourappname.mobile.api.ApiService
import com.renameyourappname.mobile.utils.PrefUtils
import javax.inject.Inject

/**
 * Created by Kobe on 2017/12/22.
 */
class DataManager @Inject constructor(private val mPrefUtils: PrefUtils, private val mApiService: ApiService){

    /**
     * 通过model层获取db或者ps或其他数据
     */
    fun <T> create(model: Class<T>): T {
        return model.getDeclaredConstructor(PrefUtils::class.java, ApiService::class.java).newInstance(mPrefUtils,mApiService)
    }

    /**
     * 直接网络请求
     */
    fun request():ApiService{
        return mApiService
    }
}