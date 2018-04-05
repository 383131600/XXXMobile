package com.renameyourappname.mobile.model

import com.renameyourappname.mobile.api.ApiService
import com.renameyourappname.mobile.utils.PrefUtils
import javax.inject.Inject

/**
 * Created by Kobe on 2017/12/22.
 */
open class BaseModel @Inject constructor(val mPrefUtils: PrefUtils, val mApiService: ApiService) {

}