package fr.enzop.models;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@Entity
public class Adherent {

    @Id
    @UuidGenerator
    private int code;
    private String nom;
    private String prenom;
    private String dateNaissance;
    private Civilite civilite;

    public Adherent() {}

    public Adherent(int code, String nom, String prenom, String dateNaissance, Civilite civilite) {
        this.code = code;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.civilite = civilite;
    }

}
