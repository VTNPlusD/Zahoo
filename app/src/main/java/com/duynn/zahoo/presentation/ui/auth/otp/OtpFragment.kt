package com.duynn.zahoo.presentation.ui.auth.otp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.navigation.fragment.findNavController
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.duynn.zahoo.R
import com.duynn.zahoo.databinding.FragmentOtpBinding
import com.duynn.zahoo.presentation.base.BaseFragment
import com.duynn.zahoo.presentation.ui.auth.otp.OtpContract.SingleEvent
import com.duynn.zahoo.presentation.ui.auth.otp.OtpContract.ViewIntent
import com.duynn.zahoo.presentation.ui.auth.otp.OtpContract.ViewState
import com.duynn.zahoo.presentation.ui.main.MainActivity
import com.duynn.zahoo.utils.extension.clicks
import com.duynn.zahoo.utils.extension.firstChange
import com.duynn.zahoo.utils.extension.getMessage
import com.duynn.zahoo.utils.extension.snack
import com.duynn.zahoo.utils.extension.textChanges
import com.duynn.zahoo.utils.extension.viewBinding
import com.duynn.zahoo.utils.types.ValidateErrorType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.androidx.viewmodel.scope.emptyState
import timber.log.Timber

/**
 * Created by duynn100198 on 10/5/21.
 */
@ExperimentalCoroutinesApi
@FlowPreview
class OtpFragment :
    BaseFragment<ViewIntent, ViewState, SingleEvent, OtpViewModel>(R.layout.fragment_otp) {
    override val viewModel by viewModel<OtpViewModel>(state = emptyState())
    override val viewBinding by viewBinding(FragmentOtpBinding::bind)

    override fun setUpView(view: View, savedInstanceState: Bundle?): Unit = with(viewBinding) {
        val state = viewModel.viewState.value
        codeOtpView.setText(state.otp)
    }

    override fun handleSingleEvent(event: SingleEvent) {
        return when (event) {
            is SingleEvent.OtpSuccess -> {
                viewBinding.root.snack("Verify OTP Success").show()
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            }
            is SingleEvent.OtpFailure -> {
                viewBinding.root.snack("Verify OTP Failure ${event.throwable.getMessage()}").show()
            }
            SingleEvent.OtpChangeNumber -> {
            }
            SingleEvent.OtpResend -> {
            }
            SingleEvent.OtpBack -> {
                Timber.d("Back Back")
                findNavController().popBackStack()
                Unit
            }
        }
    }

    override fun render(viewState: ViewState) {
        val otpErrorMessage =
            if (ValidateErrorType.ValidationError.INVALID_OTP in viewState.errors) {
                "Least number character is 6!"
            } else null

        if (viewState.otpChanged && viewBinding.errorText.text != otpErrorMessage) {
            viewBinding.errorText.text = otpErrorMessage
        }

        TransitionManager.beginDelayedTransition(
            viewBinding.root,
            AutoTransition()
                .addTarget(viewBinding.progressBar)
                .addTarget(viewBinding.submitButton)
                .setDuration(200)
        )
        viewBinding.progressBar.isInvisible = !viewState.isLoading
        viewBinding.submitButton.isInvisible = viewState.isLoading
    }

    override fun viewIntents(): Flow<ViewIntent> = viewBinding.run {
        merge(
            codeOtpView
                .textChanges()
                .map { ViewIntent.OtpChanged(it?.toString()) },
            submitButton
                .clicks()
                .map { ViewIntent.Submit },
            resendText
                .clicks()
                .map { ViewIntent.Resend },
            changeNumberText
                .clicks()
                .map { ViewIntent.ChangeNumber },
            codeOtpView
                .firstChange()
                .map { ViewIntent.OtpChangedFirstTime },
            backImage
                .clicks()
                .map { ViewIntent.OtpBack }
        )
    }
}
