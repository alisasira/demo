package ua.alisasira.spt.graph;

import ua.alisasira.spt.parse.DataContext;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ua.alisasira.spt.parse.DataParser.parse;

public class Graph {

    private List<Edge> edges = new ArrayList<>();
    private int verticesCount;

    public Graph(File file) {
        this(parse(file));
    }

    public Graph(DataContext context) {
        context.getCities().forEach(city ->
                city.getConnections().forEach(connection ->
                        addEdge(connection.getFrom(), connection.getTo(), connection.getWeight())
                ));
        verticesCount = context.getCities().getLast().getId();
    }

    public void addEdge(int from, int to, int weight) {
        edges.add(new Edge(from, to, weight));
    }

    public int getVerticesCount() {
        return verticesCount;
    }

    public Collection<Edge> adjacent(int from) {
        Collection<Edge> result = edges.stream()
                .filter(edge -> edge.getFrom() == from)
                .collect(Collectors.toList());
        return result;
    }
}