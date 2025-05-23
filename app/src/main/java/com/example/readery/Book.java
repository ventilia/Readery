package com.example.readery;

import com.google.firebase.firestore.Exclude;

public class Book {
    private String id;
    private String title;
    private String author;
    private String coverUrl;
    private double price;

    // конструктор без параметров для firestore
    public Book() {}

    // конструктор с параметрами
    public Book(String id, String title, String author, String coverUrl, double price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.coverUrl = coverUrl;
        this.price = price;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
