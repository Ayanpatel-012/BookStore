package com.dailyrounds.bookstore.Activites

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dailyrounds.bookstore.R
import com.dailyrounds.bookstore.Utils.Constants
import com.dailyrounds.bookstore.databinding.ActivityBooksBinding

class BooksActivity : AppCompatActivity() {
    private lateinit var binding:ActivityBooksBinding
    lateinit var sharedPrefs: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initSharedPreference()
        initEventListeners()
    }

    private fun initSharedPreference() {
        sharedPrefs = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    private fun initEventListeners() {
       binding.logout.setOnClickListener {
           sharedPrefs.edit().putBoolean(Constants.LOGGED_IN_STATUS,false).commit()
           startActivity(Intent(this,LoginActivity::class.java))
           finish()
       }
    }

    private fun initBinding() {
        binding= ActivityBooksBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}