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

    // инициализация репозитория
    public BookRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        bookDao = db.bookDao();
        allBooks = bookDao.getAllBooks();
    }

    // получение всех книг
    public LiveData<List<Book>> getAllBooks() {
        return allBooks;
    }

    // получение книги по id
    public LiveData<Book> getBookById(long bookId) {
        return bookDao.getBookById(bookId);
    }

    // получение книг по тегу
    public LiveData<List<Book>> getBooksByTag(long tagId) {
        return bookDao.getBooksByTag(tagId);
    }

    // поиск книг
    public LiveData<List<Book>> searchBooks(String query) {
        return bookDao.searchBooks("%" + query + "%");
    }
}