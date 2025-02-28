package fr.enzop.models;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Reservation {

    @Id
    @UuidGenerator
    private int id;

    @ManyToOne
    @JoinColumn(name = "code")
    private Adherent adherent;

    @ManyToOne
    @JoinColumn(name = "id")
    private Book book;

    private LocalDateTime dateReservation;

    private boolean endReservation = false; // Par defaut fin reservation à faux

    public Reservation() {}

    public Reservation(int id, Adherent adherent, Book book,LocalDateTime dateReservation, boolean endReservation) {
        this.id = id;
        this.adherent = adherent;
        this.book = book;
        this.dateReservation = dateReservation;
        this.endReservation = endReservation;
    }
}
