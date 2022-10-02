package com.example.k.htyo;

/*  Session container class with static variables food and account containing Food and Account objects respectively.
    Used to transfer data between views, and to maintain one instance of current food and account objects. */
public class SessionContainer {
    public static Food food;
    public static Account account = new Account();
}
