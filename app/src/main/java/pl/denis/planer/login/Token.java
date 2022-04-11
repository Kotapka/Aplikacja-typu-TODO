package pl.denis.planer.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import pl.denis.planer.R;
import pl.denis.planer.database.DatabaseHelper;
import pl.denis.planer.exceptions.EmailAlreadyUsedException;
import pl.denis.planer.exceptions.LoginAlreadyUsedException;
import pl.denis.planer.exceptions.WrongEmailAddressException;
import pl.denis.planer.exceptions.WrongLoginException;
import pl.denis.planer.exceptions.WrongPasswordException;
import pl.denis.planer.model.user.SQLiteUser;
import pl.denis.planer.model.user.User;

public class Token extends Activity {
    int numberOfTries = 0;
    DatabaseHelper databaseHelper = new DatabaseHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);
    }
    public void onClickCheckCode(View view) throws EmailAlreadyUsedException, LoginAlreadyUsedException, WrongEmailAddressException, WrongLoginException, WrongPasswordException {
        Intent intent = getIntent();
        String code = intent.getStringExtra("KEY_CODE");
        String username = intent.getStringExtra("KEY_USERNAME");
        String password = intent.getStringExtra("KEY_PASSWORD");
        String email = intent.getStringExtra("KEY_EMAIL");
        code = code.replaceAll("\\s","");
        username = username.replaceAll("\\s","");
        password = password.replaceAll("\\s","");
        email = email.replaceAll("\\s","");
        EditText userCode = findViewById(R.id.userCode);
        if(userCode.getText().toString().equals(code)){
            try {
                User user = new SQLiteUser(email, username, password);
                Toast.makeText(Token.this, "Your account has been created",
                        Toast.LENGTH_LONG).show();
                Intent intent2 = new Intent(this, Login.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                finish();
            } catch ( Exception e) {
                Toast.makeText(Token.this, e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(Token.this, "Wrong code",
                    Toast.LENGTH_LONG).show();
            numberOfTries++;
        }
        if(numberOfTries > 3){
            Toast.makeText(Token.this, "Too many tries",
                    Toast.LENGTH_LONG).show();
            Intent intent2 = new Intent(this, Login.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent2);
            finish();
        }
    }
    public void onClickGoToCreatingAccoutnScene(View view) {
        Intent intent = new Intent(this, CreatingNewAccount.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}