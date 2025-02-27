package fr.enzop.controllers;

import fr.enzop.exceptions.InvalidIsbnException;
import fr.enzop.models.Book;
import fr.enzop.models.Format;
import fr.enzop.repositories.BookRepository;
import fr.enzop.requests.BookRequest;
import fr.enzop.responses.BookResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class LibraryControllerTest {

    private static final int BOOK_ID = 2;

    private Book existingBook;

    @Mock
    private BookRepository bookRepository; // Mock du repository

    @InjectMocks
    private LibraryController libraryController; // Test du contrôleur sans requête HTTP

    @BeforeEach
    public void init() {
        existingBook = new Book(
                BOOK_ID,
                "Les Misérables",
                "Victor Hugo",
                false,
                "Livre de Poche Jeunesse (13 Aug. 2014)",
                Format.POCHE,
                "2010008995"
        );

        List<Book> booksList = new ArrayList<>();
        booksList.add(existingBook);

        // Mock findById() to return an existing book
        Mockito.when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(existingBook));

        // Mock save() to return the saved book
        Mockito.when(bookRepository.save(Mockito.any(Book.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Mock deleteById() to do nothing
        Mockito.doNothing().when(bookRepository).deleteById(BOOK_ID);

        // Modk findAllByTitleContainingIgnoreCase add the existing Book
        Mockito.when(bookRepository.findAllByTitleContainingIgnoreCase(
                Mockito.anyString()
        )).thenReturn(booksList);
    }

    @Test
    public void shouldAddBookInTheLibrary() {
        BookRequest requestbook = BookRequest.builder()
                .title("Les Misérables")
                .author("Victor Hugo")
                .available(true)
                .publisher("Livre de Poche Jeunesse (13 Aug. 2014)")
                .format(Format.POCHE)
                .isbn("2010008995")
                .build();

        BookResponse response = libraryController.AjoutLivre(requestbook);

        assertNotNull(response);
        assertEquals("Les Misérables", response.getTitle());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    public void shouldNotAddBookInTheLibrary(){
        BookRequest requestbook = BookRequest.builder()
                .title("Les Misérables")
                .author("Victor Hugo")
                .available(true)
                .publisher("Livre de Poche Jeunesse (13 Aug. 2014)")
                .format(Format.POCHE)
                .isbn("2010008996") // Mauvaise Clef
                .build();

        assertThrows(InvalidIsbnException.class, () -> libraryController.AjoutLivre(requestbook));
    }

    @Test
    public void shouldUpdateBookInTheLibrary() {
        BookRequest requestbook = BookRequest.builder()
                .title("Les Misérables")
                .author("Victor Hugo")
                .available(true)
                .publisher("Livre de Poche Jeunesse (13 Aug. 2014)")
                .format(Format.POCHE)
                .isbn("2010008995")
                .build();

        BookResponse response = libraryController.ModifierLivre(BOOK_ID, requestbook);

        assertNotNull(response);
        assertTrue(response.isAvailable());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    public void shouldDeleteBookInTheLibrary() {
        libraryController.SupprimmerLivre(BOOK_ID);

        verify(bookRepository, times(1)).deleteById(BOOK_ID);
    }

    @Test
    public void shouldGetSearchedBooksByTitle() {
        List<BookResponse> result = libraryController.rechercherParLeTitre("Les Misérables");

        assertFalse(result.isEmpty());
        assertEquals("Les Misérables", result.get(0).getTitle());
    }

    @Test
    public void shouldGetSearchedBooksByIsbn() {
        List<BookResponse> result = libraryController.rechercherParLeTitre("2010008995");

        assertFalse(result.isEmpty());
        assertEquals("2010008995", result.get(0).getIsbn());
    }

    @Test
    public void shouldGetSearchedBooksByAuthor() {
        List<BookResponse> result = libraryController.rechercherParLeTitre("Victor Hugo");

        assertFalse(result.isEmpty());
        assertEquals("Victor Hugo", result.get(0).getAuthor());
    }
}
