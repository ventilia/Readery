package com.example.readery.utils;

import android.content.Context;
import com.example.readery.data.AppDatabase;
import com.example.readery.data.Book;
import com.example.readery.data.BookTag;
import com.example.readery.data.Tag;
import java.util.concurrent.Executors;

public class DatabaseInitializer {
    // заполнение базы данных начальными данными
    public static void populateDatabase(Context context) {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(context);
            if (db.bookDao().getBookCount() == 0) {
                // вставка тегов
                Tag newTag = new Tag("New");
                long newTagId = db.tagDao().insert(newTag);

                Tag popularTag = new Tag("Popular");
                long popularTagId = db.tagDao().insert(popularTag);

                Tag editorsChoiceTag = new Tag("Editor's Choice");
                long editorsChoiceTagId = db.tagDao().insert(editorsChoiceTag);

                // вставка первой книги
                Book book1 = new Book("Sample Book", "John Doe");
                book1.setDescription("A sample book for testing.");
                book1.setPdfPath("master_and_margarita.pdf");
                book1.setCoverImagePath("cover1.jpg");
                long book1Id = db.bookDao().insert(book1);

                // вставка связей книга-тег для первой книги
                db.bookTagDao().insert(new BookTag(book1Id, newTagId));
                db.bookTagDao().insert(new BookTag(book1Id, popularTagId));
                db.bookTagDao().insert(new BookTag(book1Id, editorsChoiceTagId));

                // вставка второй книги
                Book book2 = new Book("Another Sample", "Jane Doe");
                book2.setDescription("A second sample book for demonstration.");
                book2.setPdfPath("example.pdf");
                book2.setCoverImagePath("cover2.jpg");
                long book2Id = db.bookDao().insert(book2);

                // вставка связей книга-тег для второй книги
                db.bookTagDao().insert(new BookTag(book2Id, newTagId));
                db.bookTagDao().insert(new BookTag(book2Id, editorsChoiceTagId));
            }
        });
    }
}
