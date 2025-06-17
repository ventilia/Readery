package com.example.readery.ui.settings;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.readery.R;
import com.example.readery.utils.SettingsManager;

/**
 * Фрагмент для экрана "Settings" с настройкой языка.
 */
public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        LinearLayout languageSetting = root.findViewById(R.id.language_setting);
        TextView languageText = root.findViewById(R.id.language_text);

        // Установка текущего языка
        SettingsManager settingsManager = SettingsManager.getInstance(getContext());
        String currentLang = settingsManager.getLanguage();
        languageText.setText("en".equals(currentLang) ? "English" : "Русский");

        // Обработчик клика для выбора языка
        languageSetting.setOnClickListener(v -> showLanguageDialog(settingsManager, languageText));

        return root;
    }

    private void showLanguageDialog(SettingsManager settingsManager, TextView languageText) {
        String[] languages = {"English", "Русский"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Выберите язык");
        builder.setItems(languages, (dialog, which) -> {
            String selectedLang = which == 0 ? "en" : "ru";
            settingsManager.setLanguage(selectedLang);
            languageText.setText(languages[which]);
            requireActivity().recreate(); // Перезапуск активности для применения локали
        });
        builder.show();
    }
}