package fr.enzop.responses;


import fr.enzop.models.Format;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookResponse {
    private int id;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private Format format;
    private boolean available;
}
