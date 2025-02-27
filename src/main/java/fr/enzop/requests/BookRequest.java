package fr.enzop.requests;


import fr.enzop.models.Format;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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

    public boolean paramsSet() {
        return title != null && !title.isBlank() &&
                author != null && !author.isBlank() &&
                isbn != null && !isbn.isBlank() &&
                publisher != null && !publisher.isBlank() &&
                format != null;
    }
}
