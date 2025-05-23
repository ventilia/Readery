package com.example.readery;

public class Book {
    private String id;
    private String title;
    private String author;
    private String coverUrl;
    private double price;

    // Конструктор без параметров для Firestore
    public Book() {}

    // Конструктор с параметрами
    public Book(String id, String title, String author, String coverUrl, double price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.coverUrl = coverUrl;
        this.price = price;
    }

    // Геттеры
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCoverUrl() { return coverUrl; }
    public double getPrice() { return price; }
    public void setId(String id) {
        this.id = id;
    }
}