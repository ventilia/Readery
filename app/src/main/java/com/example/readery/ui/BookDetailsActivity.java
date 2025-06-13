package com.example.readery.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.readery.R;
import com.example.readery.viewmodel.BookDetailsViewModel;

public class BookDetailsActivity extends AppCompatActivity {
    private BookDetailsViewModel viewModel;
    private TextView titleView, authorView, descriptionView;
    private ImageView coverView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        long bookId = getIntent().getLongExtra("bookId", -1); // изменен тип на long
        if (bookId == -1) {
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this).get(BookDetailsViewModel.class);
        viewModel.setBookId(bookId);

        titleView = findViewById(R.id.book_title);
        authorView = findViewById(R.id.book_author);
        descriptionView = findViewById(R.id.book_description);
        coverView = findViewById(R.id.book_cover);

        viewModel.getBook().observe(this, book -> {
            if (book != null) {
                titleView.setText(book.getTitle());   // используем геттер
                authorView.setText(book.getAuthor()); // используем геттер
                descriptionView.setText(book.getDescription()); // используем геттер
                // загрузка обложки по book.getCoverImagePath(), если реализовано
            }
        });
    }
}