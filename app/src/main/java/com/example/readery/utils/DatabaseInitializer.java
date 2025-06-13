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

                // вставка книги
                Book book1 = new Book("Sample Book", "John Doe");
                book1.setDescription("A sample book for testing.");
                book1.setPdfPath("master_and_margarita.pdf");
                book1.setCoverImagePath("cover1.jpg");
                long book1Id = db.bookDao().insert(book1);

                // вставка связей книга-тег
                BookTag bookTag1 = new BookTag(book1Id, newTagId);
                db.bookTagDao().insert(bookTag1);

                BookTag bookTag2 = new BookTag(book1Id, popularTagId);
                db.bookTagDao().insert(bookTag2);

                BookTag bookTag3 = new BookTag(book1Id, editorsChoiceTagId);
                db.bookTagDao().insert(bookTag3);
            }
        });
    }
}