package com.dailyrounds.bookstore.Daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dailyrounds.bookstore.Entities.UserEntity
import com.dailyrounds.bookstore.Utils.Constants

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user:UserEntity)
    @Query(Constants.GET_USERS_QUERY)
    suspend fun getUsers():List<UserEntity>
}