package com.example.readery.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import com.example.readery.R;
import com.example.readery.data.Book;
import com.example.readery.utils.ImagePagerAdapter;
import com.example.readery.viewmodel.BookDetailsViewModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Активность для отображения подробной информации о книге.
 */
public class BookDetailsActivity extends AppCompatActivity {
    private BookDetailsViewModel viewModel; // ViewModel для получения данных о книге

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        // Получаем bookId из Intent, переданного из фрагмента
        long bookId = getIntent().getLongExtra("bookId", -1);
        if (bookId == -1) {
            // Если bookId не передан или неверный, завершаем активность
            finish();
            return;
        }

        // Инициализируем ViewModel для работы с данными книги
        viewModel = new ViewModelProvider(this).get(BookDetailsViewModel.class);
        viewModel.setBookId(bookId); // Устанавливаем bookId для загрузки данных

        // Наблюдаем за LiveData<Book> и обновляем UI, когда данные получены
        viewModel.getBook().observe(this, book -> {
            if (book != null) {
                // Установка заголовка книги
                TextView titleView = findViewById(R.id.book_title);
                titleView.setText(book.getTitle());

                // Установка автора книги
                TextView authorView = findViewById(R.id.book_author);
                authorView.setText(book.getAuthor());

                // Настройка ViewPager для отображения изображений (обложка + дополнительные)
                ViewPager imagePager = findViewById(R.id.image_pager);
                List<String> allImages = new ArrayList<>();
                if (book.getCoverImagePath() != null && !book.getCoverImagePath().isEmpty()) {
                    allImages.add(book.getCoverImagePath());
                }
                if (book.getAdditionalImages() != null) {
                    allImages.addAll(book.getAdditionalImages());
                }
                ImagePagerAdapter pagerAdapter = new ImagePagerAdapter(this, allImages);
                imagePager.setAdapter(pagerAdapter);

                // Настройка описания с возможностью разворачивания и сворачивания
                TextView descriptionView = findViewById(R.id.book_description);
                Button expandButton = findViewById(R.id.expand_button);
                Button collapseButton = findViewById(R.id.collapse_button);

                String fullDescription = book.getDescription();
                String[] words = fullDescription.split("\\s+");
                if (words.length > 50) {
                    // Если описание длиннее 50 слов, показываем короткую версию
                    String shortDescription = String.join(" ", Arrays.copyOfRange(words, 0, 50)) + "...";
                    descriptionView.setText(shortDescription);
                    descriptionView.setMaxLines(3); // Ограничиваем до 3 строк
                    expandButton.setVisibility(View.VISIBLE); // Показываем кнопку "Развернуть"
                    collapseButton.setVisibility(View.GONE);  // Скрываем кнопку "Свернуть"
                } else {
                    // Если описание короткое, показываем полную версию
                    descriptionView.setText(fullDescription);
                    descriptionView.setMaxLines(Integer.MAX_VALUE); // Без ограничения строк
                    expandButton.setVisibility(View.GONE); // Скрываем обе кнопки
                    collapseButton.setVisibility(View.GONE);
                }

                // Обработчик нажатия на кнопку "Развернуть"
                expandButton.setOnClickListener(v -> {
                    descriptionView.setText(fullDescription);
                    descriptionView.setMaxLines(Integer.MAX_VALUE); // Показываем весь текст
                    expandButton.setVisibility(View.GONE);
                    collapseButton.setVisibility(View.VISIBLE);
                    descriptionView.animate().alpha(1f).setDuration(300).start(); // Анимация
                });

                // Обработчик нажатия на кнопку "Свернуть"
                collapseButton.setOnClickListener(v -> {
                    String shortDescription = String.join(" ", Arrays.copyOfRange(words, 0, 50)) + "...";
                    descriptionView.setText(shortDescription);
                    descriptionView.setMaxLines(3); // Сворачиваем до 3 строк
                    collapseButton.setVisibility(View.GONE);
                    expandButton.setVisibility(View.VISIBLE);
                    descriptionView.animate().alpha(1f).setDuration(300).start(); // Анимация
                });
            }
        });
    }
}