package com.example.k.htyo;

import java.util.ArrayList;

/* Account class that contains user authentication information and account specific comments */
public class Account {
    private String username;
    private String password;
    private ArrayList<Comment> comments = new ArrayList<>();

    //use default username "anonymous" if no name and password given
    public Account(){
        username = "anonymous";
    }

    public Account(String name, String pw){
        username = name;
        password = pw;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addComment(String text){
        comments.add(new Comment(username, text));
    }
}
