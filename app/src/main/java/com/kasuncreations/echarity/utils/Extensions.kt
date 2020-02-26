package com.kasuncreations.echarity.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * Show Long Toast message from string resource
 * */
fun Context?.showToastLong(@StringRes textId: Int, duration: Int = Toast.LENGTH_LONG) {
    this?.let {
        Toast.makeText(it, textId, duration).show()
    }

}

/**
 * Show Long Toast message from only String
 * */
fun Context?.showToastLong(text: String, duration: Int = Toast.LENGTH_LONG) {
    this?.let {
        Toast.makeText(it, text, duration).show()
    }
}
