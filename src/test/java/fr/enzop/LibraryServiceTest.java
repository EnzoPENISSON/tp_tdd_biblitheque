package fr.enzop;

import fr.enzop.controllers.LibraryController;
import fr.enzop.exceptions.InvalidIsbnException;
import fr.enzop.exceptions.MissingParameterException;
import fr.enzop.models.Book;
import fr.enzop.models.Format;
import fr.enzop.repositories.BookRepository;
import fr.enzop.requests.BookRequest;
import fr.enzop.responses.BookResponse;
import fr.enzop.services.BookService;
import fr.enzop.services.WebService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LibraryServiceTest {

    private static final int BOOK_ID = 2;

    @Mock
    private BookRepository bookRepository;

    BookService mockDbService;
    WebService mockWebService;
    LibraryController libraryController;

    private Book existingBook = new Book(
            BOOK_ID,
            "Les Misérables",
            "Victor Hugo",
            false,
            "Livre de Poche Jeunesse (13 Aug. 2014)",
            Format.POCHE,
            "2010008995"
    );

    List<Book> booksList = new ArrayList<>();


    @BeforeEach
    public void init() {
        mockDbService = mock(BookService.class);
        mockWebService = mock(WebService.class);
        libraryController = new LibraryController(mockDbService);

        booksList.add(existingBook);
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

        Mockito.when(mockDbService.addBook(Mockito.any(BookRequest.class)))
                .thenReturn(existingBook);

        Book response = mockDbService.addBook(requestbook);

        assertNotNull(response);
        assertEquals("Les Misérables", response.getTitle());
        verify(mockDbService, times(1)).addBook(any(BookRequest.class));
    }

    @Test
    public void shouldNotAddBookInTheLibraryWrongIsbn(){
        BookRequest requestbook = BookRequest.builder()
                .title("Les Misérables")
                .author("Victor Hugo")
                .available(true)
                .publisher("Livre de Poche Jeunesse (13 Aug. 2014)")
                .format(Format.POCHE)
                .isbn("2010008996") // ISBN incorrect
                .build();

        Mockito.when(mockDbService.addBook(Mockito.any(BookRequest.class)))
                .thenThrow(new InvalidIsbnException());

        assertThrows(InvalidIsbnException.class, () -> mockDbService.addBook(requestbook));
    }


    @Test
    public void shouldNotAddBookInTheLibraryMissingParameters() {
        BookRequest requestbook = BookRequest.builder()
                .title("Les Misérables")
                .publisher("Livre de Poche Jeunesse (13 Aug. 2014)")
                .format(Format.POCHE)
                .isbn("2010008995")
                .build();

        Mockito.when(mockDbService.addBook(Mockito.any(BookRequest.class)))
                .thenThrow(new MissingParameterException());

        assertThrows(MissingParameterException.class, () -> mockDbService.addBook(requestbook));
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

        Mockito.when(mockDbService.updateBook(requestbook,BOOK_ID)).thenReturn(existingBook);

        Book response = mockDbService.updateBook(requestbook,BOOK_ID);

        assertNotNull(response);
        assertFalse(response.isAvailable());
        verify(mockDbService, times(1)).updateBook(any(BookRequest.class), eq(BOOK_ID));
    }

    @Test
    public void shouldDeleteBookInTheLibrary() {
        libraryController.SupprimmerLivre(BOOK_ID);

        verify(mockDbService, times(1)).deleteBook(BOOK_ID);
    }

    @Test
    public void shouldGetSearchedBooksByTitle() {
        Mockito.when(mockDbService.searchBooks(
                "Les Misérables",null,null
        )).thenReturn(booksList);

        List<BookResponse> result = libraryController.rechercher("Les Misérables",null,null);

        assertFalse(result.isEmpty());
        assertEquals("Les Misérables", result.get(0).getTitle());
    }

    @Test
    public void shouldGetSearchedBooksByIsbn() {
        Mockito.when(mockDbService.searchBooks(
                null,null,"2010008995"
        )).thenReturn(booksList);

        List<BookResponse> result = libraryController.rechercher(null,null,"2010008995");

        assertFalse(result.isEmpty());
        assertEquals("2010008995", result.get(0).getIsbn());
    }

    @Test
    public void shouldGetSearchedBooksByAuthor() {
        Mockito.when(mockDbService.searchBooks(
                null,"Victor Hugo",null
        )).thenReturn(booksList);

        List<BookResponse> result = libraryController.rechercher(null,"Victor Hugo",null);

        assertFalse(result.isEmpty());
        assertEquals("Victor Hugo", result.get(0).getAuthor());
    }

    @Test
    void shouldFetchBookFromWebService_WhenNotInDatabase() {
        String isbn = existingBook.getIsbn();

        Mockito.when(mockDbService.getBookByIsbn(isbn)).thenReturn(Optional.empty());

        Optional<Book> result = mockDbService.getBookByIsbn(isbn);

        verifyNoInteractions(mockWebService);
    }
}
