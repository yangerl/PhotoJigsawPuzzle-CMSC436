package com.example.ambermirza.myapplication;



public class Building {

    public Double lat;
    public Double lng;
    public String pic;
    public boolean completed;
    public String name;

    public Building(Double lat, Double lng, String pic, Boolean completed, String name) {
        this.lat = lat;
        this.lng = lng;
        this.pic = pic;
        this.completed = completed;
        this.name = name;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    //checks to see if this is completed
    public boolean isCompleted() {
        return completed;
    }

    //set completed to be true
    public void setCompleted() {
        completed = true;
    }

    public String getPic() {
        return pic;
    }

    public String getName() {
        return name;
    }
}
