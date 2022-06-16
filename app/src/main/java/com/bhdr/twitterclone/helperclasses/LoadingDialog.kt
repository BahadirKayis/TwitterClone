package com.bhdr.twitterclone.helperclasses

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import com.bhdr.twitterclone.R

class LoadingDialog() {

lateinit var  dialog:AlertDialog
    fun loadingDialogStart( activity:Activity){
        var loadingScreen=LayoutInflater.from(activity).inflate(R.layout.loading_screen,null)
      val    builder=AlertDialog.Builder(activity)
        builder.setCancelable(false)
       builder.setView(loadingScreen)

       dialog=builder.create()
        dialog.show()

    }
    fun loadingDialogClose(){
      dialog.dismiss()
    }
}