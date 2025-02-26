package fr.enzop.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Book {

    private @Id @GeneratedValue Long id;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private Format format;
    private boolean available;


    public Book(String isbn, String title, String author, String publisher, Format format, boolean available) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.format = format;
        this.available = available;
    }

    public Book() {

    }
}
