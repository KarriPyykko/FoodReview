package com.example.k.htyo;

import java.io.Serializable;
import java.util.Date;

/*  Comment class that contains the comment feedback, name of the author, and a timestamp of the comment in milliseconds.
    Serializable is needed to send Food-objects containing comments to FoodInfoActivity from MainActivity as an EXTRA */
public class Comment implements Serializable {
    private String author;
    private String feedback;
    long time;

    public Comment(String auth, String fb){
        author = auth;
        feedback = fb;
        parseTime();
    }

    public String getAuthor() {
        return author;
    }

    public String getFeedback() {
        return feedback;
    }

    public long getTime(){
        return time;
    }

    @Override
    public String toString() {
        return feedback;
    }

    // Creates timestamp for the comment object upon creation. getTime() returns the time in milliseconds.
    private void parseTime(){
        time = new Date().getTime();
    }

    public void setFeedback(String editString){
        feedback = editString;
    }
}
