package fr.enzop.controllers;

import fr.enzop.ISBNValidator;
import fr.enzop.exceptions.InvalidIsbnException;
import fr.enzop.models.Book;
import fr.enzop.repositories.BookRepository;
import fr.enzop.requests.BookRequest;
import fr.enzop.responses.BookResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import fr.enzop.exceptions.BookNotFound;
import fr.enzop.exceptions.MissingParameterException;

import java.util.List;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
@Log4j2
public class LibraryController {

    private final BookRepository bookRepository;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse AjoutLivre(@RequestBody BookRequest request) {
        if (request == null || !request.paramsSet()) {
            throw new MissingParameterException();
        }

        ISBNValidator isbnValidator = new ISBNValidator();

        if (!isbnValidator.validateISBN(request.getIsbn())){
            throw new InvalidIsbnException();
        }

        Book book = new Book();
        BeanUtils.copyProperties(request, book);

        this.bookRepository.save(book);

        return convert(book);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookResponse ModifierLivre(@PathVariable int id, @RequestBody BookRequest request) {
        Book bookToUpdate = this.bookRepository.findById(id).orElseThrow(BookNotFound::new);;

        BeanUtils.copyProperties(request, bookToUpdate);
        this.bookRepository.save(bookToUpdate);

        return convert(bookToUpdate);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void SupprimmerLivre(@PathVariable int id) {
        this.bookRepository.deleteById(id);
    }

    @GetMapping("/search/{search}")
    @ResponseStatus(HttpStatus.OK)
    public List<BookResponse> rechercher(@PathVariable String search) {
        return this.bookRepository.findAllByTitleContainingIgnoreCase(search)
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
