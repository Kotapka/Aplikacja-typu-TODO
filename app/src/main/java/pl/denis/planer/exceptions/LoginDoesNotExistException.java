package pl.denis.planer.exceptions;

public class LoginDoesNotExistException extends RuntimeException {
    public LoginDoesNotExistException(String message){
        super(message);
    }
}
