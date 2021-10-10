package com.duynn.zahoo.presentation.ui.auth

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.duynn.zahoo.R
import com.duynn.zahoo.databinding.ActivityAuthBinding
import com.duynn.zahoo.utils.extension.viewBinding

class AuthActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityAuthBinding::inflate)
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.auth_nav_host_fragment) as NavHostFragment
        // val inflater = navHostFragment.navController.navInflater
        // val graph = inflater.inflate(R.navigation.auth)
        // graph.startDestination = R.id.otpFragment
        val navController = navHostFragment.navController
        // navController.graph = graph
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(
            navController,
            appBarConfiguration
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    override fun onSupportNavigateUp(): Boolean =
        findNavController(R.id.auth_nav_host_fragment).navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
}
