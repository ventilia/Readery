package com.example.readery.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.readery.data.Book;
import com.example.readery.ui.adapters.BookAdapter;
import com.example.readery.R;
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
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_section, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {
        Section sec = sections.get(position);
        holder.title.setText(sec.title);

        if ("New".equals(sec.title)) {

            GridLayoutManager glm = new GridLayoutManager(
                    context,
                    2,
                    GridLayoutManager.HORIZONTAL,
                    false
            );
            holder.recycler.setLayoutManager(glm);
        } else {

            holder.recycler.setLayoutManager(
                    new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            );
        }

        BookAdapter adapter = new BookAdapter(bookClickListener, context);
        adapter.setBooks(sec.books);
        holder.recycler.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return sections.size();
    }

    static class SectionViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        RecyclerView recycler;

        SectionViewHolder(View v) {
            super(v);
            title   = v.findViewById(R.id.section_title);
            recycler= v.findViewById(R.id.section_recycler_view);
        }
    }

    public static class Section {
        public final String title;
        public final List<Book> books;
        public Section(String title, List<Book> books) {
            this.title = title;
            this.books = books;
        }
    }
}