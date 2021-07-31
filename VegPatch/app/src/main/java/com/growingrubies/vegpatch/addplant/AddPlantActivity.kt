package com.growingrubies.vegpatch.addplant

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.growingrubies.vegpatch.R
import com.growingrubies.vegpatch.data.local.PlantDatabase
import com.growingrubies.vegpatch.databinding.ActivityAddPlantBinding
import com.growingrubies.vegpatch.overview.MainActivity
import timber.log.Timber

class AddPlantActivity: AppCompatActivity() {

    private lateinit var binding: ActivityAddPlantBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        Timber.i("onCreate called")

        val application = requireNotNull(this).application
        val plantDao = PlantDatabase.getInstance(application).plantDatabaseDao
        val weatherDao = PlantDatabase.getInstance(application).weatherDatabaseDao

        //Set up binding and viewmodel
        binding = ActivityAddPlantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this

        val addPlantViewModel = AddPlantViewModel(plantDao, weatherDao, application)
        binding.addPlantViewModel = addPlantViewModel

        //Set up recyclerview
        val adapter = AddPlantListAdapter(AddPlantListener {
                id -> addPlantViewModel.onPlantClicked(id)
        })
        binding.plantListRecyclerView.adapter = adapter


        //Observe plant list from ViewModel
        addPlantViewModel.plantList.observe(this, Observer {
            Timber.i("plantList change observed. It is now: $it")
            it?.let {
                adapter.submitList(it)
            } ?: Timber.i("plantList LiveData is null")
        })

        //Set up navigation extra elements (ActionBar and FAB)
        setSupportActionBar(binding.toolbar)


        //TODO: Set click listener to confirm selection and navigate to Overview Activity
        addPlantViewModel.navigateToMainActivity.observe(this, Observer {
            if (it) {
                val contentIntent = Intent(applicationContext, MainActivity::class.java)
                startActivity(contentIntent)
            }
        })


    }

    /**
     * Override functions
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
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Additional Functions...
     */

}