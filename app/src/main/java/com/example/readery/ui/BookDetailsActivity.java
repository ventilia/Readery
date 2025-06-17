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
import java.util.ArrayList;
import java.util.List;

public class BookDetailsActivity extends AppCompatActivity {
    private BookDetailsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        // Получение ID книги из Intent
        long bookId = getIntent().getLongExtra("bookId", -1);
        if (bookId == -1) {
            finish();
            return;
        }

        // Инициализация ViewModel
        viewModel = new ViewModelProvider(this).get(BookDetailsViewModel.class);
        viewModel.setBookId(bookId);

        // Наблюдение за данными книги
        viewModel.getBook().observe(this, book -> {
            if (book != null) {
                // Установка текста для заголовка, автора и описания
                TextView titleView = findViewById(R.id.book_title);
                titleView.setText(book.getTitle());

                TextView authorView = findViewById(R.id.book_author);
                authorView.setText(book.getAuthor());

                TextView descriptionView = findViewById(R.id.book_description);
                descriptionView.setText(book.getDescription(this)); // Учет локали

                // Настройка ViewPager для изображений
                ViewPager imagePager = findViewById(R.id.image_pager);

                List<String> allImages = new ArrayList<>();
                // Добавление изображения высокого разрешения или обычного обложки
                if (book.getHighResCoverImagePath() != null && !book.getHighResCoverImagePath().isEmpty()) {
                    allImages.add(book.getHighResCoverImagePath());
                } else if (book.getCoverImagePath() != null && !book.getCoverImagePath().isEmpty()) {
                    allImages.add(book.getCoverImagePath()); // Запасной вариант
                }
                // Добавление дополнительных изображений
                if (book.getAdditionalImages() != null) {
                    allImages.addAll(book.getAdditionalImages());
                }

                // Установка адаптера для ViewPager
                ImagePagerAdapter pagerAdapter = new ImagePagerAdapter(this, allImages);
                imagePager.setAdapter(pagerAdapter);
            }
        });
    }
}