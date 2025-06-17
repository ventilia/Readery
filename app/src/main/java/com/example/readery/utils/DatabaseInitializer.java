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
                // Создание тегов
                Tag newTag = new Tag("New");
                long newTagId = db.tagDao().insert(newTag);

                Tag popularTag = new Tag("Popular");
                long popularTagId = db.tagDao().insert(popularTag);

                Tag editorsTag = new Tag("Editor's Choice");
                long editorsTagId = db.tagDao().insert(editorsTag);

                String genericDescEn = "A classic work of world literature.";
                String genericDescRu = "Классическое произведение мировой литературы.";

                // ==== NEW (5 книг) ====
                Book b1 = new Book("Pride and Prejudice", "Jane Austen");
                b1.setDescriptionEn("A novel about love and social norms in 19th century England.");
                b1.setDescriptionRu("Роман о любви и социальных нормах в Англии 19 века.");
                b1.setPdfPath("books/pride_and_prejudice/book.pdf");
                b1.setCoverImagePath("books/pride_and_prejudice/cover_low_res.jpg");
                b1.setHighResCoverImagePath("books/pride_and_prejudice/cover_high_res.jpg");
                b1.setAdditionalImages(Arrays.asList(
                        "books/pride_and_prejudice/additional1.jpg",
                        "books/pride_and_prejudice/additional2.jpg"
                ));
                long id1 = db.bookDao().insert(b1);
                db.bookTagDao().insert(new BookTag(id1, newTagId));

                Book b2 = new Book("To Kill a Mockingbird", "Harper Lee");
                b2.setDescriptionEn("A novel about racial injustice and growing up in the American South.");
                b2.setDescriptionRu("Роман о расовой несправедливости и взрослении на юге США.");
                b2.setPdfPath("books/to_kill_a_mockingbird/book.pdf");
                b2.setCoverImagePath("books/to_kill_a_mockingbird/cover_low_res.jpg");
                b2.setHighResCoverImagePath("books/to_kill_a_mockingbird/cover_high_res.jpg");
                b2.setAdditionalImages(Arrays.asList(
                        "books/to_kill_a_mockingbird/additional1.jpg",
                        "books/to_kill_a_mockingbird/additional2.jpg"
                ));
                long id2 = db.bookDao().insert(b2);
                db.bookTagDao().insert(new BookTag(id2, newTagId));

                Book b3 = new Book("1984", "George Orwell");
                b3.setDescriptionEn("A dystopian novel about a totalitarian society.");
                b3.setDescriptionRu("Дистопический роман о тоталитарном обществе.");
                b3.setPdfPath("books/1984/book.pdf");
                b3.setCoverImagePath("books/1984/cover_low_res.jpg");
                b3.setHighResCoverImagePath("books/1984/cover_high_res.jpg");
                b3.setAdditionalImages(Arrays.asList(
                        "books/1984/additional1.jpg",
                        "books/1984/additional2.jpg"
                ));
                long id3 = db.bookDao().insert(b3);
                db.bookTagDao().insert(new BookTag(id3, newTagId));

                Book b4 = new Book("The Great Gatsby", "F. Scott Fitzgerald");
                b4.setDescriptionEn("A novel about the American dream and decline in the 1920s.");
                b4.setDescriptionRu("Роман о американской мечте и упадке в 1920-х годах.");
                b4.setPdfPath("books/the_great_gatsby/book.pdf");
                b4.setCoverImagePath("books/the_great_gatsby/cover_low_res.jpg");
                b4.setHighResCoverImagePath("books/the_great_gatsby/cover_high_res.jpg");
                b4.setAdditionalImages(Arrays.asList(
                        "books/the_great_gatsby/additional1.jpg",
                        "books/the_great_gatsby/additional2.jpg"
                ));
                long id4 = db.bookDao().insert(b4);
                db.bookTagDao().insert(new BookTag(id4, newTagId));

                Book b5 = new Book("Moby Dick", "Herman Melville");
                b5.setDescriptionEn("An epic tale of Captain Ahab and his obsession with a white whale.");
                b5.setDescriptionRu("Эпическая история о капитане Ахаве и его одержимости белым китом.");
                b5.setPdfPath("books/moby_dick/book.pdf");
                b5.setCoverImagePath("books/moby_dick/cover_low_res.jpg");
                b5.setHighResCoverImagePath("books/moby_dick/cover_high_res.jpg");
                b5.setAdditionalImages(Arrays.asList(
                        "books/moby_dick/additional1.jpg",
                        "books/moby_dick/additional2.jpg"
                ));
                long id5 = db.bookDao().insert(b5);
                db.bookTagDao().insert(new BookTag(id5, newTagId));

                // ==== POPULAR (5 книг) ====
                Book b6 = new Book("War and Peace", "Leo Tolstoy");
                b6.setDescriptionEn("A grand novel about Russian life during the Napoleonic wars.");
                b6.setDescriptionRu("Масштабный роман о русской жизни во времена наполеоновских войн.");
                b6.setPdfPath("books/war_and_peace/book.pdf");
                b6.setCoverImagePath("books/war_and_peace/cover_low_res.jpg");
                b6.setHighResCoverImagePath("books/war_and_peace/cover_high_res.jpg");
                b6.setAdditionalImages(Arrays.asList(
                        "books/war_and_peace/additional1.jpg",
                        "books/war_and_peace/additional2.jpg"
                ));
                long id6 = db.bookDao().insert(b6);
                db.bookTagDao().insert(new BookTag(id6, popularTagId));

                Book b7 = new Book("The Catcher in the Rye", "J.D. Salinger");
                b7.setDescriptionEn("A novel about teenage rebellion and self-discovery.");
                b7.setDescriptionRu("Роман о подростковом бунте и поиске себя.");
                b7.setPdfPath("books/the_catcher_in_the_rye/book.pdf");
                b7.setCoverImagePath("books/the_catcher_in_the_rye/cover_low_res.jpg");
                b7.setHighResCoverImagePath("books/the_catcher_in_the_rye/cover_high_res.jpg");
                b7.setAdditionalImages(Arrays.asList(
                        "books/the_catcher_in_the_rye/additional1.jpg",
                        "books/the_catcher_in_the_rye/additional2.jpg"
                ));
                long id7 = db.bookDao().insert(b7);
                db.bookTagDao().insert(new BookTag(id7, popularTagId));

                Book b8 = new Book("Jane Eyre", "Charlotte Brontë");
                b8.setDescriptionEn("A novel about a strong woman’s fight for love and independence.");
                b8.setDescriptionRu("Роман о сильной женщине и её борьбе за любовь и независимость.");
                b8.setPdfPath("books/jane_eyre/book.pdf");
                b8.setCoverImagePath("books/jane_eyre/cover_low_res.jpg");
                b8.setHighResCoverImagePath("books/jane_eyre/cover_high_res.jpg");
                b8.setAdditionalImages(Arrays.asList(
                        "books/jane_eyre/additional1.jpg",
                        "books/jane_eyre/additional2.jpg"
                ));
                long id8 = db.bookDao().insert(b8);
                db.bookTagDao().insert(new BookTag(id8, popularTagId));

                Book b9 = new Book("Crime and Punishment", "Fyodor Dostoevsky");
                b9.setDescriptionEn("A psychological novel about guilt and redemption.");
                b9.setDescriptionRu("Психологический роман о вине и искуплении.");
                b9.setPdfPath("books/crime_and_punishment/book.pdf");
                b9.setCoverImagePath("books/crime_and_punishment/cover_low_res.jpg");
                b9.setHighResCoverImagePath("books/crime_and_punishment/cover_high_res.jpg");
                b9.setAdditionalImages(Arrays.asList(
                        "books/crime_and_punishment/additional1.jpg",
                        "books/crime_and_punishment/additional2.jpg"
                ));
                long id9 = db.bookDao().insert(b9);
                db.bookTagDao().insert(new BookTag(id9, popularTagId));

                Book b10 = new Book("The Odyssey", "Homer");
                b10.setDescriptionEn("An epic poem about Odysseus’s adventures.");
                b10.setDescriptionRu("Эпическая поэма о приключениях Одиссея.");
                b10.setPdfPath("books/the_odyssey/book.pdf");
                b10.setCoverImagePath("books/the_odyssey/cover_low_res.jpg");
                b10.setHighResCoverImagePath("books/the_odyssey/cover_high_res.jpg");
                b10.setAdditionalImages(Arrays.asList(
                        "books/the_odyssey/additional1.jpg",
                        "books/the_odyssey/additional2.jpg"
                ));
                long id10 = db.bookDao().insert(b10);
                db.bookTagDao().insert(new BookTag(id10, popularTagId));

                // ==== EDITOR'S CHOICE (10 книг) ====
                Book e1 = new Book("Brave New World", "Aldous Huxley");
                e1.setDescriptionEn(genericDescEn);
                e1.setDescriptionRu(genericDescRu);
                e1.setPdfPath("books/brave_new_world/book.pdf");
                e1.setCoverImagePath("books/brave_new_world/cover_low_res.jpg");
                e1.setHighResCoverImagePath("books/brave_new_world/cover_high_res.jpg");
                e1.setAdditionalImages(Arrays.asList(
                        "books/brave_new_world/additional1.jpg",
                        "books/brave_new_world/additional2.jpg"
                ));
                long eid1 = db.bookDao().insert(e1);
                db.bookTagDao().insert(new BookTag(eid1, editorsTagId));

                Book e2 = new Book("The Scarlet Letter", "Nathaniel Hawthorne");
                e2.setDescriptionEn(genericDescEn);
                e2.setDescriptionRu(genericDescRu);
                e2.setPdfPath("books/the_scarlet_letter/book.pdf");
                e2.setCoverImagePath("books/the_scarlet_letter/cover_low_res.jpg");
                e2.setHighResCoverImagePath("books/the_scarlet_letter/cover_high_res.jpg");
                e2.setAdditionalImages(Arrays.asList(
                        "books/the_scarlet_letter/additional1.jpg",
                        "books/the_scarlet_letter/additional2.jpg"
                ));
                long eid2 = db.bookDao().insert(e2);
                db.bookTagDao().insert(new BookTag(eid2, editorsTagId));

                Book e3 = new Book("Wuthering Heights", "Emily Brontë");
                e3.setDescriptionEn(genericDescEn);
                e3.setDescriptionRu(genericDescRu);
                e3.setPdfPath("books/wuthering_heights/book.pdf");
                e3.setCoverImagePath("books/wuthering_heights/cover_low_res.jpg");
                e3.setHighResCoverImagePath("books/wuthering_heights/cover_high_res.jpg");
                e3.setAdditionalImages(Arrays.asList(
                        "books/wuthering_heights/additional1.jpg",
                        "books/wuthering_heights/additional2.jpg"
                ));
                long eid3 = db.bookDao().insert(e3);
                db.bookTagDao().insert(new BookTag(eid3, editorsTagId));

                Book e4 = new Book("The Iliad", "Homer");
                e4.setDescriptionEn(genericDescEn);
                e4.setDescriptionRu(genericDescRu);
                e4.setPdfPath("books/the_iliad/book.pdf");
                e4.setCoverImagePath("books/the_iliad/cover_low_res.jpg");
                e4.setHighResCoverImagePath("books/the_iliad/cover_high_res.jpg");
                e4.setAdditionalImages(Arrays.asList(
                        "books/the_iliad/additional1.jpg",
                        "books/the_iliad/additional2.jpg"
                ));
                long eid4 = db.bookDao().insert(e4);
                db.bookTagDao().insert(new BookTag(eid4, editorsTagId));

                Book e5 = new Book("Don Quixote", "Miguel de Cervantes");
                e5.setDescriptionEn(genericDescEn);
                e5.setDescriptionRu(genericDescRu);
                e5.setPdfPath("books/don_quixote/book.pdf");
                e5.setCoverImagePath("books/don_quixote/cover_low_res.jpg");
                e5.setHighResCoverImagePath("books/don_quixote/cover_high_res.jpg");
                e5.setAdditionalImages(Arrays.asList(
                        "books/don_quixote/additional1.jpg",
                        "books/don_quixote/additional2.jpg"
                ));
                long eid5 = db.bookDao().insert(e5);
                db.bookTagDao().insert(new BookTag(eid5, editorsTagId));

                Book e6 = new Book("Anna Karenina", "Leo Tolstoy");
                e6.setDescriptionEn(genericDescEn);
                e6.setDescriptionRu(genericDescRu);
                e6.setPdfPath("books/anna_karenina/book.pdf");
                e6.setCoverImagePath("books/anna_karenina/cover_low_res.jpg");
                e6.setHighResCoverImagePath("books/anna_karenina/cover_high_res.jpg");
                e6.setAdditionalImages(Arrays.asList(
                        "books/anna_karenina/additional1.jpg",
                        "books/anna_karenina/additional2.jpg"
                ));
                long eid6 = db.bookDao().insert(e6);
                db.bookTagDao().insert(new BookTag(eid6, editorsTagId));

                Book e7 = new Book("Les Misérables", "Victor Hugo");
                e7.setDescriptionEn(genericDescEn);
                e7.setDescriptionRu(genericDescRu);
                e7.setPdfPath("books/les_miserables/book.pdf");
                e7.setCoverImagePath("books/les_miserables/cover_low_res.jpg");
                e7.setHighResCoverImagePath("books/les_miserables/cover_high_res.jpg");
                e7.setAdditionalImages(Arrays.asList(
                        "books/les_miserables/additional1.jpg",
                        "books/les_miserables/additional2.jpg"
                ));
                long eid7 = db.bookDao().insert(e7);
                db.bookTagDao().insert(new BookTag(eid7, editorsTagId));

                Book e8 = new Book("The Brothers Karamazov", "Fyodor Dostoevsky");
                e8.setDescriptionEn(genericDescEn);
                e8.setDescriptionRu(genericDescRu);
                e8.setPdfPath("books/the_brothers_karamazov/book.pdf");
                e8.setCoverImagePath("books/the_brothers_karamazov/cover_low_res.jpg");
                e8.setHighResCoverImagePath("books/the_brothers_karamazov/cover_high_res.jpg");
                e8.setAdditionalImages(Arrays.asList(
                        "books/the_brothers_karamazov/additional1.jpg",
                        "books/the_brothers_karamazov/additional2.jpg"
                ));
                long eid8 = db.bookDao().insert(e8);
                db.bookTagDao().insert(new BookTag(eid8, editorsTagId));

                Book e9 = new Book("Great Expectations", "Charles Dickens");
                e9.setDescriptionEn(genericDescEn);
                e9.setDescriptionRu(genericDescRu);
                e9.setPdfPath("books/great_expectations/book.pdf");
                e9.setCoverImagePath("books/great_expectations/cover_low_res.jpg");
                e9.setHighResCoverImagePath("books/great_expectations/cover_high_res.jpg");
                e9.setAdditionalImages(Arrays.asList(
                        "books/great_expectations/additional1.jpg",
                        "books/great_expectations/additional2.jpg"
                ));
                long eid9 = db.bookDao().insert(e9);
                db.bookTagDao().insert(new BookTag(eid9, editorsTagId));

                Book e10 = new Book("Ulysses", "James Joyce");
                e10.setDescriptionEn(genericDescEn);
                e10.setDescriptionRu(genericDescRu);
                e10.setPdfPath("books/ulysses/book.pdf");
                e10.setCoverImagePath("books/ulysses/cover_low_res.jpg");
                e10.setHighResCoverImagePath("books/ulysses/cover_high_res.jpg");
                e10.setAdditionalImages(Arrays.asList(
                        "books/ulysses/additional1.jpg",
                        "books/ulysses/additional2.jpg"
                ));
                long eid10 = db.bookDao().insert(e10);
                db.bookTagDao().insert(new BookTag(eid10, editorsTagId));
            }
        });
    }
}