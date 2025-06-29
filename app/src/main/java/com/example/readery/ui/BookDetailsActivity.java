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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * активность для отображения деталей книги и управления загрузкой/чтением/удалением
 */
public class BookDetailsActivity extends AppCompatActivity {
    private static final String TAG = "BookDetailsActivity";
    private BookDetailsViewModel viewModel;
    private ProgressBar downloadProgressBar;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        long bookId = getIntent().getLongExtra("bookId", -1);
        if (bookId == -1) {
            Log.e(TAG, "не передан id книги в intent");
            Toast.makeText(this, "Ошибка: ID книги не передан", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d(TAG, "получен id книги из intent: " + bookId);

        viewModel = new ViewModelProvider(this).get(BookDetailsViewModel.class);
        viewModel.setBookId(bookId);

        downloadProgressBar = findViewById(R.id.download_progress_bar);
        Button addToLibraryButton = findViewById(R.id.add_to_library_button);

        viewModel.getBook().observe(this, book -> {
            if (book != null) {
                Log.d(TAG, "книга с id " + book.getId() + " найдена в локальной базе");
                setupBookDetails(book);
                checkDownloadStatus(book, addToLibraryButton);
            } else {
                Log.e(TAG, "книга с id " + bookId + " не найдена в локальной базе");
                Toast.makeText(this, "Книга не найдена", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }

    /**
     * настраивает ui с данными книги
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
     * проверяет статус загрузки книги и обновляет ui
     */
    private void checkDownloadStatus(Book book, Button addToLibraryButton) {
        FloatingActionButton deleteButton = findViewById(R.id.delete_button);
        TextView bookSizeText = findViewById(R.id.book_size_text);

        executorService.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            DownloadedBook downloadedBook = db.downloadedBookDao().getDownloadedBookById(book.getId());
            mainHandler.post(() -> {
                if (downloadedBook != null) {
                    addToLibraryButton.setText(getString(R.string.read));
                    addToLibraryButton.setOnClickListener(v -> openPdf(downloadedBook.getPdfPath(this)));
                    deleteButton.setVisibility(View.VISIBLE);
                    double sizeMB = calculateBookSize(downloadedBook);
                    bookSizeText.setText(String.format(getString(R.string.book_size), sizeMB));
                    bookSizeText.setVisibility(View.VISIBLE);
                    deleteButton.setOnClickListener(v -> deleteBook(book, downloadedBook));
                } else {
                    addToLibraryButton.setText(getString(R.string.add_to_library));
                    addToLibraryButton.setOnClickListener(v -> downloadBook(book));
                    deleteButton.setVisibility(View.GONE);
                    bookSizeText.setVisibility(View.GONE);
                }
            });
        });
    }

    /**
     * вычисляет суммарный размер pdf-файлов книги в мб
     */
    private double calculateBookSize(DownloadedBook downloadedBook) {
        File fileEn = new File(downloadedBook.getPdfPathEn());
        File fileRu = new File(downloadedBook.getPdfPathRu());
        long sizeBytes = 0;
        if (fileEn.exists()) sizeBytes += fileEn.length();
        if (fileRu.exists()) sizeBytes += fileRu.length();
        return sizeBytes / (1024.0 * 1024.0);
    }

    /**
     * удаляет книгу из локальной библиотеки
     */
    private void deleteBook(Book book, DownloadedBook downloadedBook) {
        executorService.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            db.downloadedBookDao().delete(downloadedBook);
            book.setDownloaded(false);
            db.bookDao().update(book);

            File fileEn = new File(downloadedBook.getPdfPathEn());
            File fileRu = new File(downloadedBook.getPdfPathRu());
            if (fileEn.exists()) fileEn.delete();
            if (fileRu.exists()) fileRu.delete();

            mainHandler.post(() -> {
                Toast.makeText(this, "Книга удалена из библиотеки", Toast.LENGTH_SHORT).show();
                checkDownloadStatus(book, findViewById(R.id.add_to_library_button));
            });
        });
    }

    /**
     * загружает книгу из firebase с отображением прогресса
     */
    private void downloadBook(Book book) {
        downloadProgressBar.setVisibility(View.VISIBLE);
        TextView bookSizeText = findViewById(R.id.book_size_text);
        bookSizeText.setVisibility(View.VISIBLE);
        bookSizeText.setText("Загрузка...");

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
                                long totalSizeEn = getFileSize(pdfUrlEn);
                                long totalSizeRu = getFileSize(pdfUrlRu);
                                long totalSize = totalSizeEn + totalSizeRu;

                                String pdfPathEn = downloadFile(pdfUrlEn, book.getId() + "_en.pdf", totalSize, 0);
                                String pdfPathRu = downloadFile(pdfUrlRu, book.getId() + "_ru.pdf", totalSize, totalSizeEn);

                                AppDatabase db = AppDatabase.getInstance(BookDetailsActivity.this);
                                DownloadedBook downloadedBook = new DownloadedBook();
                                downloadedBook.setBookId(book.getId());
                                downloadedBook.setPdfPathEn(pdfPathEn);
                                downloadedBook.setPdfPathRu(pdfPathRu);
                                db.downloadedBookDao().insert(downloadedBook);
                                book.setDownloaded(true);
                                db.bookDao().update(book);

                                mainHandler.post(() -> {
                                    Toast.makeText(BookDetailsActivity.this, getString(R.string.book_added_to_library), Toast.LENGTH_SHORT).show();
                                    downloadProgressBar.setVisibility(View.GONE);
                                    checkDownloadStatus(book, findViewById(R.id.add_to_library_button));
                                });
                            } catch (IOException e) {
                                mainHandler.post(() -> {
                                    String errorMessage = e.getMessage() != null && e.getMessage().contains("timeout")
                                            ? "Превышено время ожидания. Проверьте интернет."
                                            : "Ошибка загрузки: " + e.getMessage();
                                    Toast.makeText(BookDetailsActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                    downloadProgressBar.setVisibility(View.GONE);
                                    bookSizeText.setVisibility(View.GONE);
                                });
                            }
                        });
                    } else {
                        mainHandler.post(() -> {
                            Toast.makeText(BookDetailsActivity.this, "PDF-файлы не найдены", Toast.LENGTH_LONG).show();
                            downloadProgressBar.setVisibility(View.GONE);
                            bookSizeText.setVisibility(View.GONE);
                        });
                    }
                } else {
                    mainHandler.post(() -> {
                        Toast.makeText(BookDetailsActivity.this, "Книга не найдена в Firebase", Toast.LENGTH_LONG).show();
                        downloadProgressBar.setVisibility(View.GONE);
                        bookSizeText.setVisibility(View.GONE);
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mainHandler.post(() -> {
                    Toast.makeText(BookDetailsActivity.this, "Ошибка Firebase: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    downloadProgressBar.setVisibility(View.GONE);
                    bookSizeText.setVisibility(View.GONE);
                });
            }
        });
    }

    /**
     * получает размер файла по url
     */
    private long getFileSize(String url) throws IOException {
        Request request = new Request.Builder().url(url).head().build();
        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Ошибка HTTP: " + response.code());
        String contentLength = response.header("Content-Length");
        response.close();
        return contentLength != null ? Long.parseLong(contentLength) : 0;
    }

    /**
     * загружает pdf-файл с отображением общего прогресса
     */
    private String downloadFile(String url, String fileName, long totalSize, long offset) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = okHttpClient.newCall(request).execute();

        if (!response.isSuccessful()) throw new IOException("Ошибка HTTP: " + response.code());

        File file = new File(getFilesDir(), fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            long totalBytesRead = 0;

            while ((bytesRead = response.body().byteStream().read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
                long currentTotal = offset + totalBytesRead;
                double progressMB = currentTotal / (1024.0 * 1024.0);
                double totalMB = totalSize / (1024.0 * 1024.0);
                mainHandler.post(() -> {
                    TextView bookSizeText = findViewById(R.id.book_size_text);
                    bookSizeText.setText(String.format("Загружено %.2f МБ из %.2f МБ", progressMB, totalMB));
                });
            }
        } finally {
            response.body().close();
        }
        return file.getAbsolutePath();
    }

    /**
     * открывает pdf-файл в PdfViewerActivity
     */
    private void openPdf(String pdfPath) {
        if (pdfPath != null && !pdfPath.isEmpty()) {
            Intent intent = new Intent(this, PdfViewerActivity.class);
            intent.putExtra("pdfPath", pdfPath);
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.pdf_not_found), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * настраивает описание с возможностью сворачивания/разворачивания
     */
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