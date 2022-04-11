package pl.denis.planer.model.user;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import pl.denis.planer.database.DatabaseHelper;
import pl.denis.planer.database.UserTableInfo;
import pl.denis.planer.exceptions.EmailAlreadyUsedException;
import pl.denis.planer.exceptions.LoginAlreadyUsedException;
import pl.denis.planer.exceptions.LoginDoesNotExistException;
import pl.denis.planer.exceptions.WrongEmailAddressException;
import pl.denis.planer.exceptions.WrongLoginException;

public class SqliteUserDataValidator extends UserDataValidator{
    static private final DatabaseHelper databaseHelper = new DatabaseHelper();
    static private final SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
    private SqliteUserDataValidator(){
        super();
    };

    public static void checkUser(User user)
            throws EmailAlreadyUsedException, LoginAlreadyUsedException {
        checkIfLoginIsUsed(user.getLogin());
        checkIfEmailIsUsed(user.getEmailAddress());
    }

    public static boolean checkIfLoginIsUsed(String login)  {
        if(checkIfLoginIsValid(login)) {
            Cursor res = sqLiteDatabase.rawQuery("SELECT *  FROM " + UserTableInfo.TABLE_NAME +
                            " WHERE " + UserTableInfo.LOGIN + " = '" + login + "'; "
                    , null);
            if (res.getCount() == 0) {
                res.close();
                return false;
            }
            res.close();
            return true;
        }
        throw new WrongLoginException("Forbidden characters in login");
    }

    public static boolean checkIfEmailIsUsed(String emailAddress) {
        if(checkIfEmailAddressIsValid(emailAddress)){
            Cursor res = sqLiteDatabase.rawQuery("SELECT *  FROM " + UserTableInfo.TABLE_NAME +
                            " WHERE " + UserTableInfo.EMAIL_ADDRESS + " = '" +
                            emailAddress + "' ; ",
                    null);
            if (res.getCount() == 0) {
                res.close();
                return false;
            }
            res.close();
            return true;
        }
        throw new WrongEmailAddressException("forbidden characters in email");
    }

    public static boolean checkIfPasswordIsValid(String login,String password){
        if(checkIfLoginIsUsed(login)) {
            Cursor res = sqLiteDatabase.rawQuery("SELECT *  FROM " + UserTableInfo.TABLE_NAME +
                            " WHERE " + UserTableInfo.LOGIN + " = '" + login + "' " +
                            "AND " + UserTableInfo.PASSWORD + " = '" + password + "'; "
                    , null);
            if (res.getCount() == 0) {
                res.close();
                return false;
            }
            res.close();
            return true;
        }
        throw new LoginDoesNotExistException("account with this login doesn't exist");
    }

}
