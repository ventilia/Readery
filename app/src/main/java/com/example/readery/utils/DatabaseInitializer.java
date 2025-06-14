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
                // Добавление тега "Классика"
                Tag classicTag = new Tag("Classic");
                long classicTagId = db.tagDao().insert(classicTag);

                // 1. Гордость и предубеждение
                Book book1 = new Book("Гордость и предубеждение", "Джейн Остин");
                book1.setDescription("Роман о любви и социальных нормах в Англии 19 века.");
                book1.setPdfPath("books/pride_and_prejudice/book.pdf");
                book1.setCoverImagePath("books/pride_and_prejudice/cover.jpg");
                book1.setAdditionalImages(Arrays.asList(
                        "books/pride_and_prejudice/additional1.jpg",
                        "books/pride_and_prejudice/additional2.jpg"
                ));
                long book1Id = db.bookDao().insert(book1);
                db.bookTagDao().insert(new BookTag(book1Id, classicTagId));

                // 2. Убить пересмешника
                Book book2 = new Book("Убить пересмешника", "Харпер Ли");
                book2.setDescription("Роман о расовой несправедливости и взрослении на юге США.");
                book2.setPdfPath("books/to_kill_a_mockingbird/book.pdf");
                book2.setCoverImagePath("books/to_kill_a_mockingbird/cover.jpg");
                book2.setAdditionalImages(Arrays.asList(
                        "books/to_kill_a_mockingbird/additional1.jpg",
                        "books/to_kill_a_mockingbird/additional2.jpg"
                ));
                long book2Id = db.bookDao().insert(book2);
                db.bookTagDao().insert(new BookTag(book2Id, classicTagId));

                // 3. 1984
                Book book3 = new Book("1984", "Джордж Оруэлл");
                book3.setDescription("Дистопический роман о тоталитарном обществе.");
                book3.setPdfPath("books/1984/book.pdf");
                book3.setCoverImagePath("books/1984/cover.jpg");
                book3.setAdditionalImages(Arrays.asList(
                        "books/1984/additional1.jpg",
                        "books/1984/additional2.jpg"
                ));
                long book3Id = db.bookDao().insert(book3);
                db.bookTagDao().insert(new BookTag(book3Id, classicTagId));

                // 4. Великий Гэтсби
                Book book4 = new Book("Великий Гэтсби", "Фрэнсис Скотт Фицджеральд");
                book4.setDescription("Роман о американской мечте и упадке в 1920-х годах.");
                book4.setPdfPath("books/the_great_gatsby/book.pdf");
                book4.setCoverImagePath("books/the_great_gatsby/cover.jpg");
                book4.setAdditionalImages(Arrays.asList(
                        "books/the_great_gatsby/additional1.jpg",
                        "books/the_great_gatsby/additional2.jpg"
                ));
                long book4Id = db.bookDao().insert(book4);
                db.bookTagDao().insert(new BookTag(book4Id, classicTagId));

                // 5. Моби Дик
                Book book5 = new Book("Моби Дик", "Герман Мелвилл");
                book5.setDescription("Эпическая история о капитане Ахаве и его одержимости белым китом.");
                book5.setPdfPath("books/moby_dick/book.pdf");
                book5.setCoverImagePath("books/moby_dick/cover.jpg");
                book5.setAdditionalImages(Arrays.asList(
                        "books/moby_dick/additional1.jpg",
                        "books/moby_dick/additional2.jpg"
                ));
                long book5Id = db.bookDao().insert(book5);
                db.bookTagDao().insert(new BookTag(book5Id, classicTagId));

                // 6. Война и мир
                Book book6 = new Book("Война и мир", "Лев Толстой");
                book6.setDescription("Масштабный роман о русской жизни во времена наполеоновских войн.");
                book6.setPdfPath("books/war_and_peace/book.pdf");
                book6.setCoverImagePath("books/war_and_peace/cover.jpg");
                book6.setAdditionalImages(Arrays.asList(
                        "books/war_and_peace/additional1.jpg",
                        "books/war_and_peace/additional2.jpg"
                ));
                long book6Id = db.bookDao().insert(book6);
                db.bookTagDao().insert(new BookTag(book6Id, classicTagId));

                // 7. Над пропастью во ржи
                Book book7 = new Book("Над пропастью во ржи", "Джером Сэлинджер");
                book7.setDescription("Роман о подростковом бунте и поиске себя.");
                book7.setPdfPath("books/the_catcher_in_the_rye/book.pdf");
                book7.setCoverImagePath("books/the_catcher_in_the_rye/cover.jpg");
                book7.setAdditionalImages(Arrays.asList(
                        "books/the_catcher_in_the_rye/additional1.jpg",
                        "books/the_catcher_in_the_rye/additional2.jpg"
                ));
                long book7Id = db.bookDao().insert(book7);
                db.bookTagDao().insert(new BookTag(book7Id, classicTagId));

                // 8. Джейн Эйр
                Book book8 = new Book("Джейн Эйр", "Шарлотта Бронте");
                book8.setDescription("Роман о сильной женщине и ее борьбе за любовь и независимость.");
                book8.setPdfPath("books/jane_eyre/book.pdf");
                book8.setCoverImagePath("books/jane_eyre/cover.jpg");
                book8.setAdditionalImages(Arrays.asList(
                        "books/jane_eyre/additional1.jpg",
                        "books/jane_eyre/additional2.jpg"
                ));
                long book8Id = db.bookDao().insert(book8);
                db.bookTagDao().insert(new BookTag(book8Id, classicTagId));

                // 9. Преступление и наказание
                Book book9 = new Book("Преступление и наказание", "Фёдор Достоевский");
                book9.setDescription("Психологический роман о вине и искуплении.");
                book9.setPdfPath("books/crime_and_punishment/book.pdf");
                book9.setCoverImagePath("books/crime_and_punishment/cover.jpg");
                book9.setAdditionalImages(Arrays.asList(
                        "books/crime_and_punishment/additional1.jpg",
                        "books/crime_and_punishment/additional2.jpg"
                ));
                long book9Id = db.bookDao().insert(book9);
                db.bookTagDao().insert(new BookTag(book9Id, classicTagId));

                // 10. Одиссея
                Book book10 = new Book("Одиссея", "Гомер");
                book10.setDescription("Эпическая поэма о приключениях Одиссея.");
                book10.setPdfPath("books/the_odyssey/book.pdf");
                book10.setCoverImagePath("books/the_odyssey/cover.jpg");
                book10.setAdditionalImages(Arrays.asList(
                        "books/the_odyssey/additional1.jpg",
                        "books/the_odyssey/additional2.jpg"
                ));
                long book10Id = db.bookDao().insert(book10);
                db.bookTagDao().insert(new BookTag(book10Id, classicTagId));

                String genericDescription = "Классическое произведение мировой литературы.";

                // 11. О дивный новый мир
                Book book11 = new Book("О дивный новый мир", "Олдос Хаксли");
                book11.setDescription(genericDescription);
                book11.setPdfPath("books/brave_new_world/book.pdf");
                book11.setCoverImagePath("books/brave_new_world/cover.jpg");
                book11.setAdditionalImages(Arrays.asList(
                        "books/brave_new_world/additional1.jpg",
                        "books/brave_new_world/additional2.jpg"
                ));
                long book11Id = db.bookDao().insert(book11);
                db.bookTagDao().insert(new BookTag(book11Id, classicTagId));

                // 12. Алая буква
                Book book12 = new Book("Алая буква", "Натаниэль Готорн");
                book12.setDescription(genericDescription);
                book12.setPdfPath("books/the_scarlet_letter/book.pdf");
                book12.setCoverImagePath("books/the_scarlet_letter/cover.jpg");
                book12.setAdditionalImages(Arrays.asList(
                        "books/the_scarlet_letter/additional1.jpg",
                        "books/the_scarlet_letter/additional2.jpg"
                ));
                long book12Id = db.bookDao().insert(book12);
                db.bookTagDao().insert(new BookTag(book12Id, classicTagId));

                // 13. Грозовой перевал
                Book book13 = new Book("Грозовой перевал", "Эмили Бронте");
                book13.setDescription(genericDescription);
                book13.setPdfPath("books/wuthering_heights/book.pdf");
                book13.setCoverImagePath("books/wuthering_heights/cover.jpg");
                book13.setAdditionalImages(Arrays.asList(
                        "books/wuthering_heights/additional1.jpg",
                        "books/wuthering_heights/additional2.jpg"
                ));
                long book13Id = db.bookDao().insert(book13);
                db.bookTagDao().insert(new BookTag(book13Id, classicTagId));

                // 14. Илиада
                Book book14 = new Book("Илиада", "Гомер");
                book14.setDescription(genericDescription);
                book14.setPdfPath("books/the_iliad/book.pdf");
                book14.setCoverImagePath("books/the_iliad/cover.jpg");
                book14.setAdditionalImages(Arrays.asList(
                        "books/the_iliad/additional1.jpg",
                        "books/the_iliad/additional2.jpg"
                ));
                long book14Id = db.bookDao().insert(book14);
                db.bookTagDao().insert(new BookTag(book14Id, classicTagId));

                // 15. Дон Кихот
                Book book15 = new Book("Дон Кихот", "Мигель де Сервантес");
                book15.setDescription(genericDescription);
                book15.setPdfPath("books/don_quixote/book.pdf");
                book15.setCoverImagePath("books/don_quixote/cover.jpg");
                book15.setAdditionalImages(Arrays.asList(
                        "books/don_quixote/additional1.jpg",
                        "books/don_quixote/additional2.jpg"
                ));
                long book15Id = db.bookDao().insert(book15);
                db.bookTagDao().insert(new BookTag(book15Id, classicTagId));

                // 16. Анна Каренина
                Book book16 = new Book("Анна Каренина", "Лев Толстой");
                book16.setDescription(genericDescription);
                book16.setPdfPath("books/anna_karenina/book.pdf");
                book16.setCoverImagePath("books/anna_karenina/cover.jpg");
                book16.setAdditionalImages(Arrays.asList(
                        "books/anna_karenina/additional1.jpg",
                        "books/anna_karenina/additional2.jpg"
                ));
                long book16Id = db.bookDao().insert(book16);
                db.bookTagDao().insert(new BookTag(book16Id, classicTagId));

                // 17. Отверженные
                Book book17 = new Book("Отверженные", "Виктор Гюго");
                book17.setDescription(genericDescription);
                book17.setPdfPath("books/les_miserables/book.pdf");
                book17.setCoverImagePath("books/les_miserables/cover.jpg");
                book17.setAdditionalImages(Arrays.asList(
                        "books/les_miserables/additional1.jpg",
                        "books/les_miserables/additional2.jpg"
                ));
                long book17Id = db.bookDao().insert(book17);
                db.bookTagDao().insert(new BookTag(book17Id, classicTagId));

                // 18. Братья Карамазовы
                Book book18 = new Book("Братья Карамазовы", "Фёдор Достоевский");
                book18.setDescription(genericDescription);
                book18.setPdfPath("books/the_brothers_karamazov/book.pdf");
                book18.setCoverImagePath("books/the_brothers_karamazov/cover.jpg");
                book18.setAdditionalImages(Arrays.asList(
                        "books/the_brothers_karamazov/additional1.jpg",
                        "books/the_brothers_karamazov/additional2.jpg"
                ));
                long book18Id = db.bookDao().insert(book18);
                db.bookTagDao().insert(new BookTag(book18Id, classicTagId));

                // 19. Большие надежды
                Book book19 = new Book("Большие надежды", "Чарльз Диккенс");
                book19.setDescription(genericDescription);
                book19.setPdfPath("books/great_expectations/book.pdf");
                book19.setCoverImagePath("books/great_expectations/cover.jpg");
                book19.setAdditionalImages(Arrays.asList(
                        "books/great_expectations/additional1.jpg",
                        "books/great_expectations/additional2.jpg"
                ));
                long book19Id = db.bookDao().insert(book19);
                db.bookTagDao().insert(new BookTag(book19Id, classicTagId));

                // 20. Улисс
                Book book20 = new Book("Улисс", "Джеймс Джойс");
                book20.setDescription(genericDescription);
                book20.setPdfPath("books/ulysses/book.pdf");
                book20.setCoverImagePath("books/ulysses/cover.jpg");
                book20.setAdditionalImages(Arrays.asList(
                        "books/ulysses/additional1.jpg",
                        "books/ulysses/additional2.jpg"
                ));
                long book20Id = db.bookDao().insert(book20);
                db.bookTagDao().insert(new BookTag(book20Id, classicTagId));
            }
        });
    }
}