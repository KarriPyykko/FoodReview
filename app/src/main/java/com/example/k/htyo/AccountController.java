package com.example.k.htyo;

import java.util.ArrayList;

/* Singleton control class for account actions (register, login, logout) */
public class AccountController {
    private static final AccountController controller = new AccountController();
    private ArrayList<Account> accounts = new ArrayList<>();

    private AccountController(){

    }

    public static AccountController getInstance(){
        return controller;
    }

    // Method that goes through all the accounts and logs the user in if matching account is found. Returns boolean value indicating successful/failed login.
    public boolean logIn(String n, String pw){
        for (Account acc : accounts){
            if ((n.equals(acc.getUsername())) && (pw.equals(acc.getPassword()))){
                SessionContainer.account = acc;
                return true;
            }
        }
        return false;
    }

    // Logs user out by overwriting current logged in account with a default "anonymous" account.
    public void logOut(){
        SessionContainer.account = new Account();
    }

    /*  Method for registering a user. Method takes account name and password as inputs and adds the given account to accounts ArrayList
        if the account name and password are longer than 0 characters. Returns boolean value indicating successful/failed registering */
    public boolean register(String n, String pw){
        if ((n.length()>0) && (pw.length()>0)){
            accounts.add(new Account(n, pw));
            return true;
        }
        return false;
    }
}
