package com.example.readery.ui.library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.readery.R;

/**
 * Фрагмент для экрана "Library".
 * Пока пустой, но стилизован под общий дизайн приложения.
 */
public class LibraryFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Инфлейтим макет фрагмента
        View root = inflater.inflate(R.layout.fragment_library, container, false);
        return root;
    }
}