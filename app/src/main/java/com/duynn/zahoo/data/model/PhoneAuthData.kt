package com.duynn.zahoo.data.model

import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

/**
 * Created by duynn100198 on 10/9/21.
 */
sealed class PhoneAuthData : BaseData() {
    data class VerificationCompleted(val credential: PhoneAuthCredential) : PhoneAuthData()
    data class CodeSent(
        val verificationId: String,
        val token: PhoneAuthProvider.ForceResendingToken
    ) : PhoneAuthData()
}
