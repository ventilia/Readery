package com.example.readery.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DownloadedBookDao {
    @Insert
    void insert(DownloadedBook downloadedBook);

    @Query("SELECT * FROM downloaded_books WHERE bookId = :bookId")
    DownloadedBook getDownloadedBookById(long bookId);

    @Query("SELECT * FROM downloaded_books")
    LiveData<List<DownloadedBook>> getAllDownloadedBooks();
}