package com.bhdr.twitterclone.helperclasses

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import com.bhdr.twitterclone.R
import com.google.android.material.snackbar.Snackbar


fun loadingDialogStart(activity: Activity): Dialog {

    val loadingScreen = LayoutInflater.from(activity).inflate(R.layout.loading_screen, null)
    val builder = AlertDialog.Builder(activity)
    builder.setView(loadingScreen)
    builder.setCancelable(false)
    val dialog = builder.create()
    dialog.show()
    return dialog
}

fun View.gone() {
    visibility=View.GONE
}

fun View.visible() {
    visibility=View.VISIBLE
}

fun snackBar(view: View, text: String, duration: Int) {
    Snackbar.make(view, text.toString(), duration).show()
}