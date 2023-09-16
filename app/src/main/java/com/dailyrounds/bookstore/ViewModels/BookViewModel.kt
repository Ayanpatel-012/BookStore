package com.dailyrounds.bookstore.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dailyrounds.bookstore.Models.Book
import com.dailyrounds.bookstore.Repositories.BookRepository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookViewModel(private val repository: BookRepository) : ViewModel() {

    val booksLiveData: LiveData<List<Book>> get() = _booksLiveData
    private var _booksLiveData = MutableLiveData<List<Book>>()
    fun getBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _booksLiveData.postValue(repository.getBooks())
            } catch (e: Exception) {

            }
        }
    }

}

class BookViewModelFactory(private val bookRepository: BookRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookViewModel::class.java)) {
            return BookViewModel(bookRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}