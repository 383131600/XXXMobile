package com.crown.mobile.common.rxbus

class EventMessage() {
    var code: Int = -1
    var message: Any? = null
    constructor(code:Int,message: Any?):this(){
        this.code=code
        this.message=message
    }
    constructor(code:Int):this(code,"")
}