package com.example.readery.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.readery.data.AppDatabase;
import com.example.readery.data.Book;
import com.example.readery.data.BookDao;
import java.util.List;

public class BookRepository {
    private BookDao bookDao;
    private LiveData<List<Book>> allBooks;

    public BookRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        bookDao = db.bookDao();
        allBooks = bookDao.getAllBooks();
    }

    public LiveData<List<Book>> getAllBooks() {
        return allBooks;
    }

    public LiveData<Book> getBookById(long bookId) {
        return bookDao.getBookById(bookId);
    }

    public LiveData<List<Book>> getBooksByTag(long tagId) {
        return bookDao.getBooksByTag(tagId);
    }

    public LiveData<List<Book>> searchBooks(String query, String filterType, String filterOrder, String language) {
        return bookDao.searchBooks("%" + query + "%", filterType, filterOrder, language);
    }
}