package com.example.readery.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Book.class, Tag.class, BookTag.class, DownloadedBook.class}, version = 9, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract BookDao bookDao();
    public abstract TagDao tagDao();
    public abstract BookTagDao bookTagDao();
    public abstract DownloadedBookDao downloadedBookDao();

    private static AppDatabase instance;


    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "readery-database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}