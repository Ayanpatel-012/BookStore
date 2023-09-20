package com.dailyrounds.bookstore.ViewModels

import androidx.lifecycle.*
import com.dailyrounds.bookstore.Models.Book
import com.dailyrounds.bookstore.Repositories.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookViewModel(private val repository: BookRepository) : ViewModel() {
    val book:LiveData<Book> get()=_book
    private var _book= MutableLiveData<Book>()
    val updatedBookId:LiveData<Pair<String, Boolean>> get() = _updatedBookId
    private var _updatedBookId=MutableLiveData<Pair<String, Boolean>>()
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
                _updatedBookId.postValue(Pair(id,newValue))
            } catch (e: Exception) {

            }
        }
    }
    fun getBookById(id:String){
        viewModelScope.launch ( Dispatchers.IO ){
            try {
                _book.postValue( repository.getBookById(id))
            }
            catch (e:Exception){

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