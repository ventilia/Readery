package com.example.readery.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.example.readery.R;
import com.example.readery.data.Book;
import com.example.readery.viewmodel.BookDetailsViewModel;

/**
 * Активность для отображения детальной информации о книге.
 */
public class BookDetailsActivity extends AppCompatActivity {
    private BookDetailsViewModel viewModel;
    private TextView titleView, authorView, descriptionView;
    private ImageView coverView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        // Получаем ID книги из Intent
        long bookId = getIntent().getLongExtra("bookId", -1);
        if (bookId == -1) {
            finish(); // Закрываем активность, если ID не передан
            return;
        }

        // Инициализируем ViewModel
        viewModel = new ViewModelProvider(this).get(BookDetailsViewModel.class);
        viewModel.setBookId(bookId);

        // Привязываем элементы интерфейса
        titleView = findViewById(R.id.book_title);
        authorView = findViewById(R.id.book_author);
        descriptionView = findViewById(R.id.book_description);
        coverView = findViewById(R.id.book_cover);

        // Наблюдаем за изменениями данных книги
        viewModel.getBook().observe(this, book -> {
            if (book != null) {
                titleView.setText(book.getTitle());
                authorView.setText(book.getAuthor());
                descriptionView.setText(book.getDescription());

                // Загружаем обложку книги с помощью Glide
                String coverPath = book.getCoverImagePath();
                if (coverPath != null && !coverPath.isEmpty()) {
                    Glide.with(this)
                            .load("file:///android_asset/" + coverPath) // Путь к файлу в assets
                            .placeholder(R.drawable.default_cover)      // Заглушка во время загрузки
                            .error(R.drawable.error_cover)              // Изображение при ошибке
                            .into(coverView);                           // Целевой ImageView
                }
            }
        });
    }
}