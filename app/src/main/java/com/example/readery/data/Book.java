package com.example.readery.data;

import android.content.Context;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;


@Entity(tableName = "books")
public class Book {
    @PrimaryKey
    private long id;
    private String titleEn;
    private String titleRu;
    private String authorEn;
    private String authorRu;
    private String descriptionEn;
    private String descriptionRu;
    private String coverImagePathEn;
    private String coverImagePathRu;
    private String highResCoverImagePathEn;
    private String highResCoverImagePathRu;
    private List<String> additionalImagesEn;
    private List<String> additionalImagesRu;
    private boolean isDownloaded;

    public Book() {}


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
    public boolean isDownloaded() { return isDownloaded; }
    public void setDownloaded(boolean isDownloaded) { this.isDownloaded = isDownloaded; }


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
        return "ru".equals(lang) ? (descriptionRu != null ? descriptionRu : descriptionEn) : (descriptionEn != null ? descriptionEn : descriptionRu);
    }

    public String getCoverImagePath(Context context) {
        String lang = context.getResources().getConfiguration().getLocales().get(0).getLanguage();
        return "ru".equals(lang) ? coverImagePathRu : coverImagePathEn;
    }

    public String getHighResCoverImagePath(Context context) {
        String lang = context.getResources().getConfiguration().getLocales().get(0).getLanguage();
        return "ru".equals(lang) ? highResCoverImagePathRu : highResCoverImagePathEn;
    }

    public List<String> getAdditionalImages(Context context) {
        String lang = context.getResources().getConfiguration().getLocales().get(0).getLanguage();
        return "ru".equals(lang) ? additionalImagesRu : additionalImagesEn;
    }
}