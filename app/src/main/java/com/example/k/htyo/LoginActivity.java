package com.example.k.htyo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

// Activity for login view
public class LoginActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    Context context;
    TextView text;
    AccountController controller = AccountController.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = LoginActivity.this;
        username = findViewById(R.id.editTextRegister1);
        password = findViewById(R.id.editTextRegister2);
        text = findViewById(R.id.textViewLogin);
    }

    // Method for signing in on button press.
    public void signIn(View v){
        String name = username.getText().toString();
        String pw = password.getText().toString();

        // On successful login go back to MainActivity. Otherwise displays "Login failed." message on screen
        if(controller.logIn(name,pw)){
            Intent homeView = new Intent(context, MainActivity.class);
            setResult(RESULT_OK, homeView);
            finish();
        }
        else{
            text.setText("Login failed.");
        }
    }

    // Go to previous view (MainActivity) on button press
    public void previousView(View v){
        Intent homeView = new Intent(context, MainActivity.class);
        setResult(RESULT_OK, homeView);
        finish();
    }
}
