package fr.enzop.services;

import fr.enzop.models.Reservation;
import fr.enzop.repositories.ReservationRepository;
import fr.enzop.requests.ReservationRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import fr.enzop.exceptions.ReservationNotFound;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation addReservation(ReservationRequest reservationRequest) {
        Reservation reservation = new Reservation();
        BeanUtils.copyProperties(reservationRequest, reservation);
        return reservationRepository.save(reservation);
    }

    public Reservation updateReservation(ReservationRequest reservationRequest, Integer id) {
        if (!reservationRepository.existsById(id)){
            throw new ReservationNotFound();
        }

        Reservation reservationtoUpdate = reservationRepository.findById(id).orElseThrow(ReservationNotFound::new);
        BeanUtils.copyProperties(reservationRequest, reservationtoUpdate);
        return reservationRepository.save(reservationtoUpdate);
    }

    public void deleteReservation(int id) {
        reservationRepository.deleteById(id);
    }

    public List<Reservation> getAllReservation() {
        return reservationRepository.findAll();
    }

    public void deleteBook(int isbn) {
        reservationRepository.deleteById(isbn);
    }
}
