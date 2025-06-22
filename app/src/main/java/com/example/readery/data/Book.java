package com.example.readery.data;

import android.content.Context;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.List;

@Entity(tableName = "books")
public class Book {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String titleEn;
    private String titleRu;
    private String authorEn;
    private String authorRu;
    private String descriptionEn;
    private String descriptionRu;
    private String pdfPathEn; // путь к PDF на английском
    private String pdfPathRu; // путь к PDF на русском
    private String coverImagePathEn; // обложка на английском
    private String coverImagePathRu; // обложка на русском
    private String highResCoverImagePathEn; // обложка высокого разрешения на английском
    private String highResCoverImagePathRu; // обложка высокого разрешения на русском
    private List<String> additionalImagesEn; // дополнительные изображения на английском
    private List<String> additionalImagesRu; // дополнительные изображения на русском

    public Book() {
    }

    // получение заголовка с учетом локализации
    public String getTitle(Context context) {
        String lang = context.getResources().getConfiguration().getLocales().get(0).getLanguage();
        return "ru".equals(lang) ? titleRu : titleEn;
    }

    // получение автора с учетом локализации
    public String getAuthor(Context context) {
        String lang = context.getResources().getConfiguration().getLocales().get(0).getLanguage();
        return "ru".equals(lang) ? authorRu : authorEn;
    }

    // получение описания с учетом локализации
    public String getDescription(Context context) {
        String lang = context.getResources().getConfiguration().getLocales().get(0).getLanguage();
        if ("ru".equals(lang)) {
            return descriptionRu != null ? descriptionRu : descriptionEn;
        } else {
            return descriptionEn != null ? descriptionEn : descriptionRu;
        }
    }

    // получение пути к PDF с учетом локализации
    public String getPdfPath(Context context) {
        String lang = context.getResources().getConfiguration().getLocales().get(0).getLanguage();
        return "ru".equals(lang) ? pdfPathRu : pdfPathEn;
    }

    // получение пути к обложке с учетом локализации
    public String getCoverImagePath(Context context) {
        String lang = context.getResources().getConfiguration().getLocales().get(0).getLanguage();
        return "ru".equals(lang) ? coverImagePathRu : coverImagePathEn;
    }

    // получение пути к обложке высокого разрешения с учетом локализации
    public String getHighResCoverImagePath(Context context) {
        String lang = context.getResources().getConfiguration().getLocales().get(0).getLanguage();
        return "ru".equals(lang) ? highResCoverImagePathRu : highResCoverImagePathEn;
    }

    // получение дополнительных изображений с учетом локализации
    public List<String> getAdditionalImages(Context context) {
        String lang = context.getResources().getConfiguration().getLocales().get(0).getLanguage();
        return "ru".equals(lang) ? additionalImagesRu : additionalImagesEn;
    }

    // геттеры и сеттеры
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getTitleEn() { return titleEn; }
    public void setTitleEn(String titleEn) { this.titleEn = titleEn; }
    public String getTitleRu() { return titleRu; }
    public void setTitleRu(String titleRu) { this.titleRu = titleRu; }
    public String getAuthorEn() { return authorEn; }
    public void setAuthorEn(String authorEn) { this.authorEn = authorEn; }
    public String getAuthorRu() { return authorRu; }
    public void setAuthorRu(String authorRu) { this.authorRu = authorRu; }
    public String getDescriptionEn() { return descriptionEn; }
    public void setDescriptionEn(String descriptionEn) { this.descriptionEn = descriptionEn; }
    public String getDescriptionRu() { return descriptionRu; }
    public void setDescriptionRu(String descriptionRu) { this.descriptionRu = descriptionRu; }
    public String getPdfPathEn() { return pdfPathEn; }
    public void setPdfPathEn(String pdfPathEn) { this.pdfPathEn = pdfPathEn; }
    public String getPdfPathRu() { return pdfPathRu; }
    public void setPdfPathRu(String pdfPathRu) { this.pdfPathRu = pdfPathRu; }
    public String getCoverImagePathEn() { return coverImagePathEn; }
    public void setCoverImagePathEn(String coverImagePathEn) { this.coverImagePathEn = coverImagePathEn; }
    public String getCoverImagePathRu() { return coverImagePathRu; }
    public void setCoverImagePathRu(String coverImagePathRu) { this.coverImagePathRu = coverImagePathRu; }
    public String getHighResCoverImagePathEn() { return highResCoverImagePathEn; }
    public void setHighResCoverImagePathEn(String highResCoverImagePathEn) { this.highResCoverImagePathEn = highResCoverImagePathEn; }
    public String getHighResCoverImagePathRu() { return highResCoverImagePathRu; }
    public void setHighResCoverImagePathRu(String highResCoverImagePathRu) { this.highResCoverImagePathRu = highResCoverImagePathRu; }
    public List<String> getAdditionalImagesEn() { return additionalImagesEn; }
    public void setAdditionalImagesEn(List<String> additionalImagesEn) { this.additionalImagesEn = additionalImagesEn; }
    public List<String> getAdditionalImagesRu() { return additionalImagesRu; }
    public void setAdditionalImagesRu(List<String> additionalImagesRu) { this.additionalImagesRu = additionalImagesRu; }
}