package personal.bookerav2.handlers;

import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import personal.bookerav2.exceptions.InvalidCredentialsException;
import personal.bookerav2.exceptions.ResourceDuplicateException;
import personal.bookerav2.exceptions.ResourceNotFound;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final static int NOT_FOUND = 404;
    private final static int DUPLICATE_FOUND = 409;
    private final static int UNAUTHORIZED = 401;


    @ExceptionHandler(InvalidCredentialsException.class)
    public ProblemDetail handleUnauthorized(InvalidCredentialsException ice) {
        ProblemDetail pd = ProblemDetail.forStatus(UNAUTHORIZED);
        pd.setDetail(ice.getLocalizedMessage());
        return pd;
    }


    @ExceptionHandler(ResourceNotFound.class)
    public ProblemDetail handleNotFound(ResourceNotFound rnf) {
        ProblemDetail pd = ProblemDetail.forStatus(NOT_FOUND);
        pd.setDetail(rnf.getLocalizedMessage());
        return pd;
    }

    @ExceptionHandler(ResourceDuplicateException.class)
    public ProblemDetail handleDuplicate(ResourceDuplicateException rde) {
        ProblemDetail pd = ProblemDetail.forStatus(DUPLICATE_FOUND);

        pd.setDetail(rde.getLocalizedMessage());
        return pd;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleInvalidRequest(MethodArgumentNotValidException manve){
        ProblemDetail pd = ProblemDetail.forStatus(400);
        pd.setDetail(manve.getBindingResult().getFieldErrors().stream()
                    .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                    .collect(Collectors.joining("; ")));
            return pd;
        }

    @ExceptionHandler({PropertyReferenceException.class, IllegalArgumentException.class})
    public ProblemDetail handleBadRequest(Exception ex){
        ProblemDetail pd = ProblemDetail.forStatus(400);
        pd.setDetail(ex.getLocalizedMessage());
        return pd;
    }
}
