package com.bhdr.twitterclone.helperclasses

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import com.microsoft.signalr.HubConnectionBuilder
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.time.Duration.Companion.seconds

lateinit var shared: SharedPreferences
val hubConnection = HubConnectionBuilder.create("http://192.168.3.151:9009/newTweetHub").build()!!
fun Context.userId(): Int {
   shared = getSharedPreferences("com.bhdr.twitterclone", MODE_PRIVATE)
   return shared.getInt("user_Id", 0)
}


fun Context.userPhotoUrl(): String {
   shared = getSharedPreferences("com.bhdr.twitterclone", MODE_PRIVATE)
   return shared.getString("user_photoUrl", "").toString()
}

fun Context.saveSharedItem(key: String, status: String) {
   shared = getSharedPreferences("com.bhdr.twitterclone", MODE_PRIVATE)
   shared.edit().putString(key, status).apply()
}

fun Context.sharedPref(): SharedPreferences {
   shared = getSharedPreferences("com.bhdr.twitterclone", MODE_PRIVATE)
   return shared
}

fun View.gone() {
   visibility = View.GONE
}

fun View.visible() {
   visibility = View.VISIBLE
}

fun snackBar(view: View, text: String, duration: Int) {
   Snackbar.make(view, text, duration).show()
}

fun ImageView.picasso(url: String) {
   Picasso.get().load(url).into(this)
}

@RequiresApi(Build.VERSION_CODES.M)
fun Context.checkNetworkConnection(): Boolean {
   val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
   val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
   return (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
}


fun Long.toCalendar(): String {
   try {
      val aps: Long = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
         Calendar.getInstance().time.toInstant().epochSecond
      } else {
         Calendar.getInstance().time.time
      }
      val minutes = ((aps.seconds.inWholeMinutes)) - ((this.seconds.inWholeMinutes))
      return if (minutes in 60..1440) {
         "${(aps.seconds.inWholeHours) - (this.seconds.inWholeHours)}s. önce"
      } else if (minutes in 2..59) {
         "${(aps.seconds.inWholeMinutes) - (this.seconds.inWholeMinutes)}dk. önce"
      } else if (minutes < 1) {
         "${(aps.seconds) - (this.seconds)}sn. önce"
      } else {
         "${(aps.seconds.inWholeDays) - (this.seconds.inWholeDays)}g. önce"
      }
   } catch (e: Exception) {
      Log.e("exce", e.toString())
      throw IllegalStateException("tweetsRoomConvertAndAdd")
   }

}

fun toLongDate(): Long {
   return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      Calendar.getInstance().time.toInstant().epochSecond

   } else {
      Calendar.getInstance().time.time
   }
}

