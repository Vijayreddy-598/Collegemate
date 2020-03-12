package com.batch16.collegemate.Functions;

public class LatLongModel {
    Double Latitude,Longitude;
    String Name;


    public LatLongModel(Double Latitude, Double Longitude,String name) {
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.Name=name;
    }

    public LatLongModel(){
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double Latitude) {
        this.Latitude = Latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double Longitude) {
        this.Longitude = Longitude;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

}
