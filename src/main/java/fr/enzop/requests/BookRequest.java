package fr.enzop.requests;


import fr.enzop.models.Format;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookRequest {
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private Format format;
    private boolean available;

    public BookRequest() {

    }
}
