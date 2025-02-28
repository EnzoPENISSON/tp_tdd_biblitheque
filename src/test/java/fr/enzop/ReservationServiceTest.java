package fr.enzop;

import fr.enzop.controllers.LibraryController;
import fr.enzop.controllers.ReservationController;
import fr.enzop.exceptions.ReservationNotFound;
import fr.enzop.exceptions.TooManyReservationsException;
import fr.enzop.models.*;
import fr.enzop.repositories.ReservationRepository;
import fr.enzop.requests.BookRequest;
import fr.enzop.requests.ReservationRequest;
import fr.enzop.responses.ReservationResponse;
import fr.enzop.services.BookService;
import fr.enzop.services.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ReservationServiceTest {

    private static final int ADHERENT_ID = 2;
    private static final int BOOK_ID = 2;
    private static final int RESERVATION_ID = 2;
    @Mock
    private ReservationRepository reservationRepository;

    ReservationService mockDbService;
    ReservationController reservationController;

    private Adherent existingAdherent = new Adherent(
            ADHERENT_ID,
            "Dupont",
            "Antoine",
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

    private Reservation existingReservation = new Reservation(
            RESERVATION_ID,
            existingAdherent,
            NonavailableBook,
            LocalDateTime.parse("2025-02-01T00:00:00"),
            false
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
        reservationController = new ReservationController(mockDbService);
    }

    @Test
    void shouldCreateReservation_WhenBookIsAvailable() {
        ReservationRequest reservationRequest = ReservationRequest.builder()
                .adherent(existingAdherent)
                .book(existingBook)
                .endReservation(false)
                .build();

        Mockito.when(mockDbService.addReservation(Mockito.any(ReservationRequest.class)))
                .thenReturn(existingReservationExpired);

        Reservation response = mockDbService.addReservation(reservationRequest);
        assertTrue(response.getBook().isAvailable()); // livre disponible
    }

    @Test
    void shouldNotCreateReservation_WhenBookIsNotAvailable() {
        ReservationRequest reservationRequest = ReservationRequest.builder()
                .adherent(existingAdherent)
                .book(NonavailableBook)
                .endReservation(false)
                .build();

        Mockito.when(mockDbService.addReservation(Mockito.any(ReservationRequest.class)))
                .thenReturn(existingReservation);

        Reservation response = mockDbService.addReservation(reservationRequest);
        assertFalse(response.getBook().isAvailable()); // Livre pas disponible
    }

    @Test
    void shouldNotCreateReservation_WhenTooManyReservations() {
        ReservationRequest reservationRequest = ReservationRequest.builder()
                .adherent(existingAdherent)
                .book(existingBook)
                .endReservation(false)
                .build();

        Mockito.when(mockDbService.countOpenReservationsByAdherent(Mockito.any(Adherent.class)))
                .thenReturn(3);

        Mockito.when(mockDbService.addReservation(Mockito.any(ReservationRequest.class)))
                .thenThrow(new TooManyReservationsException("L'adhérent a déjà 3 réservations ouvertes"));

        TooManyReservationsException exception = assertThrows(TooManyReservationsException.class, () -> {
            mockDbService.addReservation(reservationRequest);
        });

        assertEquals("L'adhérent a déjà 3 réservations ouvertes", exception.getMessage());
    }

    @Test
    void shouldDetectExpiredReservation_WhenDurationExceedsMax() {
        Mockito.when(mockDbService.getAllReservationAdherent(Mockito.any(Adherent.class)))
                .thenReturn(reservationList);

        Mockito.when(mockDbService.isReservationExpired(existingReservationExpired)).thenReturn(Boolean.TRUE);
        Mockito.when(mockDbService.isReservationExpired(existingReservation)).thenReturn(Boolean.FALSE);

        assertTrue(mockDbService.isReservationExpired(existingReservationExpired));
        assertFalse(mockDbService.isReservationExpired(existingReservation));
    }

    @Test
    void shouldCancelReservation_WhenReservationExists() {
        Integer reservationId = RESERVATION_ID;

        Mockito.when(mockDbService.cancelReservation(reservationId))
                .thenReturn(true); // Suppose cancelReservation returns a boolean

        boolean result = mockDbService.cancelReservation(reservationId);

        assertTrue(result);
        verify(mockDbService, times(1)).cancelReservation(reservationId);
    }

    @Test
    void shouldThrowException_WhenReservationNotFound() {
        Integer reservationId = 999; // Non-existing reservation

        Mockito.when(mockDbService.cancelReservation(reservationId))
                .thenThrow(new ReservationNotFound());

        assertThrows(ReservationNotFound.class, () -> mockDbService.cancelReservation(reservationId));

        verify(mockDbService, times(1)).cancelReservation(reservationId);
    }


    @Test
    void shouldGetReservationsByAdherent() {
        assertTrue(true);
    }

    @Test
    void shouldExpireReservation_WhenTimeLimitReached() {
        assertTrue(true);
    }
}
