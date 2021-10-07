package com.duynn.zahoo.presentation.ui.auth.otp

import androidx.lifecycle.viewModelScope
import com.duynn.zahoo.presentation.base.BaseViewModel
import com.duynn.zahoo.presentation.ui.auth.otp.OtpContract.*
import com.duynn.zahoo.utils.extension.Either
import com.duynn.zahoo.utils.extension.flatMapFirst
import com.duynn.zahoo.utils.extension.leftOrNull
import com.duynn.zahoo.utils.extension.rightOrNull
import com.duynn.zahoo.utils.extension.withLatestFrom
import com.duynn.zahoo.utils.types.ValidateErrorType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import timber.log.Timber

/**
 * Created by duynn100198 on 10/5/21.
 */
@FlowPreview
@ExperimentalCoroutinesApi
class OtpViewModel :
    BaseViewModel<ViewIntent, ViewState, SingleEvent, PartialStateChange>(ViewState.initial()) {

    override fun Flow<PartialStateChange>.sendSingleEvent(): Flow<PartialStateChange> {
        return onEach { change ->
            val event = when (change) {
                is PartialStateChange.Otp.OtpSuccess -> SingleEvent.OtpSuccess
                is PartialStateChange.Otp.OtpFailure -> SingleEvent.OtpFailure(change.throwable)
                PartialStateChange.Otp.OtpChangeNumber -> SingleEvent.OtpResend
                PartialStateChange.Otp.OtpResend -> SingleEvent.OtpChangeNumber
                PartialStateChange.Otp.OtpBack -> SingleEvent.OtpBack
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

        val otpChanges = filterIsInstance<ViewIntent.Submit>()
            .withLatestFrom(otpFormFlow) { _, otpForm -> otpForm }
            .mapNotNull { it.rightOrNull() }
            .flatMapFirst { otp ->
                flow {
                    Timber.d("loginChanges=$otp-user.password")
                    emit(PartialStateChange.Otp.OtpSuccess as PartialStateChange.Otp)
                    // loginUseCase.invoke(otp).fold(
                    //     ifRight = { emit(PartialStateChange.Otp.OtpSuccess) },
                    //     ifLeft = { emit(PartialStateChange.Otp.OtpFailure(it)) }
                    // )
                }.onStart {
                    emit(PartialStateChange.Otp.Loading)
                }
            }

        val otpResend = filterIsInstance<ViewIntent.Resend>().map {
            PartialStateChange.Otp.OtpResend
        }

        val otpChangeNumber = filterIsInstance<ViewIntent.ChangeNumber>().map {
            PartialStateChange.Otp.OtpChangeNumber
        }

        val otpBack = filterIsInstance<ViewIntent.OtpBack>().map {
            PartialStateChange.Otp.OtpBack
        }

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
            otpChanges,
            otpResend,
            otpChangeNumber,
            otpBack,
            firstChanges,
            formValuesChanges
        )
    }
}
