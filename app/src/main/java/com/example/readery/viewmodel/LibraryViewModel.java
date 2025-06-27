package com.example.readery.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.readery.data.Book;
import com.example.readery.repository.BookRepository;
import java.util.List;

public class LibraryViewModel extends AndroidViewModel {
    private BookRepository repository;

    public LibraryViewModel(Application application) {
        super(application);
        repository = new BookRepository(application);
    }

    public LiveData<List<Book>> getDownloadedBooks() {
        return repository.getDownloadedBooks();
    }
}