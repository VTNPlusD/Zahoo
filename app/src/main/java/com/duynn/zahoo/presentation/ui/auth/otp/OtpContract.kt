package com.duynn.zahoo.presentation.ui.auth.otp

import android.app.Activity
import com.duynn.zahoo.data.error.AppError
import com.duynn.zahoo.presentation.base.MVIIntent
import com.duynn.zahoo.presentation.base.MVIPartialStateChange
import com.duynn.zahoo.presentation.base.MVISingleEvent
import com.duynn.zahoo.presentation.base.MVIViewState
import com.duynn.zahoo.utils.types.ValidateErrorType
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

/**
 * Created by duynn100198 on 10/5/21.
 */
interface OtpContract {
    data class ViewState(
        val errors: Set<ValidateErrorType.ValidationError>,
        val isLoading: Boolean,
        //
        val otpChanged: Boolean,
        //
        val otp: String?,
        val authInProgress: Boolean,
        val phoneAuthCredential: PhoneAuthCredential?,
        val verificationId: String?,
        val forceResendingToken: PhoneAuthProvider.ForceResendingToken?,
        val startCountdown: Boolean
    ) : MVIViewState {
        companion object {
            fun initial() = ViewState(
                errors = emptySet(),
                isLoading = false,
                otpChanged = false,
                otp = "",
                authInProgress = false,
                phoneAuthCredential = null,
                verificationId = null,
                forceResendingToken = null,
                startCountdown = false
            )
        }
    }

    sealed class ViewIntent : MVIIntent {
        data class OtpChanged(val otp: String?) : ViewIntent()
        data class Submit(val activity: Activity) : ViewIntent()
        object ChangeNumber : ViewIntent()
        object OtpChangedFirstTime : ViewIntent()
        object OtpBack : ViewIntent()
        data class VerifyPhoneNumber(val activity: Activity) : ViewIntent()
    }

    sealed class PartialStateChange : MVIPartialStateChange<ViewState> {
        data class ErrorsChanged(val errors: Set<ValidateErrorType.ValidationError>) :
            PartialStateChange() {
            override fun reduce(viewState: ViewState) = viewState.copy(errors = errors)
        }

        sealed class Otp : PartialStateChange() {
            object Loading : Otp()
            object OtpSuccess : Otp()
            object OtpClearAuth : Otp()
            data class OtpFailure(val throwable: AppError) : Otp()
            data class OnVerificationCompleted(val phoneAuthCredential: PhoneAuthCredential) : Otp()
            data class OnVerificationFailed(val throwable: AppError) : Otp()
            data class OnCodeSent(
                val verificationId: String,
                val forceResendingToken: PhoneAuthProvider.ForceResendingToken
            ) : Otp()

            override fun reduce(viewState: ViewState): ViewState {
                return when (this) {
                    Loading -> viewState.copy(isLoading = true)
                    is OtpSuccess -> viewState.copy(isLoading = false)
                    is OtpFailure -> viewState.copy(isLoading = false, authInProgress = false)
                    OtpClearAuth -> viewState.copy(isLoading = false)
                    is OnCodeSent -> viewState.copy(
                        isLoading = false,
                        authInProgress = true,
                        verificationId = verificationId,
                        forceResendingToken = forceResendingToken
                    )
                    is OnVerificationCompleted -> viewState.copy(
                        isLoading = false,
                        phoneAuthCredential = phoneAuthCredential
                    )
                    is OnVerificationFailed -> viewState.copy(isLoading = false)
                }
            }
        }

        sealed class FirstChange : PartialStateChange() {
            object OtpChangedFirstTime : FirstChange()

            override fun reduce(viewState: ViewState): ViewState {
                return when (this) {
                    OtpChangedFirstTime -> viewState.copy(otpChanged = true)
                }
            }
        }

        sealed class FormValueChange : PartialStateChange() {
            override fun reduce(viewState: ViewState): ViewState {
                return when (this) {
                    is OtpChanged -> viewState.copy(otp = otp)
                }
            }

            data class OtpChanged(val otp: String?) : FormValueChange()
        }
    }

    sealed class SingleEvent : MVISingleEvent {
        object OtpSuccess : SingleEvent()
        data class OtpFailure(val throwable: AppError) : SingleEvent()
        data class OnVerificationFailed(val throwable: AppError) : SingleEvent()
        object OtpStartCountdown : SingleEvent()
    }
}
