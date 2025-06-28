package com.example.readery.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Интерфейс DAO для работы с таблицей downloaded_books в базе данных Room.
 */
@Dao
public interface DownloadedBookDao {
    /**
     * Вставляет или обновляет запись о скачанной книге в базе данных.
     * Если запись с таким bookId уже существует, она будет заменена.
     *
     * @param downloadedBook объект скачанной книги
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DownloadedBook downloadedBook);

    /**
     * Получает запись о скачанной книге по её ID.
     *
     * @param bookId ID книги
     * @return объект DownloadedBook или null, если запись не найдена
     */
    @Query("SELECT * FROM downloaded_books WHERE bookId = :bookId")
    DownloadedBook getDownloadedBookById(long bookId);

    /**
     * Получает список всех скачанных книг в виде LiveData.
     *
     * @return LiveData со списком объектов DownloadedBook
     */
    @Query("SELECT * FROM downloaded_books")
    LiveData<List<DownloadedBook>> getAllDownloadedBooks();

    /**
     * Удаляет запись о скачанной книге из базы данных.
     *
     * @param downloadedBook объект скачанной книги для удаления
     */
    @Delete
    void delete(DownloadedBook downloadedBook);
}