package com.duynn.zahoo.presentation.ui.auth.login

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.isInvisible
import androidx.navigation.fragment.findNavController
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.duynn.zahoo.R
import com.duynn.zahoo.databinding.FragmentLoginBinding
import com.duynn.zahoo.domain.entity.Country
import com.duynn.zahoo.presentation.base.BaseFragment
import com.duynn.zahoo.presentation.ui.auth.login.LoginContract.SingleEvent
import com.duynn.zahoo.presentation.ui.auth.login.LoginContract.ViewIntent
import com.duynn.zahoo.presentation.ui.auth.login.LoginContract.ViewState
import com.duynn.zahoo.utils.extension.clicks
import com.duynn.zahoo.utils.extension.firstChange
import com.duynn.zahoo.utils.extension.getMessage
import com.duynn.zahoo.utils.extension.itemSelections
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
import kotlinx.coroutines.flow.take
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.androidx.viewmodel.scope.emptyState

/**
 *Created by duynn100198 on 10/04/21.
 */
@ExperimentalCoroutinesApi
@FlowPreview
class LoginFragment :
    BaseFragment<ViewIntent, ViewState, SingleEvent, LoginViewModel>(R.layout.fragment_login) {
    override val viewModel by viewModel<LoginViewModel>(state = emptyState())
    override val viewBinding by viewBinding(FragmentLoginBinding::bind)

    override fun setUpView(view: View, savedInstanceState: Bundle?): Unit = with(viewBinding) {
        phoneNumberEdit.setText("")
        bindVM()
    }

    private fun bindVM() {
        viewModel.tokenEvent.observe(this) {
            if (it) findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToOtpFragment())
        }
    }

    override fun handleSingleEvent(event: SingleEvent) {
        return when (event) {
            is SingleEvent.LoginSuccess -> {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToOtpFragment())
            }
            is SingleEvent.LoginFailure -> {
                viewBinding.root.snack("Login Failure ${event.throwable.getMessage()}").show()
            }
            is SingleEvent.InitCountries -> {
                viewBinding.countryCodeSpinner.adapter = ArrayAdapter(
                    requireContext(),
                    R.layout.item_country_spinner,
                    event.countries
                )
                viewBinding.countryCodeSpinner.setSelection(viewModel.viewState.value.countries.indexOf(
                    viewModel.viewState.value.country))
            }
        }
    }

    override fun render(viewState: ViewState) {
        val phoneErrorMessage =
            if (ValidateErrorType.ValidationError.INVALID_PHONE_NUMBER in viewState.errors) {
                getString(R.string.validation_req_phone_valid)
            } else null

        if (viewState.phoneChanged && viewBinding.errorText.text != phoneErrorMessage) {
            viewBinding.errorText.text = phoneErrorMessage
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
            phoneNumberEdit
                .textChanges()
                .map { ViewIntent.PhoneChanged(it?.toString()) },
            submitButton
                .clicks()
                .map { ViewIntent.Submit },
            phoneNumberEdit
                .firstChange()
                .map { ViewIntent.PhoneChangedFirstTime },
            countryCodeSpinner
                .itemSelections<Country>()
                .map { ViewIntent.CountryChanged(it) },
            flow { emit(ViewIntent.GetCountries) }.take(1)
        )
    }
}
