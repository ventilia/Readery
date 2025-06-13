package com.example.readery.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.readery.R;
import com.example.readery.data.Book;
import com.example.readery.utils.ImagePagerAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        // Получение данных книги из Intent
        Book book = (Book) getIntent().getSerializableExtra("book");
        if (book == null) {
            finish();
            return;
        }

        // Установка заголовка и автора
        TextView titleView = findViewById(R.id.book_title);
        TextView authorView = findViewById(R.id.book_author);
        titleView.setText(book.getTitle());
        authorView.setText(book.getAuthor());

        // Настройка ViewPager для изображений
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

        // Настройка описания с разворачиванием и сворачиванием
        TextView descriptionView = findViewById(R.id.book_description);
        Button expandButton = findViewById(R.id.expand_button);
        Button collapseButton = findViewById(R.id.collapse_button);

        String fullDescription = book.getDescription();
        String[] words = fullDescription.split("\\s+");
        if (words.length > 50) {
            String shortDescription = String.join(" ", Arrays.copyOfRange(words, 0, 50)) + "...";
            descriptionView.setText(shortDescription);
            expandButton.setVisibility(View.VISIBLE);
        } else {
            descriptionView.setText(fullDescription);
        }

        expandButton.setOnClickListener(v -> {
            descriptionView.setText(fullDescription);
            descriptionView.setMaxLines(Integer.MAX_VALUE);
            expandButton.setVisibility(View.GONE);
            collapseButton.setVisibility(View.VISIBLE);
            descriptionView.animate().alpha(1f).setDuration(300).start();
        });

        collapseButton.setOnClickListener(v -> {
            String shortDescription = String.join(" ", Arrays.copyOfRange(words, 0, 50)) + "...";
            descriptionView.setText(shortDescription);
            descriptionView.setMaxLines(3);
            collapseButton.setVisibility(View.GONE);
            expandButton.setVisibility(View.VISIBLE);
            descriptionView.animate().alpha(1f).setDuration(300).start();
        });
    }
}