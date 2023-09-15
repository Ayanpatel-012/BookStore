package com.dailyrounds.bookstore.Daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dailyrounds.bookstore.Entities.CountryEntity
import com.dailyrounds.bookstore.Utils.Constants

@Dao
interface CountryDao {
    @Insert
    suspend fun insert(country: CountryEntity)

    @Query(Constants.GET_COUNTRIES_QUERY)
    suspend fun getCountries(): List<CountryEntity>
}