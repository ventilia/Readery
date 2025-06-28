package com.example.readery.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Активность для отображения деталей книги и управления загрузкой/чтением/удалением.
 */
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

        // Инициализация UI-элементов
        downloadProgressBar = findViewById(R.id.download_progress_bar);
        bookSizeText = findViewById(R.id.book_size_text);
        addToLibraryButton = findViewById(R.id.add_to_library_button);
        deleteButton = findViewById(R.id.delete_button);

        // Наблюдение за данными книги
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

    /**
     * Настраивает UI с данными книги.
     */
    private void setupBookDetails(Book book) {
        TextView headerTitle = findViewById(R.id.header_title);
        headerTitle.setText(book.getTitle(this));

        TextView titleView = findViewById(R.id.book_title);
        titleView.setText(book.getTitle(this));

        TextView authorView = findViewById(R.id.book_author);
        authorView.setText(book.getAuthor(this));

        TextView descriptionView = findViewById(R.id.book_description);
        descriptionView.setText(book.getDescription(this));

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
     * Проверяет статус загрузки книги и настраивает кнопки и информацию о размере.
     */
    private void checkDownloadStatus(Book book) {
        executorService.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            DownloadedBook downloadedBook = db.downloadedBookDao().getDownloadedBookById(book.getId());
            mainHandler.post(() -> {
                if (downloadedBook != null) {
                    // Книга скачана
                    addToLibraryButton.setText(getString(R.string.read));
                    addToLibraryButton.setOnClickListener(v -> openPdf(downloadedBook.getPdfPath(this)));
                    deleteButton.setVisibility(View.VISIBLE);
                    deleteButton.setOnClickListener(v -> deleteBook(book, downloadedBook));
                    showBookSize(downloadedBook);
                } else {
                    // Книга не скачана
                    addToLibraryButton.setText(getString(R.string.add_to_library));
                    addToLibraryButton.setOnClickListener(v -> downloadBook(book));
                    deleteButton.setVisibility(View.GONE);
                    bookSizeText.setVisibility(View.GONE);
                }
            });
        });
    }

    /**
     * Отображает общий размер двух PDF-файлов (английского и русского).
     */
    private void showBookSize(DownloadedBook downloadedBook) {
        long totalSizeBytes = 0;

        // Проверяем английский PDF
        String pdfPathEn = downloadedBook.getPdfPathEn();
        if (pdfPathEn != null) {
            File pdfFileEn = new File(pdfPathEn);
            if (pdfFileEn.exists()) {
                totalSizeBytes += pdfFileEn.length();
            }
        }

        // Проверяем русский PDF
        String pdfPathRu = downloadedBook.getPdfPathRu();
        if (pdfPathRu != null) {
            File pdfFileRu = new File(pdfPathRu);
            if (pdfFileRu.exists()) {
                totalSizeBytes += pdfFileRu.length();
            }
        }

        if (totalSizeBytes > 0) {
            double totalSizeMB = totalSizeBytes / (1024.0 * 1024.0);
            String sizeText = String.format(Locale.getDefault(), "Размер: %.2f МБ", totalSizeMB);
            bookSizeText.setText(sizeText);
            bookSizeText.setVisibility(View.VISIBLE);
        } else {
            bookSizeText.setVisibility(View.GONE);
        }
    }

    /**
     * Загружает книгу из Firebase и сохраняет локально, показывая прогресс-бар.
     */
    private void downloadBook(Book book) {
        downloadProgressBar.setVisibility(View.VISIBLE);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://readery-373e8-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference ref = database.getReference("books").child(String.valueOf(book.getId()));

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String pdfUrlEn = dataSnapshot.child("pdfUrlEn").getValue(String.class);
                    String pdfUrlRu = dataSnapshot.child("pdfUrlRu").getValue(String.class);
                    if (pdfUrlEn != null && pdfUrlRu != null) {
                        executorService.execute(() -> {
                            try {
                                // Загружаем оба PDF-файла
                                String pdfPathEn = downloadFile(pdfUrlEn, book.getId() + "_en.pdf");
                                String pdfPathRu = downloadFile(pdfUrlRu, book.getId() + "_ru.pdf");

                                AppDatabase db = AppDatabase.getInstance(BookDetailsActivity.this);
                                DownloadedBook downloadedBook = new DownloadedBook();
                                downloadedBook.setBookId(book.getId());
                                downloadedBook.setPdfPathEn(pdfPathEn);
                                downloadedBook.setPdfPathRu(pdfPathRu);
                                // Вставляем или обновляем запись (используем @Insert(onConflict = REPLACE))
                                db.downloadedBookDao().insert(downloadedBook);
                                book.setDownloaded(true);
                                db.bookDao().update(book);

                                mainHandler.post(() -> {
                                    Toast.makeText(BookDetailsActivity.this, "Книга добавлена в библиотеку", Toast.LENGTH_SHORT).show();
                                    downloadProgressBar.setVisibility(View.GONE);
                                    checkDownloadStatus(book); // Обновляем UI, чтобы показать размер
                                });
                            } catch (IOException e) {
                                mainHandler.post(() -> {
                                    Toast.makeText(BookDetailsActivity.this, "Ошибка загрузки: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    downloadProgressBar.setVisibility(View.GONE);
                                });
                            }
                        });
                    } else {
                        mainHandler.post(() -> {
                            Toast.makeText(BookDetailsActivity.this, "PDF-файл не найден в Firebase", Toast.LENGTH_LONG).show();
                            downloadProgressBar.setVisibility(View.GONE);
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mainHandler.post(() -> {
                    Toast.makeText(BookDetailsActivity.this, "Ошибка Firebase: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    downloadProgressBar.setVisibility(View.GONE);
                });
            }
        });
    }

    /**
     * Удаляет книгу из локальной базы данных и файловой системы.
     */
    private void deleteBook(Book book, DownloadedBook downloadedBook) {
        executorService.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            db.downloadedBookDao().delete(downloadedBook);
            book.setDownloaded(false);
            db.bookDao().update(book);

            deletePdfFile(downloadedBook.getPdfPathEn());
            deletePdfFile(downloadedBook.getPdfPathRu());

            mainHandler.post(() -> {
                Toast.makeText(this, "Книга удалена из библиотеки", Toast.LENGTH_SHORT).show();
                checkDownloadStatus(book); // Обновляем UI
            });
        });
    }

    private String downloadFile(String url, String fileName) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS) // Тайм-аут соединения
                .readTimeout(60, TimeUnit.SECONDS)    // Тайм-аут чтения
                .writeTimeout(60, TimeUnit.SECONDS)   // Тайм-аут записи
                .build();

        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Ошибка HTTP: " + response.code());
        }

        File file = new File(getFilesDir(), fileName);
        try (FileOutputStream fos = new FileOutputStream(file);
             InputStream is = response.body().byteStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        } finally {
            response.body().close();
        }
        return file.getAbsolutePath();
    }


    private void deletePdfFile(String pdfPath) {
        if (pdfPath != null) {
            File file = new File(pdfPath);
            if (file.exists()) {
                file.delete();
            }
        }
    }


    private void openPdf(String pdfPath) {
        if (pdfPath != null && !pdfPath.isEmpty()) {
            Intent intent = new Intent(this, PdfViewerActivity.class);
            intent.putExtra("pdfPath", pdfPath);
            startActivity(intent);
        } else {
            Toast.makeText(this, "PDF-файл не найден", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}