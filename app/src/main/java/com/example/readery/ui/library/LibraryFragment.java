package com.example.readery.ui.library;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readery.R;
import com.example.readery.data.AppDatabase;
import com.example.readery.data.Book;
import com.example.readery.data.DownloadedBook;
import com.example.readery.ui.PdfViewerActivity;
import com.example.readery.ui.adapters.BookAdapter;
import com.example.readery.viewmodel.LibraryViewModel;

/**
 * Фрагмент для отображения списка загруженных книг в библиотеке.
 */
public class LibraryFragment extends Fragment {
    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private LibraryViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_library, container, false);

        // Инициализация RecyclerView
        recyclerView = root.findViewById(R.id.recycler_view_library);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Инициализация адаптера с обработчиком кликов
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

        // Инициализация ViewModel
        viewModel = new ViewModelProvider(this).get(LibraryViewModel.class);
        loadDownloadedBooks();

        return root;
    }

    /**
     * Загружает список загруженных книг и обновляет адаптер при изменении данных.
     */
    private void loadDownloadedBooks() {
        viewModel.getDownloadedBooks().observe(getViewLifecycleOwner(), books -> {
            if (books != null) {
                adapter.setBooks(books);
            }
        });
    }
}