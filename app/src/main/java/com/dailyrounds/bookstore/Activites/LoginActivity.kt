package com.dailyrounds.bookstore.Activites

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dailyrounds.bookstore.Database.BookStoreDatabase
import com.dailyrounds.bookstore.Fragments.LoginFragment
import com.dailyrounds.bookstore.Fragments.RegisterFragment
import com.dailyrounds.bookstore.Models.BookList
import com.dailyrounds.bookstore.Models.CountryList
import com.dailyrounds.bookstore.R
import com.dailyrounds.bookstore.Repositories.BookRepository
import com.dailyrounds.bookstore.Repositories.UserRepository
import com.dailyrounds.bookstore.Utils.Constants
import com.dailyrounds.bookstore.ViewModels.LoginViewModel
import com.dailyrounds.bookstore.ViewModels.LoginViewModelFactory
import com.dailyrounds.bookstore.databinding.ActivityLoginBinding
import com.dailyrounds.bookstore.enums.LoginStatus
import com.dailyrounds.bookstore.enums.RegistrationStatus
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

class LoginActivity : AppCompatActivity() {
    lateinit var sharedPrefs: SharedPreferences
    lateinit var database: BookStoreDatabase
    lateinit var viewModel: LoginViewModel
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSharedPreference()
        updateUI()
        initBinding()
        initDatabase()
        initViewModel()
        initObservers()
        initListeners()
    }
/*
This is the function which checks whether the user is logged in or not if yes it opens the books activity
 */
    private fun updateUI() {
       if(sharedPrefs.getBoolean(Constants.LOGGED_IN_STATUS,false)){
           startActivity(Intent(this,BooksActivity::class.java))
           finish()
       }
    }

    private fun initListeners() {
        binding.apply {
            loginBtn.setOnClickListener {
                this@LoginActivity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, LoginFragment.newInstance())
                    .setReorderingAllowed(true).addToBackStack(null).commit()
            }
            Rgtrbtn.setOnClickListener {
                this@LoginActivity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, RegisterFragment.newInstance())
                    .setReorderingAllowed(true).addToBackStack(null).commit()
            }
        }
    }

    private fun initBinding() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setActionBar(null)
    }

    private fun initObservers() {
        viewModel.countryLiveData.observe(this) {
        }
        viewModel.registrationStatus.observe(this) {
            when (it) {
                RegistrationStatus.REGISTERED -> {
                    sharedPrefs.edit().putBoolean(Constants.LOGGED_IN_STATUS,true).commit()
                    startActivity(Intent(this, BooksActivity::class.java))
                    finish()
                }

                else -> {}
            }
        }
        viewModel.loginStatus.observe(this) {
            when (it) {
                LoginStatus.CANLOGIN -> {
                    sharedPrefs.edit().putBoolean(Constants.LOGGED_IN_STATUS,true).commit()
                    startActivity(Intent(this, BooksActivity::class.java))
                    finish()
                }

                else -> {}
            }
        }


    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this, LoginViewModelFactory(
                UserRepository(database.UserDao(), database.CountryDao()),
                BookRepository(database.BooksDao())
            )
        )[LoginViewModel::class.java]

        lifecycleScope.launch(Dispatchers.IO) {
            if (!sharedPrefs.contains(Constants.DATABASE_INIT)) {
                val bookJson = ReadJSONFromAssets(Constants.ASSETS_PATH_BOOKS)
                val countryJson = ReadJSONFromAssets(Constants.ASSETS_PATH_COUNTRY)
                val booksData = Gson().fromJson(bookJson, BookList::class.java)
                val countryData = Gson().fromJson(countryJson, CountryList::class.java)
                viewModel.apply {
                    saveBooks(booksData)
                    saveCountries(countryData)
                }
                sharedPrefs.edit().putBoolean(Constants.DATABASE_INIT, true).commit()

            }

        }
    }

    private fun initSharedPreference() {
        sharedPrefs = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    /*
    this function will initialise the Database with books
    and country data available in json format from assets
    only the first time app is installed
     */
    private fun initDatabase() {
        database = BookStoreDatabase.getDatabase(this)
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