package ua.alisasira.spt.parse.model;

public class PathModel {

    private CityModel from;

    private CityModel to;

    public PathModel(CityModel from, CityModel to) {
        this.from = from;
        this.to = to;
    }

    public CityModel getFrom() {
        return from;
    }

    public void setFrom(CityModel from) {
        this.from = from;
    }

    public CityModel getTo() {
        return to;
    }

    public void setTo(CityModel to) {
        this.to = to;
    }
}