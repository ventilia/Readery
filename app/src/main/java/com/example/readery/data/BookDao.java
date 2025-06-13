package com.example.readery.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface BookDao {
    @Query("SELECT * FROM books")
    LiveData<List<Book>> getAllBooks();

    @Query("SELECT * FROM books WHERE id = :bookId")
    LiveData<Book> getBookById(long bookId); // изменен на long

    @Query("SELECT b.* FROM books b INNER JOIN book_tags bt ON b.id = bt.bookId WHERE bt.tagId = :tagId")
    LiveData<List<Book>> getBooksByTag(long tagId); // изменен на long

    @Query("SELECT * FROM books WHERE title LIKE :query OR author LIKE :query")
    LiveData<List<Book>> searchBooks(String query);

    @Insert
    long insert(Book book); // изменен на long для возврата id

    @Query("SELECT COUNT(*) FROM books")
    int getBookCount();
}