package com.duynn.zahoo.presentation.ui.auth.login

import androidx.lifecycle.viewModelScope
import com.duynn.zahoo.domain.usecase.GetTokenUseCase
import com.duynn.zahoo.domain.usecase.SetTokenUseCase
import com.duynn.zahoo.domain.usecase.UserGetCountriesUseCase
import com.duynn.zahoo.presentation.base.BaseViewModel
import com.duynn.zahoo.presentation.mapper.CountryMapper
import com.duynn.zahoo.presentation.ui.auth.login.LoginContract.*
import com.duynn.zahoo.utils.extension.*
import com.duynn.zahoo.utils.types.ValidateErrorType
import com.duynn.zahoo.utils.types.ValidateErrorType.validatePhone
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 *Created by duynn100198 on 10/04/21.
 */
@FlowPreview
@ExperimentalCoroutinesApi
class LoginViewModel(
    private val userGetCountriesUseCase: UserGetCountriesUseCase,
    private val mapper: CountryMapper,
    private val setTokenUseCase: SetTokenUseCase,
    getTokenUseCase: GetTokenUseCase
) : BaseViewModel<ViewIntent, ViewState, SingleEvent, PartialStateChange>(ViewState.initial()) {
    private val _tokenEvent = MutableSharedFlow<Boolean>()
    val tokenEvent get() = _tokenEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            val tokenEv = getTokenUseCase.invoke().fold(
                { false },
                { !it.isNullOrBlank() }
            )
            _tokenEvent.emit(tokenEv)
        }
    }

    override fun Flow<PartialStateChange>.sendSingleEvent(): Flow<PartialStateChange> {
        return onEach { change ->
            val event = when (change) {
                is PartialStateChange.Login.LoginSuccess -> SingleEvent.LoginSuccess
                is PartialStateChange.Login.LoginFailure -> SingleEvent.LoginFailure(change.throwable)
                is PartialStateChange.FormValueChange.CountriesChanged -> SingleEvent.InitCountries(
                    change.countries
                )
                else -> return@onEach
            }
            eventChannel.send(event)
        }
    }

    override fun Flow<ViewIntent>.toPartialStateChangesFlow(): Flow<PartialStateChange> {
        val phoneErrors = filterIsInstance<ViewIntent.PhoneChanged>()
            .map { it.phone }
            .map { validatePhone(it, viewState.value.country?.dialCode) to it }
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed()
            )

        val countryErrors = filterIsInstance<ViewIntent.CountryChanged>()
            .map { it.country }
            .map { emptySet<ValidateErrorType.ValidationError>() to it }
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed()
            )
        val countriesFlow = filterIsInstance<ViewIntent.GetCountries>()
            .map {
                userGetCountriesUseCase.invoke().fold({
                    Either.Left(it)
                }) {
                    Either.Right(mapper.map(it))
                }
            }
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed()
            )

        val loginFormFlow =
            combine(phoneErrors, countryErrors) { phone, country ->
                val errors = phone.first + country.first
                if (errors.isEmpty()) {
                    val phoneTrim = phone.second?.replace("+", "")
                    val phoneNumber = "${country.second?.dialCode ?: ""}$phoneTrim"
                    Either.Right(phoneNumber)
                } else Either.Left(errors)
            }.shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed()
            )

        val loginChanges = filterIsInstance<ViewIntent.Submit>()
            .withLatestFrom(loginFormFlow) { _, loginForm -> loginForm }
            .mapNotNull { it.rightOrNull() }
            .flatMapFirst { phoneNumber ->
                flow {
                    setTokenUseCase.invoke(phoneNumber).fold(
                        ifRight = { emit(PartialStateChange.Login.LoginSuccess) },
                        ifLeft = { emit(PartialStateChange.Login.LoginFailure(it)) }
                    )
                }.onStart {
                    emit(PartialStateChange.Login.Loading)
                }
            }

        val firstChanges = filterIsInstance<ViewIntent.PhoneChangedFirstTime>()
            .map { PartialStateChange.FirstChange.PhoneChangedFirstTime }

        val formValuesChanges = merge(
            phoneErrors
                .map { it.second }
                .map { PartialStateChange.FormValueChange.PhoneChanged(it) },
            countryErrors
                .map { it.second }
                .map { PartialStateChange.FormValueChange.CountryChanged(it) }
        )
        return merge(
            countriesFlow.map {
                val rightValue = it.rightOrNull() ?: emptyList()
                PartialStateChange.FormValueChange.CountriesChanged(
                    rightValue.sortedBy { country -> country.name }
                )
            },
            loginFormFlow
                .map {
                    PartialStateChange.ErrorsChanged(it.leftOrNull() ?: emptySet())
                },
            loginChanges,
            firstChanges,
            formValuesChanges
        )
    }
}
