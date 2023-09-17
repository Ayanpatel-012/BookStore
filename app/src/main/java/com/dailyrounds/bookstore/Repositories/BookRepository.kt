package com.dailyrounds.bookstore.Repositories

import com.dailyrounds.bookstore.Daos.BooksDao
import com.dailyrounds.bookstore.Entities.BookEntity
import com.dailyrounds.bookstore.Models.Book

class BookRepository(private val booksDao: BooksDao) {
    suspend fun saveBooks(bookList: List<Book>) {
        bookList.map {
            BookEntity(
                id = it.id,
                alias = it.alias,
                hits = it.hits,
                image = it.image,
                lastChapterDate = it.lastChapterDate,
                title = it.title,
                fav = it.fav,
            )
        }.forEach {
            try {
                booksDao.insert(it)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getBooks(): List<Book> {
        try {

            return booksDao.getBooks().map {
                Book(
                    id = it.id,
                    alias = it.alias,
                    hits = it.hits,
                    image = it.image,
                    lastChapterDate = it.lastChapterDate,
                    title = it.title,
                    fav = it.fav
                )
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun updateBook(bookId: String, newValue: Boolean) {
        try {
            booksDao.update(bookId, newValue)
        } catch (e: Exception) {
            throw e
        }
    }
}