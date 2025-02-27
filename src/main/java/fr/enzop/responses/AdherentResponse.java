package fr.enzop.responses;


import fr.enzop.models.Civilite;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
public class AdherentResponse {
    private String nom;
    private String prenom;
    private LocalDateTime dateNaissance;
    private Civilite civilite;
}
