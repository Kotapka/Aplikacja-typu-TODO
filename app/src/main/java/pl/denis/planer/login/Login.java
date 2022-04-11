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
import pl.denis.planer.model.user.User;

public class Login extends AppCompatActivity {

    DatabaseHelper databaseHelper = new DatabaseHelper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
    }
    // Switching to scene with creating account without saving data
    public void onClickCreatingNewAccount(View view) {
        Intent intent = new Intent(this, CreatingNewAccount.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    // Switching to scene with password recovery without saving data
    public void onClickPasswordRecovery(View view) {
        Intent intent = new Intent(this, PasswordRecovery.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    // Switching to main application
    public void onClickSignIn(View view) {
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        String stringUsername = username.getText().toString();
        stringUsername = stringUsername.replaceAll("\\s","");
        String stringPassword = password.getText().toString();
        stringPassword = stringPassword.replaceAll("\\s","");
        try {
            User user = new SQLiteUser(stringUsername, stringPassword);
            Global.activeUser = user;
            Toast.makeText(Login.this, "Login",
                    Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(Login.this, e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
//        Intent intent = new Intent(this, .class);
//        startActivity(intent);
    }
}