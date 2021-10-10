package com.duynn.zahoo.domain.entity

import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

/**
 * Created by duynn100198 on 10/10/21.
 */
sealed class PhoneAuth : BaseEntity() {
    data class VerificationCompleted(val credential: PhoneAuthCredential) : PhoneAuth()
    data class CodeSent(
        val verificationId: String,
        val token: PhoneAuthProvider.ForceResendingToken
    ) : PhoneAuth()
}
