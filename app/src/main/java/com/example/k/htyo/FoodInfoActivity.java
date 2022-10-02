package com.example.k.htyo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

// Activity for detailed food information
public class FoodInfoActivity extends AppCompatActivity implements MyRecyclerViewAdapter2.RecyclerViewClickListener {

    TextView text;
    Food foodInfo;
    RatingBar rating;
    TextView score;
    TextView scoreTotal;
    RecyclerView commentSection;
    EditText comment;
    Context context;
    ConstraintLayout main;
    MyRecyclerViewAdapter2 recycleAdapter;
    ArrayList<Comment> comments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_info);

        text = findViewById(R.id.textView);
        rating = findViewById(R.id.ratingBar);
        score = findViewById(R.id.textViewScore);
        scoreTotal = findViewById(R.id.textViewTotalScore);
        commentSection =  findViewById(R.id.recyclerViewComments);
        comment = findViewById(R.id.editTextComment);
        context = FoodInfoActivity.this;
        main = findViewById(R.id.mainLayout);

        // Loading initial view onCreate
        loadData();

        // rating bar change listener. Displays current rating bar value in score TextView
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                score.setText("Your score: " + rating);
            }
        });

        /*  Set on key listener for enter key press. Adds comment from comment EditText to selected Food-object's and user comments (if logged in).
            Returns boolean value indicating successful/failed key event */
        comment.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)){
                    SessionContainer.food.addComment(SessionContainer.account.getUsername() , comment.getText().toString());
                    if (!(SessionContainer.account.getUsername().equals("anonymous"))) {
                        SessionContainer.account.addComment(comment.getText().toString());
                    }

                    // Hide soft keyboard, clear comment field, and clear focus from comment field
                    hideKeyboard();
                    comment.getText().clear();
                    comment.clearFocus();

                    comments = SessionContainer.food.getComments();
                    // create and insert new RecyclerView adapter
                    MyRecyclerViewAdapter2 recycleAdapter = new MyRecyclerViewAdapter2(context, comments);
                    commentSection.setAdapter(recycleAdapter);

                    return true;
                }
                return false;
            }
        });

        /*  Set on editor action listener for IME_ACTION_DONE. Adds comment from comment EditText to selected Food-object's and user comments (if logged in).
            Returns boolean value indicating successful/failed key event */
        comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    SessionContainer.food.addComment(SessionContainer.account.getUsername() , comment.getText().toString());
                    if (!(SessionContainer.account.getUsername().equals("anonymous"))) {
                        SessionContainer.account.addComment(comment.getText().toString());
                    }

                    // Hide soft keyboard, clear comment field, and clear focus from comment field
                    hideKeyboard();
                    comment.getText().clear();
                    comment.clearFocus();

                    ArrayList<Comment> comments = SessionContainer.food.getComments(); //TODO change to comments?
                    // create and insert new RecyclerView adapter
                    MyRecyclerViewAdapter2 recycleAdapter = new MyRecyclerViewAdapter2(context, comments);
                    commentSection.setAdapter(recycleAdapter);

                    return true;
                }
                return false;
            }
        });

    }

    // Method loads EXTRA from previous view and initializes the view to contain selected food information.
    public void loadData(){
        foodInfo = (Food) getIntent().getSerializableExtra("EXTRA_INFO");

        text.setText(foodInfo.getTitle_fi() + "\n" + foodInfo.getDescription() + "\n\n" + foodInfo.getDiet());

        scoreTotal.setText("Average score: " + SessionContainer.food.getScore());

        // loading comments
        comments = SessionContainer.food.getComments();

        // create and insert RecyclerView adapter. Enable click listener.
        commentSection.setLayoutManager(new LinearLayoutManager(this));
        recycleAdapter = new MyRecyclerViewAdapter2(this, comments);
        recycleAdapter.setClickListener(this);
        commentSection.setAdapter(recycleAdapter);
    }

    // Submit selected score from RatingBar on button press
    public void submitScore(View v){
        SessionContainer.food.ChangeScore(rating.getRating());
        scoreTotal.setText("Average score: " + SessionContainer.food.getScore());
    }

    // Return to previous (MainActivity) view on button press
    public void previousView(View v){
        Intent homeView = new Intent(context, MainActivity.class);
        setResult(RESULT_OK, homeView);
        finish();
    }

    // Method for hiding the soft keyboard
    public void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(main.getWindowToken(), 0);
    }

    // Overriding MyRecyclerViewAdapter2 onItemClick method and displaying formatted comment timestamp as a toast
    @Override
    public void onItemClick(View v, int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy \t HH:mm");
        Date timestamp = new Date(recycleAdapter.getItem(position).getTime());
        String sTimestamp = sdf.format(timestamp);

        Toast.makeText(this, ""+sTimestamp, Toast.LENGTH_SHORT).show();
    }

    }
