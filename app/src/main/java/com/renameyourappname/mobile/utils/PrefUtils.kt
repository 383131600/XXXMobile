package com.renameyourappname.mobile.utils

import android.content.Context
import com.renameyourappname.mobile.common.Constant
import com.renameyourappname.mobile.helper.PreferenceHelper

/**
 * Created by Kobe on 2017/12/22.
 */
class PrefUtils(private val mContext:Context) {

    operator fun set(key: String, value: Int) {
        PreferenceHelper.write(mContext, Constant.PREF_NAME, key, value)
    }

    operator fun set(key: String, value: Boolean) {
        PreferenceHelper.write(mContext, Constant.PREF_NAME, key, value)
    }

    operator fun set(key: String, value: String) {
        PreferenceHelper.write(mContext, Constant.PREF_NAME, key, value)
    }

    fun getInt(key: String): Int {
        return PreferenceHelper.readInt(mContext, Constant.PREF_NAME, key, 0)
    }

    fun getBoolean(key: String): Boolean {
        return PreferenceHelper.readBoolean(mContext, Constant.PREF_NAME, key, false)
    }

    fun getString(key: String): String? {
        return PreferenceHelper.readString(mContext, Constant.PREF_NAME, key, "")
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return PreferenceHelper.readInt(mContext, Constant.PREF_NAME, key, defaultValue)
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return PreferenceHelper.readBoolean(mContext, Constant.PREF_NAME, key, defaultValue)
    }

    fun getString(key: String, defaultValue: String): String? {
        return PreferenceHelper.readString(mContext, Constant.PREF_NAME, key, defaultValue)
    }
}