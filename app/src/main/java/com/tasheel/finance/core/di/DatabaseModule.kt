package com.tasheel.finance.core.di

import android.content.Context
import androidx.room.Room
import com.tasheel.finance.data.db.AppDatabase
import com.tasheel.finance.data.db.ApplicationDraftDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "tasheel_db").build()

    @Provides
    fun provideDraftDao(db: AppDatabase): ApplicationDraftDao = db.draftDao()
}
