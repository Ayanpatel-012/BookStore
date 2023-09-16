package com.dailyrounds.bookstore.Daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dailyrounds.bookstore.Entities.BookEntity
import com.dailyrounds.bookstore.Utils.Constants

@Dao
interface BooksDao {
    @Insert
    suspend fun insert(book: BookEntity)
    @Query(Constants.GET_BOOKS_QUERY)
    suspend fun getBooks(): List<BookEntity>
}