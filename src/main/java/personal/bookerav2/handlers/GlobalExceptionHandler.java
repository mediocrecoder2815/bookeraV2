package personal.bookerav2.handlers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import personal.bookerav2.exceptions.InvalidCredentialsException;
import personal.bookerav2.exceptions.InvalidFileException;
import personal.bookerav2.exceptions.ResourceDuplicateException;
import personal.bookerav2.exceptions.ResourceNotFound;

import java.io.FileNotFoundException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final static int NOT_FOUND = 404;
    private final static int DUPLICATE_FOUND = 409;
    private final static int UNAUTHORIZED = 401;
    private final static int BAD_REQUEST = 400;


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
        ProblemDetail pd = ProblemDetail.forStatus(BAD_REQUEST);
        pd.setDetail(manve.getBindingResult().getFieldErrors().stream()
                    .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                    .collect(Collectors.joining("; ")));
            return pd;
        }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleConflict(DataIntegrityViolationException ex){
        ProblemDetail pd = ProblemDetail.forStatus(DUPLICATE_FOUND);
        pd.setDetail("Request conflicts with existing data");
        return pd;
    }

    @ExceptionHandler({PropertyReferenceException.class, IllegalArgumentException.class})
    public ProblemDetail handleBadRequest(Exception ex){
        ProblemDetail pd = ProblemDetail.forStatus(BAD_REQUEST);
        pd.setDetail(ex.getLocalizedMessage());
        return pd;
    }

    @ExceptionHandler({InvalidFileException.class, FileNotFoundException.class})
    public ProblemDetail handleFNFE(){
        ProblemDetail pd = ProblemDetail.forStatus(NOT_FOUND);
        pd.setDetail("Uploaded picture not found");
        return pd;
    }
}
