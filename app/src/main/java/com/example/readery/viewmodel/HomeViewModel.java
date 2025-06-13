package com.example.readery.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.readery.data.Book;
import com.example.readery.repository.BookRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private BookRepository repository;

    public HomeViewModel(Application application) {
        super(application);
        repository = new BookRepository(application);
    }

    public LiveData<List<Book>> getNewBooks() {
        return repository.getBooksByTag(1); // tagId=1 для "New"
    }

    public LiveData<List<Book>> getPopularBooks() {
        return repository.getBooksByTag(2); // tagId=2 для "Popular"
    }

    public LiveData<List<Book>> getEditorsChoiceBooks() {
        return repository.getBooksByTag(3); // tagId=3 для "Editor's Choice"
    }
}