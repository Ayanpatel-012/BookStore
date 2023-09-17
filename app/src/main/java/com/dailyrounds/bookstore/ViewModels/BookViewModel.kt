package com.dailyrounds.bookstore.ViewModels

import androidx.lifecycle.*
import com.dailyrounds.bookstore.Models.Book
import com.dailyrounds.bookstore.Repositories.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookViewModel(private val repository: BookRepository) : ViewModel() {

    val booksLiveData: LiveData<List<Book>> get() = _booksLiveData
    private var _booksLiveData = MutableLiveData<List<Book>>()
    val sortingAlgo: LiveData<Int> get() = _sortingAlgo
    private val _sortingAlgo = MutableLiveData<Int>()
    fun getBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _booksLiveData.postValue(repository.getBooks())
            } catch (e: Exception) {

            }
        }
    }

    fun updateBook(id: String, newValue: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.updateBook(id, newValue)
            } catch (e: Exception) {

            }
        }
    }

    fun updateSortingAlgo(newAlgo: Int) {
        _sortingAlgo.value = newAlgo
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