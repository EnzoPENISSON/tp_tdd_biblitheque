package fr.enzop;

import fr.enzop.controllers.AdherentController;
import fr.enzop.controllers.LibraryController;
import fr.enzop.exceptions.InvalidIsbnException;
import fr.enzop.exceptions.MissingParameterException;
import fr.enzop.models.Adherent;
import fr.enzop.models.Civilite;
import fr.enzop.repositories.AdherentRepository;
import fr.enzop.requests.AdherentRequest;
import fr.enzop.requests.BookRequest;
import fr.enzop.responses.AdherentResponse;
import fr.enzop.services.AdherentService;
import fr.enzop.services.BookService;
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
    private AdherentRepository adherentRepository;

    AdherentService mockDbService;
    AdherentController adherentController;

    private Adherent existingAdherent = new Adherent(
            ADHERENT_ID,
            "Chemin",
            "Luc",
            "luc@gmail.com",
            LocalDateTime.parse("2000-11-01T00:00:00"),
            Civilite.HOMME
    );

    @BeforeEach
    public void init() {
        mockDbService = mock(AdherentService.class);
        adherentController = new AdherentController(mockDbService);
    }

    @Test
    public void shouldAddAdherent() {
        AdherentRequest requestadherent = AdherentRequest.builder()
                .nom("Chemin")
                .prenom("Luc")
                .dateNaissance(LocalDateTime.parse("2000-11-01T00:00:00"))
                .civilite(Civilite.HOMME)
                .build();

        Mockito.when(mockDbService.addAdherent(Mockito.any(AdherentRequest.class)))
                .thenReturn(existingAdherent);

        AdherentResponse response = adherentController.AjoutAdherent(requestadherent);

        assertNotNull(response);
        assertEquals("Chemin", response.getNom());
        assertEquals("Luc", response.getPrenom());
        assertEquals(LocalDateTime.parse("2000-11-01T00:00:00"), response.getDateNaissance());
        verify(mockDbService, times(1)).addAdherent(any(AdherentRequest.class));
    }

    @Test
    public void shouldNotAddAdherentMissingParameters() {
        AdherentRequest requestadherent = AdherentRequest.builder()
                .nom("Chemin")
                .dateNaissance(LocalDateTime.parse("2000-11-01T00:00:00"))
                .civilite(Civilite.HOMME)
                .build();

        Mockito.when(mockDbService.addAdherent(Mockito.any(AdherentRequest.class)))
                .thenThrow(new MissingParameterException());

        assertThrows(MissingParameterException.class, () -> mockDbService.addAdherent(requestadherent));
    }

    @Test
    public void shouldUpdateAdherent() {
        AdherentRequest requestadherent = AdherentRequest.builder()
                .nom("Parchemin")
                .prenom("Luc")
                .dateNaissance(LocalDateTime.parse("2000-11-01T00:00:00"))
                .civilite(Civilite.HOMME)
                .build();

        Mockito.when(mockDbService.updateAdherent(requestadherent,ADHERENT_ID)).thenReturn(existingAdherent);

        AdherentResponse response = adherentController.ModifierAdherent(ADHERENT_ID, requestadherent);

        assertNotNull(response);
        verify(mockDbService, times(1)).updateAdherent(any(AdherentRequest.class), eq(ADHERENT_ID));
    }

    @Test
    public void shouldDeleteAdherent() {
        adherentController.SupprimmerAdherent(ADHERENT_ID);

        verify(mockDbService, times(1)).deleteAdherent(ADHERENT_ID);
    }
}
