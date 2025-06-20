package com.example.readery.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.readery.data.Book;
import com.example.readery.repository.BookRepository;
import java.util.ArrayList;
import java.util.List;


public class AllBooksViewModel extends AndroidViewModel {
    private BookRepository repository;
    private MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private MutableLiveData<String> filterType = new MutableLiveData<>("title");
    private MutableLiveData<String> filterOrder = new MutableLiveData<>("ASC");
    private MediatorLiveData<List<Book>> searchedBooks = new MediatorLiveData<>();
    private String language;
    private LiveData<List<Book>> currentSource;


    public AllBooksViewModel(Application application) {
        super(application);
        repository = new BookRepository(application);
        language = application.getResources().getConfiguration().getLocales().get(0).getLanguage();


        currentSource = repository.getAllBooks();
        searchedBooks.addSource(currentSource, books ->
                searchedBooks.setValue(books != null ? books : new ArrayList<>())
        );


        searchQuery.observeForever(this::updateSearchedBooks);
        filterType.observeForever(this::updateSearchedBooks);
        filterOrder.observeForever(this::updateSearchedBooks);
    }


    public LiveData<List<Book>> getSearchedBooks() {
        return searchedBooks;
    }


    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }


    public void setFilter(String type, String order) {
        filterType.setValue(type);
        filterOrder.setValue(order);
    }


    private void updateSearchedBooks(Object ignored) {
        String query = searchQuery.getValue();
        String type = filterType.getValue();
        String order = filterOrder.getValue();


        if (currentSource != null) {
            searchedBooks.removeSource(currentSource);
        }


        if (query == null || query.isEmpty()) {
            currentSource = repository.getAllBooks();
        } else {
            currentSource = repository.searchBooks("%" + query + "%", type, order, language);
        }


        searchedBooks.addSource(currentSource, books ->
                searchedBooks.setValue(books != null ? books : new ArrayList<>())
        );
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        searchQuery.removeObserver(this::updateSearchedBooks);
        filterType.removeObserver(this::updateSearchedBooks);
        filterOrder.removeObserver(this::updateSearchedBooks);
    }
}