package fr.enzop.controllers;

import fr.enzop.models.Format;
import fr.enzop.requests.BookRequest;
import fr.enzop.repositories.BookRepository;
import fr.enzop.models.Book;
import fr.enzop.TestUtil;
import org.junit.jupiter.api.BeforeEach;
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
    private static final String ENDPOINT_SEARCH = ENDPOINT + "/search";

    private static final int BOOK_ID = 2;

    private Book existingBook;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookRepository bookRepository;

    @BeforeEach
    public void init() {
        existingBook = new Book(
                BOOK_ID,
                "Les misérables",
                "Victor Hugo",
                false,
                "Livre de Poche Jeunesse (13 Aug. 2014)",
                Format.POCHE,
                "2010008995"
        );

        // Mock findById() to return an existing book
        Mockito.when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(existingBook));

        // Mock save() to return the saved book
        Mockito.when(bookRepository.save(Mockito.any(Book.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Mock deleteById() to do nothing
        Mockito.doNothing().when(bookRepository).deleteById(BOOK_ID);
    }

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

        ResultActions result = this.mockMvc.perform(
                MockMvcRequestBuilders
                        .post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.json(requestbook)));

        result.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void shouldUpdateBookInTheLibraryReturnOkAndAvailable() throws Exception {
        BookRequest requestbook = BookRequest.builder()
                .title("Les misérables")
                .author("Victor Hugo")
                .available(true)
                .publisher("Livre de Poche Jeunesse (13 Aug. 2014)")
                .format(Format.POCHE)
                .isbn("2010008995")
                .build();

        ResultActions result = this.mockMvc.perform(
                MockMvcRequestBuilders.put(ENDPOINT_ID, BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.json(requestbook))
        );

        result.andExpect(MockMvcResultMatchers.status().isOk());
        // Book is now available
        result.andExpect(MockMvcResultMatchers.jsonPath("$.available").value(true));

    }

    @Test
    public void shouldDeleteBookInTheLibraryReturnOk() throws Exception {
        ResultActions result = this.mockMvc.perform(
                MockMvcRequestBuilders.delete(ENDPOINT_ID, BOOK_ID)
        );

        result.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test void shouldGetSearchedBooksByTitleReturnOk() throws Exception {
        String bookToSearch = existingBook.getTitle();

        ResultActions result = this.mockMvc.perform(
                MockMvcRequestBuilders.get(ENDPOINT_SEARCH, bookToSearch)
        );

        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(bookToSearch));
    }
}
