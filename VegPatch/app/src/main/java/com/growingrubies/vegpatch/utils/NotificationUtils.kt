package com.growingrubies.vegpatch.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.growingrubies.vegpatch.BuildConfig
import timber.log.Timber
import com.growingrubies.vegpatch.R
import com.growingrubies.vegpatch.data.Weather
import com.growingrubies.vegpatch.weatherdetail.WeatherDetailActivity

private const val NOTIFICATION_CHANNEL_ID = BuildConfig.APPLICATION_ID + ".channel"

fun NotificationManager.sendNotification(context: Context, weather: Weather) {
    Timber.i("Notification sent")

    val notificationManager = context
        .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    // We need to create a NotificationChannel associated with our CHANNEL_ID before sending a notification.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
        && notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null
    ) {
        val name = context.getString(R.string.app_name)
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            name,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    val intent = WeatherDetailActivity.newIntent(context.applicationContext, weather)

    //create a pending intent that opens ReminderDescriptionActivity when the user clicks on the notification
    val stackBuilder = TaskStackBuilder.create(context)
        .addParentStack(WeatherDetailActivity::class.java)
        .addNextIntent(intent)
    val notificationPendingIntent = stackBuilder
        .getPendingIntent(getUniqueId(), PendingIntent.FLAG_UPDATE_CURRENT)

//    build the notification object with the data to be shown
    val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
            //TODO: Set up title and desc in notification
        .setContentTitle(weather.timeStamp as String)
        .setContentText(weather.maxTemp as String)
        .setContentIntent(notificationPendingIntent)
        .setAutoCancel(true)
        .build()

    notificationManager.notify(getUniqueId(), notification)
}

private fun getUniqueId() = ((System.currentTimeMillis() % 10000).toInt())

//    // Create intent to navigate to detail activity
//    //TODO: Add WeatherActivity and change intent direction
//    val contentIntent = Intent(applicationContext, MainActivity::class.java).apply {
//        this.putExtra("downloadBundle", bundle)
//    }
//    Timber.i("contentIntent: $contentIntent")
//
//    // Create PendingIntent to allow notification to open detail activity
//    val contentPendingIntent = PendingIntent.getActivity(
//        applicationContext,
//        NOTIFICATION_ID,
//        contentIntent,
//        PendingIntent.FLAG_UPDATE_CURRENT,
//        bundle
//    )
//
//    Timber.i("contentPendingIntent: $contentPendingIntent")
//
//    // Get an instance of NotificationCompat.Builder
//    val builder = NotificationCompat.Builder(
//        applicationContext,
//        // Use 'download' notification channel
//        applicationContext.getString(R.string.weather_notification_channel_id)
//    )
//        // Set title, text and icon to builder
//        .setSmallIcon(R.drawable._00_pumpkin)
//        .setContentTitle(applicationContext
//            .getString(R.string.notification_title))
//        .setContentText(messageBody)
//
//        // Add content intent action
//        //TODO: Fix: everytime "See changes" is clicked the detail activity is duplicated (Detail Fragment onCreate called multiple times)
//        //Have tried changing flags
//        //Bug occurs on both emulator and physical device
//        //Still occurs when set with Builder.setContentIntent() and click on notification instead of button
//        //onDestroy in Detail fragment is not called, so this isn't a configuration change
//        .addAction(
//            R.drawable._00_pumpkin,
//            applicationContext.getString(R.string.notification_button),
//            contentPendingIntent
//        )
//
//        // Set priority
//        .setPriority(NotificationCompat.PRIORITY_HIGH)
//
//    // Call notify
//    notify(NOTIFICATION_ID, builder.build())
//
//}

//Extension function to cancel all notifications
fun NotificationManager.cancelNotifications() {
    cancelAll()
}
