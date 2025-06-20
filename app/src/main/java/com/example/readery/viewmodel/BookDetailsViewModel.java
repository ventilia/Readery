package com.example.readery.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.readery.data.Book;
import com.example.readery.repository.BookRepository;

public class BookDetailsViewModel extends AndroidViewModel {
    private BookRepository repository;
    private LiveData<Book> book;


    public BookDetailsViewModel(Application application) {
        super(application);
        repository = new BookRepository(application);
    }

    // установка id книги
    public void setBookId(long bookId) { // изменен на long
        book = repository.getBookById(bookId);
    }

    // получение книги
    public LiveData<Book> getBook() {
        return book;
    }
}