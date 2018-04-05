package com.renameyourappname.mobile.model

import com.renameyourappname.mobile.api.ApiService
import com.renameyourappname.mobile.common.Constant
import com.renameyourappname.mobile.utils.PrefUtils
import javax.inject.Inject

class LoginModel @Inject constructor(mPrefUtils: PrefUtils, mApiService: ApiService) : BaseModel(mPrefUtils, mApiService) {
    /*fun login(request: LoginRequest){
        mApiService
                .login(request.access_token, request.username, request.password)
                .applySchedulers()
                .subscribe {

                }
    }*/
    fun getToken(): String? {
        return mPrefUtils.getString(Constant.TOKEN)
    }

    fun setToken(token: String) {
        mPrefUtils[Constant.TOKEN] = token
    }
}


