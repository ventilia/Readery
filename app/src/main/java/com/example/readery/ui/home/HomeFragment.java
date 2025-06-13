package com.example.readery.ui.home;

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
import com.example.readery.data.Book;
import com.example.readery.ui.BookDetailsActivity;
import com.example.readery.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private SectionAdapter adapter;
    private List<SectionAdapter.Section> sections = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // инициируем viewmodel
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // подключаем layout для фрагмента
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // настраиваем recyclerview
        recyclerView = root.findViewById(R.id.recycler_view_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // создаем адаптер с обработчиком клика по книге
        adapter = new SectionAdapter(sections, book -> {
            Intent intent = new Intent(getActivity(), BookDetailsActivity.class);
            // используем геттер getId() вместо прямого доступа к приватному полю id
            intent.putExtra("bookId", book.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        // наблюдаем за списком новых книг
        homeViewModel.getNewBooks().observe(getViewLifecycleOwner(), books -> {
            updateSection("New", books);
        });

        // наблюдаем за списком популярных книг
        homeViewModel.getPopularBooks().observe(getViewLifecycleOwner(), books -> {
            updateSection("Popular", books);
        });

        // наблюдаем за списком книг, выбранных редактором
        homeViewModel.getEditorsChoiceBooks().observe(getViewLifecycleOwner(), books -> {
            updateSection("Editor's Choice", books);
        });

        return root;
    }

    // обновляем или добавляем секцию в список
    private void updateSection(String title, List<Book> books) {
        for (int i = 0; i < sections.size(); i++) {
            if (sections.get(i).title.equals(title)) {
                sections.set(i, new SectionAdapter.Section(title, books));
                adapter.notifyItemChanged(i);
                return;
            }
        }
        sections.add(new SectionAdapter.Section(title, books));
        adapter.notifyItemInserted(sections.size() - 1);
    }
}