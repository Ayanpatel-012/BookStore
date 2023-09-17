package com.dailyrounds.bookstore.Utils

import android.content.Context
import android.widget.TextView
import com.dailyrounds.bookstore.R

fun TextView.setActive(context: Context) {
    setBackgroundResource(R.drawable.btn_bg_black)
    setTextColor(context.getColor(R.color.white))
}

fun TextView.setInactive(context: Context) {
    setBackgroundResource(R.drawable.btn_bg_white)
    setTextColor(context.getColor(R.color.black))
}