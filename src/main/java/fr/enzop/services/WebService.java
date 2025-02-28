package fr.enzop.services;

import fr.enzop.models.Book;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class WebService {

    private final RestTemplate restTemplate;
    private static final String API_BASE_URL = "https://api.books.fr/getbooks";

    public WebService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<Book> fetchBookById(int id) {
        String url = API_BASE_URL + "/" + id;
        Book book = restTemplate.getForObject(url, Book.class);
        return Optional.ofNullable(book);
    }

    public Optional<Book> fetchBookByIsbn(String isbn) {
        String url = API_BASE_URL + "?isbn=" + isbn;
        Book book = restTemplate.getForObject(url, Book.class);
        return Optional.ofNullable(book);
    }
}
