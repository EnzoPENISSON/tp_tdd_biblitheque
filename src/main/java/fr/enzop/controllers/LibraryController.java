package fr.enzop.controllers;

import fr.enzop.repositories.BookRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Getter
@Setter
public class LibraryController {

    @Autowired
    private BookRepository data;
}
