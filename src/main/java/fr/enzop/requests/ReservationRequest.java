package fr.enzop.requests;


import fr.enzop.models.Adherent;
import fr.enzop.models.Book;
import fr.enzop.models.Format;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@Builder
public class ReservationRequest {
    private Adherent adherent;
    private Book book;
    private boolean endReservation;

    public boolean paramsSet() {
        return adherent != null && book != null;
    }
}
