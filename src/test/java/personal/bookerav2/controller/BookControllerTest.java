package personal.bookerav2.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import personal.bookerav2.handlers.GlobalExceptionHandler;
import personal.bookerav2.service.BookService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import(GlobalExceptionHandler.class)
@DisplayName("BookController query-param validation")
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Test
    void negativePageReturns400() throws Exception {
        mockMvc.perform(get("/api/books").param("page", "-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void oversizedSizeReturns400() throws Exception {
        mockMvc.perform(get("/api/books").param("size", "1000"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void unknownSortByReturns400() throws Exception {
        mockMvc.perform(get("/api/books").param("sortBy", "DROP_TABLE"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void nonBooleanAscendingReturns400() throws Exception {
        mockMvc.perform(get("/api/books").param("ascending", "notabool"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void validQueryReturns200() throws Exception {
        mockMvc.perform(get("/api/books")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "name")
                        .param("ascending", "false"))
                .andExpect(status().isOk());
    }
}
