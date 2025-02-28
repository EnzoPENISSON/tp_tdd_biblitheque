package fr.enzop.services;

import fr.enzop.exceptions.TooManyReservationsException;
import fr.enzop.models.Adherent;
import fr.enzop.models.Reservation;
import fr.enzop.repositories.ReservationRepository;
import fr.enzop.requests.ReservationRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import fr.enzop.exceptions.ReservationNotFound;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation addReservation(ReservationRequest reservationRequest) {
        if (countOpenReservationsByAdherent(reservationRequest.getAdherent()) == 3) {
            throw new TooManyReservationsException("L'adhérent a déjà 3 réservations ouvertes");
        }

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

    public int countOpenReservationsByAdherent(Adherent adherent) {
        return (int) adherent.getReservations().stream()
                .filter(reservation -> !reservation.isEndReservation()) // Ne compte que celles qui ne sont pas terminées
                .count();
    }

    public boolean isReservationExpired(Reservation reservation) {
        return reservation.getDateReservation().plusMonths(4).isBefore(LocalDateTime.now());
    }

    public List<Reservation> getAllReservationAdherent(Adherent adherent) {
        return reservationRepository.findByAdherent(adherent);
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
