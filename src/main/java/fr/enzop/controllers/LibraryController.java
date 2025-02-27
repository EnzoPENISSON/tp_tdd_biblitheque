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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import fr.enzop.exceptions.BookNotFound;

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
        ISBNValidator isbnValidator = new ISBNValidator();

        if (!isbnValidator.validateISBN(request.getIsbn())){
            throw new InvalidIsbnException();
        }

        Book book = new Book();
        BeanUtils.copyProperties(request, book);

        this.bookRepository.save(book);

        log.debug("Livre ajouter");

        return convert(book);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookResponse ModifierLivre(@PathVariable int id, @RequestBody BookRequest request) {
        Book bookToUpdate = this.bookRepository.findById(id).orElseThrow(BookNotFound::new);;

        BeanUtils.copyProperties(request, bookToUpdate);
        this.bookRepository.save(bookToUpdate);
        log.debug("Livre modifier");

        return convert(bookToUpdate);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void SupprimmerLivre(@PathVariable int id) {
        this.bookRepository.deleteById(id);
    }

    @GetMapping("/search/{search}")
    @ResponseStatus(HttpStatus.OK)
    public List<BookResponse> rechercherParLeTitre(@PathVariable String search) {
        return this.bookRepository.findAllByTitleContainingIgnoreCase(search)
                .stream()
                .map(this::convert)
                .toList();
    }

    private BookResponse convert(Book chambre) {
        BookResponse resp = BookResponse.builder().build();
        BeanUtils.copyProperties(chambre, resp);
        return resp;
    }
}
