package com.dailyrounds.bookstore.Utils

class Constants {
    companion object {
        //Table Names
        const val DATABASE_NAME = "BookStoreDB"
        const val BOOK_TABLE = "BookTable"
        const val USER_TABLE = "UserTable"
        const val COUNTRY_TABLE = "CountryTable"

        //Queries
        const val GET_BOOKS_QUERY = "SELECT * FROM $BOOK_TABLE"
        const val GET_BOOKSBYID_QUERY = "SELECT * FROM $BOOK_TABLE WHERE id =:id"
        const val GET_USERBYID_QUERY = "SELECT * FROM $USER_TABLE WHERE userId =:id"
        const val GET_COUNTRIES_QUERY = "SELECT * FROM $COUNTRY_TABLE"
        const val UPDATE_BOOK_QUERY = "UPDATE $BOOK_TABLE SET fav = :newValue WHERE id= :id"

        //Shared Preference keys
        const val SHARED_PREF_NAME = "MySharedPref"
        const val DATABASE_INIT = "DatabaseIntialized"
        const val LOGGED_IN_STATUS = "loggedInStatus"
        const val LOGGED_IN_USERID = "LoggedInUser"

        //Misllaneous
        const val ASSETS_PATH_BOOKS = "BooksData.json"
        const val ASSETS_PATH_COUNTRY = "CountryData.json"
        const val BOOK_ID = "bookId"
        const val BOOK_TITLE = "bookTitle"
        const val BOOK_SUBTITLE = "bookSubTitle"
        const val BOOK_HITS = "bookHits"
        const val BOOK_IS_FAV = "bookIsFav"
        const val BOOK_IMG = "bookImg"
        const val BOOK_BUNDLE = "bookBundle"
        const val SORTING_ALGO = "SortingBy"
        const val BY_DEFAULT = 0
        const val BY_TITLE = 1
        const val BY_HITS = 2
        const val BY_FAV = 3


    }
}