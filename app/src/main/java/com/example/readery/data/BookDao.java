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
    LiveData<Book> getBookById(long bookId);

    @Query("SELECT b.* FROM books b INNER JOIN book_tags bt ON b.id = bt.bookId WHERE bt.tagId = :tagId")
    LiveData<List<Book>> getBooksByTag(long tagId);

    @Query("SELECT * FROM books WHERE " +
            "(CASE WHEN :language = 'ru' THEN titleRu ELSE titleEn END) LIKE :query OR " +
            "(CASE WHEN :language = 'ru' THEN authorRu ELSE authorEn END) LIKE :query " +
            "ORDER BY " +
            "CASE WHEN :filterType = 'title' AND :filterOrder = 'ASC' THEN " +
            "(CASE WHEN :language = 'ru' THEN titleRu ELSE titleEn END) END ASC, " +
            "CASE WHEN :filterType = 'title' AND :filterOrder = 'DESC' THEN " +
            "(CASE WHEN :language = 'ru' THEN titleRu ELSE titleEn END) END DESC, " +
            "CASE WHEN :filterType = 'author' AND :filterOrder = 'ASC' THEN " +
            "(CASE WHEN :language = 'ru' THEN authorRu ELSE authorEn END) END ASC, " +
            "CASE WHEN :filterType = 'author' AND :filterOrder = 'DESC' THEN " +
            "(CASE WHEN :language = 'ru' THEN authorRu ELSE authorEn END) END DESC")
    LiveData<List<Book>> searchBooks(String query, String filterType, String filterOrder, String language);

    @Insert
    long insert(Book book);

    @Query("SELECT COUNT(*) FROM books")
    int getBookCount();

    @Query("SELECT * FROM books WHERE isDownloaded = 1")
    LiveData<List<Book>> getDownloadedBooks();
}