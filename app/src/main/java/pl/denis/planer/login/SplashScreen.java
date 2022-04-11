package pl.denis.planer.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import pl.denis.planer.R;
import pl.denis.planer.database.DatabaseHelper;
import pl.denis.planer.database.ProjectTableInfo;
import pl.denis.planer.database.TaskTableInfo;
import pl.denis.planer.database.UserTableInfo;
import pl.denis.planer.model.project.Project;
import pl.denis.planer.model.task.Task;
import pl.denis.planer.model.user.User;


public class SplashScreen extends AppCompatActivity {

    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialize model
            DatabaseHelper.setContext(this);
        //
        setContentView(R.layout.activity_splash_screen);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        getSupportActionBar().hide();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                GoToMain();
            }
        },2000);
    }
    private void GoToMain(){
        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
        finish();
    }
}
