package com.dailyrounds.bookstore.Activites

import android.icu.text.CaseMap.Title
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dailyrounds.bookstore.R
import com.dailyrounds.bookstore.Utils.Constants
import com.dailyrounds.bookstore.databinding.ActivityBookDetailsBinding

class BookDetailsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityBookDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityBookDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle=intent?.getBundleExtra(Constants.BOOK_BUNDLE)
       val image=bundle?.getString(Constants.BOOK_IMG)
        val fav=bundle?.getBoolean(Constants.BOOK_IS_FAV)
        val hits=bundle?.getInt(Constants.BOOK_HITS)
        val subTitle=bundle?.getString(Constants.BOOK_SUBTITLE)
        val Title=bundle?.getString(Constants.BOOK_TITLE)
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
}