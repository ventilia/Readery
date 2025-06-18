package com.example.readery.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.readery.data.Book;
import com.example.readery.repository.BookRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel для экрана "All Books", управляющая данными книг и поиском.
 */
public class AllBooksViewModel extends AndroidViewModel {
    private BookRepository repository;
    private MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private MutableLiveData<String> filterType = new MutableLiveData<>("title");
    private MutableLiveData<String> filterOrder = new MutableLiveData<>("ASC");
    private MediatorLiveData<List<Book>> searchedBooks = new MediatorLiveData<>();
    private String language;
    private LiveData<List<Book>> currentSource;

    /**
     * конструктор ViewModel
     * @param application приложение для доступа к контексту
     */
    public AllBooksViewModel(Application application) {
        super(application);
        repository = new BookRepository(application);
        language = application.getResources().getConfiguration().getLocales().get(0).getLanguage();

        // инициализируем searchedBooks с allBooks
        currentSource = repository.getAllBooks();
        searchedBooks.addSource(currentSource, books ->
                searchedBooks.setValue(books != null ? books : new ArrayList<>())
        );

        // наблюдаем за изменениями фильтров и запроса
        searchQuery.observeForever(this::updateSearchedBooks);
        filterType.observeForever(this::updateSearchedBooks);
        filterOrder.observeForever(this::updateSearchedBooks);
    }

    /**
     * получает отфильтрованные и отсортированные книги
     * @return LiveData со списком книг
     */
    public LiveData<List<Book>> getSearchedBooks() {
        return searchedBooks;
    }

    /**
     * устанавливает запрос для поиска книг
     * @param query строка запроса
     */
    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    /**
     * устанавливает тип фильтрации и порядок
     * @param type тип фильтрации ("title" или "author")
     * @param order порядок ("ASC" или "DESC")
     */
    public void setFilter(String type, String order) {
        filterType.setValue(type);
        filterOrder.setValue(order);
    }

    /**
     * обновляет список книг на основе текущего запроса и фильтров
     */
    private void updateSearchedBooks(Object ignored) {
        String query = searchQuery.getValue();
        String type = filterType.getValue();
        String order = filterOrder.getValue();

        // удаляем предыдущий источник
        if (currentSource != null) {
            searchedBooks.removeSource(currentSource);
        }

        // определяем новый источник данных
        if (query == null || query.isEmpty()) {
            currentSource = repository.getAllBooks();
        } else {
            currentSource = repository.searchBooks("%" + query + "%", type, order, language);
        }

        // добавляем новый источник и обрабатываем данные
        searchedBooks.addSource(currentSource, books ->
                searchedBooks.setValue(books != null ? books : new ArrayList<>())
        );
    }

    /**
     * очистка ресурсов при уничтожении ViewModel
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        searchQuery.removeObserver(this::updateSearchedBooks);
        filterType.removeObserver(this::updateSearchedBooks);
        filterOrder.removeObserver(this::updateSearchedBooks);
    }
}