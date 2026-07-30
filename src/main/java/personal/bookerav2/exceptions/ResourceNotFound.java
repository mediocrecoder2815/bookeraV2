package personal.bookerav2.exceptions;

public class ResourceNotFound  extends RuntimeException{
    private final int errorCode = 404;
    public ResourceNotFound(String message){
        super(message);
    }
}
