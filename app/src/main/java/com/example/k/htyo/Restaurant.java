package com.example.k.htyo;

import java.util.ArrayList;

// Restaurant class for restaurant specific information
public class Restaurant {

    private String name;
    private ArrayList<Food> menu = new ArrayList<>();

    public Restaurant(String n){
        name = n;
    }

    // Creates a new Food-object and adds it to the menu. Takes category, diet, english title, description, and finnish title as input parameters
    public void addToMenu(String category, String diet, String title_en, String description, String title_fi){
        Food food = new Food(category, diet, title_en, description, title_fi);
        menu.add(food);
    }

    public ArrayList<Food> getMenu(){
        return menu;
    }

    @Override
    public String toString() {
        return name;
    }
}
