package com.duynn.zahoo.data.repository.source.remote

import android.app.Activity
import android.app.Application
import com.duynn.zahoo.data.model.PhoneAuthData
import com.duynn.zahoo.data.model.UserData
import com.duynn.zahoo.data.repository.source.UserDataSource
import com.duynn.zahoo.data.repository.source.remote.api.ApiService
import com.duynn.zahoo.data.repository.source.remote.api.firebase.FirebaseRef.REF_NEW_USER
import com.duynn.zahoo.data.repository.source.remote.api.firebase.FirebaseRef.REF_USERS
import com.duynn.zahoo.domain.scheduler.AppDispatchers.IO
import com.duynn.zahoo.domain.scheduler.DispatchersProvider
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 *Created by duynn100198 on 10/04/21.
 */
@KoinApiExtension
class UserRemoteDataSourceImpl(
    private val apiService: ApiService,
    private val application: Application,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) : UserDataSource.Remote, KoinComponent {
    private val dispatchersProvider = get<DispatchersProvider>(named(IO))
    private val userRef = firebaseDatabase.getReference(REF_USERS)
    private val newUserRef = firebaseDatabase.getReference(REF_NEW_USER)

    @ExperimentalCoroutinesApi
    override suspend fun getUser(phoneNumber: String): DataSnapshot =
        suspendCancellableCoroutine {
            userRef.child(phoneNumber).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    it.resume(snapshot)
                }

                override fun onCancelled(error: DatabaseError) {
                    it.resumeWithException(error.toException())
                }
            })
        }

    override suspend fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential): Unit =
        suspendCancellableCoroutine { continuation ->
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) continuation.resume(Unit)
                    else continuation.resumeWithException(task.exception!!)
                }
        }

    override suspend fun verifyPhoneNumber(token: String, activity: Activity): PhoneAuthData =
        suspendCancellableCoroutine {
            val callback =
                object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        Timber.i("onVerificationCompleted: $credential")
                        it.resume(PhoneAuthData.VerificationCompleted(credential))
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        Timber.i(e, "onVerificationFailed:${e.message}")
                        it.resumeWithException(e)
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        Timber.i("onCodeSent: $verificationId")
                        it.resume(PhoneAuthData.CodeSent(verificationId, token))
                    }
                }
            val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(token)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(callback)
                .setActivity(activity)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }

    override suspend fun createUser(userData: UserData) =
        suspendCancellableCoroutine<Unit> { continuation ->
            userRef.child(userData.id).setValue(userData)
                .addOnSuccessListener {
                    newUserRef.setValue(userData)
                    continuation.resume(Unit)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
}
