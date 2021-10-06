package com.duynn.zahoo.presentation.ui.auth.login

import com.duynn.zahoo.data.error.AppError
import com.duynn.zahoo.domain.entity.Country
import com.duynn.zahoo.presentation.base.MVIIntent
import com.duynn.zahoo.presentation.base.MVIPartialStateChange
import com.duynn.zahoo.presentation.base.MVISingleEvent
import com.duynn.zahoo.presentation.base.MVIViewState
import com.duynn.zahoo.utils.types.ValidateErrorType.ValidationError

/**
 *Created by duynn100198 on 10/04/21.
 */
interface LoginContract {
    data class ViewState(
        val errors: Set<ValidationError>,
        val isLoading: Boolean,
        //
        val phoneChanged: Boolean,
        //
        val phone: String?,
        val country: Country?,
        val countries: List<Country>
    ) : MVIViewState {
        companion object {
            fun initial() = ViewState(
                errors = emptySet(),
                isLoading = false,
                phoneChanged = false,
                phone = null,
                country = null,
                countries = emptyList()
            )
        }
    }

    sealed class ViewIntent : MVIIntent {
        data class PhoneChanged(val phone: String?) : ViewIntent()
        data class CountryChanged(val country: Country?) : ViewIntent()

        object Submit : ViewIntent()

        object GetCountries : ViewIntent()

        object PhoneChangedFirstTime : ViewIntent()
    }

    sealed class PartialStateChange : MVIPartialStateChange<ViewState> {
        data class ErrorsChanged(val errors: Set<ValidationError>) : PartialStateChange() {
            override fun reduce(viewState: ViewState) = viewState.copy(errors = errors)
        }

        sealed class Login : PartialStateChange() {
            object Loading : Login()
            object LoginSuccess : Login()
            data class LoginFailure(val throwable: AppError) : Login()

            override fun reduce(viewState: ViewState): ViewState {
                return when (this) {
                    Loading -> viewState.copy(isLoading = true)
                    is LoginSuccess -> viewState.copy(isLoading = false)
                    is LoginFailure -> viewState.copy(isLoading = false)
                }
            }
        }

        sealed class FirstChange : PartialStateChange() {
            object PhoneChangedFirstTime : FirstChange()

            override fun reduce(viewState: ViewState): ViewState {
                return when (this) {
                    PhoneChangedFirstTime -> viewState.copy(phoneChanged = true)
                }
            }
        }

        sealed class FormValueChange : PartialStateChange() {
            override fun reduce(viewState: ViewState): ViewState {
                return when (this) {
                    is CountriesChanged -> viewState.copy(countries = countries)
                    is CountryChanged -> viewState.copy(country = country)
                    is PhoneChanged -> viewState.copy(phone = phone)
                }
            }

            data class CountriesChanged(val countries: List<Country>) : FormValueChange()
            data class CountryChanged(val country: Country?) : FormValueChange()
            data class PhoneChanged(val phone: String?) : FormValueChange()
        }
    }

    sealed class SingleEvent : MVISingleEvent {
        object LoginSuccess : SingleEvent()
        data class LoginFailure(val throwable: AppError) : SingleEvent()
        data class InitCountries(val countries: List<Country>) : SingleEvent()
    }
}
