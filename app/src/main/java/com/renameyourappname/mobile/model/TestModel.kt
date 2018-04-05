package com.renameyourappname.mobile.model

import android.util.Log
import com.renameyourappname.mobile.api.ApiService
import com.renameyourappname.mobile.utils.PrefUtils
import javax.inject.Inject

/**
 * Created by Kobe on 2017/12/22.
 */
class TestModel @Inject constructor(mPrefUtils: PrefUtils, mApiService: ApiService) : BaseModel(mPrefUtils, mApiService) {
    fun test(){
        Log.e("vivi","111111111111111111111111111111111")
    }

}