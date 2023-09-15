package com.dailyrounds.bookstore.Activites

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.dailyrounds.bookstore.Database.BookStoreDatabase
import com.dailyrounds.bookstore.Models.BookList
import com.dailyrounds.bookstore.Models.CountryList
import com.dailyrounds.bookstore.R
import com.dailyrounds.bookstore.Utils.Constants
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

class LoginActivity : AppCompatActivity() {
    lateinit var  sharedPrefs:SharedPreferences
    lateinit var database:BookStoreDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initSharedPreference()
        initDatabase()
    }

    private fun initSharedPreference() {
        sharedPrefs=getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

/*
this function will initialise the Database with books
and country data available in json format from assets
only the first time app is installed
 */
    private fun initDatabase() {
        database= BookStoreDatabase.getDatabase(this)
    lifecycleScope.launch(Dispatchers.IO){
        if(!sharedPrefs.contains(Constants.DATABASE_INIT)){
            val bookJson= ReadJSONFromAssets(Constants.ASSETS_PATH_BOOKS)
            val countryJson=ReadJSONFromAssets(Constants.ASSETS_PATH_COUNTRY)
            val booksData= Gson().fromJson(bookJson, BookList::class.java)
            val countryData=Gson().fromJson(countryJson,CountryList::class.java)
            sharedPrefs.edit().putBoolean(Constants.DATABASE_INIT,true).commit()

        }
    }
    }
    private fun ReadJSONFromAssets(path: String): String {
        try {
            val file = this.assets.open("$path")
            val bufferedReader = BufferedReader(InputStreamReader(file))
            val stringBuilder = StringBuilder()
            bufferedReader.useLines { lines ->
                lines.forEach {
                    stringBuilder.append(it)
                }
            }
            return stringBuilder.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

}