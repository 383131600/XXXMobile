package com.renameyourappname.mobile.moudule.base.ui

import android.app.Activity
import java.util.*

/**
 * Created by Kobe on 2017/12/25.
 */
object BaseAppManager {
    private val TAG = BaseAppManager::class.java.simpleName

    private var instance: BaseAppManager? = null
    private val mActivities = Stack<Activity>()

    fun size(): Int {
        return mActivities.size
    }


    @Synchronized
    fun getForwardActivity(): Activity? {
        return if (size() > 0) mActivities[size() - 1] else null
    }

    @Synchronized
    fun getForward2Activity(): Activity? {
        return if (size() > 1) mActivities[size() - 2] else null
    }

    @Synchronized
    fun addActivity(activity: Activity) {
        mActivities.push(activity)
    }

    @Synchronized
    fun removeActivity(activity: Activity) {
        if (mActivities.contains(activity)) {
            mActivities.remove(activity)
        }
    }

    @Synchronized
    fun clear() {
        var index = size() - 1
        while (index >= 0) {
            val activity = mActivities.pop()
            removeActivity(activity)
            activity.finish()
            index--
        }
    }

    @Synchronized
    fun clearToTop() {
        var index = size() - 1
        while (index > 0) {
            val activity = mActivities.pop()
            removeActivity(activity)
            activity.finish()
            index--
        }
    }

}