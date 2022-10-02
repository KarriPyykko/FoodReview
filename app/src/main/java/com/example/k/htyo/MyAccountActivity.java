package com.example.k.htyo;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

// Activity for account details
public class MyAccountActivity extends AppCompatActivity {

    TextView text;
    EditText text2;
    RecyclerView rec;
    ArrayList<Comment> comments;
    Context context;
    ConstraintLayout main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        main = findViewById(R.id.accountMain);
        context = MyAccountActivity.this;
        text2 = findViewById(R.id.editTextMyAccount);
        rec = findViewById(R.id.accountComments);
        rec.setLayoutManager(new LinearLayoutManager(this));

        // Insert comments into the RecyclerView
        comments = SessionContainer.account.getComments();
        MyRecyclerViewAdapter2 recycleAdapter = new MyRecyclerViewAdapter2(context, comments);
        rec.setAdapter(recycleAdapter);

        // Set EditText view unfocusable by default and update username into EditText field
        text2.setFocusableInTouchMode(false);
        text2.setFocusable(false);
        text2.setText(SessionContainer.account.getUsername());

        // Set on EditText key listener for enter key press
        text2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)){
                    // hide keyboard, clear focus and update modified username on key press
                    hideKeyboard();
                    text2.clearFocus();
                    SessionContainer.account.setUsername(text2.getText().toString());
                    return true;
                }
                return false;
            }
        });

        // Set on EditText editor action listener for IME_ACTION_DONE
        text2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE)) {
                    // hide keyboard, clear focus and update modified username on key press
                    hideKeyboard();
                    text2.clearFocus();
                    SessionContainer.account.setUsername(text2.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    // Go back to previous view (MainActivity) on button press
    public void previousView(View v){
        Intent homeView = new Intent(context, MainActivity.class);
        setResult(RESULT_OK, homeView);
        finish();
    }

    // Set focus on EditText field on button press
    public void editName(View v){
        text2.setFocusableInTouchMode(true);
        text2.requestFocus();
    }

    // Method for hiding the soft keyboard
    public void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(main.getWindowToken(), 0);
    }
}
