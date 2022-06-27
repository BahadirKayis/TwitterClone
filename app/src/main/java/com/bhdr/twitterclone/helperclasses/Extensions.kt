package com.bhdr.twitterclone.helperclasses

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import com.bhdr.twitterclone.R
import com.google.android.material.snackbar.Snackbar


fun loadingDialogStart(activity: Activity): AlertDialog {

    val loadingScreen = LayoutInflater.from(activity).inflate(R.layout.loading_screen, null)
    val builder = AlertDialog.Builder(activity)
    builder.setCancelable(false)
    builder.setView(loadingScreen)
    val dialog = builder.create()

    return dialog
}

fun snackBar(view: View, text: String, duration: Int) {
    Snackbar.make(view, text.toString(), duration).show()
}