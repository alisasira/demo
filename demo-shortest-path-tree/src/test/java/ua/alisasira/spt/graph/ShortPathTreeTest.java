package ua.alisasira.spt.graph;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class ShortPathTreeTest {

    private final Graph graph = new Graph(get("input.txt"));

    @Test
    public void testTree() {

        int FROM = 1;
        int TO = 4;

        ShortPathTree tree = new ShortPathTree(graph, FROM);
        assertThat(tree.distanceTo(TO), equalTo(3));

        List<Edge> path = tree.pathTo(TO);
        assertThat(path, hasSize(3));
        assertThat(path, hasItem(edge(1, 2, 1)));
        assertThat(path, hasItem(edge(2, 3, 1)));
        assertThat(path, hasItem(edge(3, 4, 1)));
    }

    private static Edge edge(int from, int to, int weight) {
        return new Edge(from, to, weight);
    }

    public static File get(String path) {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);
        try {
            return resource != null ? new File(resource.toURI()) : null;
        } catch (URISyntaxException e) {
            return null;
        }
    }
}