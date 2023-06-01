package ua.alisasira.spt.parse;

import ua.alisasira.spt.parse.model.CityModel;
import ua.alisasira.spt.parse.model.PathModel;

import java.util.LinkedList;

public class DataContext {

    private LinkedList<CityModel> cities = new LinkedList<>();

    private LinkedList<PathModel> paths = new LinkedList<>();

    public LinkedList<CityModel> getCities() {
        return cities;
    }

    public LinkedList<PathModel> getPaths() {
        return paths;
    }

    public CityModel findCityByName(String name) {
        return cities.stream().filter(city -> city.getName().equals(name)).findFirst().orElse(null);
    }
}