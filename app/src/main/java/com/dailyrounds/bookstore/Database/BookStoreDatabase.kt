package com.dailyrounds.bookstore.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dailyrounds.bookstore.Daos.BooksDao
import com.dailyrounds.bookstore.Entities.BookEntity
import com.dailyrounds.bookstore.Utils.Constants

@Database(entities = [BookEntity::class], version = 1, exportSchema = false)
abstract class BookStoreDatabase : RoomDatabase() {

    abstract fun BooksDao(): BooksDao

    companion object {
        @Volatile
        private var INSTANCE: BookStoreDatabase? = null
        fun getDatabase(context: Context): BookStoreDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookStoreDatabase::class.java,
                    Constants.DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}