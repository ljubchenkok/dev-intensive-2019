package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.inputmethod.InputMethodManager
import android.util.DisplayMetrics




fun Activity.hideKeyboard(){
    val view = this.currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if(imm !=null) {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}

fun Activity.isKeyboardOpen():Boolean{
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    val screenHeight:Int = displayMetrics.heightPixels
    val rect = Rect()
    window.decorView.getWindowVisibleDisplayFrame(rect)
    val frameHeight:Int = rect.top
    return  screenHeight - frameHeight > 100
}

fun Activity.isKeyboardClosed():Boolean{
    return !isKeyboardOpen()
}

