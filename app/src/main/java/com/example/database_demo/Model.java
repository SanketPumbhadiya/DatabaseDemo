package com.example.database_demo;

public class Model {
    String fname,lname,gender;


    public Model(String fname, String lname, String gender){
        this.fname = fname;
        this.lname = lname;
        this.gender = gender;
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
