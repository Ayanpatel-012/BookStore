package com.dailyrounds.bookstore.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dailyrounds.bookstore.Utils.Constants

@Entity(tableName = Constants.USER_TABLE)
class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val userId: String,
    @ColumnInfo(name = "password")
    val password: String,
    @ColumnInfo(name = "country")
    val country: String
)