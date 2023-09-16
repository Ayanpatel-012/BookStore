package com.dailyrounds.bookstore.Activites

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dailyrounds.bookstore.Adaptors.Adaptor
import com.dailyrounds.bookstore.Adaptors.EventClickListener
import com.dailyrounds.bookstore.Database.BookStoreDatabase
import com.dailyrounds.bookstore.Models.Book
import com.dailyrounds.bookstore.Repositories.BookRepository
import com.dailyrounds.bookstore.Utils.Constants
import com.dailyrounds.bookstore.ViewModels.BookViewModel
import com.dailyrounds.bookstore.ViewModels.BookViewModelFactory
import com.dailyrounds.bookstore.databinding.ActivityBooksBinding

class BooksActivity : AppCompatActivity(), EventClickListener {
    lateinit var database: BookStoreDatabase
    lateinit var sharedPrefs: SharedPreferences
    private lateinit var viewmodel: BookViewModel
    private lateinit var binding: ActivityBooksBinding
    private var bookList = ArrayList<Book>()
    private var adaptor: Adaptor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSharedPreference()
        initBinding()
        initDatabase()
        initViewModel()
        initObservers()
        initEventListeners()
        setupRv()
    }

    private fun initDatabase() {
        database = BookStoreDatabase.getDatabase(this)
    }

    private fun initViewModel() {
        viewmodel = ViewModelProvider(
            this, BookViewModelFactory(BookRepository(database.BooksDao()))
        )[BookViewModel::class.java]
    }

    private fun initObservers() {
        viewmodel.booksLiveData.observe(this) {
            bookList.clear()
            bookList.addAll(it)
            adaptor?.notifyDataSetChanged()
        }
    }

    private fun setupRv() {
        adaptor = Adaptor(bookList, this)
        binding.rv.let {
            it.adapter = adaptor
            it.layoutManager = LinearLayoutManager(this)
        }
        viewmodel.getBooks()
    }

    private fun initSharedPreference() {
        sharedPrefs = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    private fun initEventListeners() {
        binding.logout.setOnClickListener {
            sharedPrefs.edit().putBoolean(Constants.LOGGED_IN_STATUS, false).commit()
            sharedPrefs.edit().putString(Constants.LOGGED_IN_USERID,"").commit()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun initBinding() {
        binding = ActivityBooksBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onFavClicked(id: String, newValue: Boolean) {

    }
}