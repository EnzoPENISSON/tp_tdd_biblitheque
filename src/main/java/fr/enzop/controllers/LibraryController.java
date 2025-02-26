package fr.enzop.controllers;

import fr.enzop.models.Book;
import fr.enzop.repositories.BookRepository;
import fr.enzop.requests.BookRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
@Log4j2
public class LibraryController {

    private final BookRepository bookRepository;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public String createChambre(@RequestBody BookRequest request) {
        Book book = new Book();
        BeanUtils.copyProperties(request, book);

        this.bookRepository.save(book);

        log.debug("Livre ajouter");

        return book.getId();
    }

}
