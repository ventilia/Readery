package com.example.readery.data;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface TagDao {
    @Insert
    long insert(Tag tag);
}