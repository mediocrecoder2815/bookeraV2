package personal.bookerav2.exceptions;


import lombok.Getter;

@Getter
public class CustomError {
    private final int errorCode;
    private final String msg;

    public CustomError(int code, String msg){
        this.errorCode = code;
        this.msg = msg;
    }
}
