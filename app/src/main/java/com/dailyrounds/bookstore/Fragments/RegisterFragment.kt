package com.dailyrounds.bookstore.Fragments

import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dailyrounds.bookstore.Database.BookStoreDatabase
import com.dailyrounds.bookstore.Models.BookList
import com.dailyrounds.bookstore.Models.Country
import com.dailyrounds.bookstore.Models.CountryList
import com.dailyrounds.bookstore.R
import com.dailyrounds.bookstore.Repositories.BookRepository
import com.dailyrounds.bookstore.Repositories.UserRepository
import com.dailyrounds.bookstore.Utils.Constants
import com.dailyrounds.bookstore.ViewModels.LoginViewModel
import com.dailyrounds.bookstore.ViewModels.LoginViewModelFactory
import com.dailyrounds.bookstore.databinding.FragmentRegisterBinding
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {
    lateinit var viewModel: LoginViewModel
    lateinit var listener: FragmentEventListener
    lateinit var database: BookStoreDatabase
     var countryData=ArrayList<Country>()
    private lateinit var binding:FragmentRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding= FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
       // return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDatabase()
        initViewModel()
        initData()
        initObservers()
        initTextWatchersAndListener()
    }

    private fun initData() {
        viewModel.getCountries()
    }

    private fun initObservers() {
       viewModel.countryLiveData.observe(requireActivity()){
           countryData= it as ArrayList<Country>
       }
    }

    private fun initDatabase() {
        database = BookStoreDatabase.getDatabase(requireActivity())
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            requireActivity(), LoginViewModelFactory(
                UserRepository(database.UserDao(), database.CountryDao()),
                BookRepository(database.BooksDao())
            )
        )[LoginViewModel::class.java]
    }
    private fun initTextWatchersAndListener() {
        val avoidSpaceFilter = InputFilter { source, _, _, _, _, _ ->
            val sourceText = source.toString()
            if (" " !in sourceText) return@InputFilter null // keep original
            sourceText.replace(" ", "")
        }

        binding.etPassword.apply { filters += avoidSpaceFilter }
        binding.etPassword.addTextChangedListener(
            onTextChanged = {text,_,_,_ ->
                validatePassword(text.toString())
            }
        )
        binding.etName.addTextChangedListener(
            onTextChanged = {text,_,_,_ ->
                validateName(text.toString())
            }
        )
        binding.btnRegister.setOnClickListener{
            if(validateName(binding.etName.text.toString().trim()) && validatePassword(binding.etPassword.text.toString().trim())){
                Toast.makeText(requireActivity(),"VALID",Toast.LENGTH_LONG).show()
            }
        }

    }
    private fun validateName(username:String):Boolean{
        if(username.isNullOrEmpty()){
            binding.tvNameError.text="Please enter username"
            binding.tvNameError.visibility=View.VISIBLE
            return false
        }
        binding.tvNameError.text=""
        binding.tvNameError.visibility=View.GONE

        return true

    }
    private fun validatePassword(text: String?):Boolean{
        val password=text.toString()
        var  isAtLeast8=false
        var hasUppercase=false
        var hasNumber=false
        var hasSymbol=false

        if(password.isNullOrEmpty()) {
            binding.tvPasswordError.text="Please enter a password"
            binding.tvPasswordError.visibility=View.VISIBLE
        }
        else{
            binding.tvPasswordError.text=""
            binding.tvPasswordError.visibility=View.GONE
        }

        if (password.length >= 8) {
            isAtLeast8 = true;
            binding.frameOne.setCardBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.holo_green_dark))
        } else {
            isAtLeast8 = false;
            binding.frameOne.setCardBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.darker_gray))
        }
        if (password.matches(Regex("(.*[A-Z].*)"))) {
            hasUppercase = true;
            binding.frameTwo.setCardBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.holo_green_dark))
        } else {
            hasUppercase = false;
            binding.frameTwo.setCardBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.darker_gray))
        }
        if (password.matches(Regex("(.*[0-9].*)"))) {
            hasNumber = true
            binding.frameThree.setCardBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.holo_green_dark))
        } else {
            hasNumber = false
            binding.frameThree.setCardBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.darker_gray))
        }
        if (password.matches(Regex("(.*[!@#$%^&*()].*)"))) {
            hasSymbol = true
            binding.frameFour.setCardBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.holo_green_dark))
        } else {
            hasSymbol = false;
            binding.frameFour.setCardBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.darker_gray))
        }
        return isAtLeast8 && hasNumber && hasSymbol && hasUppercase

    }


    companion object {
        @JvmStatic
        fun newInstance() =
            RegisterFragment().apply {
            }
    }
}