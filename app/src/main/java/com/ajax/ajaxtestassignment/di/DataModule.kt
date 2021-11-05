package com.ajax.ajaxtestassignment.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton
import java.util.concurrent.TimeUnit
import com.ajax.ajaxtestassignment.api.contacts.ContactsService
import com.ajax.ajaxtestassignment.db.AppDatabase
import com.ajax.ajaxtestassignment.db.contacts.ContactsDao
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
class DataModule {

    @Provides
    @Singleton
    internal fun provideRoomDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "Contact"
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    internal fun provideAPIContactsService(retrofit: Retrofit): ContactsService =
        retrofit.create(ContactsService::class.java)


    @Provides
    @Singleton
    internal fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://randomuser.me/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    internal fun provideHttpCache(context: Context): Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(context.cacheDir, cacheSize.toLong())
    }

    @Provides
    @Singleton
    internal fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("SHARED_PREFS_NAME", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    internal fun provideOkhttpClientWithLogging(
        cache: Cache,
        context: Context
    ): OkHttpClient {
        val client = OkHttpClient.Builder()

        with(client) {
            connectTimeout(1, TimeUnit.MINUTES)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(15, TimeUnit.SECONDS)
        }

        return client.cache(cache).build()
    }

    @Provides
    @Singleton
    internal fun provideContactDao(db: AppDatabase): ContactsDao = db.userDao()
}
