package com.tasheel.finance.core.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

// Repositories use @Inject constructor and @Singleton — no explicit bindings needed.
// This module is reserved for future interface-to-impl bindings.
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule
