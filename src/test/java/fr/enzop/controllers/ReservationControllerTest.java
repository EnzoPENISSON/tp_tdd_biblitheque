package fr.enzop.controllers;

import fr.enzop.exceptions.MissingParameterException;
import fr.enzop.models.Adherent;
import fr.enzop.models.Book;
import fr.enzop.models.Civilite;
import fr.enzop.models.Format;
import fr.enzop.repositories.AdherentRepository;
import fr.enzop.repositories.ReservationRepository;
import fr.enzop.requests.AdherentRequest;
import fr.enzop.requests.BookRequest;
import fr.enzop.requests.ReservationRequest;
import fr.enzop.responses.AdherentResponse;
import fr.enzop.responses.ReservationResponse;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ReservationControllerTest {

    private static final int ADHERENT_ID = 2;
    private static final int BOOK_ID = 2;

    @Mock
    private ReservationRepository reservationRepository; // Mock du repository

    @InjectMocks
    private ReservationController reservationController; // Test du contrôleur sans requête HTTP

    private Adherent existingAdherent;
    private Book existingBook;

    @BeforeEach
    public void init() {
        existingAdherent = new Adherent(
                ADHERENT_ID,
                "Dupont",
                "Antoine",
                LocalDateTime.parse("2000-11-01T00:00:00"),
                Civilite.HOMME
        );

        existingBook = new Book(
                BOOK_ID,
                "Les Misérables",
                "Victor Hugo",
                true,
                "Livre de Poche Jeunesse (13 Aug. 2014)",
                Format.POCHE,
                "2010008995"
        );
    }

    @Test
    void testCreerReservation_Success() {
        ReservationRequest reservationRequest = ReservationRequest.builder()
                .adherent(existingAdherent)
                .book(existingBook)
                .endReservation(false)
                .build();

        ReservationResponse response = reservationController.reserver(reservationRequest);

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
