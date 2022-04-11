package pl.denis.planer.exceptions;

public class LoginAlreadyUsedException extends RuntimeException {
    public LoginAlreadyUsedException(String message) {
        super(message);
    }
}
