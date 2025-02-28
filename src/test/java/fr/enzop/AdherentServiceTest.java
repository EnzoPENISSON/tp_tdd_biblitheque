package fr.enzop;

import fr.enzop.controllers.AdherentController;
import fr.enzop.exceptions.MissingParameterException;
import fr.enzop.models.Adherent;
import fr.enzop.models.Civilite;
import fr.enzop.repositories.AdherentRepository;
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
public class AdherentServiceTest {

    private static final int ADHERENT_ID = 2;

    @Mock
    private AdherentRepository adherentRepository; // Mock du repository

    @InjectMocks
    private AdherentController adherentController; // Test du contrôleur sans requête HTTP

    @BeforeEach
    public void init() {
        Adherent existingAdherent = new Adherent(
                ADHERENT_ID,
                "Dupont",
                "Antoine",
                LocalDateTime.parse("2000-11-01T00:00:00"),
                Civilite.HOMME
        );

        // Mock findById() to return an existing adherent
        Mockito.when(adherentRepository.findById(ADHERENT_ID)).thenReturn(Optional.of(existingAdherent));

        // Mock save() to return the saved adherent
        Mockito.when(adherentRepository.save(Mockito.any(Adherent.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    public void shouldAddAdherent() {
        AdherentRequest requestadherent = AdherentRequest.builder()
                .nom("Chemin")
                .prenom("Luc")
                .dateNaissance(LocalDateTime.parse("2000-11-01T00:00:00"))
                .civilite(Civilite.HOMME)
                .build();

        AdherentResponse response = adherentController.AjoutAdherent(requestadherent);

        assertNotNull(response);
        assertEquals("Chemin", response.getNom());
        assertEquals("Luc", response.getPrenom());
        assertEquals(LocalDateTime.parse("2000-11-01T00:00:00"), response.getDateNaissance());
        verify(adherentRepository, times(1)).save(any(Adherent.class));
    }

    @Test
    public void shouldNotAddAdherentMissingParameters() {
        AdherentRequest requestadherent = AdherentRequest.builder()
                .nom("Chemin")
                .dateNaissance(LocalDateTime.parse("2000-11-01T00:00:00"))
                .civilite(Civilite.HOMME)
                .build();

        assertThrows(MissingParameterException.class, () -> adherentController.AjoutAdherent(requestadherent));
    }

    @Test
    public void shouldUpdateAdherent() {
        AdherentRequest requestadherent = AdherentRequest.builder()
                .nom("Parchemin")
                .prenom("Luc")
                .dateNaissance(LocalDateTime.parse("2000-11-01T00:00:00"))
                .civilite(Civilite.HOMME)
                .build();

        AdherentResponse response = adherentController.ModifierAdherent(ADHERENT_ID, requestadherent);

        assertNotNull(response);
        verify(adherentRepository, times(1)).save(any(Adherent.class));
    }

    @Test
    public void shouldDeleteAdherent() {
        adherentController.SupprimmerAdherent(ADHERENT_ID);

        verify(adherentRepository, times(1)).deleteById(ADHERENT_ID);
    }
}
