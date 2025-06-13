package com.example.readery.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Book.class, Tag.class, BookTag.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BookDao bookDao();
    public abstract TagDao tagDao();     // добавлен dao для тегов
    public abstract BookTagDao bookTagDao(); // добавлен dao для связей книга-тег

    private static AppDatabase instance;

    // получение единственного экземпляра базы данных
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "readery-database")
                    .fallbackToDestructiveMigration() // пересоздание бд при изменении версии
                    .build();
        }
        return instance;
    }
}