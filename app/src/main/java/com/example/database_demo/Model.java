package com.example.database_demo;

public class Model {
    String fname,lname,gender;
    int id;

    public Model(int id, String fname, String lname, String gender){
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }
    public String getFname() {
        return fname;
    }
    public String getLname() {
        return lname;
    }
    public String getGender() {
        return gender;
    }
}
