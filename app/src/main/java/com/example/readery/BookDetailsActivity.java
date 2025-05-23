package com.example.readery;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.TextPaint;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class BookDetailsActivity extends AppCompatActivity {
    private ViewPager photoViewPager;
    private TextView titleTextView, authorTextView, descriptionShortTextView, expandDescriptionTextView, descriptionFullTextView, priceTextView;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        db = FirebaseFirestore.getInstance();
        String bookId = getIntent().getStringExtra("book_id");

        photoViewPager = findViewById(R.id.photo_viewpager);
        titleTextView = findViewById(R.id.book_title);
        authorTextView = findViewById(R.id.book_author);
        descriptionShortTextView = findViewById(R.id.book_description_short);
        expandDescriptionTextView = findViewById(R.id.expand_description);
        descriptionFullTextView = findViewById(R.id.book_description_full);
        priceTextView = findViewById(R.id.book_price);

        loadBookData(bookId);
    }

    private void loadBookData(String bookId) {
        db.collection("books").document(bookId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Book book = document.toObject(Book.class);
                    if (book != null) {
                        displayBookDetails(book);
                    }
                }
            }
        });
    }

    private void displayBookDetails(Book book) {
        titleTextView.setText(book.getTitle());
        authorTextView.setText(book.getAuthor());

        String description = book.getDescription() != null ? book.getDescription() : "Описание отсутствует";
        String[] words = description.split("\\s+");
        if (words.length > 50) {
            String shortDescription = String.join(" ", java.util.Arrays.copyOfRange(words, 0, 50)) + "...";
            descriptionShortTextView.setText(shortDescription);
            descriptionShortTextView.setVisibility(View.VISIBLE);
            expandDescriptionTextView.setVisibility(View.VISIBLE);

            // Полное описание с кликабельной надписью "свернуть"
            String fullDescriptionWithCollapse = description + " свернуть";
            SpannableString spannableDescription = new SpannableString(fullDescriptionWithCollapse);
            ClickableSpan collapseSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    descriptionShortTextView.setVisibility(View.VISIBLE);
                    expandDescriptionTextView.setVisibility(View.VISIBLE);
                    descriptionFullTextView.setVisibility(View.GONE);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                }
            };
            spannableDescription.setSpan(collapseSpan, description.length() + 1, fullDescriptionWithCollapse.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableDescription.setSpan(new ForegroundColorSpan(0xFF0000FF), description.length() + 1, fullDescriptionWithCollapse.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            descriptionFullTextView.setText(spannableDescription);
            descriptionFullTextView.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
            descriptionFullTextView.setVisibility(View.GONE);

            expandDescriptionTextView.setOnClickListener(v -> {
                descriptionShortTextView.setVisibility(View.GONE);
                expandDescriptionTextView.setVisibility(View.GONE);
                descriptionFullTextView.setVisibility(View.VISIBLE);
            });
        } else {
            descriptionFullTextView.setText(description);
            descriptionFullTextView.setVisibility(View.VISIBLE);
            descriptionShortTextView.setVisibility(View.GONE);
            expandDescriptionTextView.setVisibility(View.GONE);
        }

        List<String> photos = new ArrayList<>();
        if (book.getCoverUrl() != null) {
            photos.add(book.getCoverUrl());
        }
        if (book.getPhotoUrls() != null && !book.getPhotoUrls().isEmpty()) {
            photos.addAll(book.getPhotoUrls());
        }
        PhotoPagerAdapter adapter = new PhotoPagerAdapter(this, photos);
        photoViewPager.setAdapter(adapter);

        double price = book.getPrice();
        double discount = book.getDiscount();
        if (discount > 0) {
            double discountedPrice = price - (price * discount / 100);
            String originalPriceText = String.format("%.2f ₽", price);
            String discountedPriceText = String.format("%.2f ₽", discountedPrice);
            SpannableString spannable = new SpannableString(originalPriceText + " " + discountedPriceText);
            spannable.setSpan(new StrikethroughSpan(), 0, originalPriceText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            priceTextView.setText(spannable);
        } else {
            priceTextView.setText(String.format("%.2f ₽", price));
        }
    }
}