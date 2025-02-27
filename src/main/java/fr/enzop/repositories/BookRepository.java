package fr.enzop.repositories;

import fr.enzop.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findAllByTitleContainingIgnoreCase(String title);
}