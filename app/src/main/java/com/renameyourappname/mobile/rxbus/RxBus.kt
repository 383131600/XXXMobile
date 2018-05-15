package com.renameyourappname.mobile.rxbus

import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable


/**
 * 有异常处理的 Rxbus
 * 使用完一定要dispose()释放资源
 * @author Donkor
 */

class RxBus {
    private val mBus: Relay<Any>

    init {
        this.mBus = PublishRelay.create<Any>().toSerialized()
    }

    fun post(obj: Any) {
        mBus.accept(obj)
    }

    fun <T> toObservable(tClass: Class<T>): Observable<T> {
        return mBus.ofType(tClass)
    }

    fun toObservable(): Observable<Any> {
        return mBus
    }

    fun hasObservers(): Boolean {
        return mBus.hasObservers()
    }

    private object Holder {
        val BUS = RxBus()
    }

    companion object {
        @Volatile
        private var instance: RxBus? = null
        fun getRxInstance(): RxBus {
            if (instance == null) {
                synchronized(RxBus::class.java) {
                    if (instance == null) {
                        instance = Holder.BUS
                    }
                }
            }
            return instance!!
        }
    }
}