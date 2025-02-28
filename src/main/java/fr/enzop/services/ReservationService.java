package fr.enzop.services;

import fr.enzop.exceptions.BookNotAvailable;
import fr.enzop.exceptions.TooManyReservationsException;
import fr.enzop.models.Adherent;
import fr.enzop.models.Book;
import fr.enzop.models.Reservation;
import fr.enzop.repositories.BookRepository;
import fr.enzop.repositories.ReservationRepository;
import fr.enzop.requests.ReservationRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;
import fr.enzop.exceptions.ReservationNotFound;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;


    public ReservationService(ReservationRepository reservationRepository, BookRepository bookRepository) {
        this.reservationRepository = reservationRepository;
        this.bookRepository = bookRepository;

    }

    public Reservation addReservation(ReservationRequest reservationRequest) {
        if (countOpenReservationsByAdherent(reservationRequest.getAdherent()) == 3) {
            throw new TooManyReservationsException("L'adhérent a déjà 3 réservations ouvertes");
        }

        BookService bookService = new BookService(bookRepository, new WebService(new RestTemplate()));

        if (!bookService.bookAvailable(reservationRequest.getBook())){
            throw new BookNotAvailable();
        }

        Reservation reservation = new Reservation();
        BeanUtils.copyProperties(reservationRequest, reservation);

        Reservation savedReservation = reservationRepository.save(reservation);

        bookService.setBookNotAvailable(reservationRequest.getBook());

        return savedReservation;
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
        return reservationRepository.findAllReservationByAdherent(adherent);
    }

    public void deleteReservation(int id) {
        reservationRepository.deleteById(id);
    }

    public boolean cancelReservation(int id){
        if (reservationRepository.existsById(id)) {
            Reservation reservation = reservationRepository.findById(id).orElseThrow(ReservationNotFound::new);
            reservation.setEndReservation(false);

            BookService bookService = new BookService(bookRepository, new WebService(new RestTemplate()));
            bookService.setBookAvailable(reservation.getBook());


            return reservationRepository.save(reservation).isEndReservation();
        } else {
            throw new ReservationNotFound();
        }
    }

    public List<Reservation> getAllReservation() {
        return reservationRepository.findAll();
    }

    public void deleteBook(int isbn) {
        reservationRepository.deleteById(isbn);
    }

}
