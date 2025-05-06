package com.era.photosearch.di

import android.app.Application
import androidx.room.Room
import com.era.photosearch.data.local.ERADatabase
import com.era.photosearch.data.local.SearchQueryDao
import com.era.photosearch.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
    ): ERADatabase =
        Room.databaseBuilder(app, ERADatabase::class.java, Constants.APP_DATABASE).build()

    @Provides
    @Singleton
    fun provideSearchQueryDao(db: ERADatabase): SearchQueryDao = db.searchQueryDao()
}