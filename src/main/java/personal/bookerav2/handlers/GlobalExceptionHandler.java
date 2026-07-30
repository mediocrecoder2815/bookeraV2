package personal.bookerav2.handlers;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import personal.bookerav2.exceptions.ResourceDuplicateException;
import personal.bookerav2.exceptions.ResourceNotFound;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFound.class)
    public ProblemDetail handleNotFound(ResourceNotFound rnf) {
        ProblemDetail pd = ProblemDetail.forStatus(404);
        pd.setDetail(rnf.getLocalizedMessage());
        return pd;
    }

    @ExceptionHandler(ResourceDuplicateException.class)
    public ProblemDetail handleDuplicate(ResourceDuplicateException rde) {
        ProblemDetail pd = ProblemDetail.forStatus(409);
        pd.setDetail(rde.getLocalizedMessage());
        return pd;
    }
}
