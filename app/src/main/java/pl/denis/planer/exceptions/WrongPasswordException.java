package pl.denis.planer.exceptions;

/**
 * thrown if given password was wrong
 */
public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException(String message){
        super(message);
    }
}
