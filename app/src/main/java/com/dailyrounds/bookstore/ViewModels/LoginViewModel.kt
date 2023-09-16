package com.dailyrounds.bookstore.ViewModels

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
import com.dailyrounds.bookstore.enums.RegistrationStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository,
    private val bookRepository: BookRepository
) : ViewModel() {
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
        _registrationStatus.value=RegistrationStatus.INPROGRESS
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

}

class LoginViewModelFactory(
    private val userRepository: UserRepository,
    private val bookRepository: BookRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userRepository, bookRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}