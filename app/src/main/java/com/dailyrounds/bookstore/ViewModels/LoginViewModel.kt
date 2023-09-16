package com.dailyrounds.bookstore.ViewModels

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dailyrounds.bookstore.Models.*
import com.dailyrounds.bookstore.Repositories.BookRepository
import com.dailyrounds.bookstore.Repositories.UserRepository
import com.dailyrounds.bookstore.Utils.Constants
import com.dailyrounds.bookstore.enums.LoginStatus
import com.dailyrounds.bookstore.enums.RegistrationStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository, private val bookRepository: BookRepository
) : ViewModel() {
    val loginStatus: LiveData<LoginStatus>
        get() = _loginStatus
    private var _loginStatus = MutableLiveData<LoginStatus>()
    val registrationStatus: LiveData<RegistrationStatus>
        get() = _registrationStatus
    private var _registrationStatus = MutableLiveData<RegistrationStatus>()
    val countryLiveData: LiveData<List<Country>>
        get() = _countryLiveData
    private val _countryLiveData = MutableLiveData<List<Country>>()
    fun saveBooks(bookList: BookList) {
        viewModelScope.launch {
            try {
                bookRepository.saveBooks(bookList)
            } catch (e: Exception) {

            }
        }
    }

    fun saveCountries(countryList: CountryList) {
        viewModelScope.launch {
            try {
                userRepository.saveCountries(countryList)
            } catch (e: Exception) {

            }
        }

    }

    fun getCountries() {
        viewModelScope.launch {
            try {
                _countryLiveData.value = userRepository.getCountries()
            } catch (e: Exception) {

            }
        }
    }

    fun registerUser(username: String, password: String, country: String) {
        _registrationStatus.value = RegistrationStatus.INPROGRESS
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var user: User? = userRepository.getUserById(username)
                if (user != null) {
                    _registrationStatus.postValue(RegistrationStatus.USER_PRESENT)
                } else {
                    userRepository.insertUser(User(username, password, country))
                    _registrationStatus.postValue(RegistrationStatus.REGISTERED)
                }
            } catch (e: Exception) {
                _registrationStatus.postValue(RegistrationStatus.ERROR)
            }
        }
    }

    fun loginUser(username: String, password: String) {
        _loginStatus.postValue(LoginStatus.INPROGRESS)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var user: User? = userRepository.getUserById(username)
                if (user == null) {
                    _loginStatus.postValue(LoginStatus.USER_NOT_FOUND_ERROR)
                }
                else{
                    if(user.username!=username) _loginStatus.postValue(LoginStatus.USER_NOT_FOUND_ERROR)
                    else if (!user.password.equals(password)){
                        Log.d("AYAN", "$password,${user.password}")
                        _loginStatus.postValue(LoginStatus.PASSWORD_ERROR)
                    }
                    else _loginStatus.postValue(LoginStatus.CANLOGIN)
                }

            } catch (e: Exception) {
                _loginStatus.postValue(LoginStatus.DB_ERROR)
            }
        }
    }

}

class LoginViewModelFactory(
    private val userRepository: UserRepository, private val bookRepository: BookRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userRepository, bookRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}