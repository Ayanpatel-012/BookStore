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
import com.dailyrounds.bookstore.Database.BookStoreDatabase
import com.dailyrounds.bookstore.Models.Country
import com.dailyrounds.bookstore.R
import com.dailyrounds.bookstore.Repositories.BookRepository
import com.dailyrounds.bookstore.Repositories.UserRepository
import com.dailyrounds.bookstore.ViewModels.LoginViewModel
import com.dailyrounds.bookstore.ViewModels.LoginViewModelFactory
import com.dailyrounds.bookstore.databinding.FragmentRegisterBinding
import com.dailyrounds.bookstore.enums.RegistrationStatus

class RegisterFragment : Fragment() {
    lateinit var viewModel: LoginViewModel
    lateinit var database: BookStoreDatabase
    var countryData = ArrayList<Country>()
    private lateinit var binding: FragmentRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
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
        viewModel.countryLiveData.observe(requireActivity()) {
            countryData = it as ArrayList<Country>
        }
        viewModel.registrationStatus.observe(requireActivity()) {
            when (it) {
                RegistrationStatus.USER_PRESENT -> {
                    binding.tvNameError.text = "username already exists!"
                    binding.tvNameError.visibility = View.VISIBLE
                    showLoader(false)
                }
                RegistrationStatus.REGISTERED -> {
                    showToast("Successfully registered")
                    showLoader(false)
                }
                RegistrationStatus.ERROR -> {
                    showToast("Error while registration")
                    showLoader(false)
                }
                RegistrationStatus.INPROGRESS -> {
                    showLoader(true)
                }

            }
        }
    }

    private fun showLoader(show: Boolean) {
        if (show) {
            binding.loader.visibility = View.VISIBLE
            binding.btnRegister.visibility = View.GONE
        } else {
            binding.loader.visibility = View.GONE
            binding.btnRegister.visibility = View.VISIBLE
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
        binding.etPassword.addTextChangedListener(onTextChanged = { text, _, _, _ ->
            validatePassword(text.toString())
        })
        binding.etName.addTextChangedListener(onTextChanged = { text, _, _, _ ->
            validateName(text.toString())
        })
        binding.btnRegister.setOnClickListener {
            val username = binding?.etName?.text.toString().trim()
            val password = binding?.etPassword?.text.toString().trim()
            if (validateName(username) && validatePassword(password)) {
                viewModel.registerUser(
                    username, password, "india"
                )
            }
        }

    }

    private fun validateName(username: String): Boolean {
        if (username.isNullOrEmpty()) {
            binding.tvNameError.text = "Please enter username"
            binding.tvNameError.visibility = View.VISIBLE
            return false
        }
        binding.tvNameError.text = ""
        binding.tvNameError.visibility = View.GONE

        return true

    }

    private fun validatePassword(text: String?): Boolean {
        val password = text.toString()
        var isAtLeast8: Boolean
        var hasUppercase: Boolean
        var hasNumber: Boolean
        var hasSymbol: Boolean

        if (password.isNullOrEmpty()) {
            binding.tvPasswordError.text = "Please enter a password"
            binding.tvPasswordError.visibility = View.VISIBLE
        } else {
            binding.tvPasswordError.text = ""
            binding.tvPasswordError.visibility = View.GONE
        }

        if (password.length >= 8) {
            isAtLeast8 = true;
            binding.frameOne.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(), R.color.holo_green_dark
                )
            )
        } else {
            isAtLeast8 = false;
            binding.frameOne.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(), R.color.lighter_gray
                )
            )
        }
        if (password.matches(Regex("(.*[A-Z].*)"))) {
            hasUppercase = true;
            binding.frameTwo.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(), R.color.holo_green_dark
                )
            )
        } else {
            hasUppercase = false;
            binding.frameTwo.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(), R.color.lighter_gray
                )
            )
        }
        if (password.matches(Regex("(.*[0-9].*)"))) {
            hasNumber = true
            binding.frameThree.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(), R.color.holo_green_dark
                )
            )
        } else {
            hasNumber = false
            binding.frameThree.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(), R.color.lighter_gray
                )
            )
        }
        if (password.matches(Regex("(.*[!@#$%^&*()].*)"))) {
            hasSymbol = true
            binding.frameFour.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(), R.color.holo_green_dark
                )
            )
        } else {
            hasSymbol = false;
            binding.frameFour.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(), R.color.lighter_gray
                )
            )
        }
        return isAtLeast8 && hasNumber && hasSymbol && hasUppercase

    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }


    companion object {
        @JvmStatic
        fun newInstance() = RegisterFragment().apply {}
    }
}