package ua.alisasira.spt.graph;

import java.util.*;

public class ShortPathTree {

    private int[] distanceTo; // length of shortest path from s to v

    private Edge[] edgeTo; // last edge on shortest path from s to v

    private PriorityQueue<QueueItem> queue = new PriorityQueue<>(Comparator.comparing(o -> o.weight));

    public ShortPathTree(Graph graph, int s) {
        int size = graph.getVerticesCount() + 1;

        distanceTo = new int[size];
        edgeTo = new Edge[size];

        for (int v = 0; v < size; v++) {
            distanceTo[v] = Integer.MAX_VALUE;
        }

        distanceTo[s] = 0;
        queue.add(new QueueItem(s, 0));

        while (!queue.isEmpty()) {
            int v = queue.poll().v;

            for (Edge edge : graph.adjacent(v)) {
                relax(edge);
            }
        }
    }

    public int distanceTo(int v) {
        return distanceTo[v];
    }

    public List<Edge> pathTo(int v) {
        List<Edge> path = new LinkedList<>();
        for (Edge e = edgeTo[v]; e != null; e = edgeTo[e.getFrom()]) {
            path.add(e);
        }
        Collections.reverse(path);
        return path;
    }

    private void relax(Edge edge) {
        int v = edge.getFrom();
        int w = edge.getTo();

        if (distanceTo[w] > distanceTo[v] + edge.getWeight()) {
            distanceTo[w] = distanceTo[v] + edge.getWeight();
            edgeTo[w] = edge;

            queue.remove(new QueueItem(w));
            queue.add(new QueueItem(w, distanceTo[w]));
        }
    }

    static class QueueItem {
        int v;

        Integer weight;

        public QueueItem(int v) {
            this.v = v;
        }

        public QueueItem(int v, Integer weight) {
            this.v = v;
            this.weight = weight;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            QueueItem queueItem = (QueueItem) o;
            return v == queueItem.v;
        }

        @Override
        public int hashCode() {
            return Objects.hash(v);
        }
    }
}