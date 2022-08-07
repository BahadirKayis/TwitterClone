package com.bhdr.twitterclone.helperclasses

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

lateinit var shared: SharedPreferences

fun Context.userId(): Int {
   shared = getSharedPreferences("com.bhdr.twitterclone", MODE_PRIVATE)
   return shared.getInt("user_Id", 0)
}


fun Context.userPhotoUrl(): String {
   shared = getSharedPreferences("com.bhdr.twitterclone", Context.MODE_PRIVATE)
   return shared.getString("user_photoUrl", "").toString()
}

fun Context.saveSharedItem(key: String, status: Boolean) {
   shared = getSharedPreferences("com.bhdr.twitterclone", Context.MODE_PRIVATE)
   shared.edit().putBoolean(key, status).apply()
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

fun Any?.toInt(): Int {
   val d = 5.25
   return d.toInt()
}

@RequiresApi(Build.VERSION_CODES.M)
fun Context.checkNetworkConnection(): Boolean {
   val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
   val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
   return (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))

}


