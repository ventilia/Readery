package com.example.readery;

import android.os.Bundle;
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
    private TextView titleTextView, authorTextView, descriptionTextView, priceTextView, discountedPriceTextView;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        // инициализация firestore
        db = FirebaseFirestore.getInstance();

        // получение book_id из intent
        String bookId = getIntent().getStringExtra("book_id");

        // привязка UI элементов
        photoViewPager = findViewById(R.id.photo_viewpager);
        titleTextView = findViewById(R.id.book_title);
        authorTextView = findViewById(R.id.book_author);
        descriptionTextView = findViewById(R.id.book_description);
        priceTextView = findViewById(R.id.book_price);
        discountedPriceTextView = findViewById(R.id.book_discounted_price);

        // загрузка данных книги
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
        // заполнение текстовых полей
        titleTextView.setText(book.getTitle());
        authorTextView.setText(book.getAuthor());
        descriptionTextView.setText(book.getDescription() != null ? book.getDescription() : "Описание отсутствует");

        // настройка карусели фото
        List<String> photos = new ArrayList<>();
        if (book.getCoverUrl() != null) {
            photos.add(book.getCoverUrl()); // добавляем обложку как первое фото
        }
        if (book.getPhotoUrls() != null && !book.getPhotoUrls().isEmpty()) {
            photos.addAll(book.getPhotoUrls()); // добавляем дополнительные фото
        }
        PhotoPagerAdapter adapter = new PhotoPagerAdapter(this, photos);
        photoViewPager.setAdapter(adapter);

        // отображение цены и скидки
        double price = book.getPrice();
        double discount = book.getDiscount();
        if (discount > 0) {
            double discountedPrice = price - (price * discount / 100);
            priceTextView.setText(String.format("%.2f ₽", price));
            priceTextView.setPaintFlags(priceTextView.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
            discountedPriceTextView.setText(String.format("%.2f ₽", discountedPrice));
        } else {
            priceTextView.setText(String.format("%.2f ₽", price));
            discountedPriceTextView.setVisibility(View.GONE);
        }
    }
}