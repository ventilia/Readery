package com.example.readery.ui.home;

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
import com.example.readery.ui.adapters.BookAdapter;
import com.example.readery.viewmodel.HomeViewModel;

public class HomeFragment extends Fragment {

    private HomeViewModel viewModel;
    private BookAdapter newBooksAdapter;
    private BookAdapter popularBooksAdapter;
    private BookAdapter editorsChoiceBooksAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Инициализация ViewModel
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Настройка адаптеров для каждой секции
        newBooksAdapter = new BookAdapter(book -> {
            // Обработка клика по книге (например, переход к деталям)
            // Можно добавить Intent, как в AllBooksFragment
        }, requireContext());

        popularBooksAdapter = new BookAdapter(book -> {
            // Обработка клика по книге
        }, requireContext());

        editorsChoiceBooksAdapter = new BookAdapter(book -> {
            // Обработка клика по книге
        }, requireContext());

        // Настройка RecyclerView для "Новые"
        RecyclerView recyclerViewNew = root.findViewById(R.id.recycler_view_new);
        recyclerViewNew.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewNew.setAdapter(newBooksAdapter);

        // Настройка RecyclerView для "Популярные"
        RecyclerView recyclerViewPopular = root.findViewById(R.id.recycler_view_popular);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewPopular.setAdapter(popularBooksAdapter);

        // Настройка RecyclerView для "Выбор редакции"
        RecyclerView recyclerViewEditorsChoice = root.findViewById(R.id.recycler_view_editors_choice);
        recyclerViewEditorsChoice.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewEditorsChoice.setAdapter(editorsChoiceBooksAdapter);

        // Подписка на данные из ViewModel
        viewModel.getNewBooks().observe(getViewLifecycleOwner(), books -> {
            newBooksAdapter.setBooks(books);
        });

        viewModel.getPopularBooks().observe(getViewLifecycleOwner(), books -> {
            popularBooksAdapter.setBooks(books);
        });

        viewModel.getEditorsChoiceBooks().observe(getViewLifecycleOwner(), books -> {
            editorsChoiceBooksAdapter.setBooks(books);
        });

        return root;
    }
}