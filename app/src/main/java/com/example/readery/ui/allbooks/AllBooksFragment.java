package com.example.readery.ui.allbooks;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readery.R;
import com.example.readery.ui.BookDetailsActivity;
import com.example.readery.ui.adapters.BookAdapter;
import com.example.readery.viewmodel.AllBooksViewModel;

public class AllBooksFragment extends Fragment {
    private AllBooksViewModel viewModel;
    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private String currentFilterType = "title"; // По умолчанию фильтр по названию
    private String currentFilterOrder = "ASC"; // По умолчанию прямой порядок

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(AllBooksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_all_books, container, false);

        recyclerView = root.findViewById(R.id.recycler_view_all_books);

        // Настройка GridLayoutManager с динамическим количеством колонок
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidthPx = metrics.widthPixels;
        final float MIN_ITEM_WIDTH_DP = 120f;
        int minItemWidthPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                MIN_ITEM_WIDTH_DP,
                metrics
        );
        int spanCount = Math.max(1, screenWidthPx / minItemWidthPx);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));

        adapter = new BookAdapter(book -> {
            Intent intent = new Intent(getActivity(), BookDetailsActivity.class);
            intent.putExtra("bookId", book.getId());
            startActivity(intent);
        }, requireContext());
        recyclerView.setAdapter(adapter);

        SearchView searchView = root.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.setSearchQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.setSearchQuery(newText);
                return true;
            }
        });

        ImageButton filterButton = root.findViewById(R.id.filter_button);
        filterButton.setOnClickListener(v -> showFilterDialog());

        viewModel.getSearchedBooks().observe(getViewLifecycleOwner(), books -> {
            adapter.setBooks(books);
        });

        return root;
    }

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Фильтр");

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_filter, null);
        builder.setView(dialogView);

        Spinner filterTypeSpinner = dialogView.findViewById(R.id.filter_type_spinner);
        Spinner filterOrderSpinner = dialogView.findViewById(R.id.filter_order_spinner);

        builder.setPositiveButton("Применить", (dialog, which) -> {
            String filterType = filterTypeSpinner.getSelectedItem().toString();
            String filterOrder = filterOrderSpinner.getSelectedItem().toString();
            currentFilterType = filterType.equals("Название") ? "title" : "author";
            currentFilterOrder = filterOrder.equals("А-Я") ? "ASC" : "DESC";
            viewModel.setFilter(currentFilterType, currentFilterOrder);
        });

        builder.setNegativeButton("Отмена", null);

        builder.show();
    }
}