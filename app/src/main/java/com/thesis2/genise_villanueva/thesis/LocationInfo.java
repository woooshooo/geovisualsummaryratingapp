package com.thesis2.genise_villanueva.thesis;

class LocationInfo {
    String address;
    String website;
    String contactno;
    String rating;

    LocationInfo(){

    }
    LocationInfo(String address, String website, String contactno, String rating) {
        this.address = address;
        this.website = website;
        this.contactno = contactno;
        this.rating = rating;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
