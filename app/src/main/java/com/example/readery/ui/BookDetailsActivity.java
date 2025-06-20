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



public class BookDetailsActivity extends AppCompatActivity {
    private BookDetailsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);


        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // получаем id книги
        long bookId = getIntent().getLongExtra("bookId", -1);
        if (bookId == -1) {
            finish();
            return;
        }


        viewModel = new ViewModelProvider(this).get(BookDetailsViewModel.class);
        viewModel.setBookId(bookId);


        viewModel.getBook().observe(this, book -> {
            if (book != null) {

                TextView titleView = findViewById(R.id.book_title);
                titleView.setText(book.getTitle(this));

                TextView authorView = findViewById(R.id.book_author);
                authorView.setText(book.getAuthor(this));

                TextView descriptionView = findViewById(R.id.book_description);
                setupDescription(descriptionView, book.getDescription(this));


                ViewPager imagePager = findViewById(R.id.image_pager);
                List<String> allImages = new ArrayList<>();


                if (book.getHighResCoverImagePath() != null && !book.getHighResCoverImagePath().isEmpty()) {
                    allImages.add(book.getHighResCoverImagePath());
                }

                else if (book.getCoverImagePath() != null && !book.getCoverImagePath().isEmpty()) {
                    allImages.add(book.getCoverImagePath());
                }

                if (book.getAdditionalImages() != null) {
                    allImages.addAll(book.getAdditionalImages());
                }

                ImagePagerAdapter pagerAdapter = new ImagePagerAdapter(this, allImages);
                imagePager.setAdapter(pagerAdapter);

                CircleIndicator indicator = findViewById(R.id.indicator);
                indicator.setViewPager(imagePager);
            }
        });
    }


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