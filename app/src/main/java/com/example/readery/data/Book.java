package com.example.readery.data;

import android.content.Context;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.List;

@Entity(tableName = "books")
public class Book {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private String author;
    private String descriptionEn; // Описание на английском
    private String descriptionRu; // Описание на русском
    private String pdfPath;
    private String coverImagePath;
    private String highResCoverImagePath;
    private List<String> additionalImages;

    // Основной конструктор с минимальными параметрами
    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    // Метод для получения описания в зависимости от языка
    public String getDescription(Context context) {
        String lang = context.getResources().getConfiguration().getLocales().get(0).getLanguage();
        if ("ru".equals(lang)) {
            return descriptionRu != null ? descriptionRu : (descriptionEn != null ? descriptionEn : "Описание недоступно");
        } else {
            return descriptionEn != null ? descriptionEn : (descriptionRu != null ? descriptionRu : "Description not available");
        }
    }

    // Геттеры и сеттеры
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public String getDescriptionRu() {
        return descriptionRu;
    }

    public void setDescriptionRu(String descriptionRu) {
        this.descriptionRu = descriptionRu;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    public String getCoverImagePath() {
        return coverImagePath;
    }

    public void setCoverImagePath(String coverImagePath) {
        this.coverImagePath = coverImagePath;
    }

    public String getHighResCoverImagePath() {
        return highResCoverImagePath;
    }

    public void setHighResCoverImagePath(String highResCoverImagePath) {
        this.highResCoverImagePath = highResCoverImagePath;
    }

    public List<String> getAdditionalImages() {
        return additionalImages;
    }

    public void setAdditionalImages(List<String> additionalImages) {
        this.additionalImages = additionalImages;
    }
}