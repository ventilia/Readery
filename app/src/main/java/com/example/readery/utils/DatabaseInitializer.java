package com.example.readery.utils;

import android.content.Context;
import com.example.readery.data.AppDatabase;
import com.example.readery.data.Book;
import com.example.readery.data.BookTag;
import com.example.readery.data.Tag;
import java.util.Arrays;
import java.util.concurrent.Executors;

/**
 * Класс для инициализации базы данных тестовыми данными.
 */
public class DatabaseInitializer {

    /**
     * Заполняет базу данных тестовыми книгами и тегами, если она пуста.
     *
     * @param context Контекст приложения.
     */
    public static void populateDatabase(Context context) {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(context);

            if (db.bookDao().getBookCount() == 0) {
                // Создание тегов
                Tag newTag = new Tag("New");
                long newTagId = db.tagDao().insert(newTag);

                // Только одна книга для теста
                Book b3 = new Book();
                b3.setTitleEn("1984");
                b3.setTitleRu("1984");
                b3.setAuthorEn("George Orwell");
                b3.setAuthorRu("Джордж Оруэлл");
                b3.setDescriptionEn("A dystopian novel about a totalitarian society.");
                b3.setDescriptionRu("Дистопический роман о тоталитарном обществе.");
             //  b3.setPdfPathEn("books/1984/book_en.pdf");
              //  b3.setPdfPathRu("books/1984/book_ru.pdf");
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
                long id3 = db.bookDao().insert(b3);
                db.bookTagDao().insert(new BookTag(id3, newTagId));
            }
        });
    }
}