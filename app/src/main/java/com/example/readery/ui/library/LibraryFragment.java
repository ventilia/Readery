package com.example.readery.ui.library;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class LibraryFragment extends Fragment {
    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private LibraryViewModel viewModel;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_library, container, false);

        recyclerView = root.findViewById(R.id.recycler_view_library);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new BookAdapter(book -> {
            executorService.execute(() -> {
                AppDatabase db = AppDatabase.getInstance(getContext());
                DownloadedBook downloadedBook = db.downloadedBookDao().getDownloadedBookById(book.getId());
                mainHandler.post(() -> {
                    if (downloadedBook != null) {
                        Intent intent = new Intent(getActivity(), PdfViewerActivity.class);
                        intent.putExtra("pdfPath", downloadedBook.getPdfPath(getContext()));
                        startActivity(intent);
                    }
                });
            });
        }, requireContext());

        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(LibraryViewModel.class);
        loadDownloadedBooks();

        return root;
    }


    private void loadDownloadedBooks() {
        viewModel.getDownloadedBooks().observe(getViewLifecycleOwner(), books -> {
            if (books != null) {
                adapter.setBooks(books);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}