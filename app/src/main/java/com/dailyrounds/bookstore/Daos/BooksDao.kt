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
    @Query(Constants.UPDATE_BOOK_QUERY)
    suspend fun update(id:String,newValue:Boolean)
    @Query(Constants.GET_BOOKSBYID_QUERY)
    suspend fun getBookById(id:String):BookEntity
}