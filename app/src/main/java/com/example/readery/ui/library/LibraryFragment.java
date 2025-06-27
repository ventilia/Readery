package com.example.readery.ui.library;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readery.R;
import com.example.readery.data.AppDatabase;
import com.example.readery.data.Book;
import com.example.readery.data.DownloadedBook;
import com.example.readery.ui.PdfViewerActivity;
import com.example.readery.ui.adapters.BookAdapter;

import java.util.ArrayList;
import java.util.List;

public class LibraryFragment extends Fragment {
    private RecyclerView recyclerView;
    private BookAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_library, container, false);

        recyclerView = root.findViewById(R.id.recycler_view_library);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new BookAdapter(book -> {
            AppDatabase db = AppDatabase.getInstance(getContext());
            DownloadedBook downloadedBook = db.downloadedBookDao().getDownloadedBookById(book.getId());
            if (downloadedBook != null) {
                Intent intent = new Intent(getActivity(), PdfViewerActivity.class);
                intent.putExtra("pdfPath", downloadedBook.getPdfPath(getContext()));
                startActivity(intent);
            }
        }, requireContext());
        recyclerView.setAdapter(adapter);

        loadDownloadedBooks();

        return root;
    }

    private void loadDownloadedBooks() {
        AppDatabase db = AppDatabase.getInstance(getContext());
        db.downloadedBookDao().getAllDownloadedBooks().observe(getViewLifecycleOwner(), downloadedBooks -> {
            if (downloadedBooks != null) {
                List<Book> books = new ArrayList<>();
                for (DownloadedBook dbBook : downloadedBooks) {
                    Book book = db.bookDao().getBookById(dbBook.getBookId()).getValue();
                    if (book != null) books.add(book);
                }
                adapter.setBooks(books);
            }
        });
    }
}