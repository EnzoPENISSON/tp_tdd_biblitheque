package fr.enzop.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@Entity
public class Book {

    @Id
    @UuidGenerator
    private String id;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private boolean available;
    private Format format;
}
