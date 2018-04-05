package com.renameyourappname.mobile.utils

import android.content.Context
import org.json.JSONArray
import java.util.*

/**
 * Created by Kobe on 2017/12/26.
 */
object PreferenceHelper {
    fun write(context: Context, fileName: String, k: String, v: Int) {
        val preference = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE)
        val editor = preference.edit()
        editor.putInt(k, v)
        editor.commit()
    }

    fun write(context: Context, fileName: String, k: String,
              v: Boolean) {
        val preference = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean(k, v)
        editor.commit()
    }

    fun write(context: Context, fileName: String, k: String,
              v: String) {
        val preference = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE)
        val editor = preference.edit()
        editor.putString(k, v)
        editor.commit()
    }


    fun writeListInterger(context: Context, fileName: String, k: String,
                          v: List<Int>) {

        val preference = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE)
        val jsonArray = JSONArray()
        for (integer in v) {
            jsonArray.put(integer)
        }
        val editor = preference.edit()
        editor.putString(k, jsonArray.toString())
        editor.commit()
    }


    fun readInt(context: Context, fileName: String, k: String): Int {
        val preference = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE)
        return preference.getInt(k, 0)
    }

    fun readInt(context: Context, fileName: String, k: String,
                defv: Int): Int {
        val preference = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE)
        return preference.getInt(k, defv)
    }

    fun readBoolean(context: Context, fileName: String, k: String): Boolean {
        val preference = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE)
        return preference.getBoolean(k, false)
    }

    fun readBoolean(context: Context, fileName: String,
                    k: String, defBool: Boolean): Boolean {
        val preference = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE)
        return preference.getBoolean(k, defBool)
    }

    fun readString(context: Context, fileName: String, k: String): String? {
        val preference = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE)
        return preference.getString(k, null)
    }

    fun readString(context: Context, fileName: String, k: String,
                   defV: String): String? {
        val preference = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE)
        return preference.getString(k, defV)
    }


    fun readIntegerList(context: Context, fileName: String, k: String,
                        defV: List<Int>): List<Int> {
        val preference = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE)
        val integerList = ArrayList<Int>()
        try {
            val jsonArray = JSONArray(preference.getString(k, "[]"))
            for (i in 0 until jsonArray.length()) {
                integerList.add(jsonArray.getInt(i))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return if (integerList.size == 0) {
            defV
        } else integerList
    }


    fun remove(context: Context, fileName: String, k: String) {
        val preference = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE)
        val editor = preference.edit()
        editor.remove(k)
        editor.commit()
    }

    fun clean(cxt: Context, fileName: String) {
        val preference = cxt.getSharedPreferences(fileName,
                Context.MODE_PRIVATE)
        val editor = preference.edit()
        editor.clear()
        editor.commit()
    }
}