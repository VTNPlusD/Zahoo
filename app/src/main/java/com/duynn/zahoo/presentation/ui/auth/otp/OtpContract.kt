package com.duynn.zahoo.presentation.ui.auth.otp

import com.duynn.zahoo.data.error.AppError
import com.duynn.zahoo.presentation.base.MVIIntent
import com.duynn.zahoo.presentation.base.MVIPartialStateChange
import com.duynn.zahoo.presentation.base.MVISingleEvent
import com.duynn.zahoo.presentation.base.MVIViewState
import com.duynn.zahoo.utils.types.ValidateErrorType

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
        val otp: String?
    ) : MVIViewState {
        companion object {
            fun initial() = ViewState(
                errors = emptySet(),
                isLoading = false,
                otpChanged = false,
                otp = ""
            )
        }
    }

    sealed class ViewIntent : MVIIntent {
        data class OtpChanged(val otp: String?) : ViewIntent()
        object Submit : ViewIntent()
        object Resend : ViewIntent()
        object ChangeNumber : ViewIntent()
        object OtpChangedFirstTime : ViewIntent()
        object OtpBack : ViewIntent()
    }

    sealed class PartialStateChange : MVIPartialStateChange<ViewState> {
        data class ErrorsChanged(val errors: Set<ValidateErrorType.ValidationError>) :
            PartialStateChange() {
            override fun reduce(viewState: ViewState) = viewState.copy(errors = errors)
        }

        sealed class Otp : PartialStateChange() {
            object Loading : Otp()
            object OtpSuccess : Otp()
            object OtpResend : Otp()
            object OtpChangeNumber : Otp()
            object OtpBack : Otp()
            data class OtpFailure(val throwable: AppError) : Otp()

            override fun reduce(viewState: ViewState): ViewState {
                return when (this) {
                    Loading -> viewState.copy(isLoading = true)
                    is OtpSuccess -> viewState.copy(isLoading = false)
                    is OtpFailure -> viewState.copy(isLoading = false)
                    OtpChangeNumber -> viewState.copy(isLoading = false)
                    OtpResend -> viewState.copy(isLoading = false)
                    OtpBack -> viewState.copy(isLoading = false)
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
        object OtpResend : SingleEvent()
        object OtpChangeNumber : SingleEvent()
        object OtpBack : SingleEvent()
        data class OtpFailure(val throwable: AppError) : SingleEvent()
    }
}
