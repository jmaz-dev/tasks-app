package com.devmasterteam.tasks.view

import android.content.Intent
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.NavigationUI
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.ActivityMainBinding
import com.devmasterteam.tasks.service.helper.BiometricHelper
import com.devmasterteam.tasks.viewmodel.MainViewModel

class MainActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener {
            startActivity(Intent(this.applicationContext, TaskFormActivity::class.java))
        }

        //Listener
        listeners()

        //loadUserName
        viewModel.loadUserName()

        // Navegação
        setupNavigation()

        // Observadores
        observe()

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        when (buttonView.id) {
            R.id.toggle_biometric -> {
                if (BiometricHelper.isBiometricAvailable(this)) {
                    viewModel.toggleBiometric(isChecked)

                } else {
                    Toast.makeText(this, getString(R.string.ERROR_BIOMETRICS), Toast.LENGTH_SHORT)
                        .show()
                    buttonView.isChecked = false
                }
            }
        }
    }

    private fun listeners() {
        val navView = findViewById<NavigationView>(R.id.nav_view)
        val toggleButton = navView.menu.findItem(R.id.toggle_biometric).actionView as SwitchCompat

        /*Verify if the biometric is active*/
        toggleButton.isChecked = viewModel.isBiometricActive()

        /*Listener*/
        toggleButton.setOnCheckedChangeListener(this)


    }

    private fun setupNavigation() {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_all_tasks, R.id.nav_next_tasks, R.id.nav_expired),
            drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        /*Nav Click Listener need to be called in the End*/
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_logout -> {
                    viewModel.logout()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }

                else -> {
                    NavigationUI.onNavDestinationSelected(it, navController)
                    drawerLayout.close()
                }
            }
            true
        }
    }

    private fun observe() {
        viewModel.personName.observe(this) {
            val headerText = binding.navView.getHeaderView(0).findViewById<TextView>(R.id.text_name)
            headerText.text = getString(R.string.hello, it)
        }
    }


}