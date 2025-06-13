package com.example.readery.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "book_tags")
public class BookTag {
    @PrimaryKey(autoGenerate = true)
    private long id; // уникальный идентификатор для связи

    private long bookId; // id книги
    private long tagId;  // id тега

    // конструктор
    public BookTag(long bookId, long tagId) {
        this.bookId = bookId;
        this.tagId = tagId;
    }

    // геттеры и сеттеры
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }
}