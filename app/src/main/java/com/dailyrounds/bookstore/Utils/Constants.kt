package com.dailyrounds.bookstore.Utils

 class Constants{
     companion object{
         //Table Names
         const val DATABASE_NAME="BookStoreDB"
         const val BOOK_TABLE="BookTable"
         const val USER_TABLE="UserTable"

         //Queries
         const val UPDATE_BOOK_QUERY="UPDATE $BOOK_TABLE SET fav = :newValue WHERE id= :id"
         const val GET_BOOKS_QUERY="SELECT * FROM $BOOK_TABLE"
         const val GET_USERS_QUERY="SELECT * FROM $USER_TABLE"

         //Shared Preference keys

     }
 }