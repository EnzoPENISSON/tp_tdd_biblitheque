package fr.enzop.services;

import fr.enzop.models.Reservation;
import fr.enzop.repositories.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdherentService {

    private final ReservationRepository reservationRepository;

    public AdherentService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> getAllReservation() {
        return reservationRepository.findAll();
    }

    public void deleteBook(int isbn) {
        reservationRepository.deleteById(isbn);
    }
}
