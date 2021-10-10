package com.duynn.zahoo.presentation.ui.auth.otp

import androidx.lifecycle.viewModelScope
import com.duynn.zahoo.data.error.AppError
import com.duynn.zahoo.domain.entity.PhoneAuth
import com.duynn.zahoo.domain.usecase.ClearAuthUseCase
import com.duynn.zahoo.domain.usecase.GetTokenUseCase
import com.duynn.zahoo.domain.usecase.SignInWithPhoneAuthCredentialUseCase
import com.duynn.zahoo.domain.usecase.TokenObservableUseCase
import com.duynn.zahoo.domain.usecase.VerifyPhoneNumberUseCase
import com.duynn.zahoo.presentation.base.BaseViewModel
import com.duynn.zahoo.presentation.mapper.PhoneAuthMapper
import com.duynn.zahoo.presentation.ui.auth.otp.OtpContract.*
import com.duynn.zahoo.utils.extension.Either
import com.duynn.zahoo.utils.extension.flatMapFirst
import com.duynn.zahoo.utils.extension.fold
import com.duynn.zahoo.utils.extension.getOrNull
import com.duynn.zahoo.utils.extension.leftOrNull
import com.duynn.zahoo.utils.extension.rightOrNull
import com.duynn.zahoo.utils.extension.toOption
import com.duynn.zahoo.utils.extension.withLatestFrom
import com.duynn.zahoo.utils.types.ValidateErrorType
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

/**
 * Created by duynn100198 on 10/5/21.
 */
@FlowPreview
@ExperimentalCoroutinesApi
class OtpViewModel(
    tokenObservableUseCase: TokenObservableUseCase,
    private val clearAuthUseCase: ClearAuthUseCase,
    private val verifyPhoneNumberUseCase: VerifyPhoneNumberUseCase,
    private val getTokenUseCase: GetTokenUseCase,
    private val signInWithPhoneAuthCredentialUseCase: SignInWithPhoneAuthCredentialUseCase,
    private val phoneAuthMapper: PhoneAuthMapper
) : BaseViewModel<ViewIntent, ViewState, SingleEvent, PartialStateChange>(ViewState.initial()) {

    init {
        viewModelScope.launch {
            val phone = getTokenUseCase.invoke().getOrNull()
            processIntent(ViewIntent.PhoneNumberSuccess(phone))
        }
    }

    val tokenEvent = tokenObservableUseCase.invoke()
        .map { it.toOption() }
        .distinctUntilChanged()
        .filter { it.isEmpty() }
        .map {}

    override fun Flow<PartialStateChange>.sendSingleEvent(): Flow<PartialStateChange> {
        return onEach { change ->
            val event = when (change) {
                is PartialStateChange.Otp.OtpSuccess -> SingleEvent.OtpSuccess
                is PartialStateChange.Otp.OtpFailure -> SingleEvent.OtpFailure(change.throwable)
                is PartialStateChange.Otp.OnCodeSent -> SingleEvent.OtpStartCountdown
                is PartialStateChange.Otp.OnVerificationFailed -> SingleEvent.OnVerificationFailed(
                    change.throwable)
                else -> return@onEach
            }
            eventChannel.send(event)
        }
    }

    override fun Flow<ViewIntent>.toPartialStateChangesFlow(): Flow<PartialStateChange> {
        val otpErrors = filterIsInstance<ViewIntent.OtpChanged>()
            .map { it.otp }
            .map { ValidateErrorType.validateOtp(it) to it }
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed()
            )

        val otpFormFlow =
            otpErrors.map {
                val errors = it.first
                if (errors.isEmpty()) Either.Right(
                    it.second
                ) else Either.Left(errors)
            }.shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed()
            )

        val otpEventFlow = merge(
            filterIsInstance<ViewIntent.Submit>()
                .withLatestFrom(otpFormFlow) { _, otpForm -> otpForm }
                .mapNotNull { it.rightOrNull() }
                .flatMapFirst { otp ->
                    flow {
                        viewState.value.verificationId?.let {
                            signInWithPhoneAuthCredentialUseCase.invoke(
                                PhoneAuthProvider.getCredential(it, otp)
                            ).fold({ err ->
                                emit(PartialStateChange.Otp.OtpFailure(err))
                            }, {
                                emit(PartialStateChange.Otp.OtpSuccess)
                            })
                        } ?: run {
                            emit(PartialStateChange.Otp.OtpFailure(AppError.WrongVerification))
                        }
                    }.onStart { emit(PartialStateChange.Otp.Loading) }
                },
            filterIsInstance<ViewIntent.ChangeNumber>().flatMapFirst {
                flow {
                    clearAuthUseCase.invoke().fold({
                        emit(PartialStateChange.Otp.OtpFailure(it))
                    }, {
                        emit(PartialStateChange.Otp.OtpClearAuth)
                    })
                }
            },
            filterIsInstance<ViewIntent.OtpBack>().flatMapFirst {
                flow {
                    clearAuthUseCase.invoke().fold({
                        emit(PartialStateChange.Otp.OtpFailure(it))
                    }, {
                        emit(PartialStateChange.Otp.OtpClearAuth)
                    })
                }
            })

        val phoneFlow = merge(
            filterIsInstance<ViewIntent.PhoneNumberSuccess>().map {
                PartialStateChange.Otp.PhoneNumberSuccess(it.phone)
            },
            filterIsInstance<ViewIntent.VerifyPhoneNumber>().flatMapFirst {
                flow {
                    verifyPhoneNumberUseCase.invoke(it.activity)
                        .fold({ err ->
                            emit(PartialStateChange.Otp.OnVerificationFailed(err))
                        }, { phoneAuthData ->
                            when (val phoneAuth = phoneAuthMapper.map(phoneAuthData)) {
                                is PhoneAuth.CodeSent -> {
                                    emit(PartialStateChange.Otp.OnCodeSent(phoneAuth.verificationId,
                                        phoneAuth.token))
                                }
                                is PhoneAuth.VerificationCompleted -> {
                                    emit(PartialStateChange.Otp.OnVerificationCompleted(
                                        phoneAuth.credential))
                                    emit(PartialStateChange.Otp.OtpSuccess)
                                }
                            }
                        })
                }.onStart { emit(PartialStateChange.Otp.Loading) }
            }
        )

        val firstChanges = filterIsInstance<ViewIntent.OtpChangedFirstTime>()
            .map { PartialStateChange.FirstChange.OtpChangedFirstTime }

        val formValuesChanges = merge(
            otpErrors
                .map { it.second }
                .map { PartialStateChange.FormValueChange.OtpChanged(it) }
        )
        return merge(
            otpFormFlow
                .map {
                    PartialStateChange.ErrorsChanged(it.leftOrNull() ?: emptySet())
                },
            otpEventFlow,
            firstChanges,
            formValuesChanges,
            phoneFlow
        )
    }
}
