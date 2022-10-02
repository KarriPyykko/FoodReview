package com.example.k.htyo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

// Main controller activity
public class MainActivity extends AppCompatActivity {
    RecyclerView rec;
    Spinner spinner;
    Spinner spinnerDate;
    Context context;
    DrawerLayout drawer;
    NavigationView nav;
    TextView header;
    JSONHandler json = JSONHandler.getInstance();
    AccountController controller = AccountController.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

        rec = findViewById(R.id.recyclerViewFood);
        // Add divider between RecyclerView rows
        rec.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        spinnerDate = findViewById(R.id.spinner2);
        // Create and set Spinner adapter from date strings
        ArrayAdapter<String> spinnerAdapter2 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, json.getDates());
        spinnerDate.setAdapter(spinnerAdapter2);

        spinner = findViewById(R.id.spinner);
        // Create and set Spinner adapter from Restaurant-objects
        ArrayAdapter<Restaurant> spinnerAdapter = new ArrayAdapter<Restaurant>(context, android.R.layout.simple_spinner_item, json.getRestaurants(0));
        spinner.setAdapter(spinnerAdapter);

        drawer = findViewById(R.id.drawer_layout);
        nav = findViewById(R.id.nav_view);
        View headerView  = nav.getHeaderView(0);
        header = headerView.findViewById(R.id.headerTxt);
        // Set account username to drawer header
        header.setText(SessionContainer.account.getUsername());

        // Set on navigation item selected listener. Starts a new activity based on the item selected.
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getTitle().toString().equals("log in")){
                    Intent openView = new Intent(context, LoginActivity.class);
                    startActivity(openView);
                }
                else if (menuItem.getTitle().toString().equals("register")){
                    Intent openView = new Intent(context, RegisterActivity.class);
                    startActivity(openView);
                }
                else if (menuItem.getTitle().toString().equals("my account")){
                    if (!(SessionContainer.account.getUsername().equals("anonymous"))) {
                        Intent openView = new Intent(context, MyAccountActivity.class);
                        startActivity(openView);
                    }
                }
                else if (menuItem.getTitle().toString().equals("log out")){
                    controller.logOut();
                    header.setText(SessionContainer.account.getUsername());
                    Toast.makeText(context, "You have been logged out.", Toast.LENGTH_SHORT).show();
                }
                else {
                    drawer.closeDrawers();
                    return true;
                }
                return false;
            }
        });

        // Set on Spinner selected item listener for date-spinner. Updates restaurant spinner based on selected date
        spinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);
                ArrayAdapter<Restaurant> spinnerAdapter = new ArrayAdapter<Restaurant>(context, android.R.layout.simple_spinner_item, json.getRestaurants(position));
                spinner.setAdapter(spinnerAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // Method updates the MainActivity view on button press
    public void updateView(View v){
        Restaurant restaurant = (Restaurant) spinner.getSelectedItem();
        ArrayList<Food> menu = restaurant.getMenu();

        rec.setLayoutManager(new LinearLayoutManager(this));
        final MyRecyclerViewAdapter recycleAdapter = new MyRecyclerViewAdapter(this, menu);

        rec.setAdapter(recycleAdapter);

        // Set on RecyclerView on click listener
        recycleAdapter.setClickListener(new MyRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Get food object at clicked position
                Food food = recycleAdapter.getItem(position);

                // Overwrite SessionContainer food object with the selected food object
                SessionContainer.food = food;
                //open new activity (FoodInfoActivity)
                Intent openView = new Intent(context, FoodInfoActivity.class);
                openView.putExtra("EXTRA_INFO", food);
                startActivity(openView);

            }
        });
    }

    // When resuming MainActivity view, update drawer header with username
    @Override
    protected void onResume() {
        super.onResume();

        header.setText(SessionContainer.account.getUsername());
    }

    // Save restaurants in JSON format on button press
    public void foodToJson(View v){
        json.saveJSON(context);
    }

    // Method opens/closes the drawer view on button press
    public void openDrawer(View v){
          if(!drawer.isDrawerOpen(Gravity.LEFT)) {
              drawer.openDrawer(Gravity.LEFT);
              return;
          }
          drawer.closeDrawer(Gravity.LEFT);
    }
}
