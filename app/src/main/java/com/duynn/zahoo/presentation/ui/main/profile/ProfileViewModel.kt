package com.duynn.zahoo.presentation.ui.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duynn.zahoo.domain.usecase.UserLogoutUseCase
import com.duynn.zahoo.utils.extension.fold
import kotlinx.coroutines.launch

/**
 *Created by duynn100198 on 10/04/21.
 */
class ProfileViewModel(private val userLogoutUseCase: UserLogoutUseCase) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            userLogoutUseCase.invoke().fold(
                ifLeft = {
                    // something handle logout error
                },
                ifRight = {
                    // something handle logout success
                }
            )
        }
    }
}
