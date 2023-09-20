package com.dailyrounds.bookstore.Activites

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dailyrounds.bookstore.Adaptors.Adaptor
import com.dailyrounds.bookstore.Adaptors.EventClickListener
import com.dailyrounds.bookstore.Database.BookStoreDatabase
import com.dailyrounds.bookstore.Models.Book
import com.dailyrounds.bookstore.Repositories.BookRepository
import com.dailyrounds.bookstore.Utils.Constants
import com.dailyrounds.bookstore.Utils.setActive
import com.dailyrounds.bookstore.Utils.setInactive
import com.dailyrounds.bookstore.ViewModels.BookViewModel
import com.dailyrounds.bookstore.ViewModels.BookViewModelFactory
import com.dailyrounds.bookstore.databinding.ActivityBooksBinding

class BooksActivity : AppCompatActivity(), EventClickListener {
    lateinit var database: BookStoreDatabase
    lateinit var sharedPrefs: SharedPreferences
    private lateinit var viewmodel: BookViewModel
    private lateinit var binding: ActivityBooksBinding
    private var bookList = ArrayList<Book>()
    private var sortedBooks = ArrayList<Book>()
    private var adaptor: Adaptor? = null
    private var sortedBy=0
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

    override fun onResume() {
        super.onResume()
        viewmodel.getBooks()
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
        viewmodel.updatedBookId.observe(this){
            sortedBooks.forEach {book ->
                if(book.id==it.first) book.fav=it.second
            }
            adaptor?.notifyDataSetChanged()
        }
        viewmodel.booksLiveData.observe(this) {
            bookList.clear()
            bookList.addAll(it)
            viewmodel.updateSortingAlgo(sharedPrefs.getInt(Constants.SORTING_ALGO,Constants.BY_DEFAULT))
        }
        viewmodel.sortingAlgo.observe(this) {
            sharedPrefs.edit().putInt(Constants.SORTING_ALGO, it).commit()
            sortedBy=it
            when (it) {
                Constants.BY_TITLE -> {
                    binding.apply {
                        sortTitle.setActive(this@BooksActivity)
                        sortFavs.setInactive(this@BooksActivity)
                        sortHits.setInactive(this@BooksActivity)
                    }
                    sortedBooks.apply {
                        clear()
                        if(!binding.switch1.isChecked)addAll(bookList.sortedBy { book -> book.title })
                        else addAll(bookList.sortedByDescending { book -> book.title })
                        adaptor?.notifyDataSetChanged()
                    }

                }
                Constants.BY_HITS -> {
                    binding.apply {
                        sortTitle.setInactive(this@BooksActivity)
                        sortFavs.setInactive(this@BooksActivity)
                        sortHits.setActive(this@BooksActivity)
                    }
                    sortedBooks.apply {
                        clear()
                        if(!binding.switch1.isChecked)addAll(bookList.sortedBy { book -> book.hits })
                        else addAll(bookList.sortedByDescending { book -> book.hits })
                        adaptor?.notifyDataSetChanged()
                    }
                }
                Constants.BY_FAV -> {
                    binding.apply {
                        sortTitle.setInactive(this@BooksActivity)
                        sortFavs.setActive(this@BooksActivity)
                        sortHits.setInactive(this@BooksActivity)
                    }
                    sortedBooks.apply {
                        clear()
                        addAll(bookList.filter { book -> book.fav })
                        addAll(bookList.filterNot { book -> book.fav })
                        adaptor?.notifyDataSetChanged()
                    }

                }
                else -> {
                    binding.apply {
                        sortTitle.setInactive(this@BooksActivity)
                        sortFavs.setInactive(this@BooksActivity)
                        sortHits.setInactive(this@BooksActivity)
                    }
                    sortedBooks.apply {
                        clear()
                        addAll(bookList)
                    }
                    adaptor?.notifyDataSetChanged()
                }
            }
        }
    }


    private fun setupRv() {
        viewmodel.getBooks()
        adaptor = Adaptor(sortedBooks, this)
        binding.rv.let {
            it.adapter = adaptor
            it.layoutManager = LinearLayoutManager(this)
        }
    }

    private fun initSharedPreference() {
        sharedPrefs = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    private fun initEventListeners() {
        binding.apply {
            logout.setOnClickListener {
                logout()
            }
            sortTitle.setOnClickListener { viewmodel.updateSortingAlgo(Constants.BY_TITLE)}
            sortFavs.setOnClickListener { viewmodel.updateSortingAlgo(Constants.BY_FAV)}
            sortHits.setOnClickListener { viewmodel.updateSortingAlgo(Constants.BY_HITS)}
            switch1.setOnCheckedChangeListener{_,isChecked->
                if(isChecked) {
                    when(sortedBy){
                        Constants.BY_TITLE->{sortedBooks.clear(); sortedBooks.addAll(bookList.sortedByDescending { book ->book.title  })}
                        Constants.BY_HITS->{sortedBooks.clear(); sortedBooks.addAll(bookList.sortedByDescending { book ->book.hits  })}
                    }
                }
                else {
                    when(sortedBy){
                        Constants.BY_TITLE->{sortedBooks.clear(); sortedBooks.addAll(bookList.sortedBy { book ->book.title  })}
                        Constants.BY_HITS->{sortedBooks.clear(); sortedBooks.addAll(bookList.sortedBy { book ->book.hits  })}
                    }
                }
                adaptor?.notifyDataSetChanged()
            }
        }

    }

    private fun logout() {
        clearSharedPreferences()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun clearSharedPreferences() {
        sharedPrefs.edit().apply {
            putBoolean(Constants.LOGGED_IN_STATUS, false)
            putString(Constants.LOGGED_IN_USERID, "")
            putInt(Constants.SORTING_ALGO, Constants.BY_DEFAULT)
        }.commit()
        bookList.forEach {
            viewmodel.updateBook(it.id, false)
        }
    }

    private fun initBinding() {
        binding = ActivityBooksBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onFavClicked(id: String, newValue: Boolean) {
        viewmodel.updateBook(id, newValue)
    }

    override fun openBookDetails(book: Book) {
        val intent = Intent(this, BookDetailsActivity::class.java)
        val bundle = Bundle()
        bundle.apply {
            putString(Constants.BOOK_ID, book.id)
            putString(Constants.BOOK_TITLE, book.title)
            putString(Constants.BOOK_SUBTITLE, book.alias)
            putInt(Constants.BOOK_HITS, book.hits)
            putBoolean(Constants.BOOK_IS_FAV, book.fav)
            putString(Constants.BOOK_IMG, book.image)

        }
        intent.putExtra(Constants.BOOK_BUNDLE, bundle)
        startActivity(intent)

    }

}