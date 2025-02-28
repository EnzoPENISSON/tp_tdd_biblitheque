package fr.enzop.controllers;

import fr.enzop.models.Book;
import fr.enzop.requests.BookRequest;
import fr.enzop.responses.BookResponse;
import fr.enzop.services.BookService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/library")
@Log4j2
public class LibraryController {

    private final BookService bookService;

    public LibraryController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse AjoutLivre(@RequestBody BookRequest request) {
        Book book = this.bookService.addBook(request);
        return convert(book);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookResponse ModifierLivre(@PathVariable int id, @RequestBody BookRequest request) {
        Book bookToUpdate = this.bookService.updateBook(request,id);

        return convert(bookToUpdate);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void SupprimmerLivre(@PathVariable int id) {
        this.bookService.deleteBook(id);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<BookResponse> rechercher(@RequestParam(required = false) String title,
                                         @RequestParam(required = false) String author,
                                         @RequestParam(required = false) String isbn
    ) {
        return this.bookService.searchBooks(title,author,isbn)
                .stream()
                .map(this::convert)
                .toList();
    }

    private BookResponse convert(Book book) {
        BookResponse resp = BookResponse.builder().build();
        BeanUtils.copyProperties(book, resp);
        return resp;
    }
}
