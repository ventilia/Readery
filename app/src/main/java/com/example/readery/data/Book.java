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
    private String pdfPath;
    private String coverImagePath;
    private String highResCoverImagePath;
    private List<String> additionalImages;

    public Book() {
    }

    public String getTitle(Context context) {
        String lang = context.getResources().getConfiguration().getLocales().get(0).getLanguage();
        return "ru".equals(lang) ? titleRu : titleEn;
    }


    public String getAuthor(Context context) {
        String lang = context.getResources().getConfiguration().getLocales().get(0).getLanguage();
        return "ru".equals(lang) ? authorRu : authorEn;
    }


    public String getDescription(Context context) {
        String lang = context.getResources().getConfiguration().getLocales().get(0).getLanguage();
        if ("ru".equals(lang)) {
            return descriptionRu != null ? descriptionRu : descriptionEn;
        } else {
            return descriptionEn != null ? descriptionEn : descriptionRu;
        }
    }



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