package fr.enzop.controllers;

import fr.enzop.ISBNValidator;
import fr.enzop.exceptions.AdherentNotFound;
import fr.enzop.exceptions.BookNotFound;
import fr.enzop.exceptions.InvalidIsbnException;
import fr.enzop.exceptions.MissingParameterException;
import fr.enzop.models.Adherent;
import fr.enzop.models.Book;
import fr.enzop.repositories.AdherentRepository;
import fr.enzop.repositories.BookRepository;
import fr.enzop.requests.AdherentRequest;
import fr.enzop.requests.BookRequest;
import fr.enzop.responses.AdherentResponse;
import fr.enzop.responses.BookResponse;
import fr.enzop.services.AdherentService;
import fr.enzop.services.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adherent")
@Log4j2
public class AdherentController {

    private final AdherentService adherentService;

    public AdherentController(AdherentService adherentService) {
        this.adherentService = adherentService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public AdherentResponse AjoutAdherent(@RequestBody AdherentRequest request) {
        Adherent adherent = this.adherentService.addAdherent(request);
        return convert(adherent);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AdherentResponse ModifierAdherent(@PathVariable int id, @RequestBody AdherentRequest request) {
        Adherent adherentToUpdate = this.adherentService.updateAdherent(request,id);
        return convert(adherentToUpdate);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void SupprimmerAdherent(@PathVariable int id) {
        this.adherentService.deleteAdherent(id);
    }

    private AdherentResponse convert(Adherent adherent) {
        AdherentResponse resp = AdherentResponse.builder().build();
        BeanUtils.copyProperties(adherent, resp);
        return resp;
    }
}
