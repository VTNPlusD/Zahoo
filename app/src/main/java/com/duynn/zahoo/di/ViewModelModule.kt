package com.duynn.zahoo.di

import com.duynn.zahoo.presentation.ui.auth.login.LoginViewModel
import com.duynn.zahoo.presentation.ui.auth.otp.OtpViewModel
import com.duynn.zahoo.presentation.ui.main.MainViewModel
import com.duynn.zahoo.presentation.ui.splash.SplashViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 *Created by duynn100198 on 10/04/21.
 */
/**
 * You can create your ViewModel with scope, however it is not required because
 * 1 ViewModel can be used by several LifeCycleOwners.
 */
@ExperimentalCoroutinesApi
@FlowPreview
val viewModelModule = module {
    viewModel {
        LoginViewModel(
            userGetCountriesUseCase = get(),
            mapper = get(),
            getTokenUseCase = get(),
            setTokenUseCase = get()
        )
    }
    viewModel {
        OtpViewModel(
            tokenObservableUseCase = get(),
            clearAuthUseCase = get(),
            verifyPhoneNumberUseCase = get(),
            phoneAuthMapper = get(),
            signInWithPhoneAuthCredentialUseCase = get()
        )
    }
    viewModel {
        SplashViewModel(checkAuthUseCase = get())
    }
    viewModel {
        MainViewModel(
            userObservableUseCase = get(),
            app = get()
        )
    }
}
