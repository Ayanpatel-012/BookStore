package com.dailyrounds.bookstore.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dailyrounds.bookstore.Utils.Constants

@Entity(tableName = Constants.COUNTRY_TABLE)
class CountryEntity(
    @PrimaryKey(autoGenerate = false) var CountryCode: String,
    @ColumnInfo(name = "CountryName") var CountryName: String,
)