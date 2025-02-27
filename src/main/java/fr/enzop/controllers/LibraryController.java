package fr.enzop.controllers;

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

import java.util.Optional;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
@Log4j2
public class LibraryController {

    private final BookRepository bookRepository;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public int AjoutLivre(@RequestBody BookRequest request) {
        Book book = new Book();
        BeanUtils.copyProperties(request, book);

        this.bookRepository.save(book);

        log.debug("Livre ajouter");

        return book.getId();
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

    private BookResponse convert(Book chambre) {
        BookResponse resp = BookResponse.builder().build();
        BeanUtils.copyProperties(chambre, resp);
        return resp;
    }
}
