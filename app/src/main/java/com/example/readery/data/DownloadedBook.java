package com.example.readery.data;

import android.content.Context;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "downloaded_books")
public class DownloadedBook {
    @PrimaryKey
    private long bookId;
    private String pdfPathEn;
    private String pdfPathRu;

    // Конструктор по умолчанию для Room
    public DownloadedBook() {}

    // Геттеры и сеттеры
    public long getBookId() { return bookId; }
    public void setBookId(long bookId) { this.bookId = bookId; }
    public String getPdfPathEn() { return pdfPathEn; }
    public void setPdfPathEn(String pdfPathEn) { this.pdfPathEn = pdfPathEn; }
    public String getPdfPathRu() { return pdfPathRu; }
    public void setPdfPathRu(String pdfPathRu) { this.pdfPathRu = pdfPathRu; }

    // Метод для получения пути к PDF с учетом локализации
    public String getPdfPath(Context context) {
        String lang = context.getResources().getConfiguration().getLocales().get(0).getLanguage();
        return "ru".equals(lang) ? pdfPathRu : pdfPathEn;
    }
}