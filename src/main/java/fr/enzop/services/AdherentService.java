package fr.enzop.services;

import fr.enzop.ISBNValidator;
import fr.enzop.exceptions.AdherentNotFound;
import fr.enzop.exceptions.BookNotFound;
import fr.enzop.exceptions.InvalidIsbnException;
import fr.enzop.exceptions.MissingParameterException;
import fr.enzop.models.Adherent;
import fr.enzop.models.Book;
import fr.enzop.models.Reservation;
import fr.enzop.repositories.AdherentRepository;
import fr.enzop.repositories.ReservationRepository;
import fr.enzop.requests.AdherentRequest;
import fr.enzop.requests.BookRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdherentService {

    private final AdherentRepository adherentRepository;

    public AdherentService(AdherentRepository adherentRepository) {
        this.adherentRepository = adherentRepository;
    }

    public Optional<Adherent> getAdherentyId(int id) {
        return adherentRepository.findById(id);
    }

    public Adherent addAdherent(AdherentRequest request) {
        if (request == null || !request.paramsSet()) {
            throw new MissingParameterException();
        }

        Adherent adherent = new Adherent();
        BeanUtils.copyProperties(request, adherent);

        return adherentRepository.save(adherent);
    }

    public Adherent updateAdherent(AdherentRequest request, Integer id) {
        if (request == null || !request.paramsSet()) {
            throw new MissingParameterException();
        }

        if (!adherentRepository.existsById(id)) {
            throw new AdherentNotFound();
        }

        Adherent adherentToUpdate = this.getAdherentyId(id).orElseThrow(AdherentNotFound::new);;
        BeanUtils.copyProperties(request, adherentToUpdate);
        return adherentRepository.save(adherentToUpdate);
    }

    public List<Adherent> getAllAdherent() {
        return adherentRepository.findAll();
    }

    public void deleteAdherent(int id) {
        adherentRepository.deleteById(id);
    }
}
