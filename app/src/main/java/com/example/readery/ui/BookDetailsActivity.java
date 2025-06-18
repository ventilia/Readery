package com.example.readery.ui;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import com.example.readery.R;
import com.example.readery.data.Book;
import com.example.readery.utils.ImagePagerAdapter;
import com.example.readery.utils.SettingsManager;
import com.example.readery.viewmodel.BookDetailsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * активность для отображения детальной информации о книге.
 * actionbar скрыт, все текстовые данные поддерживают локализацию.
 */
public class BookDetailsActivity extends AppCompatActivity {
    private BookDetailsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        // скрываем actionbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // получаем id книги из intent
        long bookId = getIntent().getLongExtra("bookId", -1);
        if (bookId == -1) {
            finish(); // закрываем активность, если id книги не передан
            return;
        }

        // инициализируем viewmodel
        viewModel = new ViewModelProvider(this).get(BookDetailsViewModel.class);
        viewModel.setBookId(bookId);

        // наблюдаем за данными книги
        viewModel.getBook().observe(this, book -> {
            if (book != null) {
                // устанавливаем переведенные данные книги
                TextView titleView = findViewById(R.id.book_title);
                titleView.setText(book.getTitle(this));

                TextView authorView = findViewById(R.id.book_author);
                authorView.setText(book.getAuthor(this));

                TextView descriptionView = findViewById(R.id.book_description);
                descriptionView.setText(book.getDescription(this));

                // настраиваем viewpager для отображения изображений
                ViewPager imagePager = findViewById(R.id.image_pager);
                List<String> allImages = new ArrayList<>();

                // добавляем изображение высокого разрешения, если оно есть
                if (book.getHighResCoverImagePath() != null && !book.getHighResCoverImagePath().isEmpty()) {
                    allImages.add(book.getHighResCoverImagePath());
                }
                // в качестве запасного варианта добавляем обычное изображение обложки
                else if (book.getCoverImagePath() != null && !book.getCoverImagePath().isEmpty()) {
                    allImages.add(book.getCoverImagePath());
                }
                // добавляем дополнительные изображения, если они есть
                if (book.getAdditionalImages() != null) {
                    allImages.addAll(book.getAdditionalImages());
                }

                // устанавливаем адаптер для viewpager
                ImagePagerAdapter pagerAdapter = new ImagePagerAdapter(this, allImages);
                imagePager.setAdapter(pagerAdapter);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        // устанавливаем локаль на основе сохраненных настроек
        SettingsManager settingsManager = SettingsManager.getInstance(base);
        String lang = settingsManager.getLanguage();
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);
        base = base.createConfigurationContext(config);
        super.attachBaseContext(base);
    }
}