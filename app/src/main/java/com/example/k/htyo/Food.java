package com.example.k.htyo;

import java.io.Serializable;
import java.util.ArrayList;

/*  Food class that contains information parsed from JSON, added comments and ratings.
    Implements Serializable to send Food-objects from MainActivity to FoodInfoActivity as an EXTRA */
public class Food implements Serializable {
    private String category;
    private String diet;
    private String title_en;
    private String description;
    private String title_fi;
    private float score = 0;
    private int ratingCount = 0;
    private ArrayList<Comment> comments = new ArrayList<>();

    public Food(String cat, String d, String ti_en, String desc, String ti_fi){
        category = cat;
        diet = d;
        title_en = ti_en;
        description = desc;
        title_fi = ti_fi;
    }

    // Changes score based on given rating and increments ratingCount
    public void ChangeScore(float value){
        score = score + value;
        ratingCount++;
    }

    public void addComment(String name, String text){
        comments.add(new Comment(name, text));
    }

    // returns averaged score rounded to one decimal
    public float getScore(){
        return (float) (Math.round(score/ratingCount*10.0)/10.0);
    }

    public String getCategory() {
        return category;
    }

    public String getDiet() {
        return diet;
    }

    public String getTitle_en() {
        return title_en;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle_fi() {
        return title_fi;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    @Override
    public String toString() {
        return category + "\n" + diet + "\n" + title_en + "\n" + description + "\n" + title_fi;
    }
}
