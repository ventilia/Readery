package com.example.readery.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.readery.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.graphics.drawable.ColorDrawable;

/**
 * Главная активити приложения с нижней навигацией.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Настройка BottomNavigationView
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Обновленная конфигурация с новыми пунктами Library и Settings
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_all_books,
                R.id.navigation_library,
                R.id.navigation_settings)
                .build();

        // Настройка навигации
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // Установка кремового цвета для ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.cream_background)));
        }
    }
}