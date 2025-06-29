package com.example.readery.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.readery.R;
import com.example.readery.data.AppDatabase;
import com.example.readery.data.Book;
import com.example.readery.data.DownloadedBook;
import com.example.readery.utils.ImagePagerAdapter;
import com.example.readery.viewmodel.BookDetailsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookDetailsActivity extends AppCompatActivity {
    private static final String TAG = "BookDetailsActivity";
    private BookDetailsViewModel viewModel;
    private ProgressBar downloadProgressBar;
    private TextView bookSizeText;
    private Button addToLibraryButton;
    private FloatingActionButton deleteButton;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        long bookId = getIntent().getLongExtra("bookId", -1);
        if (bookId == -1) {
            Log.e(TAG, "Не передан ID книги в Intent");
            Toast.makeText(this, "Ошибка: ID книги не передан", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d(TAG, "Получен ID книги из Intent: " + bookId);

        viewModel = new ViewModelProvider(this).get(BookDetailsViewModel.class);
        viewModel.setBookId(bookId);

        downloadProgressBar = findViewById(R.id.download_progress_bar);
        bookSizeText = findViewById(R.id.book_size_text);
        addToLibraryButton = findViewById(R.id.add_to_library_button);
        deleteButton = findViewById(R.id.delete_button);

        viewModel.getBook().observe(this, book -> {
            if (book != null) {
                Log.d(TAG, "Книга с ID " + book.getId() + " найдена в локальной базе");
                setupBookDetails(book);
                checkDownloadStatus(book);
            } else {
                Log.e(TAG, "Книга с ID " + bookId + " не найдена в локальной базе");
                Toast.makeText(this, "Книга не найдена", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }

    private void setupBookDetails(Book book) {
        TextView headerTitle = findViewById(R.id.header_title);
        headerTitle.setText(book.getTitle(this));

        TextView titleView = findViewById(R.id.book_title);
        titleView.setText(book.getTitle(this));

        TextView authorView = findViewById(R.id.book_author);
        authorView.setText(book.getAuthor(this));

        TextView descriptionView = findViewById(R.id.book_description);
        String fullDescription = book.getDescription(this);
        setupDescription(descriptionView, fullDescription);

        ViewPager imagePager = findViewById(R.id.image_pager);
        List<String> allImages = new ArrayList<>();
        if (book.getHighResCoverImagePath(this) != null && !book.getHighResCoverImagePath(this).isEmpty()) {
            allImages.add(book.getHighResCoverImagePath(this));
        } else if (book.getCoverImagePath(this) != null && !book.getCoverImagePath(this).isEmpty()) {
            allImages.add(book.getCoverImagePath(this));
        }
        if (book.getAdditionalImages(this) != null) {
            allImages.addAll(book.getAdditionalImages(this));
        }

        ImagePagerAdapter pagerAdapter = new ImagePagerAdapter(this, allImages);
        imagePager.setAdapter(pagerAdapter);

        me.relex.circleindicator.CircleIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(imagePager);
    }

    private void setupDescription(TextView descriptionView, String fullDescription) {
        String[] words = fullDescription.split("\\s+");
        if (words.length > 25) {
            String shortDescription = String.join(" ", Arrays.copyOfRange(words, 0, 25)) + " ... ";
            String expandText = getString(R.string.expand);
            SpannableString expandSpan = new SpannableString(shortDescription + expandText);
            expandSpan.setSpan(new ForegroundColorSpan(getResources().getColor(android.R.color.holo_blue_dark)),
                    shortDescription.length(), expandSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            descriptionView.setText(expandSpan);

            descriptionView.setOnClickListener(v -> {
                CharSequence currentText = descriptionView.getText();
                if (currentText.toString().endsWith(expandText)) {
                    String collapseText = getString(R.string.collapse);
                    SpannableString collapseSpan = new SpannableString(fullDescription + " " + collapseText);
                    collapseSpan.setSpan(new ForegroundColorSpan(getResources().getColor(android.R.color.holo_blue_dark)),
                            fullDescription.length() + 1, collapseSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    descriptionView.setText(collapseSpan);
                } else if (currentText.toString().endsWith(getString(R.string.collapse))) {
                    descriptionView.setText(expandSpan);
                }
            });
        } else {
            descriptionView.setText(fullDescription);
        }
    }

    private void checkDownloadStatus(Book book) {
        executorService.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            DownloadedBook downloadedBook = db.downloadedBookDao().getDownloadedBookById(book.getId());
            mainHandler.post(() -> {
                if (downloadedBook != null) {
                    addToLibraryButton.setText(getString(R.string.read));
                    addToLibraryButton.setOnClickListener(v -> openPdf(downloadedBook.getPdfPath(this)));
                    deleteButton.setVisibility(View.VISIBLE);
                    deleteButton.setOnClickListener(v -> deleteBook(book, downloadedBook));
                    showBookSize(downloadedBook);
                } else {
                    addToLibraryButton.setText(getString(R.string.add_to_library));
                    addToLibraryButton.setOnClickListener(v -> downloadBook(book));
                    deleteButton.setVisibility(View.GONE);
                    bookSizeText.setVisibility(View.GONE);
                }
            });
        });
    }

    private void showBookSize(DownloadedBook downloadedBook) {
        // Реализация отображения размера книги
    }

    private void downloadBook(Book book) {
        // Реализация загрузки книги
    }

    private void deleteBook(Book book, DownloadedBook downloadedBook) {
        // Реализация удаления книги
    }

    private void openPdf(String pdfPath) {
        // Реализация открытия PDF
    }
}