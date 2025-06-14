package com.example.readery.ui;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import com.example.readery.R;
import com.example.readery.data.Book;
import com.example.readery.utils.ImagePagerAdapter;
import com.example.readery.viewmodel.BookDetailsViewModel;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * Активность для отображения подробной информации о книге.
 */
public class BookDetailsActivity extends AppCompatActivity {
    private BookDetailsViewModel viewModel; // ViewModel для получения данных о книге

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details); // Используем обновленный layout

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

                // Установка полного описания книги
                TextView descriptionView = findViewById(R.id.book_description);
                descriptionView.setText(book.getDescription());

                // Настройка ViewPager для отображения изображений (обложка + дополнительные)
                ViewPager imagePager = findViewById(R.id.image_pager);
                TabLayout tabLayout = findViewById(R.id.tab_layout);

                List<String> allImages = new ArrayList<>();
                if (book.getCoverImagePath() != null && !book.getCoverImagePath().isEmpty()) {
                    allImages.add(book.getCoverImagePath());
                }
                if (book.getAdditionalImages() != null) {
                    allImages.addAll(book.getAdditionalImages());
                }
                ImagePagerAdapter pagerAdapter = new ImagePagerAdapter(this, allImages);
                imagePager.setAdapter(pagerAdapter);

                // Связываем TabLayout с ViewPager для отображения индикаторов
                tabLayout.setupWithViewPager(imagePager);
            }
        });
    }
}