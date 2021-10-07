package com.duynn.zahoo.utils.types

import android.util.Patterns
import androidx.core.util.PatternsCompat
import timber.log.Timber

/**
 *Created by duynn100198 on 10/04/21.
 */
object ValidateErrorType {
    private const val LEAST_NUMBER_CHARACTER = 6

    enum class ValidationError {
        INVALID_PHONE_NUMBER,
        INVALID_EMAIL_ADDRESS,
        INVALID_PASSWORD,
        INVALID_OTP,
    }

    fun validateEmail(email: String?): Set<ValidationError> {
        val errors = mutableSetOf<ValidationError>()

        if (email == null || !PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            errors += ValidationError.INVALID_EMAIL_ADDRESS
        }

        // more validation here

        return errors
    }

    fun validatePassword(password: String?): Set<ValidationError> {
        val errors = mutableSetOf<ValidationError>()

        if (password == null || password != "123456") {
            errors += ValidationError.INVALID_PASSWORD
        }

        // more validation here

        return errors
    }

    fun validateOtp(otp: String?): Set<ValidationError> {
        val errors = mutableSetOf<ValidationError>()

        otp?.let {
            if (it.length < LEAST_NUMBER_CHARACTER) {
                errors += ValidationError.INVALID_OTP
            }
        } ?: run { errors += ValidationError.INVALID_OTP }
        // more validation here

        return errors
    }

    fun validatePhone(phone: String?, dialCode: String?): Set<ValidationError> {
        Timber.d("phoneNumber - phone: $phone")
        val errors = mutableSetOf<ValidationError>()
        phone?.let {
            val phoneNumber = "${dialCode ?: ""}${it.replace("+", "")}"
            Timber.d("phoneNumber: $phoneNumber")
            if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
                errors += ValidationError.INVALID_PHONE_NUMBER
            }
        } ?: run { errors += ValidationError.INVALID_PHONE_NUMBER }
        // more validation here
        return errors
    }
}
