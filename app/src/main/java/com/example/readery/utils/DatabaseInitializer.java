package com.example.readery.utils;

import android.content.Context;
import com.example.readery.data.AppDatabase;
import com.example.readery.data.Book;
import com.example.readery.data.BookTag;
import com.example.readery.data.Tag;
import java.util.Arrays;
import java.util.concurrent.Executors;

public class DatabaseInitializer {
    public static void populateDatabase(Context context) {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(context);
            if (db.bookDao().getBookCount() == 0) {
                // Вставка тегов
                Tag newTag = new Tag("New");
                long newTagId = db.tagDao().insert(newTag);

                Tag popularTag = new Tag("Popular");
                long popularTagId = db.tagDao().insert(popularTag);

                Tag editorsChoiceTag = new Tag("Editor's Choice");
                long editorsChoiceTagId = db.tagDao().insert(editorsChoiceTag);

                // Вставка первой книги
                Book book1 = new Book("Sample Book", "John Doe");
                book1.setDescription("A sample book for testing with a very long description that exceeds fifty words to demonstrate the expand and collapse functionality in the book details activity. This text will be truncated initially and expanded upon clicking the button.");
                book1.setPdfPath("master_and_margarita.pdf");
                book1.setCoverImagePath("cover1.jpg");
                book1.setAdditionalImages(Arrays.asList("additional1.jpg", "additional2.jpg"));
                long book1Id = db.bookDao().insert(book1);

                db.bookTagDao().insert(new BookTag(book1Id, newTagId));
                db.bookTagDao().insert(new BookTag(book1Id, popularTagId));
                db.bookTagDao().insert(new BookTag(book1Id, editorsChoiceTagId));

                // Вставка второй книги
                Book book2 = new Book("Another Sample", "Jane Doe");
                book2.setDescription("A second sample book for demonstration.");
                book2.setPdfPath("example.pdf");
                book2.setCoverImagePath("cover2.jpg");
                book2.setAdditionalImages(Arrays.asList("additional3.jpg"));
                long book2Id = db.bookDao().insert(book2);

                db.bookTagDao().insert(new BookTag(book2Id, newTagId));
                db.bookTagDao().insert(new BookTag(book2Id, editorsChoiceTagId));

                // Вставка третьей книги
                Book book3 = new Book("Another fkjkfkf", "Jane Doeeeeee");
                book3.setDescription("A second sample book for demvvvvonstration.");
                book3.setPdfPath("example.pdf");
                book3.setCoverImagePath("zz.jpg");
                book3.setAdditionalImages(Arrays.asList("additional4.jpg"));
                long book3Id = db.bookDao().insert(book3);

                db.bookTagDao().insert(new BookTag(book3Id, newTagId));
                db.bookTagDao().insert(new BookTag(book3Id, editorsChoiceTagId));

                // Вставка четвертой книги
                Book book4 = new Book("Ano", "Jan");
                book4.setDescription("A second sample book fo.");
                book4.setPdfPath("example.pdf");
                book4.setCoverImagePath("zxxx.jpg");
                book4.setAdditionalImages(Arrays.asList("additional5.jpg"));
                long book4Id = db.bookDao().insert(book4);

                db.bookTagDao().insert(new BookTag(book4Id, newTagId));
                db.bookTagDao().insert(new BookTag(book4Id, editorsChoiceTagId));
            }
        });
    }
}