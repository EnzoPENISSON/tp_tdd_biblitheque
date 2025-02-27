package fr.enzop.controllers;

import fr.enzop.exceptions.MissingParameterException;
import fr.enzop.models.Adherent;
import fr.enzop.models.Civilite;
import fr.enzop.repositories.AdherentRepository;
import fr.enzop.repositories.ReservationRepository;
import fr.enzop.requests.AdherentRequest;
import fr.enzop.responses.AdherentResponse;
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

    @Mock
    private ReservationRepository reservationRepository; // Mock du repository

    @InjectMocks
    private ReservationController reservationController; // Test du contrôleur sans requête HTTP

    @BeforeEach
    public void init() {
    }
}
