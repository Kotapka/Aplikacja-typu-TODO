package pl.denis.planer.exceptions;

public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException(String message){
        super(message);
    }
}
