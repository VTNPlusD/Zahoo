package com.duynn.zahoo.presentation.ui.auth.otp

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
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
import com.duynn.zahoo.utils.dialog.showAlertDialogInfo
import com.duynn.zahoo.utils.extension.backPresses
import com.duynn.zahoo.utils.extension.clicks
import com.duynn.zahoo.utils.extension.completions
import com.duynn.zahoo.utils.extension.firstChange
import com.duynn.zahoo.utils.extension.flatMapFirst
import com.duynn.zahoo.utils.extension.getMessage
import com.duynn.zahoo.utils.extension.observe
import com.duynn.zahoo.utils.extension.snack
import com.duynn.zahoo.utils.extension.textChanges
import com.duynn.zahoo.utils.extension.viewBinding
import com.duynn.zahoo.utils.types.ValidateErrorType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
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
    private val countDownTimer = object : CountDownTimer(60000, 1000) {
        override fun onTick(l: Long) {
            viewBinding.resendText.text = (l / 1000).toString()
        }

        override fun onFinish() {
            onHideSoftKeyBoard()
            viewBinding.resendText.text = getString(R.string.resend)
            viewBinding.resendText.isClickable = true
        }
    }

    override fun setUpView(view: View, savedInstanceState: Bundle?): Unit = with(viewBinding) {
        val state = viewModel.viewState.value
        codeOtpView.setText(state.otp)
        bindVM()
    }

    private fun bindVM() {
        viewModel.eventGoBack.observe(this) {
            findNavController().popBackStack()
        }
        viewModel.token.observe(this) {
            viewBinding.infoText.text = String.format(getString(R.string.otp_sent), it)
        }
    }

    override fun handleSingleEvent(event: SingleEvent) {
        when (event) {
            is SingleEvent.OtpSuccess -> {
                countDownTimer.cancel()
                onHideSoftKeyBoard()
                val intent = Intent(requireContext(), MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                }
                startActivity(intent)
                requireActivity().finish()
            }
            is SingleEvent.OtpFailure -> {
                viewBinding.codeOtpView.setText("")
                Timber.d(event.throwable.getMessage())
                viewBinding.root.snack(event.throwable.getMessage()).show()
                cancelCountdown()
            }
            is SingleEvent.OtpStartCountdown -> {
                startCountdown()
            }
            is SingleEvent.OnVerificationFailed -> {
                viewBinding.codeOtpView.setText("")
                onHideSoftKeyBoard()
                Timber.d(event.throwable.getMessage())
                viewBinding.root.snack(event.throwable.getMessage()).show()
            }
        }
    }

    override fun render(viewState: ViewState) = with(viewBinding) {
        Timber.d("viewState $viewState")
        val otpErrorMessage =
            if (ValidateErrorType.ValidationError.INVALID_OTP in viewState.errors) {
                "Least number character is 6!"
            } else null

        if (viewState.otpChanged && errorText.text != otpErrorMessage) {
            errorText.text = otpErrorMessage
        }
        TransitionManager.beginDelayedTransition(
            root,
            AutoTransition()
                .addTarget(viewBinding.progressBar)
                .addTarget(viewBinding.submitButton)
                .setDuration(200)
        )
        progressBar.isInvisible = !viewState.isLoading
        submitButton.isInvisible = viewState.isLoading
    }

    override fun viewIntents(): Flow<ViewIntent> = viewBinding.run {
        merge(
            codeOtpView
                .textChanges()
                .map { ViewIntent.OtpChanged(it?.toString()) },
            submitButton
                .clicks()
                .onEach { cancelCountdown() }
                .map { ViewIntent.Submit(requireActivity()) },
            resendText
                .clicks()
                .map { ViewIntent.VerifyPhoneNumber(requireActivity()) },
            changeNumberText
                .clicks()
                .onEach { countDownTimer.cancel() }
                .map { ViewIntent.ChangeNumber },
            codeOtpView
                .firstChange()
                .map { ViewIntent.OtpChangedFirstTime },
            backImage
                .clicks()
                .flatMapFirst { showSignOutDialog() }
                .map { countDownTimer.cancel() }
                .map { ViewIntent.OtpBack },
            codeOtpView
                .completions()
                .onEach { cancelCountdown() }
                .map { ViewIntent.Submit(requireActivity()) },
            requireActivity().onBackPressedDispatcher.backPresses(this@OtpFragment)
                .flatMapFirst { showSignOutDialog() }
                .map { countDownTimer.cancel() }
                .map { ViewIntent.OtpBack },
            flow { emit(ViewIntent.VerifyPhoneNumber(requireActivity())) }.take(1)
        )
    }

    private fun showSignOutDialog() =
        requireActivity().showAlertDialogInfo {
            title(getString(R.string.verification_cancel_title))
            message(getString(R.string.verification_cancel_message))
            cancelable(true)
        }

    private fun startCountdown() {
        viewBinding.resendText.apply {
            isClickable = false
        }
        countDownTimer.start()
    }

    private fun cancelCountdown() {
        onHideSoftKeyBoard()
        viewBinding.resendText.apply {
            text = getString(R.string.resend)
            isClickable = true
        }
        countDownTimer.cancel()
    }
}
