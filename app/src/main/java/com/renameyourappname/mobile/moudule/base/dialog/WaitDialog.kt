package com.renameyourappname.mobile.moudule.base.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import com.renameyourappname.mobile.R

/**
 * Created by Kobe on 2017/12/25.
 */
class WaitDialog:Dialog {
    private var ctx: Context? = null
    constructor(context: Context) : super(context){
        init(context)
    }

    constructor(context: Context, defStyle: Int):super(context,defStyle){
        init(context)
    }

    constructor(context: Context, cancelable: Boolean, listener: DialogInterface.OnCancelListener):super(context,cancelable,listener){
        init(context)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.dismiss()
    }


    private fun init(context: Context) {
        ctx = context

        setCancelable(false)
        //requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_wait, null)
        setContentView(view)
    }
}