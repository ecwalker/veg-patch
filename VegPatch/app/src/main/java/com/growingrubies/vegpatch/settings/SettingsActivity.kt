package com.growingrubies.vegpatch.settings

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.growingrubies.vegpatch.R
import com.growingrubies.vegpatch.data.local.PlantDatabase
import com.growingrubies.vegpatch.databinding.ActivitySettingsBinding
import com.growingrubies.vegpatch.overview.MainActivity
import timber.log.Timber

class SettingsActivity: AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivitySettingsBinding
    private var selectedCity: Any? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            navigateToMainActivity(settingsViewModel)
        }

        // Settings adapter: Linking to cities_list
        val spinner: Spinner = findViewById(R.id.spinner)
        spinner.onItemSelectedListener = this

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.cities_list,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        //Listener for navigation livedata for sharedPref and db update trigger
        settingsViewModel.isNavigation.observe(this, Observer {
            if (it == true) {
                selectedCity?.let {
                    settingsViewModel.saveSharedPreferences(it, this)
                }
            }
        })
    }


    /**
     * Navigation functions
     */

    private fun navigateToMainActivity(viewModel: SettingsViewModel) {
        //Navigation with liveData trigger for updateWeatherForecast
        viewModel.onNavigation()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    /**
     * Save shared preferences and refresh weather forecast on navigation
     */

    private fun updateWeatherForecast(viewModel: SettingsViewModel) {
        selectedCity?.let {
            viewModel.saveSharedPreferences(it, this)
        }
    }


    /**
     * Responding to user selection on spinner
     */

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        Timber.i("Spinner onItemSelected called")
        selectedCity = parent.getItemAtPosition(pos)
        Timber.i("$selectedCity selected from spinner.")
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Toast asking to fill in city
        Timber.i("Spinner onNothingSelected called")
        Toast.makeText(this, R.string.no_city_selected, Toast.LENGTH_SHORT).show()
    }
}