package com.growingrubies.vegpatch.plantdetail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.growingrubies.vegpatch.R
import com.growingrubies.vegpatch.settings.SettingsActivity
import com.growingrubies.vegpatch.data.local.PlantDatabase
import com.growingrubies.vegpatch.databinding.ActivityPlantDetailBinding
import com.growingrubies.vegpatch.overview.MainActivity
import timber.log.Timber

class PlantDetailActivity: AppCompatActivity() {

    private lateinit var binding: ActivityPlantDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        Timber.i("onCreate called")

        val application = requireNotNull(this).application
        val plantDao = PlantDatabase.getInstance(application).plantDatabaseDao
        val weatherDao = PlantDatabase.getInstance(application).weatherDatabaseDao

        //Set up binding and viewmodel
        binding = ActivityPlantDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this

        val plantDetailViewModel = PlantDetailViewModel(plantDao, weatherDao, application)
        binding.plantDetailViewModel = plantDetailViewModel

        //Retrieve the current plant's info from database
        val idFromBundle = intent.getLongExtra("plantId", 0L)
        plantDetailViewModel.getPlantFromDatabase(idFromBundle)

        //Set the icon depending on the current plant selected
        plantDetailViewModel.currentPlant.observe(this, Observer {
            plantDetailViewModel.mapImages(binding.iconImageView, it)
        })

        //Listener for remove plant button
        binding.removePlantButton.setOnClickListener {
            //Set plant as inactive
            plantDetailViewModel.onRemovePlantClicked()
            //Navigate with intent
            val contentIntent = Intent(applicationContext, MainActivity::class.java)
            startActivity(contentIntent)
        }

        //Set up navigation extra elements (ActionBar and FAB)
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