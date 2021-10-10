package com.duynn.zahoo.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duynn.zahoo.domain.usecase.CheckAuthUseCase
import com.duynn.zahoo.utils.extension.fold
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 *Created by duynn100198 on 10/04/21.
 */
class SplashViewModel(private val checkAuthUseCase: CheckAuthUseCase) : ViewModel() {
    private val _authEvent = MutableSharedFlow<Boolean>()
    val authEvent get() = _authEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            val auth = checkAuthUseCase.invoke().fold({ false }) { it }
            _authEvent.emit(auth)
        }
    }
}
