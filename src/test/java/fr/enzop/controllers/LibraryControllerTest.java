package fr.enzop.controllers;

import fr.enzop.TestUtil;
import fr.enzop.models.Format;
import fr.enzop.repositories.BookRepository;
import fr.enzop.requests.BookRequest;
import org.junit.jupiter.api.BeforeEach;
import fr.enzop.models.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest
@AutoConfigureMockMvc
public class LibraryControllerTest {
    private static final String ENDPOINT = "/api/library";
    private static final String ENDPOINT_ID = ENDPOINT + "/{id}";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAddBook() throws Exception {
        BookRequest requestbook = BookRequest.builder()
                .title("Les misérables")
                .author("Victor Hugo")
                .available(true)
                .publisher("Livre de Poche Jeunesse (13 Aug. 2014)")
                .format(Format.POCHE)
                .isbn("2010008995")
                .build();

        // when
        ResultActions result = this.mockMvc.perform(
                MockMvcRequestBuilders
                        .post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.json(requestbook)));

        // then
        result.andExpect(MockMvcResultMatchers.status().isCreated());
    }
}
