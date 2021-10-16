package com.duynn.zahoo.presentation.ui.main

import android.app.Application
import androidx.lifecycle.ViewModel
import com.duynn.zahoo.domain.usecase.UserObservableUseCase
import com.duynn.zahoo.utils.extension.toOption
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

/**
 *Created by duynn100198 on 10/04/21.
 */
class MainViewModel(
    private val userObservableUseCase: UserObservableUseCase,
    private val app: Application
) : ViewModel() {

    val logoutEvent = userObservableUseCase.invoke()
        .map { it.toOption() }
        .distinctUntilChanged()
        .filter { it.isEmpty() }
        .map {}

    companion object {
        const val TAG = "MainViewModel"
    }
}
