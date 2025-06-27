package com.example.readery.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<Book> books = new ArrayList<>();
    private OnBookClickListener listener;
    private Context context;

    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    public BookAdapter(OnBookClickListener listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    public void setBooks(List<Book> books) {
        this.books = (books != null) ? books : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_library, parent, false);
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
        }

        AppDatabase db = AppDatabase.getInstance(context);
        DownloadedBook downloadedBook = db.downloadedBookDao().getDownloadedBookById(book.getId());
        if (downloadedBook != null) {
            holder.actionButton.setText("Читать");
            holder.actionButton.setOnClickListener(v -> listener.onBookClick(book));
        } else {
            holder.actionButton.setText("Добавить");
            holder.actionButton.setOnClickListener(null); // не используется в библиотеке
        }

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
        return books != null ? books.size() : 0;
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView author;
        ImageView cover;
        Button actionButton;

        BookViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.book_title);
            author = itemView.findViewById(R.id.book_author);
            cover = itemView.findViewById(R.id.book_cover);
            actionButton = itemView.findViewById(R.id.action_button);
        }
    }
}