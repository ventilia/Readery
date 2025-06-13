package com.example.readery.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readery.R;
import com.example.readery.data.Book;
import com.example.readery.ui.adapters.BookAdapter;

import java.util.List;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionViewHolder> {
    private Context context;
    private List<Section> sections;
    private BookAdapter.OnBookClickListener bookClickListener;

    public SectionAdapter(Context context, List<Section> sections, BookAdapter.OnBookClickListener listener) {
        this.context = context;
        this.sections = sections;
        this.bookClickListener = listener;
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {
        Section section = sections.get(position);
        holder.title.setText(section.title);
        BookAdapter bookAdapter = new BookAdapter(bookClickListener, context);
        bookAdapter.setBooks(section.books);
        holder.recyclerView.setAdapter(bookAdapter);
    }

    @Override
    public int getItemCount() {
        return sections.size();
    }

    static class SectionViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        RecyclerView recyclerView;

        SectionViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.section_title);
            recyclerView = itemView.findViewById(R.id.section_recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        }
    }

    static class Section {
        String title;
        List<Book> books;

        Section(String title, List<Book> books) {
            this.title = title;
            this.books = books;
        }
    }
}