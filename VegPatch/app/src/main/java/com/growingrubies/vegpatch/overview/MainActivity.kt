package com.growingrubies.vegpatch.overview

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.growingrubies.vegpatch.R
import com.growingrubies.vegpatch.settings.SettingsActivity
import com.growingrubies.vegpatch.addplant.AddPlantActivity
import com.growingrubies.vegpatch.data.local.PlantDatabase
import com.growingrubies.vegpatch.databinding.ActivityMainBinding
import com.growingrubies.vegpatch.plantdetail.PlantDetailActivity
import com.growingrubies.vegpatch.utils.WeatherCodes
import com.growingrubies.vegpatch.weatherdetail.WeatherDetailActivity
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        Timber.i("onCreate called")

        val application = requireNotNull(this).application
        val dataSource = PlantDatabase.getInstance(application).plantDatabaseDao
        val weatherDataSource = PlantDatabase.getInstance(application).weatherDatabaseDao

        //Set up binding and viewmodel
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this

        val overviewViewModel = OverviewActivityViewModel(dataSource, weatherDataSource, application)
        binding.overviewViewModel = overviewViewModel

        //Set up recyclerview
        val adapter = OverviewListAdapter(PlantListener {
                id -> overviewViewModel.onPlantClicked(id)
        })
        binding.activePlantListRecyclerView.adapter = adapter

        //Observe plant list from ViewModel
        overviewViewModel.plantList.observe(this, Observer {
            Timber.i("plantList change observed. It is now: $it")
            it?.let {
                adapter.submitList(it)
            } ?: Timber.i("plantList LiveData is null")
            binding.numPlantsFigureTextView.text = "${it.size}"
        })

        //Observe number of database returns, then set weather icon
        overviewViewModel.dbReturns.observe(this, Observer {
            if (it == 2) {
                overviewViewModel.setWeatherImage(binding.weatherImageView,
                    overviewViewModel.weatherForecast.value,
                    overviewViewModel.plantList.value)
            }
        })

        //Set up navigation elements
        setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener {
            val intent = Intent(applicationContext, SettingsActivity::class.java)
            startActivity(intent)
        }

        overviewViewModel.navigateToPlantDetail.observe(this, Observer {
            val contentIntent = Intent(applicationContext, PlantDetailActivity::class.java).apply {
                this.putExtra("plantId", it)
            }
            startActivity(contentIntent)
        })

        binding.fab.setOnClickListener {
            val contentIntent = Intent(applicationContext, AddPlantActivity::class.java)
            startActivity(contentIntent)
        }

        binding.weatherImageView.setOnClickListener {
            overviewViewModel.currentWeatherCode.value?.let {
                val currentWeatherString = setWeatherString(it)
                val intent = WeatherDetailActivity.newIntent(this, currentWeatherString)
                startActivity(intent)
            }

        }
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

    private fun setWeatherString(weatherCode: WeatherCodes): String {
        return when (weatherCode) {
            is WeatherCodes.NoWarning -> getString(R.string.no_weather_warning)
            is WeatherCodes.ColdWarning -> getString(R.string.cold_weather_warning)
            is WeatherCodes.HeatWarning -> getString(R.string.heat_weather_warning)
            is WeatherCodes.Unset -> getString(R.string.weather_warning_unset)
        }
    }


}