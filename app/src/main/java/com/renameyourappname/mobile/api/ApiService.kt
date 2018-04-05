package com.renameyourappname.mobile.api

import com.renameyourappname.mobile.model.bean.response.LoginResponse
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


/**
 * Created by Kobe on 2017/12/21.
 * Api接口
 */
interface ApiService {
//    /**
//     * 首页精选
//     */
//    @GET("v2/feed?")
//    fun getFirstHomeData(@Query("num") num:Int): Observable<HomeBean>
//
//    /**
//     * 热门搜索词
//     */
//    @GET("v3/queries/hot")
//    fun getHotWord():Observable<ArrayList<String>>

    @FormUrlEncoded
    @POST("pub/login")
    fun login(@Field("access_token")access_token:String,@Field("username")username:String,@Field("password")password:String): Observable<LoginResponse>
}