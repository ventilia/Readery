package com.example.readery.data;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface BookTagDao {
    @Insert
    long insert(BookTag bookTag);
}