package com.growingrubies.vegpatch.utils

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.growingrubies.vegpatch.data.local.PlantDatabase
import com.growingrubies.vegpatch.data.local.PlantLocalRepository
import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.coroutines.CoroutineContext
import com.growingrubies.vegpatch.data.dto.Result
import com.growingrubies.vegpatch.data.dto.WeatherDTO

//TODO: Remove if not used...
//class NotificationJobIntentService : JobIntentService(), CoroutineScope {
//
//    private var coroutineJob: Job = Job()
//    override val coroutineContext: CoroutineContext
//        get() = Dispatchers.IO + coroutineJob
//
//    companion object {
//        private const val JOB_ID = 2771
//
//        //Call this to start the JobIntentService to handle the geofencing transition events
//        //TODO: intent probably isn't needed here??
//        fun enqueueWork(context: Context, intent: Intent) {
//            enqueueWork(
//                context,
//                NotificationJobIntentService::class.java, JOB_ID,
//                intent
//            )
//        }
//    }
//
//    override fun onHandleWork(intent: Intent) {
//        sendNotification()
//    }
//
//
//    private fun sendNotification() {
//        Timber.i("sendNotification called")
//
//        //Get the local repository instance
//        val plantDao = PlantDatabase.getInstance(this).plantDatabaseDao
//        val weatherDao = PlantDatabase.getInstance(this).weatherDatabaseDao
//        val plantRepository = PlantLocalRepository(plantDao, weatherDao)
//
//        //val notificationManager: NotificationManager
//        //val notificationManager: NotificationManager = NotificationManager()
//        val notificationManager = this
//            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        //Interaction to the repository has to be through a coroutine scope
//        CoroutineScope(coroutineContext).launch(SupervisorJob()) {
//            //get the reminder with the request id
//            Timber.i("Notification coroutine launched")
//            val result = plantRepository.getWeatherForecast()
//            if (result is Result.Success<*>) {
//                val weatherDTO = result.data as WeatherDTO
//                if (weatherDTO.maxTemp >= 25 || weatherDTO.minTemp <= 0) {
//                    //send a notification to the user with details about extreme weather
//                        // TODO: Add element to check whether any plants are affected and use in logic
//                    notificationManager.sendNotification(
//                        this@NotificationJobIntentService,
//                        weatherDTO)
//                }
//            }
//
//        }
//    }
//
//}