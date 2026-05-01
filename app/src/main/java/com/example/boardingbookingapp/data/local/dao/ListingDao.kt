package com.example.birdnest.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.birdnest.data.local.entity.ListingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ListingDao {

    @Query("SELECT * FROM listings ORDER BY createdAt DESC")
    fun getAll(): Flow<List<ListingEntity>>

    @Query("""
        SELECT * FROM listings
        WHERE title LIKE '%' || :query || '%'
           OR address LIKE '%' || :query || '%'
        ORDER BY createdAt DESC
    """)
    fun search(query: String): Flow<List<ListingEntity>>

    @Query("""
        SELECT * FROM listings
        WHERE (:roomType   IS NULL OR roomType   = :roomType)
          AND (:genderPolicy IS NULL OR genderPolicy = :genderPolicy)
          AND pricePerMonth <= :maxPrice
        ORDER BY createdAt DESC
    """)
    fun filterBy(maxPrice: Int, roomType: String?, genderPolicy: String?): Flow<List<ListingEntity>>

    @Upsert
    suspend fun upsertAll(listings: List<ListingEntity>)

    @Query("DELETE FROM listings")
    suspend fun deleteAll()
}
