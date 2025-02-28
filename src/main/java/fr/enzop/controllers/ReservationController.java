package fr.enzop.controllers;

import fr.enzop.models.Reservation;
import fr.enzop.requests.ReservationRequest;
import fr.enzop.responses.ReservationResponse;
import fr.enzop.services.ReservationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservation")
@Log4j2
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse reserver(@RequestBody ReservationRequest request) {
        Reservation reservation = this.reservationService.addReservation(request);
        return convert(reservation);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReservationResponse ModifierReservation(@PathVariable int id, @RequestBody ReservationRequest request) {
        Reservation reservationToUpdate = this.reservationService.updateReservation(request,id);

        return convert(reservationToUpdate);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void AnnulerReservation(@PathVariable int id) {
        this.reservationService.deleteReservation(id);
    }

    private ReservationResponse convert(Reservation reservation) {
        ReservationResponse resp = ReservationResponse.builder().build();
        BeanUtils.copyProperties(reservation, resp);
        return resp;
    }

}
