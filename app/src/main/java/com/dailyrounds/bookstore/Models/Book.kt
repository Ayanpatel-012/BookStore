package com.dailyrounds.bookstore.Models

data class Book(
    var id: String,
    var alias: String,
    var hits: Int,
    var image: String,
    var lastChapterDate: Int,
    var title: String,
    var fav: Boolean
)
