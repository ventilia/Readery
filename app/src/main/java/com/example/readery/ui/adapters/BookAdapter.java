package com.example.readery.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.readery.R;
import com.example.readery.data.Book;
import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<Book> books = new ArrayList<>();
    private OnBookClickListener listener;
    private Animation scaleDown;
    private Animation scaleUp;
    private Animation pulse;

    // Интерфейс для обработки кликов
    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    // Конструктор адаптера
    public BookAdapter(OnBookClickListener listener, Context context) {
        this.listener = listener;
        // Загрузка анимаций
        scaleDown = AnimationUtils.loadAnimation(context, R.anim.scale_down);
        scaleUp = AnimationUtils.loadAnimation(context, R.anim.scale_up);
        pulse = AnimationUtils.loadAnimation(context, R.anim.hold_pulse);
    }

    // Установка списка книг
    public void setBooks(List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthor());

        String coverPath = book.getCoverImagePath();
        if (coverPath != null && !coverPath.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load("file:///android_asset/" + coverPath)
                    .placeholder(R.drawable.default_cover)
                    .error(R.drawable.error_cover)
                    .into(holder.cover);
        }

        // Обработка касаний
        holder.itemView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.clearAnimation();
                    v.startAnimation(scaleDown);
                    scaleDown.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            v.startAnimation(pulse);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                    });
                    break;

                case MotionEvent.ACTION_UP:
                    v.clearAnimation();
                    v.startAnimation(scaleUp);
                    listener.onBookClick(book);
                    break;

                case MotionEvent.ACTION_CANCEL:
                    v.clearAnimation();
                    v.startAnimation(scaleUp);
                    break;
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    // Внутренний класс ViewHolder
    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView author;
        ImageView cover;

        BookViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.book_title);
            author = itemView.findViewById(R.id.book_author);
            cover = itemView.findViewById(R.id.book_cover);
        }
    }
}