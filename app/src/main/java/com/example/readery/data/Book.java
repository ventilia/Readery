package com.example.readery.data;

import android.content.Context;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.List;

/**
 * Класс, представляющий книгу с локализованными данными.
 */
@Entity(tableName = "books")
public class Book {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String titleEn; // Название на английском
    private String titleRu; // Название на русском
    private String authorEn; // Автор на английском
    private String authorRu; // Автор на русском
    private String descriptionEn; // Описание на английском
    private String descriptionRu; // Описание на русском
    private String pdfPath; // Путь к PDF-файлу
    private String coverImagePath; // Путь к обложке низкого разрешения
    private String highResCoverImagePath; // Путь к обложке высокого разрешения
    private List<String> additionalImages; // Список дополнительных изображений

    public Book() {
    }

    /**
     * Возвращает название книги в зависимости от текущего языка устройства.
     *
     * @param context Контекст приложения для получения текущей локали.
     * @return Название книги на русском или английском языке.
     */
    public String getTitle(Context context) {
        String lang = context.getResources().getConfiguration().getLocales().get(0).getLanguage();
        return "ru".equals(lang) ? titleRu : titleEn;
    }

    /**
     * Возвращает автора книги в зависимости от текущего языка устройства.
     *
     * @param context Контекст приложения для получения текущей локали.
     * @return Автор книги на русском или английском языке.
     */
    public String getAuthor(Context context) {
        String lang = context.getResources().getConfiguration().getLocales().get(0).getLanguage();
        return "ru".equals(lang) ? authorRu : authorEn;
    }

    /**
     * Возвращает описание книги в зависимости от текущего языка устройства.
     * Если описание на текущем языке отсутствует, возвращается описание на другом языке.
     *
     * @param context Контекст приложения для получения текущей локали.
     * @return Описание книги на русском или английском языке.
     */
    public String getDescription(Context context) {
        String lang = context.getResources().getConfiguration().getLocales().get(0).getLanguage();
        if ("ru".equals(lang)) {
            return descriptionRu != null ? descriptionRu : descriptionEn;
        } else {
            return descriptionEn != null ? descriptionEn : descriptionRu;
        }
    }

    // Геттеры и сеттеры для всех полей

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getTitleRu() {
        return titleRu;
    }

    public void setTitleRu(String titleRu) {
        this.titleRu = titleRu;
    }

    public String getAuthorEn() {
        return authorEn;
    }

    public void setAuthorEn(String authorEn) {
        this.authorEn = authorEn;
    }

    public String getAuthorRu() {
        return authorRu;
    }

    public void setAuthorRu(String authorRu) {
        this.authorRu = authorRu;
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