package fr.enzop.models;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Adherent {

    @Id
    @UuidGenerator
    private int code;
    private String nom;
    private String prenom;
    private String email;
    private LocalDateTime dateNaissance;
    private Civilite civilite;

    @OneToMany
    private List<Reservation> reservations;

    public Adherent() {}

    public Adherent(int code, String nom, String prenom,String email, LocalDateTime dateNaissance, Civilite civilite) {
        this.code = code;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.dateNaissance = dateNaissance;
        this.civilite = civilite;
        this.reservations = new ArrayList<>();
    }

}
