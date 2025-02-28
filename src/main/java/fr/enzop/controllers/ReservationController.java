package fr.enzop.controllers;

import fr.enzop.ISBNValidator;
import fr.enzop.exceptions.BookNotFound;
import fr.enzop.exceptions.InvalidIsbnException;
import fr.enzop.exceptions.MissingParameterException;
import fr.enzop.models.Book;
import fr.enzop.models.Reservation;
import fr.enzop.repositories.ReservationRepository;
import fr.enzop.requests.BookRequest;
import fr.enzop.requests.ReservationRequest;
import fr.enzop.responses.BookResponse;
import fr.enzop.responses.ReservationResponse;
import fr.enzop.services.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservation")
@Log4j2
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }



    private ReservationResponse convert(Reservation reservation) {
        ReservationResponse resp = ReservationResponse.builder().build();
        BeanUtils.copyProperties(reservation, resp);
        return resp;
    }

}
