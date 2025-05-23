package com.example.readery;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class BookDetailsActivity extends AppCompatActivity {
    private ViewPager photoViewPager;
    private TextView titleTextView, authorTextView, descriptionTextView, priceTextView;
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
        descriptionTextView = findViewById(R.id.book_description);
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
        descriptionTextView.setText(book.getDescription() != null ? book.getDescription() : "Описание отсутствует");

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