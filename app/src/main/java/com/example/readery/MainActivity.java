package com.example.readery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp; // Импорт для инициализации
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BookAdapter.OnBookClickListener {
    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private List<Book> bookList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация Firebase
        FirebaseApp.initializeApp(this);

        // Инициализация Firestore
        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recycler_view_books);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        bookList = new ArrayList<>();
        adapter = new BookAdapter(bookList, this);
        recyclerView.setAdapter(adapter);

        loadBooks(""); // Загрузка книг

        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadBooks(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadBooks(newText);
                return true;
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                return true;
            } else if (id == R.id.navigation_library) {
                startActivity(new Intent(MainActivity.this, LibraryActivity.class));
                return true;
            } else if (id == R.id.navigation_profile) {
                return true;
            }
            return false;
        });
    }

    private void loadBooks(String query) {
        db.collection("books")
                .whereGreaterThanOrEqualTo("title", query)
                .whereLessThanOrEqualTo("title", query + "\uf8ff")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        bookList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Book book = document.toObject(Book.class);
                            book.setId(document.getId());
                            bookList.add(book);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onBookClick(Book book) {
        Intent intent = new Intent(this, BookDetailsActivity.class);
        intent.putExtra("book_id", book.getId());
        startActivity(intent);
    }
}