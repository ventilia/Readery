package com.example.readery.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * Абстрактный класс базы данных Room для приложения Reader.
 */
@Database(entities = {Book.class, Tag.class, BookTag.class}, version = 6, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract BookDao bookDao();
    public abstract TagDao tagDao();
    public abstract BookTagDao bookTagDao();

    private static AppDatabase instance;

    /**
     * Получение экземпляра базы данных.
     *
     * @param context Контекст приложения.
     * @return Экземпляр базы данных.
     */
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "readery-database")
                    .fallbackToDestructiveMigration() // Очищает базу данных при изменении версии
                    .build();
        }
        return instance;
    }
}