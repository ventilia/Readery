package com.example.readery.ui.allbooks;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.readery.R;
import com.example.readery.ui.BookDetailsActivity;
import com.example.readery.ui.adapters.BookAdapter;
import com.example.readery.viewmodel.AllBooksViewModel;

public class AllBooksFragment extends Fragment {
    private AllBooksViewModel viewModel;
    private RecyclerView recyclerView;
    private BookAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(AllBooksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_all_books, container, false);

        recyclerView = root.findViewById(R.id.recycler_view_all_books);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BookAdapter(book -> {
            Intent intent = new Intent(getActivity(), BookDetailsActivity.class);
            intent.putExtra("bookId", book.getId()); // используем геттер и long
            startActivity(intent);
        });
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

        viewModel.getSearchedBooks().observe(getViewLifecycleOwner(), books -> {
            adapter.setBooks(books);
        });

        return root;
    }
}