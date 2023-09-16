package com.dailyrounds.bookstore.Utils

 class Constants{
     companion object{
         //Table Names
         const val DATABASE_NAME="BookStoreDB"
         const val BOOK_TABLE="BookTable"
         const val USER_TABLE="UserTable"
         const val COUNTRY_TABLE="CountryTable"

         //Queries
         const val UPDATE_BOOK_QUERY="UPDATE $BOOK_TABLE SET fav = :newValue WHERE id= :id"
         const val GET_BOOKS_QUERY="SELECT * FROM $BOOK_TABLE"
         const val GET_USERS_QUERY="SELECT * FROM $USER_TABLE"
         const val GET_COUNTRIES_QUERY="SELECT * FROM $COUNTRY_TABLE"

         //Shared Preference keys
         const val SHARED_PREF_NAME="MySharedPref"
         const val DATABASE_INIT="DatabaseIntialized"
         const val LOGGED_IN_STATUS="loggedInStatus"

         //Misllaneous
         const val ASSETS_PATH_BOOKS="BooksData.json"
         const val ASSETS_PATH_COUNTRY="CountryData.json"



     }
 }