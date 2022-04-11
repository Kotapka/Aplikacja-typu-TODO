package pl.denis.planer.model.user;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import pl.denis.planer.database.DatabaseHelper;
import pl.denis.planer.database.UserTableInfo;
import pl.denis.planer.exceptions.LoginAlreadyUsedException;
import pl.denis.planer.exceptions.UserDoesNotExistException;
import pl.denis.planer.exceptions.WrongEmailAddressException;
import pl.denis.planer.exceptions.WrongLoginException;
import pl.denis.planer.exceptions.WrongPasswordException;
import pl.denis.planer.exceptions.EmailAlreadyUsedException;

public class SQLiteUser extends User{

    private int id ;
    protected static final DatabaseHelper databaseHelper = new DatabaseHelper();
    protected static final SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

    public SQLiteUser(String emailAddress,String login, String password)
            throws WrongEmailAddressException, WrongLoginException, WrongPasswordException,
            EmailAlreadyUsedException, LoginAlreadyUsedException {
        setEmailAddress(emailAddress);
        setLogin(login);
        setPassword(password);
        addUserToDatabase(this);
        Cursor res = sqLiteDatabase.rawQuery("SELECT " + UserTableInfo.ID + "  FROM "
                        + UserTableInfo.TABLE_NAME +
                        " WHERE " + UserTableInfo.LOGIN + " = '" +  login + "'; "
                , null);
        res.moveToFirst();
        setId(res.getInt(res.getColumnIndexOrThrow(UserTableInfo.ID)));
        res.close();
    }

    public SQLiteUser(String login, String password) throws WrongLoginException {
        if(!SqliteUserDataValidator.checkIfLoginIsValid(login)){
            throw new WrongLoginException("forbidden characters in login");
        }
        if(!SqliteUserDataValidator.checkIfPasswordIsValid(password)){
            throw new WrongPasswordException("forbidden characters in password");
        }
        if(SqliteUserDataValidator.checkIfLoginIsUsed(login)){
            if(SqliteUserDataValidator.checkIfPasswordIsValid(login,password)){
                Cursor res = sqLiteDatabase.rawQuery("SELECT *  FROM "
                                + UserTableInfo.TABLE_NAME +
                                " WHERE " + UserTableInfo.LOGIN + " = '" +  login + "'; "
                        , null);
                res.moveToFirst();
                super.setId(res.getInt(res.getColumnIndexOrThrow(UserTableInfo.ID)));
                super.setLogin(login);
                super.setPassword(password);
                super.setEmailAddress(res.getString(res.getColumnIndexOrThrow(UserTableInfo.EMAIL_ADDRESS)));
                res.close();
            }
            else{
                throw new WrongPasswordException("Wrong password");
            }

        }
        else {
            throw new UserDoesNotExistException("there is no user with login: " + login);
        }
    }

    protected void setId(int id) {
        this.id = id;
    }
    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setEmailAddress(String eMailAddress) throws WrongEmailAddressException, EmailAlreadyUsedException {
        if(SqliteUserDataValidator.checkIfEmailIsUsed(eMailAddress)){
            throw new EmailAlreadyUsedException("Account with this email address already exist");
        }
        super.setEmailAddress(eMailAddress);
        updateUser();
    }

    @Override
    public void setPassword(String password) throws WrongPasswordException {
        super.setPassword(password);
        updateUser();
    }

    @Override
    public void setLogin(String login) throws LoginAlreadyUsedException, WrongLoginException {
        if(SqliteUserDataValidator.checkIfLoginIsUsed(login)){
            throw new LoginAlreadyUsedException("Account with this login already exist");
        }
        super.setLogin(login);
        updateUser();
    }
    public static void resetPassword(String email,String password){
        if(UserDataValidator.checkIfEmailAddressIsValid(email)){
            if(UserDataValidator.checkIfPasswordIsValid(password)) {
                if(SqliteUserDataValidator.checkIfEmailIsUsed(email)){
                    SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
                    sqLiteDatabase.execSQL("UPDATE " + UserTableInfo.TABLE_NAME +
                            " SET " + UserTableInfo.PASSWORD + " = '" + password + "' " +
                            " WHERE " +
                            UserTableInfo.EMAIL_ADDRESS + " = '" + email + "';"
                    );
                }
                else {
                    throw new WrongEmailAddressException("there is no account with this email");
                }
            }
            else{
                throw new WrongPasswordException("Forbidden characters in password");
            }
        }
        else{
            throw new WrongEmailAddressException("Forbidden characters in email");

        }
    }

    private void updateUser(){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("UPDATE " + UserTableInfo.TABLE_NAME +
                " SET " + UserTableInfo.EMAIL_ADDRESS + " = '" + getEmailAddress() + "', " +
                    UserTableInfo.LOGIN + " = '" + getLogin() + "', " +
                    UserTableInfo.PASSWORD + " = '" + getPassword() + "' " +
                " WHERE " +
                    UserTableInfo.ID + " = '" + getId() + "';"
        );
    }

    private void addUserToDatabase(User user)
            throws EmailAlreadyUsedException, LoginAlreadyUsedException {
        ContentValues contentValues = new ContentValues();
        SqliteUserDataValidator.checkUser(this);
        contentValues.put(UserTableInfo.EMAIL_ADDRESS, user.getEmailAddress());
        contentValues.put(UserTableInfo.LOGIN, user.getLogin());
        contentValues.put(UserTableInfo.PASSWORD, user.getPassword());
        try{
            sqLiteDatabase.insert(UserTableInfo.TABLE_NAME, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
