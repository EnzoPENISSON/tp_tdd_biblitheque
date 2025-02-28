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
import java.util.function.Supplier;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final WebService webService;

    public BookService(BookRepository bookRepository, WebService webService) {
        this.bookRepository = bookRepository;
        this.webService = webService;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(int id) {
        return bookRepository.findById(id)
                .or(() -> fetchAndSaveBookFromWebService(() -> webService.fetchBookById(id)));
    }

    public Optional<Book> getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .or(() -> fetchAndSaveBookFromWebService(() -> webService.fetchBookByIsbn(isbn)));
    }

    public List<Book> searchBooks(String title, String author, String isbn) {
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
        if (!isbnValidator.validateISBN(request.getIsbn())) {
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
        if (!isbnValidator.validateISBN(request.getIsbn())) {
            throw new InvalidIsbnException();
        }

        if (!bookRepository.existsById(id)) {
            throw new BookNotFound();
        }

        Book bookToUpdate = this.getBookByIsbn(request.getIsbn()).orElseThrow(BookNotFound::new);
        BeanUtils.copyProperties(request, bookToUpdate);
        return bookRepository.save(bookToUpdate);
    }

    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }

    public boolean bookAvailable(Book book) {
        Book existingBook = bookRepository.findById(book.getId()).orElseThrow(BookNotFound::new);
        return existingBook.isAvailable();
    }

    public void setBookAvailable(Book book) {
        Book bookToSetAvailable = bookRepository.findById(book.getId()).orElseThrow(BookNotFound::new);
        bookToSetAvailable.setAvailable(true);
    }

    public void setBookNotAvailable(Book book) {
        Book bookToSetAvailable = bookRepository.findById(book.getId()).orElseThrow(BookNotFound::new);
        bookToSetAvailable.setAvailable(false);
    }

    private Optional<Book> fetchAndSaveBookFromWebService(Supplier<Optional<Book>> fetchFunction) {
        return fetchFunction.get().map(bookRepository::save);
    }
}