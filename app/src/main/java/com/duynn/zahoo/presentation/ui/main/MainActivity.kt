package com.duynn.zahoo.presentation.ui.main

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.duynn.zahoo.R
import com.duynn.zahoo.databinding.ActivityMainBinding
import com.duynn.zahoo.presentation.ui.auth.AuthActivity
import com.duynn.zahoo.utils.dialog.dismissAlertDialog
import com.duynn.zahoo.utils.extension.*
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.androidx.viewmodel.scope.emptyState
import timber.log.Timber

/**
 *Created by duynn100198 on 10/04/21.
 */

class MainActivity : AppCompatActivity() {
    private var currentNavController: StateFlow<NavController>? = null
    private val binding by viewBinding(ActivityMainBinding::inflate)
    private val viewModel by viewModel<MainViewModel>(state = emptyState())

    var onSupportNavigateUp: (() -> Boolean)? = null

    private val navHostFragment
        get() =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        savedInstanceState?.let {
            setupBottomNavigationBar()
        } ?: run {
            dismissAlertDialog()
        }
        bindVM()
    }

    override fun onResume() {
        super.onResume()
        clearNotifications()
    }

    private fun clearNotifications() {
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancelAll()
    }

    private fun bindVM() {
        viewModel.logoutEvent.observe(this) {
            Timber.d("[LOGOUT]")
            startActivity(Intent(this@MainActivity, AuthActivity::class.java))
            finish()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        val navGraphIds = listOf(
            R.navigation.chat,
            R.navigation.group,
            R.navigation.phone_book,
            R.navigation.setting
        )

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = binding.navView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment,
            intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this) { navController ->
            setupActionBarWithNavController(navController)
        }
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        onSupportNavigateUp?.invoke()?.let { return it }
        return currentNavController?.value?.navigateUp() ?: false
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Timber.d("[onActivityResult] { requestCode: $requestCode, resultCode: $resultCode, data: $data }")

        (navHostFragment ?: return)
            .childFragmentManager
            .fragments
            .forEach { it.onActivityResult(requestCode, resultCode, data) }
    }

    fun hideBottomNav() = binding.navView.gone()

    fun showBottomNav() = binding.navView.visible()
}
