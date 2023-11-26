package com.work.ticketbook.utility

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.widget.Toast

/**
 * @author: Harpreet Kaur
 *
 * class to show messages to user
 */
object Messages {

    /**
      * To show toast messages to users
     */
    fun toast(context: Context, message: String) {
        if (!TextUtils.isEmpty(message)) {
            val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
            toast.setGravity(Gravity . CENTER, 0, 0);
            toast.show()
        }
    }

    fun log(message: String) {
        Log.d("Ticket Book App", message)
    }

}