package com.example.readery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<Book> bookList;
    private OnBookClickListener listener;

    // Интерфейс для обработки кликов
    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    public BookAdapter(List<Book> bookList, OnBookClickListener listener) {
        this.bookList = bookList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthor());
        holder.price.setText(String.format("%.2f ₽", book.getPrice()));
        Glide.with(holder.itemView.getContext()).load(book.getCoverUrl()).into(holder.cover);

        // Обработка клика по карточке
        holder.itemView.setOnClickListener(v -> listener.onBookClick(book));
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView cover;
        TextView title, author, price;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.book_cover);
            title = itemView.findViewById(R.id.book_title);
            author = itemView.findViewById(R.id.book_author);
            price = itemView.findViewById(R.id.book_price);
        }
    }
}