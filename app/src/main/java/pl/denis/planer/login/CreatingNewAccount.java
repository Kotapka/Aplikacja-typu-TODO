package pl.denis.planer.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import pl.denis.planer.R;
import pl.denis.planer.database.DatabaseHelper;
import pl.denis.planer.login.mailoperations.SendMailTask;
import pl.denis.planer.model.user.SqliteUserDataValidator;

public class CreatingNewAccount extends AppCompatActivity {

    DatabaseHelper databaseHelper = new DatabaseHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_new_account);
        getSupportActionBar().hide();
    }
    //Onclick function to "gobackbutton"
    //Going back to Login screen without saving data
    public void onClickGoToLoginScene(View view) {
        Intent intent = new Intent(this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    //Overwriting back button
    //Going back to Login screen without saving data
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    //Going to Token class
    public void onClickGoToToken(View view) {
        EditText username = findViewById(R.id.usernameNewAccount);
        EditText email = findViewById(R.id.emailNewAccount);
        EditText password = findViewById(R.id.passwordNewAccount);
        EditText passwordRepeat = findViewById(R.id.passwordNewAccountRepeat);
        String stringPassword = password.getText().toString();
        String stringPasswordRepeat = passwordRepeat.getText().toString();
        String stringEmail = email.getText().toString();
        String stringUsername = username.getText().toString();
        stringPassword = stringPassword.replaceAll("\\s","");
        stringPasswordRepeat = stringPasswordRepeat.replaceAll("\\s","");
        stringUsername = stringUsername.replaceAll("\\s","");
        stringEmail = stringEmail.replaceAll("\\s","");
        boolean connection = Con();
        if(SqliteUserDataValidator.checkIfEmailIsUsed(stringEmail)){
            Toast.makeText(CreatingNewAccount.this, "Email is already used",
                    Toast.LENGTH_LONG).show();
        }
        else if(SqliteUserDataValidator.checkIfLoginIsUsed(stringUsername)){
            Toast.makeText(CreatingNewAccount.this, "Login is already used",
                    Toast.LENGTH_LONG).show();
        }
        else if(!SqliteUserDataValidator.checkIfLoginIsValid(stringUsername)){
            Toast.makeText(CreatingNewAccount.this, "forbidden characters in login",
                    Toast.LENGTH_LONG).show();
        }
        else if(!SqliteUserDataValidator.checkIfEmailAddressIsValid(stringEmail)){
            Toast.makeText(CreatingNewAccount.this, "invalid email address",
                    Toast.LENGTH_LONG).show();
        }
        else if(!SqliteUserDataValidator.checkIfPasswordIsValid(stringPassword)){
            Toast.makeText(CreatingNewAccount.this, "forbidden characters in password",
                    Toast.LENGTH_LONG).show();
        }
        else if(!connection) {
            Toast.makeText(CreatingNewAccount.this, "Check Your Internet Connection",
                    Toast.LENGTH_LONG).show();
        }
        else if(stringUsername.isEmpty() || stringUsername.length() > 20){
            Toast.makeText(CreatingNewAccount.this,
                    "Login can not be empty and must be shorter than 20 characters",
                    Toast.LENGTH_LONG).show();
        }
        else if(stringEmail.isEmpty() || email.length() > 50){
            Toast.makeText(CreatingNewAccount.this,
                    "Email can not be empty and must be shorter than 50 characters",
                    Toast.LENGTH_LONG).show();
        }
        else if(stringPassword.isEmpty() || stringPassword.length() > 20){
            Toast.makeText(CreatingNewAccount.this,
                    "Password can not be empty and must be shorter than 20 characters",
                    Toast.LENGTH_LONG).show();
        }
        else if(!stringPassword.equals(stringPasswordRepeat)){
            Toast.makeText(CreatingNewAccount.this, "Passwords must be the same!",
                    Toast.LENGTH_LONG).show();
        }
        else{
            //Generating code
            int code = getRandomNumber();
            System.out.println(code);                                                             //DELETE CODE !!!!!!!!!!!!!!!!!!!!!!!!
            //Sending email with code
            Log.i("SendMailActivity", "Send Button Clicked.");
            String fromEmail = "verifytodo123@gmail.com";
            String fromPassword = "VerifyToDO123321";
            String toEmail = email.getText().toString();
            String emailSubject = "ToDO Your Code";
            String emailBody = "Your code: " + code;
            Toast.makeText(CreatingNewAccount.this, emailBody,
                    Toast.LENGTH_LONG).show();
                new SendMailTask(CreatingNewAccount.this).execute(fromEmail,
                 fromPassword, toEmail, emailSubject, emailBody);
            String stringCode = "" + code;
            stringCode = stringCode.replaceAll("\\s","");
            Intent intent = new Intent(this, Token.class);
            intent.putExtra("KEY_CODE",stringCode);
            saveData(stringCode);
            intent.putExtra("KEY_PASSWORD",stringPassword);
            saveData(stringPassword);
            intent.putExtra("KEY_USERNAME",stringUsername);
            saveData(stringUsername);
            intent.putExtra("KEY_EMAIL",stringEmail);
            saveData(stringEmail);
            startActivity(intent);
        }
    }
    //Shared Preferences
    private void saveData(String str) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("LOGIN",str);
        editor.apply();
    }
    //Checking connection
    public boolean Con(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                        .getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        }
        else {
            connected = false;
        }
        return connected;
    }
    //Generate ranodm number from 1000 to 9999
    public int getRandomNumber() {
        return (int) ((Math.random() * (9999 - 1000)) + 1000);
    }
}