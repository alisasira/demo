package ua.alisasira.spt.parse.model;

import java.util.ArrayList;
import java.util.List;

public class CityModel {

    private int id;

    private String name;

    private List<ConnectionModel> connections = new ArrayList<>();

    public CityModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ConnectionModel> getConnections() {
        return connections;
    }

    public void addConnection(int to, int weight) {
        connections.add(new ConnectionModel(id, to, weight));
    }
}