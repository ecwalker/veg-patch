package com.growingrubies.vegpatch

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.os.Build
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit
import androidx.work.*
import com.growingrubies.vegpatch.utils.RefreshDataWorker

/**
 * Override application to setup background work via WorkManager
 */
class VegPatchApplication : Application() {

    val applicationScope = CoroutineScope(Dispatchers.Default)

    private fun delayedInit() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()

        //Get shared preference (city)
        val cityPref = this.getString(R.string.preference_file_key)
        val cityKey = this.getString(R.string.city_key)
        val sharedPref = this.getSharedPreferences(cityPref, Context.MODE_PRIVATE)

        //Create Data object with city sharedPref
        val cityData = Data.Builder().putString(cityKey, sharedPref.getString(cityKey, null))

        //Build periodic work request
        val repeatingRequest
                = PeriodicWorkRequestBuilder<RefreshDataWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .setInputData(cityData.build())
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest)
    }

    /**
     * onCreate is called before the first screen is shown to the user.
     *
     * Use it to setup any background tasks, running expensive setup operations in a background
     * thread to avoid delaying app start.
     */
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.i("Scheduler onCreate called")
        delayedInit()
    }
}