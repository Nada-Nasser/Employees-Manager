package com.example.listing;

public class Admin
{
    String Name;
    String Email;

    public Admin(String name, String email) {
        Name = name;
        Email = email;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "Name='" + Name + '\'' +
                ", Email='" + Email + '\'' +
                '}';
    }
}
