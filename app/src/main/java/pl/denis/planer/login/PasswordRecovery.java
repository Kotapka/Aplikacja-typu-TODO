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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pl.denis.planer.R;
import pl.denis.planer.database.DatabaseHelper;
import pl.denis.planer.login.mailoperations.SendMailTask;
import pl.denis.planer.model.user.SqliteUserDataValidator;

public class PasswordRecovery extends AppCompatActivity {
    int numberOfTries = 0;
    String stringRecoveryCode;
    String toEmail;
    DatabaseHelper databaseHelper = new DatabaseHelper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);
        getSupportActionBar().hide();
    }
    public void onClickGoToLoginScene(View view) {
        Intent intent = new Intent(this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
                                                                                                    // SPRAWDZIC CZY EMAIL ISTNIEJE
    public void onClickSendRecoveryCode(View view) {
        EditText email = findViewById(R.id.mailRecovery);
        String stringEmail = email.getText().toString();
        boolean connection = Con();
        if (!connection) {
            Toast.makeText(PasswordRecovery.this, "Check Your Internet Connection",
                    Toast.LENGTH_LONG).show();
        } else if(!SqliteUserDataValidator.checkIfEmailIsUsed(stringEmail)){
            Toast.makeText(PasswordRecovery.this, "Your email do not exist",
                    Toast.LENGTH_LONG).show();
        } else if (stringEmail.isEmpty() || email.length() > 50) {
            Toast.makeText(PasswordRecovery.this,
                    "Email can not be empty and must be shorter than 50 characters",
                    Toast.LENGTH_LONG).show();
        } else {
            //Generating code
            int code = getRandomNumber();
            System.out.println(code);                                                             //DELETE CODE !!!!!!!!!!!!!!!!!!!!!!!!
            //Sending email with code
            Log.i("SendMailActivity", "Send Button Clicked.");
            String fromEmail = "verifytodo123@gmail.com";
            String fromPassword = "VerifyToDO123321";
            toEmail = email.getText().toString();
            toEmail = toEmail.replaceAll("\\s","");
            String emailSubject = "ToDO Your Code";
            String emailBody = "Your code: " + code;
            Toast.makeText(this, emailBody,
                    Toast.LENGTH_LONG).show();
            new SendMailTask(this).execute(fromEmail,
                    fromPassword, toEmail, emailSubject, emailBody);
            stringRecoveryCode = "" + code;
        }
    }
    public void onClickCheckRecoveryCode(View view){
        EditText userCode = findViewById(R.id.userRecoveryCode);
        String stringUserRecoveryCode = userCode.getText().toString();
        stringUserRecoveryCode = stringUserRecoveryCode.replaceAll("\\s","");
        if(stringUserRecoveryCode.equals(stringRecoveryCode)){
            Intent intent = new Intent(this, PasswordReset.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("KEY_EMAIL",toEmail);
            saveData(toEmail);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(PasswordRecovery.this, "Wrong code",
                    Toast.LENGTH_LONG).show();
            numberOfTries++;
        }
        if(numberOfTries > 3){
            Toast.makeText(this, "Too many tries",
                    Toast.LENGTH_LONG).show();
            Intent intent2 = new Intent(this, Login.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent2);
            finish();
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