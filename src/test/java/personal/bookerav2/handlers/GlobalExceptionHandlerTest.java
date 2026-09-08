package personal.bookerav2.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import personal.bookerav2.exceptions.InvalidCredentialsException;
import personal.bookerav2.exceptions.ResourceDuplicateException;
import personal.bookerav2.exceptions.ResourceNotFound;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("GlobalExceptionHandler unit tests")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("should return 404 for ResourceNotFound")
    void shouldHandleResourceNotFound() {
        ResourceNotFound ex = new ResourceNotFound("Book not found");

        ProblemDetail pd = handler.handleNotFound(ex);

        assertEquals(404, pd.getStatus());
        assertEquals("Book not found", pd.getDetail());
    }

    @Test
    @DisplayName("should return 409 for ResourceDuplicateException")
    void shouldHandleResourceDuplicate() {
        ResourceDuplicateException ex = new ResourceDuplicateException("Username taken");

        ProblemDetail pd = handler.handleDuplicate(ex);

        assertEquals(409, pd.getStatus());
        assertEquals("Username taken", pd.getDetail());
    }

    @Test
    @DisplayName("should return 401 for InvalidCredentialsException")
    void shouldHandleInvalidCredentials() {
        InvalidCredentialsException ex = new InvalidCredentialsException("Wrong password");

        ProblemDetail pd = handler.handleUnauthorized(ex);

        assertEquals(401, pd.getStatus());
        assertEquals("Wrong password", pd.getDetail());
    }

    @Test
    @DisplayName("should return 400 for MethodArgumentNotValidException")
    void shouldHandleMethodArgumentNotValid() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = new FieldError("request", "username", "must not be blank");
        FieldError fieldError2 = new FieldError("request", "password", "must not be blank");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ProblemDetail pd = handler.handleInvalidRequest(ex);

        assertEquals(400, pd.getStatus());
        assertTrue(pd.getDetail().contains("username: must not be blank"));
        assertTrue(pd.getDetail().contains("password: must not be blank"));
    }

    @Test
    @DisplayName("should return 400 for IllegalArgumentException")
    void shouldHandleIllegalArgument() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid sort field");

        ProblemDetail pd = handler.handleBadRequest(ex);

        assertEquals(400, pd.getStatus());
        assertEquals("Invalid sort field", pd.getDetail());
    }

    @Test
    @DisplayName("should return 400 for single field error")
    void shouldHandleSingleFieldError() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("request", "name", "must not be blank");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ProblemDetail pd = handler.handleInvalidRequest(ex);

        assertEquals(400, pd.getStatus());
        assertEquals("name: must not be blank", pd.getDetail());
    }
}
