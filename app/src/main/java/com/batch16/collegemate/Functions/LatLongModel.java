package com.batch16.collegemate.Functions;

class LatLongModel {
    Double Latitude,Longitude;
    String Name;
    int count;

    public LatLongModel(Double Latitude, Double Longitude,String name,int cou) {
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.Name=name;
        this.count=cou;
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

    public void setCount(int count) {
        this.count = count;
    }
    public int getCount() {
        return count;
    }
}
