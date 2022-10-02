package com.example.k.htyo;

import android.content.Context;
import android.os.StrictMode;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/* Singleton class JSONHandler to fetch, parse, and save JSON data. */
public class JSONHandler {
    private String content = null;
    private static final JSONHandler handler = new JSONHandler();
    private ArrayList<Restaurant> restaurantsDay1 = new ArrayList<>();
    private ArrayList<Restaurant> restaurantsDay2 = new ArrayList<>();
    private ArrayList<Restaurant> restaurantsDay3 = new ArrayList<>();
    private ArrayList<ArrayList<Restaurant>> restaurants = new ArrayList<>();
    private ArrayList<String> dates = new ArrayList<>();


    // Constructor for JSONHandler
    private JSONHandler(){
        // fetching JSON data
        loadJSON();

        // Parse current date and next 2 days to dates ArrayList
        parseDates();

        // Parse restaurant information to day specific ArrayLists
        restaurantsDay1.add(parseRestaurant("ylioppilastalo", dates.get(0)));
        restaurantsDay1.add(parseRestaurant("laseri", dates.get(0)));
        restaurantsDay1.add(parseRestaurant("ravintola-parempi", dates.get(0)));

        restaurantsDay2.add(parseRestaurant("ylioppilastalo", dates.get(1)));
        restaurantsDay2.add(parseRestaurant("laseri", dates.get(1)));
        restaurantsDay2.add(parseRestaurant("ravintola-parempi", dates.get(1)));

        restaurantsDay3.add(parseRestaurant("ylioppilastalo", dates.get(2)));
        restaurantsDay3.add(parseRestaurant("laseri", dates.get(2)));
        restaurantsDay3.add(parseRestaurant("ravintola-parempi", dates.get(2)));

        // adding custom restaurant just in case there are no other food lists available.
        Restaurant test = new Restaurant("neekeriluola");
        test.addToMenu("Haiseva yllätys","Luomu","Pound of shit", "sitä itseään. Ebin X--D", "kilo paskaa");
        test.addToMenu("Viidakon sydämmestä","Orgaaninen","Monkey intestine and wakanda surprise", "WHERE'S THE LAMB SAUCE", "Apinan peräsuolta ja wakandan jekkua");
        restaurantsDay1.add(test);

        restaurants.add(restaurantsDay1);
        restaurants.add(restaurantsDay2);
        restaurants.add(restaurantsDay3);
    }

    public static JSONHandler getInstance(){
        return handler;
    }

    // Method for fetching JSON data from Skinfo API.
    private void loadJSON(){

        // Setting thread policy
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try{
            // define URL, set request method and open connection
            URL url = new URL("https://skinfo.dy.fi/api/restaurants.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream in = conn.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();

            // Read line by line, append to string builder and convert to string
            String line;
            while((line = br.readLine()) != null){
                sb.append(line);
            }
            this.content = sb.toString();
            in.close();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*  Method for parsing specific restaurant of given date. Takes input parameters of restaurant name and date string.
        Returns parsed information as a Restaurant-object */
    public Restaurant parseRestaurant(String name, String date){
        Restaurant restaurant = new Restaurant(name);

        try {

            JSONObject jObjects = new JSONObject(content);
            JSONObject jObject = jObjects.getJSONObject(name);
            JSONObject days = jObject.getJSONObject("days");
            JSONObject today = days.getJSONObject(date);
            JSONArray foods = today.getJSONArray("foods");
            if (foods.length()>0) {
                for (int i = 0; i < foods.length(); i++) {
                    JSONObject info = foods.getJSONObject(i);

                    String category = info.getString("category");
                    String diet = info.getString("diet");
                    String title_en = info.getString("title_en");
                    String description = info.getString("description");
                    String title_fi = info.getString("title_fi");

                    restaurant.addToMenu(category, diet, title_en, description, title_fi);
                }
            }
            // Set default food category if there is no food list available
            else {
                restaurant.addToMenu("EI RUOKALISTAA.","","","","");
            }
        // Catch JSONException and set default food category in the case of an exception. For example, if date field isn't found
        } catch (JSONException e) {
            e.printStackTrace();
            restaurant.addToMenu("EI RUOKALISTAA.","","","","");
            return restaurant;
        }

        return restaurant;
    }

    // Return a single restaurant specified by day and restaurant index
    public Restaurant getRestaurant(int index, int day){
        return restaurants.get(day).get(index);
    }

    // Return all restaurants specified by day
    public ArrayList<Restaurant> getRestaurants(int day){
        return restaurants.get(day);
    }

    // Method saves all the restaurants in JSON format into a text file "foods.txt" using Gson.
    public void saveJSON(Context context){
        String str = new Gson().toJson(restaurants);
        String filename =  "foods.txt";

        try {
            OutputStreamWriter osw = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));

            osw.write(str);
            osw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method parses current date and the following two days as strings
    public void parseDates(){
        try {
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            String day1 = format.format(date);
            c.setTime(format.parse(day1));

            // increment the date by 1 day using Calendar object
            c.add(Calendar.DATE, 1);
            String day2 = format.format(c.getTime());
            c.add(Calendar.DATE, 1);
            String day3 = format.format(c.getTime());

            dates.add(day1);
            dates.add(day2);
            dates.add(day3);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getDates(){
        return dates;
    }
}
