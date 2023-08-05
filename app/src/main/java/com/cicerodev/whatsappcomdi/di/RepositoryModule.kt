package com.cicerodev.whatsappcomdi.di

import com.cicerodev.whatsappcomdi.data.repository.FirebaseRepository
import com.cicerodev.whatsappcomdi.data.repository.FirebaseRepositoryImp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideFirebaseRepository(
        auth: FirebaseAuth,
        database: FirebaseDatabase,
        storage: FirebaseStorage
    ): FirebaseRepository {
        return FirebaseRepositoryImp(auth, database, storage)
    }
}