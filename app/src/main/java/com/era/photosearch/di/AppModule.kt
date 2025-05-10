package com.era.photosearch.di

import android.app.Application
import androidx.room.Room
import com.era.photosearch.BuildConfig
import com.era.photosearch.data.local.ERADatabase
import com.era.photosearch.data.local.SearchQueryDao
import com.era.photosearch.data.remote.ApiService
import com.era.photosearch.data.remote.AuthInterceptor
import com.era.photosearch.data.repository.PhotoRepositoryImpl
import com.era.photosearch.data.repository.SearchRepositoryImpl
import com.era.photosearch.domain.repository.PhotoRepository
import com.era.photosearch.domain.repository.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideApiKey(): String {
        return NativeLib().getApiKey()
    }

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
    ): ERADatabase =
        Room.databaseBuilder(app, ERADatabase::class.java, BuildConfig.DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideSearchQueryDao(db: ERADatabase): SearchQueryDao = db.searchQueryDao()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .baseUrl(BuildConfig.BASE_URL)
        .build()

    @Provides
    fun provideAuthInterceptor(apiKey: String): AuthInterceptor = AuthInterceptor(apiKey)

    @Provides
    @Singleton
    fun provideHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient = OkHttpClient.Builder().run {
        addInterceptor(authInterceptor)
        if (BuildConfig.DEBUG) {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
        build()
    }

    @Provides
    @Singleton
    fun providePhotoRepository(apiService: ApiService): PhotoRepository =
        PhotoRepositoryImpl(apiService)

    @Provides
    @Singleton
    fun provideSearchRepository(searchQueryDao: SearchQueryDao): SearchRepository =
        SearchRepositoryImpl(searchQueryDao)

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @IODispatcher
    @Provides
    @Singleton
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @DefaultDispatcher
    @Provides
    @Singleton
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class IODispatcher

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DefaultDispatcher
}