package com.dailyrounds.bookstore.Fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dailyrounds.bookstore.Database.BookStoreDatabase
import com.dailyrounds.bookstore.Repositories.BookRepository
import com.dailyrounds.bookstore.Repositories.UserRepository
import com.dailyrounds.bookstore.Utils.Constants
import com.dailyrounds.bookstore.ViewModels.LoginViewModel
import com.dailyrounds.bookstore.ViewModels.LoginViewModelFactory
import com.dailyrounds.bookstore.databinding.FragmentLoginBinding
import com.dailyrounds.bookstore.enums.LoginStatus

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    lateinit var viewModel: LoginViewModel
    lateinit var database: BookStoreDatabase
    lateinit var sharedPrefs: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSharedPreference()
        initDatabase()
        initViewModel()
        initObservers()
        initTextWatchersAndListener()
    }

    private fun initSharedPreference() {
        sharedPrefs =
            requireActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    private fun initTextWatchersAndListener() {
        val avoidSpaceFilter = InputFilter { source, _, _, _, _, _ ->
            val sourceText = source.toString()
            if (" " !in sourceText) return@InputFilter null // keep original
            sourceText.replace(" ", "")
        }
        binding.apply {
            etPassword.apply { filters += avoidSpaceFilter }
            etName.apply { filters += avoidSpaceFilter }
            etPassword.addTextChangedListener(onTextChanged = { text, _, _, _ ->
                validatePassword(text.toString())
            })
            etName.addTextChangedListener(onTextChanged = { text, _, _, _ ->
                validateName(text.toString())
            })
            btnLogin.setOnClickListener {
                val username = etName.text.toString().trim()
                val password = etPassword.text.toString().trim()
                if (validateName(username) && validatePassword(password)) {
                    viewModel.loginUser(username, password)
                }
            }
        }
    }

    private fun validateName(username: String): Boolean {
        if (username.isNullOrEmpty()) {
            binding.apply {
                tvNameError.text = "Please enter username"
                tvNameError.visibility = View.VISIBLE
            }
            return false
        }
        binding.apply {
            tvNameError.text = ""
            tvNameError.visibility = View.GONE
        }

        return true
    }

    private fun validatePassword(password: String?): Boolean {
        if (password.isNullOrEmpty()) {
            binding.apply {
                tvPasswordError.text = "Please enter a password"
                tvPasswordError.visibility = View.VISIBLE
            }
            return false
        }
        binding.apply {
            tvPasswordError.text = ""
            tvPasswordError.visibility = View.GONE
        }
        return true
    }

    private fun initObservers() {
        viewModel.loginStatus.observe(requireActivity()) {
            when (it) {
                LoginStatus.CANLOGIN -> {
                    val username = binding.etName.text.toString().trim()
                    sharedPrefs.edit().putString(Constants.LOGGED_IN_USERID, username).commit()
                    showToast("Hi $username! Welcome to the books world")
                    showLoader(false)
                }
                LoginStatus.PASSWORD_ERROR -> {
                    binding.tvPasswordError.text = "oops!password doesn't match"
                    binding.tvPasswordError.visibility = View.VISIBLE
                    showLoader(false)
                }
                LoginStatus.USER_NOT_FOUND_ERROR -> {
                    binding.tvNameError.text = "username not found"
                    binding.tvNameError.visibility = View.VISIBLE
                    showLoader(false)
                }
                LoginStatus.DB_ERROR -> {
                    showToast("Error Fetching data.Try again later")
                    showLoader(false)
                }
                LoginStatus.INPROGRESS -> showLoader(true)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoader(show: Boolean) {
        if (show) {
            binding.loader.visibility = View.VISIBLE
            binding.btnLogin.visibility = View.GONE
        } else {
            binding.loader.visibility = View.GONE
            binding.btnLogin.visibility = View.VISIBLE
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            requireActivity(), LoginViewModelFactory(
                UserRepository(database.UserDao(), database.CountryDao()),
                BookRepository(database.BooksDao())
            )
        )[LoginViewModel::class.java]
    }

    private fun initDatabase() {
        database = BookStoreDatabase.getDatabase(requireActivity())
    }


    override fun onDetach() {
        super.onDetach()
        clearViews()
    }

    private fun clearViews() {
        binding.apply {
            tvNameError.text = ""
            tvNameError.visibility = View.GONE
            tvPasswordError.text = ""
            tvPasswordError.visibility = View.GONE
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}