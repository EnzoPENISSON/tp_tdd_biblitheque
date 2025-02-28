package fr.enzop.responses;


import fr.enzop.models.Adherent;
import fr.enzop.models.Book;
import fr.enzop.models.Civilite;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ReservationResponse {
    private int id;
    private Adherent adherent;
    private Book book;
    private boolean endReservation;
}
