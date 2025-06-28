package com.example.readery.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.readery.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

/**
 * Активность для отображения PDF-файла книги.
 */
public class PdfViewerActivity extends AppCompatActivity {
    private static final String TAG = "PdfViewerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        // Находим PDFView из разметки
        PDFView pdfView = findViewById(R.id.pdf_view);

        // Получаем путь к PDF из Intent
        String pdfPath = getIntent().getStringExtra("pdfPath");

        // Проверяем, что путь передан и не пустой
        if (pdfPath != null && !pdfPath.isEmpty()) {
            File pdfFile = new File(pdfPath);
            if (pdfFile.exists()) {
                try {
                    // Загружаем PDF-файл
                    pdfView.fromFile(pdfFile)
                            .enableSwipe(true)        // Включаем свайп для листания страниц
                            .enableDoubletap(true)    // Включаем зум двойным тапом
                            .onError(t -> {
                                // Обработка ошибок загрузки
                                Log.e(TAG, "Ошибка при загрузке PDF: " + t.getMessage());
                                Toast.makeText(this, "Не удалось загрузить PDF", Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .load();
                } catch (Exception e) {
                    Log.e(TAG, "Исключение при загрузке PDF: " + e.getMessage());
                    Toast.makeText(this, "Не удалось загрузить PDF", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                // Файл не существует
                Log.e(TAG, "PDF-файл не найден по пути: " + pdfPath);
                Toast.makeText(this, "PDF-файл не найден", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            // Путь не передан
            Log.e(TAG, "Путь к PDF не передан в Intent");
            Toast.makeText(this, "Ошибка: путь к PDF не передан", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}