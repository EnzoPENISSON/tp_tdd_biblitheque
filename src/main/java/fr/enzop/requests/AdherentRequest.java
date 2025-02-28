package fr.enzop.requests;


import fr.enzop.models.Civilite;
import fr.enzop.models.Format;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
public class AdherentRequest {
    private String nom;
    private String prenom;
    private String email;
    private LocalDateTime dateNaissance;
    private Civilite civilite;

    public boolean paramsSet() {
        return nom != null && prenom != null && email != null && dateNaissance != null && civilite != null;
    }
}
