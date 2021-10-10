package com.duynn.zahoo.presentation.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.duynn.zahoo.presentation.ui.auth.AuthActivity
import com.duynn.zahoo.presentation.ui.main.MainActivity
import com.duynn.zahoo.utils.extension.observe
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.androidx.viewmodel.scope.emptyState

class SplashActivity : AppCompatActivity() {

    private val viewModel by viewModel<SplashViewModel>(state = emptyState())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.authEvent.observe(this) {
            val clazz = if (it) MainActivity::class.java else AuthActivity::class.java
            startActivity(Intent(this, clazz))
            finish()
        }
    }
}
