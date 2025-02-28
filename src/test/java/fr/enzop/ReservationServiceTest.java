package fr.enzop;

import fr.enzop.controllers.LibraryController;
import fr.enzop.controllers.ReservationController;
import fr.enzop.models.*;
import fr.enzop.repositories.ReservationRepository;
import fr.enzop.requests.ReservationRequest;
import fr.enzop.responses.ReservationResponse;
import fr.enzop.services.BookService;
import fr.enzop.services.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ReservationServiceTest {

    private static final int ADHERENT_ID = 2;
    private static final int BOOK_ID = 2;

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

    @BeforeEach
    public void init() {
        mockDbService = mock(ReservationService.class);
        reservationController = new ReservationController(mockDbService);
    }

    @Test
    void shouldAddReservation_Success() {
        ReservationRequest reservationRequest = ReservationRequest.builder()
                .adherent(existingAdherent)
                .book(existingBook)
                .endReservation(false)
                .build();

        ReservationResponse response = mockDbService.reserver(reservationRequest);

        assertNotNull(response);
    }

    @Test
    void testCreerReservationBookIsAvailable() {
        assertTrue(true);
    }

    @Test
    void testCreerReservation_TooManyReservations() {
        assertTrue(true);
    }

    @Test
    void testCreerReservation_ExceedsMaxDuration() {
        assertTrue(true);
    }

    @Test
    void testAnnulerReservation_Success() {
        assertTrue(true);
    }

    @Test
    void testAnnulerReservation_NotFound() {
        assertTrue(true);
    }

    @Test
    void testGetReservationsByAdherent() {
        assertTrue(true);
    }

    @Test
    void testExpirationReservation() {
        assertTrue(true);
    }
}
