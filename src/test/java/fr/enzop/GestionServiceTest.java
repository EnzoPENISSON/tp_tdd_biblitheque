package fr.enzop;

import fr.enzop.controllers.AdherentController;
import fr.enzop.controllers.ReservationController;
import fr.enzop.exceptions.MissingParameterException;
import fr.enzop.exceptions.ReservationNotFound;
import fr.enzop.models.*;
import fr.enzop.repositories.AdherentRepository;
import fr.enzop.repositories.ReservationRepository;
import fr.enzop.requests.AdherentRequest;
import fr.enzop.responses.AdherentResponse;
import fr.enzop.services.AdherentService;
import fr.enzop.services.GestionService;
import fr.enzop.services.MailService;
import fr.enzop.services.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GestionServiceTest {

    private static final int ADHERENT_ID = 2;
    private static final int BOOK_ID = 2;
    private static final int RESERVATION_ID = 2;

    GestionService mockgestionService;
    ReservationService mockDbService;
    MailService mockMailService;
    ReservationController reservationController;

    private Adherent existingAdherent = new Adherent(
            ADHERENT_ID,
            "Dupont",
            "Antoine",
            "antoine@gmail.com",
            LocalDateTime.parse("2000-11-01T00:00:00"),
            Civilite.HOMME
    );
    private Book existingBook = new Book(
            BOOK_ID,
            "Les Misérables",
            "Victor Hugo",
            true,
            "Livre de Poche Jeunesse (13 Aug. 2014)",
            Format.POCHE,
            "2010008995"
    );

    private Book NonavailableBook = new Book(
            5,
            "Le Roman de Renart T01",
            "Thierry Martin",
            false,
            "Delcourt (24 Jan. 2007)",
            Format.POCHE,
            "2756003581"
    );

    private Reservation existingReservationExpired = new Reservation(
            RESERVATION_ID,
            existingAdherent,
            existingBook,
            LocalDateTime.parse("2024-09-01T00:00:00"),
            false
    );

    List<Reservation> reservationList = new ArrayList<>();

    @BeforeEach
    public void init() {
        mockDbService = mock(ReservationService.class);
        mockMailService = mock(MailService.class);
        mockgestionService = mock(GestionService.class);
        reservationController = new ReservationController(mockDbService,mockMailService);
    }

    @Test
    void shouldThrowException_WhenReservationNotFound() {
        int reservationId = 999; // Non-existing reservation

        Mockito.when(mockDbService.cancelReservation(reservationId))
                .thenThrow(new ReservationNotFound());

        assertThrows(ReservationNotFound.class, () -> mockDbService.cancelReservation(reservationId));

        verify(mockDbService, times(1)).cancelReservation(reservationId);
    }

    @Test
    void shouldGetAllReservations_WhenNotEndReserved() {
        Mockito.when(mockgestionService.getAllOpenReservation())
                .thenReturn(reservationList);

        List<Reservation> result = mockgestionService.getAllOpenReservation();

        assertNotNull(result);
        assertEquals(reservationList.size(), result.size());

        verify(mockgestionService, times(1)).getAllOpenReservation();
    }

    @Test
    void shouldGetAllHistoricalReservations_WhenEndReserved() {
        // list de réservation historique
        List<Reservation> reservations = Arrays.asList(
                new Reservation(
                        RESERVATION_ID,
                        existingAdherent,
                        NonavailableBook,
                        LocalDateTime.parse("2024-02-01T00:00:00"),
                        true
                ),
                new Reservation(
                        RESERVATION_ID,
                        existingAdherent,
                        NonavailableBook,
                        LocalDateTime.parse("2023-02-01T00:00:00"),
                        true
                )
        );

        Mockito.when(mockgestionService.getAllHistoricAdherent(existingAdherent)).thenReturn(reservations);

        List<Reservation> historiqueReservation = mockgestionService.getAllHistoricAdherent(existingAdherent);

        assertNotNull(historiqueReservation);
        assertEquals(2, historiqueReservation.size());
        assertTrue(historiqueReservation.stream().allMatch(Reservation::isEndReservation));
        verify(mockgestionService, times(1)).getAllHistoricAdherent(existingAdherent);
    }


    @Test
    public void shouldSendReminderEmail_WhenReservationsAreOverdue() {
        List<Reservation> overdueReservations = List.of(existingReservationExpired);

        when(mockgestionService.isReservationAdherantExpired(existingAdherent)).thenReturn(overdueReservations);

        mockgestionService.sendOverdueReservationReminder(existingAdherent);

        verify(mockMailService, times(1)).sendEmail(
                eq(existingAdherent.getEmail()),
                eq("Rappel de réservation dépassée"),
                anyString() // Accepte n'importe quel contenu
        );
    }


    @Test
    public void shouldNotSendReminderEmail_WhenNoReservationsAreOverdue() {
        List<Reservation> overdueReservations = Arrays.asList();
        Mockito.when(mockgestionService.isReservationAdherantExpired(existingAdherent)).thenReturn(overdueReservations);
        mockgestionService.sendOverdueReservationReminder(existingAdherent);
        verify(mockMailService, never()).sendEmail(anyString(), anyString(), anyString(), anyString());
    }

}
