package personal.bookerav2.handlers;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import personal.bookerav2.exceptions.ResourceDuplicateException;
import personal.bookerav2.exceptions.ResourceNotFound;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final static int NOT_FOUND = 404;
    private final static int DUPLICATE_FOUND = 409;


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
}
