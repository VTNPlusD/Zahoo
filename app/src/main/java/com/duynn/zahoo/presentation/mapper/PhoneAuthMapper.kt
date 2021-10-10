package com.duynn.zahoo.presentation.mapper

import com.duynn.zahoo.data.model.PhoneAuthData
import com.duynn.zahoo.domain.entity.PhoneAuth

/**
 * Created by duynn100198 on 10/10/21.
 */
class PhoneAuthMapper : BaseMapper<PhoneAuthData, PhoneAuth>() {
    override fun map(data: PhoneAuthData): PhoneAuth {
        return when (data) {
            is PhoneAuthData.VerificationCompleted -> PhoneAuth.VerificationCompleted(data.credential)
            is PhoneAuthData.CodeSent -> PhoneAuth.CodeSent(data.verificationId, data.token)
        }
    }
}
