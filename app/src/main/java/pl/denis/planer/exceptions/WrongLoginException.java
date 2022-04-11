package pl.denis.planer.exceptions;

/**
 * thrown if user's login was not in database
 */
public class WrongLoginException extends RuntimeException {
    public WrongLoginException(String message){
        super(message);
    }
}
