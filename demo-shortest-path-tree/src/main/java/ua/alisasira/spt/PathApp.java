package ua.alisasira.spt;

import ua.alisasira.spt.graph.Edge;
import ua.alisasira.spt.graph.Graph;
import ua.alisasira.spt.graph.ShortPathTree;
import ua.alisasira.spt.parse.DataContext;
import ua.alisasira.spt.parse.DataParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class PathApp {

    private static final String INPUT_FILE_NAME = "input.txt";
    private static final String OUTPUT_FILE_NAME = "output.txt";
    public static final String RED = "\u001B[31m";

    public static void main(String[] args) throws IOException {
        File input = new File(INPUT_FILE_NAME);
        if (!input.exists()) {
            System.out.printf("Input file %s was not found%n", input.getAbsolutePath());
            return;
        }

        DataContext context = DataParser.parse(input);

        Graph graph = new Graph(context);

        StringBuilder output = new StringBuilder();

        context.getPaths().forEach(path -> {
            ShortPathTree spt = new ShortPathTree(graph, path.getFrom().getId());
            List<Edge> route = spt.pathTo(path.getTo().getId());

            output.append(String.format(
                    "Route from %s to %s\nTotal cost %s\n",
                    path.getFrom().getName(),
                    path.getTo().getName(),
                    spt.distanceTo(path.getTo().getId())
            ));

            route.forEach(edge ->
                    output.append(edge.toString()).append("\n"));
            output.append("\n");
        });

        FileWriter writer = new FileWriter(OUTPUT_FILE_NAME);
        writer.write(output.toString());
        writer.close();

        System.out.println(RED + "Output file: " + new File(OUTPUT_FILE_NAME).getAbsolutePath());
    }
}