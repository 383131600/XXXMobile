package com.renameyourappname.mobile.model.bean.request

data class LoginRequest(
        val access_token:String,
        val username:String,
        val password:String
)