package fr.enzop.controllers;

import fr.enzop.models.Format;
import fr.enzop.requests.BookRequest;
import fr.enzop.controllers.LibraryController;
import fr.enzop.repositories.BookRepository;
import fr.enzop.models.Book;
import fr.enzop.TestUtil;
import fr.enzop.requests.BookRequestUpdate;
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
import java.util.UUID;

@WebMvcTest(LibraryController.class)
public class LibraryControllerTest {
    private static final String ENDPOINT = "/api/library";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookRepository bookRepository;

    @Test
    public void testAddBook() throws Exception {
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
                    savedBook.setId(1); // Simule un ID généré
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
    public void testEditBook() throws Exception {
        // Given
        int bookId = 2;

        // Simulated existing book in the repository
        Book existingBook = new Book(
                bookId,
                "Les misérables",
                "Victor Hugo",
                false,
                "Livre de Poche Jeunesse (13 Aug. 2014)",
                Format.POCHE,
                "2010008995"
        );

        // Updated book request
        BookRequestUpdate requestbook = BookRequestUpdate.builder()
                .id(bookId)
                .title("Les misérables")
                .author("Victor Hugo")
                .available(true)
                .publisher("Livre de Poche Jeunesse (13 Aug. 2014)")
                .format(Format.POCHE)
                .isbn("2010008995")
                .build();

        // Mock findById() to return an existing book
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));

        Mockito.when(bookRepository.save(Mockito.any(Book.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ResultActions result = this.mockMvc.perform(
                MockMvcRequestBuilders.put(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.json(requestbook))
        );

        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.title").value(requestbook.getTitle()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.author").value(requestbook.getAuthor()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.available").value(requestbook.isAvailable()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.publisher").value(requestbook.getPublisher()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.format").value(requestbook.getFormat().toString()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(requestbook.getIsbn()));
    }
}
