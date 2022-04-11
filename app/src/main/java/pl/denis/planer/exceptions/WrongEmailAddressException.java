package pl.denis.planer.exceptions;

public class WrongEmailAddressException extends RuntimeException {
    public WrongEmailAddressException(String message){
        super(message);
    }
}
