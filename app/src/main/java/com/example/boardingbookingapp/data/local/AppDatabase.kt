package com.example.boardingbookingapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.boardingbookingapp.data.local.dao.ChatDao
import com.example.boardingbookingapp.data.local.dao.ListingDao
import com.example.boardingbookingapp.data.local.dao.UserDao
import com.example.boardingbookingapp.data.local.entity.ChatMessageEntity
import com.example.boardingbookingapp.data.local.entity.ListingEntity
import com.example.boardingbookingapp.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, ListingEntity::class, ChatMessageEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun listingDao(): ListingDao
    abstract fun chatDao(): ChatDao
}
