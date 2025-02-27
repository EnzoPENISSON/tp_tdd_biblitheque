package fr.enzop.repositories;

import fr.enzop.models.Adherent;
import fr.enzop.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdherentRepository extends JpaRepository<Adherent, Integer> {
}