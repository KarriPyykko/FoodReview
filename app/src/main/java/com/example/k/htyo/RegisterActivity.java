package com.example.k.htyo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

// Activity for registering view
public class RegisterActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    Context context;
    TextView text;
    AccountController controller = AccountController.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context = RegisterActivity.this;
        username = findViewById(R.id.editTextRegister1);
        password = findViewById(R.id.editTextRegister2);
        text = findViewById(R.id.textViewLogin);
    }

    // Method for registering on button press.
    public void register(View v){
        String name = username.getText().toString();
        String pw = password.getText().toString();

        // On successful login go back to MainActivity. Otherwise displays "Registering failed." message on screen
        if(controller.register(name,pw)){
            Intent homeView = new Intent(context, MainActivity.class);
            setResult(RESULT_OK, homeView);
            finish();
        }
        else{
            text.setText("Registering failed.");
        }
    }

    // Go to previous view (MainActivity) on button press
    public void previousView(View v){
        Intent homeView = new Intent(context, MainActivity.class);
        setResult(RESULT_OK, homeView);
        finish();
    }
}
