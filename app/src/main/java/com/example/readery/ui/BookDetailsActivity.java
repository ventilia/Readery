package com.example.readery.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import com.example.readery.R;
import com.example.readery.data.Book;
import com.example.readery.utils.ImagePagerAdapter;
import com.example.readery.utils.SettingsManager;
import com.example.readery.viewmodel.BookDetailsViewModel;
import me.relex.circleindicator.CircleIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * активность для отображения детальной информации о книге.
 * actionbar скрыт, текст поддерживает локализацию и сворачивание.
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
            finish(); // закрываем активность, если id не передан
            return;
        }

        // инициализируем viewmodel
        viewModel = new ViewModelProvider(this).get(BookDetailsViewModel.class);
        viewModel.setBookId(bookId);

        // наблюдаем за данными книги
        viewModel.getBook().observe(this, book -> {
            if (book != null) {
                // устанавливаем переведенные данные
                TextView titleView = findViewById(R.id.book_title);
                titleView.setText(book.getTitle(this));

                TextView authorView = findViewById(R.id.book_author);
                authorView.setText(book.getAuthor(this));

                TextView descriptionView = findViewById(R.id.book_description);
                setupDescription(descriptionView, book.getDescription(this));

                // настраиваем viewpager для изображений
                ViewPager imagePager = findViewById(R.id.image_pager);
                List<String> allImages = new ArrayList<>();

                // добавляем изображение высокого разрешения
                if (book.getHighResCoverImagePath() != null && !book.getHighResCoverImagePath().isEmpty()) {
                    allImages.add(book.getHighResCoverImagePath());
                }
                // запасной вариант - обычная обложка
                else if (book.getCoverImagePath() != null && !book.getCoverImagePath().isEmpty()) {
                    allImages.add(book.getCoverImagePath());
                }
                // дополнительные изображения
                if (book.getAdditionalImages() != null) {
                    allImages.addAll(book.getAdditionalImages());
                }

                // устанавливаем адаптер для viewpager
                ImagePagerAdapter pagerAdapter = new ImagePagerAdapter(this, allImages);
                imagePager.setAdapter(pagerAdapter);

                // связываем индикатор с viewpager
                CircleIndicator indicator = findViewById(R.id.indicator);
                indicator.setViewPager(imagePager);
            }
        });
    }

    /**
     * настраивает описание с возможностью сворачивания/разворачивания.
     * @param descriptionView TextView для отображения текста
     * @param fullDescription полное описание книги
     */
    private void setupDescription(TextView descriptionView, String fullDescription) {
        String[] words = fullDescription.split("\\s+");
        if (words.length > 25) {
            String shortDescription = String.join(" ", Arrays.copyOfRange(words, 0, 25)) + " ... ";
            SpannableString expandText = new SpannableString(shortDescription + "развернуть");
            expandText.setSpan(
                    new ForegroundColorSpan(getResources().getColor(android.R.color.holo_blue_dark)),
                    shortDescription.length(),
                    expandText.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            descriptionView.setText(expandText);

            descriptionView.setOnClickListener(v -> {
                CharSequence currentText = descriptionView.getText();
                if (currentText.toString().endsWith("развернуть")) {
                    SpannableString collapseText = new SpannableString(fullDescription + " скрыть");
                    collapseText.setSpan(
                            new ForegroundColorSpan(getResources().getColor(android.R.color.holo_blue_dark)),
                            fullDescription.length() + 1,
                            collapseText.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                    descriptionView.setText(collapseText);
                } else if (currentText.toString().endsWith("скрыть")) {
                    descriptionView.setText(expandText);
                }
            });
        } else {
            descriptionView.setText(fullDescription);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        // устанавливаем локаль на основе настроек
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