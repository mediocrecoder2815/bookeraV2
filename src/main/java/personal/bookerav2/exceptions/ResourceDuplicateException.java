package personal.bookerav2.exceptions;

import lombok.Getter;

@Getter
public class ResourceDuplicateException  extends RuntimeException{
    public ResourceDuplicateException(String message){
        super(message);
    }
}
