package fr.enzop.controllers;

import fr.enzop.models.Format;
import fr.enzop.requests.BookRequest;
import fr.enzop.repositories.BookRepository;
import fr.enzop.models.Book;
import fr.enzop.TestUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@WebMvcTest(LibraryController.class)
public class LibraryControllerTest {
    private static final String ENDPOINT = "/api/library";
    private static final String ENDPOINT_ID = ENDPOINT + "/{id}";

    private static final int BOOK_ID = 2;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookRepository bookRepository;

    @Test
    public void  shouldAddBookInTheLibraryReturnCreated() throws Exception {
        // Given
        BookRequest requestbook = BookRequest.builder()
                .title("Les misérables")
                .author("Victor Hugo")
                .available(true)
                .publisher("Livre de Poche Jeunesse (13 Aug. 2014)")
                .format(Format.POCHE)
                .isbn("2010008995")
                .build();

        // Simuler l'enregistrement du livre
        Mockito.when(bookRepository.save(Mockito.any(Book.class)))
                .thenAnswer(invocation -> {
                    Book savedBook = invocation.getArgument(0);
                    savedBook.setId(BOOK_ID); // Simule un ID généré
                    return savedBook;
                });

        ResultActions result = this.mockMvc.perform(
                MockMvcRequestBuilders
                        .post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.json(requestbook)));

        result.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void shouldUpdateBookInTheLibraryReturnOkAndAvailable() throws Exception {
        Book existingBook = new Book(
                BOOK_ID,
                "Les misérables",
                "Victor Hugo",
                false,
                "Livre de Poche Jeunesse (13 Aug. 2014)",
                Format.POCHE,
                "2010008995"
        );

        BookRequest requestbook = BookRequest.builder()
                .title("Les misérables")
                .author("Victor Hugo")
                .available(true)
                .publisher("Livre de Poche Jeunesse (13 Aug. 2014)")
                .format(Format.POCHE)
                .isbn("2010008995")
                .build();

        // Mock findById() to return an existing book
        Mockito.when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(existingBook));

        // Mock save() to return the updated book
        Mockito.when(bookRepository.save(Mockito.any(Book.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        ResultActions result = this.mockMvc.perform(
                MockMvcRequestBuilders.put(ENDPOINT_ID, BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.json(requestbook))
        );

        result.andExpect(MockMvcResultMatchers.status().isOk());
        // Book is now available
        result.andExpect(MockMvcResultMatchers.jsonPath("$.available").value(true));

    }
}
