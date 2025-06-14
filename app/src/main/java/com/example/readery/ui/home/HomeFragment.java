package com.example.readery.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.readery.R;
import com.example.readery.ui.BookDetailsActivity;
import com.example.readery.ui.adapters.BookAdapter;
import com.example.readery.viewmodel.HomeViewModel;

public class HomeFragment extends Fragment {

    private HomeViewModel viewModel;
    private BookAdapter newBooksAdapter;
    private BookAdapter popularBooksAdapter;
    private BookAdapter editorsChoiceBooksAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Инициализация адаптеров с обработчиком кликов
        newBooksAdapter = new BookAdapter(book -> {
            Intent intent = new Intent(getActivity(), BookDetailsActivity.class);
            intent.putExtra("bookId", book.getId());
            startActivity(intent);
        }, requireContext());

        popularBooksAdapter = new BookAdapter(book -> {
            Intent intent = new Intent(getActivity(), BookDetailsActivity.class);
            intent.putExtra("bookId", book.getId());
            startActivity(intent);
        }, requireContext());

        editorsChoiceBooksAdapter = new BookAdapter(book -> {
            Intent intent = new Intent(getActivity(), BookDetailsActivity.class);
            intent.putExtra("bookId", book.getId());
            startActivity(intent);
        }, requireContext());

        // RecyclerView для "Новые"
        RecyclerView recyclerViewNew = root.findViewById(R.id.recycler_view_new);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        recyclerViewNew.setLayoutManager(gridLayoutManager);
        recyclerViewNew.setAdapter(newBooksAdapter);

        // RecyclerView для "Популярные"
        RecyclerView recyclerViewPopular = root.findViewById(R.id.recycler_view_popular);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewPopular.setAdapter(popularBooksAdapter);

        // RecyclerView для "Выбор редакции"
        RecyclerView recyclerViewEditorsChoice = root.findViewById(R.id.recycler_view_editors_choice);
        recyclerViewEditorsChoice.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewEditorsChoice.setAdapter(editorsChoiceBooksAdapter);

        // Подписка на данные из ViewModel
        viewModel.getNewBooks().observe(getViewLifecycleOwner(), newBooksAdapter::setBooks);
        viewModel.getPopularBooks().observe(getViewLifecycleOwner(), popularBooksAdapter::setBooks);
        viewModel.getEditorsChoiceBooks().observe(getViewLifecycleOwner(), editorsChoiceBooksAdapter::setBooks);

        return root;
    }
}