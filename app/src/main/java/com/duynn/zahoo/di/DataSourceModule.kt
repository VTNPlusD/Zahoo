package com.duynn.zahoo.di

import android.content.ContentResolver
import android.content.Context
import com.duynn.zahoo.data.repository.source.UserDataSource
import com.duynn.zahoo.data.repository.source.local.UserLocalDataSourceImpl
import com.duynn.zahoo.data.repository.source.local.api.SharedPrefApi
import com.duynn.zahoo.data.repository.source.local.api.pref.SharedPrefApiImpl
import com.duynn.zahoo.data.repository.source.remote.UserRemoteDataSourceImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

// private fun appDatabase(context: Context): DatabaseManager =
//    Room.databaseBuilder(context, DatabaseManager::class.java, DatabaseConfig.DATABASE_NAME)
//        .addMigrations(MigrationManager.MIGRATION_1_2).build()

private fun contentResolver(context: Context): ContentResolver = context.contentResolver

/**
 *Created by duynn100198 on 10/04/21.
 */
@ExperimentalCoroutinesApi
@OptIn(KoinApiExtension::class)
val dataSourceModule = module {
    /**
     * Local setting module
     */
    single { contentResolver(context = get()) }
    single<SharedPrefApi> {
        SharedPrefApiImpl(context = get(), moshi = get())
    }
    single { Firebase.auth }
    single { Firebase.database }
    //    single { appDatabase(context = get()) }
    //    single<DatabaseApi> {
    //        DatabaseApiImpl(databaseManager = get())
    //    }
    /**
     * Data source module
     */

    single<UserDataSource.Local> {
        UserLocalDataSourceImpl(
//            databaseApi = get(),
            sharedPrefApi = get(),
            application = get(),
            moshi = get()
        )
    }
    single<UserDataSource.Remote> {
        UserRemoteDataSourceImpl(
            apiService = get(),
            application = get(),
            firebaseAuth = get(),
            firebaseDatabase = get()
        )
    }
}
