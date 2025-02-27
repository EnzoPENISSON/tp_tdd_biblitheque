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
    private int id;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private boolean available;
    private Format format;

    public Book() {}

    public Book(int id, String title, String author, boolean available, String publisher, Format format, String isbn) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.available = available;
        this.publisher = publisher;
        this.format = format;
        this.isbn = isbn;
    }
}
