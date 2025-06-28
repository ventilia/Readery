package com.example.readery.utils;

import android.content.Context;
import com.example.readery.data.AppDatabase;
import com.example.readery.data.Book;
import com.example.readery.data.BookTag;
import com.example.readery.data.Tag;
import java.util.Arrays;
import java.util.concurrent.Executors;

/**
 * Класс для инициализации базы данных тестовыми данными
 */
public class DatabaseInitializer {

    /**
     * Заполняет базу данных тестовыми книгами и тегами, если она пуста
     *
     * @param context контекст приложения
     */
    public static void populateDatabase(Context context) {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(context);

            if (db.bookDao().getBookCount() == 0) {
                // Создание тегов
                Tag newTag = new Tag("New");
                long newTagId = db.tagDao().insert(newTag);

                // Книга с id 1, соответствующим Firebase
                Book b3 = new Book();
                b3.setId(1); // Явно задаем id 1
                b3.setTitleEn("1984");
                b3.setTitleRu("1984");
                b3.setAuthorEn("George Orwell");
                b3.setAuthorRu("Джордж Оруэлл");
                b3.setDescriptionEn("A dystopian novel about a totalitarian society.");
                b3.setDescriptionRu("Дистопический роман о тоталитарном обществе.");
                b3.setCoverImagePathEn("books/1984/cover_low_res_en.jpg");
                b3.setCoverImagePathRu("books/1984/cover_low_res_ru.jpg");
                b3.setHighResCoverImagePathEn("books/1984/cover_high_res_en.jpg");
                b3.setHighResCoverImagePathRu("books/1984/cover_high_res_ru.jpg");
                b3.setAdditionalImagesEn(Arrays.asList(
                        "books/1984/additional1_en.jpg",
                        "books/1984/additional2_en.jpg"
                ));
                b3.setAdditionalImagesRu(Arrays.asList(
                        "books/1984/additional1_ru.jpg",
                        "books/1984/additional2_ru.jpg"
                ));
                db.bookDao().insert(b3); // Вставляем книгу с id 1
                db.bookTagDao().insert(new BookTag(1, newTagId)); // Связываем книгу с тегом
            }
        });
    }
}