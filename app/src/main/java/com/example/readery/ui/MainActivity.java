package com.example.readery.ui;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.readery.R;
import com.example.readery.utils.SettingsManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.graphics.drawable.ColorDrawable;
import java.util.Locale;

/**
 * Главная активити приложения с нижней навигацией и поддержкой смены языка.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Настройка BottomNavigationView
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_all_books,
                R.id.navigation_library,
                R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // Установка кремового цвета для ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.cream_background)));
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        SettingsManager settingsManager = SettingsManager.getInstance(base);
        String lang = settingsManager.getLanguage();
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);
        base = base.createConfigurationContext(config);
        super.attachBaseContext(base);
    }
}