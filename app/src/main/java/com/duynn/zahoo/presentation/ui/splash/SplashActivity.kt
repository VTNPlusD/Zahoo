package com.duynn.zahoo.presentation.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.duynn.zahoo.presentation.ui.auth.AuthActivity
import com.duynn.zahoo.presentation.ui.main.MainActivity
import com.duynn.zahoo.utils.extension.observe
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.androidx.viewmodel.scope.emptyState
import timber.log.Timber

class SplashActivity : AppCompatActivity() {

    private val viewModel by viewModel<SplashViewModel>(state = emptyState())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.authEvent.observe(this) {
            Timber.i("viewModel.authEvent")
            val clazz = if (it) MainActivity::class.java else AuthActivity::class.java
            val intent = Intent(this, clazz).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            startActivity(intent)
            finish()
        }
    }
}
