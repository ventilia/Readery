package com.example.readery.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.List;

@Entity(tableName = "books")
public class Book {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private String author;
    private String description;
    private String pdfPath;
    private String coverImagePath;
    private List<String> additionalImages;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<String> getAdditionalImages() {
        return additionalImages;
    }

    public void setAdditionalImages(List<String> additionalImages) {
        this.additionalImages = additionalImages;
    }
}