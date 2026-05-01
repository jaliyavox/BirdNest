package com.example.boardingbookingapp.di

import android.content.Context
import androidx.room.Room
import com.example.boardingbookingapp.data.local.AppDatabase
import com.example.boardingbookingapp.data.local.dao.ChatDao
import com.example.boardingbookingapp.data.local.dao.ListingDao
import com.example.boardingbookingapp.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "birdnest_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideListingDao(db: AppDatabase): ListingDao = db.listingDao()
    @Provides fun provideUserDao(db: AppDatabase): UserDao = db.userDao()
    @Provides fun provideChatDao(db: AppDatabase): ChatDao = db.chatDao()
}
