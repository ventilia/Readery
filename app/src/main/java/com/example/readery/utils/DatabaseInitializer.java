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
                Tag classicTag = new Tag("Classic");
                long classicTagId = db.tagDao().insert(classicTag);
                Tag romanceTag = new Tag("Romance");
                long romanceTagId = db.tagDao().insert(romanceTag);

                // Книга с id 1, соответствующим Firebase
                Book b3 = new Book();
                b3.setId(1); // Явно задаем id 1
                b3.setTitleEn("Anna Karenina");
                b3.setTitleRu("Анна Каренина");
                b3.setAuthorEn("Leo Tolstoy");
                b3.setAuthorRu("Лев Толстой");
                b3.setDescriptionEn("Anna Karenina is a novel by Leo Tolstoy, published in 1877. It tells the story of Anna Karenina, a married aristocrat who has an affair with the affluent Count Vronsky, leading to her social downfall. The novel explores themes of love, infidelity, and the constraints of society, set against the backdrop of Russian high society. Tolstoy's work is renowned for its depth of character and its exploration of moral and philosophical questions. The novel is set in 19th-century Russia and follows not only Anna's tragic story but also the lives of other characters, such as Konstantin Levin, who represents Tolstoy's own philosophical struggles. The book is divided into eight parts and is known for its realistic depiction of Russian society, its psychological depth, and its exploration of themes like faith, family, and the meaning of life. Tolstoy's masterful storytelling and his ability to weave multiple narratives together have made 'Anna Karenina' one of the greatest novels in world literature.");
                b3.setDescriptionRu("Анна Каренина — роман Льва Толстого, опубликованный в 1877 году. Он рассказывает историю Анны Карениной, замужней аристократки, которая вступает в связь с богатым графом Вронским, что приводит к её социальному падению. Роман исследует темы любви, неверности и социальных ограничений на фоне русского высшего общества. Произведение Толстого известно своей глубиной характеров и исследованием моральных и философских вопросов. Роман разворачивается в России XIX века и следует не только трагической истории Анны, но и жизням других персонажей, таких как Константин Левин, который отражает собственные философские искания Толстого. Книга разделена на восемь частей и известна своим реалистичным изображением русского общества, психологической глубиной и исследованием тем веры, семьи и смысла жизни. Мастерское повествование Толстого и его способность сплетать множество сюжетных линий вместе сделали 'Анну Каренину' одним из величайших романов мировой литературы.");
                b3.setCoverImagePathEn("books/anna_karenina/cover_low_res_en.jpg");
                b3.setCoverImagePathRu("books/anna_karenina/cover_low_res_ru.jpg");
                b3.setHighResCoverImagePathEn("books/anna_karenina/cover_high_res_en.jpg");
                b3.setHighResCoverImagePathRu("books/anna_karenina/cover_high_res_ru.jpg");
                b3.setAdditionalImagesEn(Arrays.asList(
                        "books/anna_karenina/additional1_en.jpg",
                        "books/anna_karenina/additional2_en.jpg"
                ));
                b3.setAdditionalImagesRu(Arrays.asList(
                        "books/anna_karenina/additional1_ru.jpg",
                        "books/anna_karenina/additional2_ru.jpg"
                ));
                db.bookDao().insert(b3); // Вставляем книгу с id 1
                // Связываем книгу со всеми тегами
                db.bookTagDao().insert(new BookTag(1, newTagId));
                db.bookTagDao().insert(new BookTag(1, classicTagId));
                db.bookTagDao().insert(new BookTag(1, romanceTagId));
            }
        });
    }
}