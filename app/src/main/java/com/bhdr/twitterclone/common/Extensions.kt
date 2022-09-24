package com.bhdr.twitterclone.common

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.text.toSpannable
import com.google.android.material.snackbar.Snackbar
import com.microsoft.signalr.HubConnectionBuilder
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.time.Duration.Companion.seconds

lateinit var shared: SharedPreferences

var toStartSignalRTweet = false
val hubConnection =
   HubConnectionBuilder.create(Constants.HUB_CONNECTION_URL).build()!!

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


fun Context.checkNetworkConnection(): Boolean {

   val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

   val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
      (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
   } else {
      @Suppress("DEPRECATION")
      val netInfo = cm.activeNetworkInfo
      @Suppress("DEPRECATION")
      netInfo != null && netInfo.isConnectedOrConnecting
   }

   return result
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
      } else if (minutes in 1..59) {
         "${(aps.seconds.inWholeMinutes) - (this.seconds.inWholeMinutes)}dk. önce"
      } else if (minutes < 1) {
         "${(aps.seconds) - (this.seconds)}n. önce"
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

fun spannableFactory(): Spannable.Factory {
   val spannableFactory = object : Spannable.Factory() {
      override fun newSpannable(source: CharSequence?): Spannable {
         val spannable = source!!.toSpannable()
         val len1 = source.split(" ")
         val getTagIndex = source.indexOf("#")
         var lastIndex = 0
         len1.forEach {
            if (it.contains("#")) {

               lastIndex = it.length
            }

         }
         spannable.setSpan(
            ForegroundColorSpan(Color.parseColor("#03A9F4")),
            getTagIndex,
            getTagIndex + lastIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
         )

         return spannable
      }
   }
   return spannableFactory
}

fun String.userNameCount(): String {
   return if (this.length > 11) {
      this.substring(0, 9) + "..."
   } else {
      this
   }
}


