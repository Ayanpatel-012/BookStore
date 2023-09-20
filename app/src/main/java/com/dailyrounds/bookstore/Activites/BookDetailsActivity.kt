package com.dailyrounds.bookstore.Activites

import android.icu.text.CaseMap.Title
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dailyrounds.bookstore.Database.BookStoreDatabase
import com.dailyrounds.bookstore.Models.Book
import com.dailyrounds.bookstore.R
import com.dailyrounds.bookstore.Repositories.BookRepository
import com.dailyrounds.bookstore.Utils.Constants
import com.dailyrounds.bookstore.ViewModels.BookViewModel
import com.dailyrounds.bookstore.ViewModels.BookViewModelFactory
import com.dailyrounds.bookstore.databinding.ActivityBookDetailsBinding

class BookDetailsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityBookDetailsBinding
    private lateinit var viewmodel: BookViewModel
    private lateinit var id:String
    private lateinit var book: Book
    lateinit var database: BookStoreDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initDatabase()
        initViewModel()
        initObservers()
        initBookDataFromIntent()
        initUI()
        initObservers()
        initListeners()
    }

    private fun initUI() {
        viewmodel.getBookById(id)
    }

    private fun initObservers() {
       viewmodel.book.observe(this){
           book=it
           updateUI()
       }
        viewmodel.updatedBookId.observe(this){
           book.fav=it.second
            updateUI()
        }
    }

    private fun updateUI() {
        val image=book.image
        val fav=book.fav
        val hits=book.hits
        val subTitle=book.alias
        val Title=book.title
        if (fav == true) {
            binding.favImg.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.favImg.context,
                    R.drawable.fav_check
                )
            )
        } else {
            binding.favImg.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.favImg.context,
                    R.drawable.fav_uncheck
                )
            )
        }
        Glide.with(binding.bookImg).load(image).into(binding.bookImg)
        binding.hitsCnt.text=hits.toString()
        binding.subTitle.text=subTitle
        binding.title.text=Title
    }

    private fun initListeners() {
        binding.favImg.setOnClickListener {
        viewmodel.updateBook(id,!book.fav)
        }
    }

    private fun initViewModel() {
        viewmodel = ViewModelProvider(
            this, BookViewModelFactory(BookRepository(database.BooksDao()))
        )[BookViewModel::class.java]
    }
    private fun initDatabase() {
        database = BookStoreDatabase.getDatabase(this)
    }

    private fun initBinding() {
        binding= ActivityBookDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initBookDataFromIntent() {

        val bundle=intent?.getBundleExtra(Constants.BOOK_BUNDLE)
        id= bundle?.getString(Constants.BOOK_ID).toString()

    }

}