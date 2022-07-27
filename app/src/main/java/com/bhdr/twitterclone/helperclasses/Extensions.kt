package com.bhdr.twitterclone.helperclasses

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.bhdr.twitterclone.R
import com.bhdr.twitterclone.models.Users
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

lateinit var shared: SharedPreferences

fun Context.userId(): Int {
   shared = getSharedPreferences("com.bhdr.twitterclone", Context.MODE_PRIVATE)
   return shared.getInt("user_Id", 0)
}

fun Context.userPhotoUrl(): String {
   shared = getSharedPreferences("com.bhdr.twitterclone", Context.MODE_PRIVATE)
   return shared.getString("user_photoUrl", "").toString()
}

fun Context.sharedPref(): SharedPreferences {
   shared = getSharedPreferences("com.bhdr.twitterclone", Context.MODE_PRIVATE)
   return shared
}

fun View.gone() {
   visibility = View.GONE
}

fun View.visible() {
   visibility = View.VISIBLE
}

fun snackBar(view: View, text: String, duration: Int) {
   Snackbar.make(view, text.toString(), duration).show()
}

fun ImageView.picasso(url: String) {
   Picasso.get().load(url).into(this)
}

