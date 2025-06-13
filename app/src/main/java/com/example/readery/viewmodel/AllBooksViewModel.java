package com.example.readery.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.example.readery.data.Book;
import com.example.readery.repository.BookRepository;
import java.util.List;

public class AllBooksViewModel extends AndroidViewModel {
    private BookRepository repository;
    private MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private LiveData<List<Book>> searchedBooks;

    public AllBooksViewModel(Application application) {
        super(application);
        repository = new BookRepository(application);
        searchedBooks = Transformations.switchMap(searchQuery, query ->
                repository.searchBooks("%" + query + "%")
        );
    }

    public LiveData<List<Book>> getBooks() {
        return repository.getAllBooks();
    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    public LiveData<List<Book>> getSearchedBooks() {
        return searchedBooks;
    }
}