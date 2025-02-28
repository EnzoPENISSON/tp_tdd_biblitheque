package fr.enzop.services;

import fr.enzop.ISBNValidator;
import fr.enzop.exceptions.BookNotFound;
import fr.enzop.exceptions.InvalidIsbnException;
import fr.enzop.exceptions.MissingParameterException;
import fr.enzop.models.Book;
import fr.enzop.repositories.BookRepository;
import fr.enzop.requests.BookRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    //private final RestTemplate restTemplate;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        //this.restTemplate = restTemplate;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(int id) {
        return bookRepository.findById(id);
    }

    public Optional<Book> getBookByIsbn(int isbn) {
        return bookRepository.findById(isbn);
    }

    public List<Book> searchBooks(String title, String author,String isbn) {
        if (title != null && !title.isEmpty()) {
            return bookRepository.findByTitleContainingIgnoreCase(title);
        }
        if (author != null && !author.isEmpty()) {
            return bookRepository.findByAuthorContainingIgnoreCase(author);
        }
        if (isbn != null && !isbn.isEmpty()) {
            return bookRepository.findByIsbnContainingIgnoreCase(isbn);
        }
        return List.of();
    }

    public Book addBook(BookRequest request) {
        if (request == null || !request.paramsSet()) {
            throw new MissingParameterException();
        }

        ISBNValidator isbnValidator = new ISBNValidator();
        System.out.println(isbnValidator.validateISBN(request.getIsbn()));

        if (!isbnValidator.validateISBN(request.getIsbn())){
            throw new InvalidIsbnException();
        }

        Book book = new Book();
        BeanUtils.copyProperties(request, book);
        return bookRepository.save(book);
    }

    public Book updateBook(BookRequest request, Integer id) {
        if (request == null || !request.paramsSet()) {
            throw new MissingParameterException();
        }

        ISBNValidator isbnValidator = new ISBNValidator();

        if (!isbnValidator.validateISBN(request.getIsbn())){
            throw new InvalidIsbnException();
        }

        if (!bookRepository.existsById(id)) {
            throw new BookNotFound();
        }

        Book bookToUpdate = this.getBookByIsbn(id).orElseThrow(BookNotFound::new);;
        BeanUtils.copyProperties(request, bookToUpdate);
        return bookRepository.save(bookToUpdate);
    }

    public void deleteBook(int isbn) {
        bookRepository.deleteById(isbn);
    }

//    public Book fetchAndSaveBookInfo(String isbn) {
//        String url = "https://api.example.com/books?isbn=" + isbn;
//        Book bookInfo = restTemplate.getForObject(url, Book.class);
//        if (bookInfo != null) {
//            return bookRepository.save(bookInfo);
//        }
//        throw new RuntimeException("Impossible de récupérer les infos pour l'ISBN : " + isbn);
//    }
}
