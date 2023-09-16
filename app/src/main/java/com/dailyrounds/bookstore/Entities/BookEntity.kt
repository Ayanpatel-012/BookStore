package com.dailyrounds.bookstore.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dailyrounds.bookstore.Utils.Constants

@Entity(tableName = Constants.BOOK_TABLE)
class BookEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    @ColumnInfo(name = "alias")
    val alias: String,
    @ColumnInfo(name = "hits")
    var hits: Int,
    @ColumnInfo(name = "image")
    var image: String,
    @ColumnInfo(name = "lastChapterDate")
    var lastChapterDate: Int,
    @ColumnInfo(name = "title")
    var title: String,
)
