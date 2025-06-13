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
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.recycler_view_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SectionAdapter(requireContext(), sections, book -> {
            Intent intent = new Intent(getActivity(), BookDetailsActivity.class);
            intent.putExtra("bookId", book.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        homeViewModel.getNewBooks().observe(getViewLifecycleOwner(), books -> {
            updateSection("New", books);
        });

        homeViewModel.getPopularBooks().observe(getViewLifecycleOwner(), books -> {
            updateSection("Popular", books);
        });

        homeViewModel.getEditorsChoiceBooks().observe(getViewLifecycleOwner(), books -> {
            updateSection("Editor's Choice", books);
        });

        return root;
    }

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