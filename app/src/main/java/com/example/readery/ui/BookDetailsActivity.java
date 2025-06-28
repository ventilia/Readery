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
import com.example.readery.utils.SettingsManager;
import com.example.readery.viewmodel.BookDetailsViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Активность для отображения деталей книги и управления загрузкой
 */
public class BookDetailsActivity extends AppCompatActivity {
    private static final String TAG = "BookDetailsActivity";
    private BookDetailsViewModel viewModel;
    private ProgressBar downloadProgressBar;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        // Скрываем ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Извлекаем ID книги из Intent
        long bookId = getIntent().getLongExtra("bookId", -1);
        if (bookId == -1) {
            Log.e(TAG, "Не передан ID книги в Intent");
            Toast.makeText(this, "Ошибка: ID книги не передан", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d(TAG, "Получен ID книги из Intent: " + bookId);

        // Инициализируем ViewModel
        viewModel = new ViewModelProvider(this).get(BookDetailsViewModel.class);
        viewModel.setBookId(bookId);

        downloadProgressBar = findViewById(R.id.download_progress_bar);
        Button addToLibraryButton = findViewById(R.id.add_to_library_button);

        // Подписываемся на данные книги из локальной базы
        viewModel.getBook().observe(this, book -> {
            if (book != null) {
                Log.d(TAG, "Книга с ID " + book.getId() + " найдена в локальной базе");
                setupBookDetails(book);
                checkDownloadStatus(book, addToLibraryButton);
            } else {
                Log.e(TAG, "Книга с ID " + bookId + " не найдена в локальной базе");
                Toast.makeText(this, "Книга не найдена в локальной базе", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Настраиваем кнопку "назад"
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }

    /**
     * Заполняем UI данными книги
     */
    private void setupBookDetails(Book book) {
        TextView headerTitle = findViewById(R.id.header_title);
        headerTitle.setText(book.getTitle(this));

        TextView titleView = findViewById(R.id.book_title);
        titleView.setText(book.getTitle(this));

        TextView authorView = findViewById(R.id.book_author);
        authorView.setText(book.getAuthor(this));

        TextView descriptionView = findViewById(R.id.book_description);
        setupDescription(descriptionView, book.getDescription(this));

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

    /**
     * Проверяем, загружена ли книга, и настраиваем кнопку
     */
    private void checkDownloadStatus(Book book, Button addToLibraryButton) {
        executorService.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            DownloadedBook downloadedBook = db.downloadedBookDao().getDownloadedBookById(book.getId());
            mainHandler.post(() -> {
                if (downloadedBook != null) {
                    addToLibraryButton.setText("Читать");
                    addToLibraryButton.setOnClickListener(v -> openPdf(downloadedBook.getPdfPath(this)));
                } else {
                    addToLibraryButton.setText("Добавить в библиотеку");
                    addToLibraryButton.setOnClickListener(v -> downloadBook(book));
                }
            });
        });
    }

    /**
     * Загружаем книгу из Firebase
     */
    private void downloadBook(Book book) {
        downloadProgressBar.setVisibility(View.VISIBLE);

        // Подключаемся к Firebase с явным указанием URL базы данных
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://readery-373e8-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference ref = database.getReference("books").child(String.valueOf(book.getId()));

        Log.d(TAG, "Запрос к Firebase по пути: " + ref.toString());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "DataSnapshot exists: " + dataSnapshot.exists());
                Log.d(TAG, "Содержимое DataSnapshot: " + dataSnapshot.toString());

                if (dataSnapshot.exists()) {
                    String pdfUrlEn = dataSnapshot.child("pdfUrlEn").getValue(String.class);
                    String pdfUrlRu = dataSnapshot.child("pdfUrlRu").getValue(String.class);
                    Log.d(TAG, "pdfUrlEn: " + pdfUrlEn);
                    Log.d(TAG, "pdfUrlRu: " + pdfUrlRu);

                    if (pdfUrlEn != null && pdfUrlRu != null) {
                        executorService.execute(() -> {
                            try {
                                String pdfPathEn = downloadFile(pdfUrlEn, book.getId() + "_en.pdf");
                                String pdfPathRu = downloadFile(pdfUrlRu, book.getId() + "_ru.pdf");

                                AppDatabase db = AppDatabase.getInstance(BookDetailsActivity.this);
                                DownloadedBook downloadedBook = new DownloadedBook();
                                downloadedBook.setBookId(book.getId());
                                downloadedBook.setPdfPathEn(pdfPathEn);
                                downloadedBook.setPdfPathRu(pdfPathRu);
                                db.downloadedBookDao().insert(downloadedBook);
                                book.setDownloaded(true);
                                db.bookDao().update(book);

                                mainHandler.post(() -> {
                                    Toast.makeText(BookDetailsActivity.this, "Книга добавлена в библиотеку", Toast.LENGTH_SHORT).show();
                                    downloadProgressBar.setVisibility(View.GONE);
                                    Button addToLibraryButton = findViewById(R.id.add_to_library_button);
                                    addToLibraryButton.setText("Читать");
                                    addToLibraryButton.setOnClickListener(v -> openPdf(downloadedBook.getPdfPath(BookDetailsActivity.this)));
                                });
                            } catch (IOException e) {
                                Log.e(TAG, "Ошибка загрузки PDF: " + e.getMessage());
                                mainHandler.post(() -> {
                                    Toast.makeText(BookDetailsActivity.this, "Ошибка загрузки: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    downloadProgressBar.setVisibility(View.GONE);
                                });
                            }
                        });
                    } else {
                        Log.e(TAG, "URL PDF не найдены в Firebase для книги с ID " + book.getId());
                        mainHandler.post(() -> {
                            Toast.makeText(BookDetailsActivity.this, "URL PDF не найдены в Firebase", Toast.LENGTH_LONG).show();
                            downloadProgressBar.setVisibility(View.GONE);
                        });
                    }
                } else {
                    Log.e(TAG, "Книга с ID " + book.getId() + " не найдена в Firebase");
                    mainHandler.post(() -> {
                        Toast.makeText(BookDetailsActivity.this, "Книга с ID " + book.getId() + " не найдена в Firebase", Toast.LENGTH_LONG).show();
                        downloadProgressBar.setVisibility(View.GONE);
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Ошибка Firebase: " + databaseError.getMessage());
                mainHandler.post(() -> {
                    Toast.makeText(BookDetailsActivity.this, "Ошибка Firebase: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    downloadProgressBar.setVisibility(View.GONE);
                });
            }
        });
    }

    /**
     * Загружаем PDF-файл по URL и сохраняем локально
     */
    private String downloadFile(String url, String fileName) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Ошибка HTTP: " + response.code());
        }

        File file = new File(getFilesDir(), fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            long totalBytesRead = 0;
            long totalBytes = response.body().contentLength();

            while ((bytesRead = response.body().byteStream().read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
                if (totalBytes > 0) {
                    int progress = (int) (totalBytesRead * 100 / totalBytes);
                    mainHandler.post(() -> downloadProgressBar.setProgress(progress));
                }
            }
        } finally {
            response.body().close();
        }
        return file.getAbsolutePath();
    }

    /**
     * Открываем PDF-файл
     */
    private void openPdf(String pdfPath) {
        Intent intent = new Intent(this, PdfViewerActivity.class);
        intent.putExtra("pdfPath", pdfPath);
        startActivity(intent);
    }

    /**
     * Настраиваем описание с возможностью сворачивания
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
        SettingsManager settingsManager = SettingsManager.getInstance(base);
        String lang = settingsManager.getLanguage();
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);
        base = base.createConfigurationContext(config);
        super.attachBaseContext(base);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}