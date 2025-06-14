package com.example.readery.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.readery.R;
import com.example.readery.animation.CardAnimationHelper;
import com.example.readery.data.Book;
import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<Book> books = new ArrayList<>();
    private OnBookClickListener listener;

    // Интерфейс для обработки кликов по книге
    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    // Конструктор адаптера
    public BookAdapter(OnBookClickListener listener, Context context) {
        this.listener = listener;
    }

    // Метод для установки списка книг
    public void setBooks(List<Book> books) {
        this.books = books;
        notifyDataSetChanged(); // Уведомляем адаптер об изменении данных
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Создаем представление элемента списка из макета item_book
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        // Получаем книгу по текущей позиции
        Book book = books.get(position);
        holder.title.setText(book.getTitle());   // Устанавливаем заголовок книги
        holder.author.setText(book.getAuthor()); // Устанавливаем автора книги

        // Загружаем обложку книги
        String coverPath = book.getCoverImagePath();
        if (coverPath != null && !coverPath.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load("file:///android_asset/" + coverPath) // Путь к обложке
                    .placeholder(R.drawable.default_cover)      // Заглушка на время загрузки
                    .error(R.drawable.error_cover)              // Картинка при ошибке
                    .into(holder.cover);                        // Устанавливаем в ImageView
        }

        // Обработка касаний с анимацией
        holder.itemView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:        // При нажатии
                    CardAnimationHelper.animatePress(v); // Анимация нажатия
                    return true;
                case MotionEvent.ACTION_UP:          // При отпускании
                    CardAnimationHelper.animateRelease(v); // Анимация отпускания
                    listener.onBookClick(book);          // Вызываем обработчик клика
                    return true;
                case MotionEvent.ACTION_CANCEL:      // При отмене касания
                    CardAnimationHelper.animateRelease(v); // Анимация отпускания
                    return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        // Возвращаем количество книг в списке
        return books.size();
    }

    // Внутренний класс для хранения элементов представления книги
    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView title;   // Поле для заголовка
        TextView author;  // Поле для автора
        ImageView cover;  // Поле для обложки

        BookViewHolder(View itemView) {
            super(itemView);
            // Инициализируем элементы интерфейса
            title = itemView.findViewById(R.id.book_title);
            author = itemView.findViewById(R.id.book_author);
            cover = itemView.findViewById(R.id.book_cover);
        }
    }
}