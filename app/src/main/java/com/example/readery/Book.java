package com.example.readery;

import com.google.firebase.firestore.Exclude;

import java.util.List;

public class Book {
    private String id;
    private String title;
    private String author;
    private String coverUrl;
    private List<String> photoUrls; // Список URL или закодированных изображений
    private String description;
    private double price;
    private double discount;

    // Конструктор без параметров для Firestore
    public Book() {}

    // Конструктор с параметрами
    public Book(String id, String title, String author, String coverUrl, List<String> photoUrls,
                String description, double price, double discount) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.coverUrl = coverUrl;
        this.photoUrls = photoUrls;
        this.description = description;
        this.price = price;
        this.discount = discount;
    }

    @Exclude
    public String getId() {
        return id;
    }

    @Exclude
    public void setId(String id) {
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

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}