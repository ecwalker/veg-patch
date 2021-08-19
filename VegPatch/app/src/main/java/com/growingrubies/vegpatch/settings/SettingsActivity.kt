package com.growingrubies.vegpatch.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.growingrubies.vegpatch.R
import com.growingrubies.vegpatch.data.local.PlantDatabase
import com.growingrubies.vegpatch.databinding.ActivityMainBinding
import com.growingrubies.vegpatch.databinding.ActivitySettingsBinding
import com.growingrubies.vegpatch.overview.MainActivity

class SettingsActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        val application = requireNotNull(this).application
        val plantDataSource = PlantDatabase.getInstance(application).plantDatabaseDao
        val weatherDataSource = PlantDatabase.getInstance(application).weatherDatabaseDao

        //Set up binding and ViewModel
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this

        val settingsViewModel = SettingsViewModel(plantDataSource, weatherDataSource, application)
        binding.settingViewModel = settingsViewModel


        //Set up navigation

        setSupportActionBar(binding.toolbar)

        binding.saveButton.setOnClickListener {
            val latitude = binding.latitudeEditTextNumber.text.toString()
            val longitude = binding.longitudeEditTextNumber.text.toString()
            val isLocationValidated = settingsViewModel.validateLocationEntered(latitude, longitude, this)
            if (isLocationValidated) {
                settingsViewModel.saveSharedPreferences(latitude, longitude, this)
                navigateToMainActivity()

            }
        }

    }

    /**
     * Navigation functions
     */

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}