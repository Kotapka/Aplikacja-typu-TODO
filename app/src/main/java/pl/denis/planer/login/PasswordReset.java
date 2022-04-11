package pl.denis.planer.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import pl.denis.planer.R;
import pl.denis.planer.database.DatabaseHelper;
import pl.denis.planer.model.user.SQLiteUser;

public class PasswordReset extends AppCompatActivity {

    DatabaseHelper databaseHelper = new DatabaseHelper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
    }
    public void onClickResetPassword(View view){
        Intent intent = getIntent();
        String email = intent.getStringExtra("KEY_EMAIL");
        EditText password = findViewById(R.id.passwordReset);
        EditText passwordRepeat = findViewById(R.id.passwordResetRepeat);
        String stringPassword = password.getText().toString();
        String stringPasswordRepeat = passwordRepeat.getText().toString();
        stringPassword = stringPassword.replaceAll("\\s","");
        stringPasswordRepeat = stringPasswordRepeat.replaceAll("\\s","");
        if(stringPassword.isEmpty() || stringPassword.length() > 20){
            Toast.makeText(PasswordReset.this,
                    "Password can not be empty and must be shorter than 20 characters",
                    Toast.LENGTH_LONG).show();
        }
        else if(!stringPassword.equals(stringPasswordRepeat)){
            Toast.makeText(PasswordReset.this, "Passwords must be the same!",
                    Toast.LENGTH_LONG).show();
        }else{
            try {
                SQLiteUser.resetPassword(email, stringPassword);
            } catch (Exception e) {
                Toast.makeText(PasswordReset.this, e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
            Toast.makeText(PasswordReset.this, "Passwords changed, you must to login now",
                    Toast.LENGTH_LONG).show();
            Intent intent2 = new Intent(this, Login.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent2);
            finish();
        }
    }
}