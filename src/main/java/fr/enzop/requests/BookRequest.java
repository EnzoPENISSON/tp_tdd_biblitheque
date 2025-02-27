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
    @NotBlank @NotNull
    private String isbn;
    @NotBlank @NotNull
    private String title;
    @NotBlank @NotNull
    private String author;
    @NotBlank @NotNull
    private String publisher;
    @NotBlank @NotNull
    private Format format;
    @NotNull
    private boolean available;
}
