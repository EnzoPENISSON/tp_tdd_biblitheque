package fr.enzop.repositories;

import fr.enzop.models.Adherent;
import fr.enzop.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByAdherent(Adherent adherent);

    List<Reservation> findByEndReservationFalse();

    List<Reservation> findByAdherentAndEndReservationTrue(Adherent adherent);
}