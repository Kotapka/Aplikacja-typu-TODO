package pl.denis.planer.model.user;

public class UserDataValidator {
    private static final String VALID_LETTERS = "[a-zA-z0-9]";
    protected UserDataValidator(){};
    public static boolean checkIfEmailAddressIsValid(String emailAddress){
        return (emailAddress.matches(VALID_LETTERS+"+@"+VALID_LETTERS+"+(\\."+VALID_LETTERS+"+)+"));
    }
    public static boolean checkIfLoginIsValid(String login){
        return (login.matches(VALID_LETTERS+"+"));
    }
    public static boolean checkIfPasswordIsValid(String password){
        return (password.matches(VALID_LETTERS+"+"));
    }
}
