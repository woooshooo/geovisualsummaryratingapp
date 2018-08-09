package com.thesis2.genise_villanueva.thesis;

public class Data {
    String data_id;
    String location;
    Coordinates coordinates;
    LocationInfo locationInfo;
    SentimentInfo sentimentInfo;

    Data(){

    }

    Data(String data_id, String location, Coordinates coordinates, LocationInfo locationInfo,SentimentInfo sentimentInfo) {
        this.data_id = data_id;
        this.location = location;
        this.coordinates = coordinates;
        this.locationInfo = locationInfo;
        this.sentimentInfo = sentimentInfo;
    }

    public String getData_id() {
        return data_id;
    }

    public void setData_id(String data_id) {
        this.data_id = data_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public LocationInfo getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(LocationInfo locationInfo) {
        this.locationInfo = locationInfo;
    }

    public SentimentInfo getSentimentInfo() {
        return sentimentInfo;
    }

    public void setSentimentInfo(SentimentInfo sentimentInfo) {
        this.sentimentInfo = sentimentInfo;
    }
}
