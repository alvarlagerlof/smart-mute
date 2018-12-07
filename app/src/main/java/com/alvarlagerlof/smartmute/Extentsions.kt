package com.alvarlagerlof.smartmute

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Created by alvar on 2017-08-18.
 */
// Shared preferences
inline fun SharedPreferences.edit(func: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    editor.func()
    editor.commit()
}



// Menu
fun Menu.show(menuId: Int) {
    val item = this.findItem(menuId)
    if (item != null) {
        item.isVisible = true
    }
}

fun Menu.hide(menuId: Int) {
    val item = this.findItem(menuId)
    if (item != null) {
        item.isVisible = false

    }
}


// Keyboard
fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard() {
    this.requestFocus()
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}


// Connectivity
fun Context.isConnected(): Boolean {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnected
}


// Int
fun Int.constrain(min: Int, max: Int) = if (this <= min) min else if (this >= max) max else this

fun Int.even() = this % 2 == 0
fun Int.odd() = this % 2 != 0

fun Float.constrain(min: Float, max: Float) = if (this <= min) min else if (this >= max) max else this
