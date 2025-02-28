package fr.enzop.services;

import fr.enzop.models.Adherent;
import fr.enzop.models.Reservation;
import fr.enzop.repositories.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GestionService {

    private final ReservationRepository reservationRepository;
    private final MailService mailService;

    public GestionService(ReservationRepository reservationRepository, MailService mailService) {
        this.reservationRepository = reservationRepository;
        this.mailService = mailService;
    }

    public List<Reservation> getAllOpenReservation() {
        return reservationRepository.findByEndReservationFalse();
    }

    public List<Reservation> getAllHistoricAdherent(Adherent adherent) {
        return reservationRepository.findByAdherentAndEndReservationTrue(adherent);
    }

    public List<Reservation> isReservationAdherantExpired(Adherent existingAdherent) {
        return  reservationRepository.findByAdherent(existingAdherent);
    }
    public void sendOverdueReservationReminder(Adherent adherent) {
        List<Reservation> overdueReservations = isReservationAdherantExpired(adherent);
        if (!overdueReservations.isEmpty()) {
            StringBuilder emailBody = new StringBuilder("Voici les réservations dépassées :\n");
            for (Reservation reservation : overdueReservations) {
                emailBody.append(reservation.getBook().getTitle()).append("\n");
            }
            mailService.sendEmail(adherent.getEmail(), "Rappel de réservation dépassée", emailBody.toString());
        }
    }
}
