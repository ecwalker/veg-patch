package com.growingrubies.vegpatch.weatherdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.growingrubies.vegpatch.R
import com.growingrubies.vegpatch.settings.SettingsActivity
import com.growingrubies.vegpatch.data.local.PlantDatabase
import com.growingrubies.vegpatch.databinding.ActivityWeatherDetailBinding
import timber.log.Timber

class WeatherDetailActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_WeatherDataItem = "EXTRA_WeatherDataItem"

        // Receive the weather object after the user clicks on the notification
        fun newIntent(context: Context, weatherInfoString: String): Intent {
            val intent = Intent(context, WeatherDetailActivity::class.java)
            intent.putExtra(EXTRA_WeatherDataItem, weatherInfoString)
            return intent
        }
    }

    private lateinit var binding: ActivityWeatherDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        Timber.i("onCreate called")

        val application = requireNotNull(this).application
        val dataSource = PlantDatabase.getInstance(application).plantDatabaseDao
        val weatherDataSource = PlantDatabase.getInstance(application).weatherDatabaseDao

        //Set up binding and viewmodel
        binding = ActivityWeatherDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this

        val weatherDetailViewModel = WeatherDetailViewModel(dataSource, weatherDataSource, application)
        binding.weatherDetailViewModel = weatherDetailViewModel

        //Retrieve weather object from bundle
        val weatherStringFromBundle = intent.getStringExtra(EXTRA_WeatherDataItem)
            ?: getString(R.string.weather_warning_unset)

        binding.weatherWarningTextView.text = weatherStringFromBundle
        weatherDetailViewModel.setWeatherImage(binding.weatherImageView, weatherStringFromBundle)


        //Set up navigation elements
        setSupportActionBar(binding.toolbar)

    }

    /**
     * Override functions for overflow menu navigation
     */

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                navigateToSettings()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToSettings() {
        val intent = Intent(applicationContext, SettingsActivity::class.java)
        startActivity(intent)
    }


    /**
     * Additional Functions...
     */
}