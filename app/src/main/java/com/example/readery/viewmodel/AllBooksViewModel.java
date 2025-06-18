package com.example.readery.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.readery.data.Book;
import com.example.readery.repository.BookRepository;
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

    /**
     * Конструктор ViewModel.
     *
     * @param application Приложение, для доступа к контексту.
     */
    public AllBooksViewModel(Application application) {
        super(application);
        repository = new BookRepository(application);
        // Определяем текущий язык устройства
        language = application.getResources().getConfiguration().getLocales().get(0).getLanguage();

        // Настройка MediatorLiveData для отслеживания изменений в searchQuery, filterType и filterOrder
        searchedBooks.addSource(searchQuery, query -> updateSearchedBooks());
        searchedBooks.addSource(filterType, type -> updateSearchedBooks());
        searchedBooks.addSource(filterOrder, order -> updateSearchedBooks());
    }

    /**
     * Получает все книги из репозитория.
     *
     * @return LiveData со списком всех книг.
     */
    public LiveData<List<Book>> getBooks() {
        return repository.getAllBooks();
    }

    /**
     * Устанавливает запрос для поиска книг.
     *
     * @param query Строка запроса.
     */
    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    /**
     * Устанавливает тип фильтрации и порядок.
     *
     * @param type  Тип фильтрации ("title" или "author").
     * @param order Порядок ("ASC" или "DESC").
     */
    public void setFilter(String type, String order) {
        filterType.setValue(type);
        filterOrder.setValue(order);
    }

    /**
     * Получает отфильтрованные и отсортированные книги на основе текущего запроса и фильтров.
     *
     * @return LiveData со списком книг, соответствующих запросу и фильтрам.
     */
    public LiveData<List<Book>> getSearchedBooks() {
        return searchedBooks;
    }

    /**
     * Обновляет список книг на основе текущего запроса, фильтров и языка.
     */
    private void updateSearchedBooks() {
        String query = searchQuery.getValue();
        String type = filterType.getValue();
        String order = filterOrder.getValue();

        if (query == null || query.isEmpty()) {
            // Если запрос пуст, возвращаем все книги
            searchedBooks.setValue(repository.getAllBooks().getValue());
        } else {
            // Иначе выполняем поиск с учетом языка
            LiveData<List<Book>> booksLiveData = repository.searchBooks(
                    "%" + query + "%", type, order, language);
            searchedBooks.addSource(booksLiveData, books -> {
                searchedBooks.setValue(books);
                searchedBooks.removeSource(booksLiveData);
            });
        }
    }
}