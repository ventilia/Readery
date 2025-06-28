package com.example.readery.data;

import android.content.Context;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Сущность для хранения данных о скачанной книге в базе данных Room.
 */
@Entity(tableName = "downloaded_books")
public class DownloadedBook {
    @PrimaryKey
    private long bookId; // ID книги
    private String pdfPathEn; // Путь к PDF на английском
    private String pdfPathRu; // Путь к PDF на русском

    // Конструктор по умолчанию для Room
    public DownloadedBook() {}

    // Геттеры и сеттеры
    /**
     * Возвращает ID книги.
     */
    public long getBookId() { return bookId; }

    /**
     * Устанавливает ID книги.
     */
    public void setBookId(long bookId) { this.bookId = bookId; }

    /**
     * Возвращает путь к PDF на английском языке.
     */
    public String getPdfPathEn() { return pdfPathEn; }

    /**
     * Устанавливает путь к PDF на английском языке.
     */
    public void setPdfPathEn(String pdfPathEn) { this.pdfPathEn = pdfPathEn; }

    /**
     * Возвращает путь к PDF на русском языке.
     */
    public String getPdfPathRu() { return pdfPathRu; }

    /**
     * Устанавливает путь к PDF на русском языке.
     */
    public void setPdfPathRu(String pdfPathRu) { this.pdfPathRu = pdfPathRu; }

    /**
     * Возвращает путь к PDF с учетом текущей локализации.
     *
     * @param context контекст приложения
     * @return путь к PDF (русский или английский в зависимости от языка)
     */
    public String getPdfPath(Context context) {
        String lang = context.getResources().getConfiguration().getLocales().get(0).getLanguage();
        return "ru".equals(lang) ? pdfPathRu : pdfPathEn;
    }
}