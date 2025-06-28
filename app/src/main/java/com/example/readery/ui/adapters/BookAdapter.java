package com.example.readery.ui.adapters;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
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
import com.example.readery.data.AppDatabase;
import com.example.readery.data.Book;
import com.example.readery.data.DownloadedBook;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Адаптер для отображения списка книг в RecyclerView.
 */
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<Book> books = new ArrayList<>();
    private OnBookClickListener listener;
    private Context context;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    /**
     * Интерфейс для обработки кликов по книге.
     */
    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    public BookAdapter(OnBookClickListener listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    /**
     * Устанавливает список книг для отображения.
     */
    public void setBooks(List<Book> books) {
        this.books = (books != null) ? books : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Используем новую разметку item_book.xml
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);

        holder.title.setText(book.getTitle(context));
        holder.author.setText(book.getAuthor(context));

        String coverPath = book.getCoverImagePath(context);
        if (coverPath != null && !coverPath.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load("file:///android_asset/" + coverPath)
                    .placeholder(R.drawable.default_cover)
                    .error(R.drawable.error_cover)
                    .into(holder.cover);
        } else {
            holder.cover.setImageResource(R.drawable.default_cover);
        }

        // Обработчик кликов на всю карточку
        holder.itemView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    CardAnimationHelper.animatePress(v);
                    return true;
                case MotionEvent.ACTION_UP:
                    CardAnimationHelper.animateRelease(v);
                    listener.onBookClick(book);
                    return true;
                case MotionEvent.ACTION_CANCEL:
                    CardAnimationHelper.animateRelease(v);
                    return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    /**
     * ViewHolder для элементов списка книг.
     */
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