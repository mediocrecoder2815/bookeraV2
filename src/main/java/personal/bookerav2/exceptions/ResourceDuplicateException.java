package personal.bookerav2.exceptions;

import lombok.Getter;

@Getter
public class ResourceDuplicateException  extends RuntimeException{
    final int errorCode = 401;
    public ResourceDuplicateException(String message){
        super(message);
    }
}
