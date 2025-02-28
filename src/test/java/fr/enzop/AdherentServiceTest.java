package fr.enzop;

import fr.enzop.exceptions.MissingParameterException;
import fr.enzop.models.Adherent;
import fr.enzop.models.Civilite;
import fr.enzop.repositories.AdherentRepository;
import fr.enzop.requests.AdherentRequest;
import fr.enzop.services.AdherentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdherentServiceTest {

    private static final int ADHERENT_ID = 2;

    @Mock
    private AdherentRepository adherentRepository;

    @InjectMocks
    private AdherentService adherentService; // Real service implementation

    private Adherent existingAdherent;

    @BeforeEach
    public void init() {
        existingAdherent = new Adherent(
                ADHERENT_ID,
                "Chemin",
                "Luc",
                "luc@gmail.com",
                LocalDateTime.parse("2000-11-01T00:00:00"),
                Civilite.HOMME
        );
    }

    @Test
    public void shouldAddAdherent() {
        AdherentRequest requestAdherent = AdherentRequest.builder()
                .nom("Chemin")
                .prenom("Luc")
                .email("luc@gmail.com")
                .dateNaissance(LocalDateTime.parse("2000-11-01T00:00:00"))
                .civilite(Civilite.HOMME)
                .build();

        when(adherentRepository.save(any(Adherent.class))).thenReturn(existingAdherent);

        Adherent response = adherentService.addAdherent(requestAdherent);

        assertNotNull(response);
        assertEquals("Chemin", response.getNom());
        assertEquals("Luc", response.getPrenom());
        verify(adherentRepository, times(1)).save(any(Adherent.class));
    }

    @Test
    public void shouldNotAddAdherentMissingParameters() {
        AdherentRequest requestAdherent = AdherentRequest.builder()
                .nom("Chemin")
                .dateNaissance(LocalDateTime.parse("2000-11-01T00:00:00"))
                .civilite(Civilite.HOMME)
                .build(); // Missing "prenom" and "email"

        assertThrows(MissingParameterException.class, () -> adherentService.addAdherent(requestAdherent));
    }

    @Test
    public void shouldUpdateAdherent() {
        AdherentRequest requestAdherent = AdherentRequest.builder()
                .nom("Parchemin")
                .prenom("Luc")
                .email("luc@gmail.com")
                .dateNaissance(LocalDateTime.parse("2000-11-01T00:00:00"))
                .civilite(Civilite.HOMME)
                .build();

        when(adherentRepository.findById(ADHERENT_ID)).thenReturn(Optional.of(existingAdherent));
        when(adherentRepository.save(any(Adherent.class))).thenReturn(existingAdherent);

        Adherent response = adherentService.updateAdherent(requestAdherent, ADHERENT_ID);

        assertNotNull(response);
        verify(adherentRepository, times(1)).save(any(Adherent.class));
    }

    @Test
    public void shouldDeleteAdherent() {
        when(adherentRepository.findById(ADHERENT_ID)).thenReturn(Optional.of(existingAdherent));
        doNothing().when(adherentRepository).delete(existingAdherent);

        adherentService.deleteAdherent(ADHERENT_ID);

        verify(adherentRepository, times(1)).delete(existingAdherent);
    }

    @Test
    public void shouldNotDeleteNonExistingAdherent() {
        when(adherentRepository.findById(ADHERENT_ID)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> adherentService.deleteAdherent(ADHERENT_ID));
    }
}
