package io.behrenle;

import java.util.Optional;
import java.util.stream.Collectors;

public class SearchTree {

    public int solve(Graph g) {
        for (int i = 1; i <= g.size(); i++) {
            if (solve(new Instance(g, i)))
                return i;
        }
        return g.size();
    }

    private void removeSingletons(Graph g) {
        var deleteVertices = g.getVertices()
                .stream()
                .filter(v -> g.degree(v) == 0)
                .collect(Collectors.toList());
        deleteVertices.forEach(g::deleteVertex);
    }

    private Optional<Integer> findFirstDegOne(Instance i) {
        return i.getGraph().getVertices()
                .stream()
                .filter(v -> i.getGraph().degree(v) == 1)
                .findFirst();
    }

    private void removeDegOne(Instance i) {
        var vertex = findFirstDegOne(i);

        while (vertex.isPresent()) {
            var neighbor = i.getGraph()
                    .getNeighbors(vertex.get())
                    .stream()
                    .findFirst()
                    .get();
            i.getGraph().deleteVertex(neighbor);
            i.decK();
            vertex = findFirstDegOne(i);
        }
    }

    private Optional<Integer> findFirstHighDeg(Instance i) {
        return i.getGraph().getVertices()
                .stream()
                .filter(v -> i.getGraph().degree(v) > i.getK())
                .findFirst();
    }

    private void removeHighDeg(Instance i) {
        var vertex = findFirstHighDeg(i);

        while (vertex.isPresent()) {
            i.getGraph().deleteVertex(vertex.get());
            i.decK();
            vertex = findFirstHighDeg(i);
        }
    }

    private boolean solve(Instance i) {
        if (i.getK() < 0)
            return false;

        if (i.getGraph().getEdgeCount() == 0)
            return true;

        var e1 = i.getGraph().getVertices()
                .stream()
                .filter(v -> i.getGraph().degree(v) > 0)
                .findFirst()
                .get();

        var e2 = i.getGraph().getNeighbors(e1).stream().findFirst().get();

        Graph graphCopy1 = i.getGraph().getCopy();
        graphCopy1.deleteVertex(e1);
        Instance instance1 = new Instance(graphCopy1, i.getK() - 1);
        removeHighDeg(instance1);
        removeDegOne(instance1);
        removeSingletons(instance1.getGraph());

        Graph graphCopy2 = i.getGraph().getCopy();
        graphCopy2.deleteVertex(e2);
        Instance instance2 = new Instance(graphCopy2, i.getK() - 1);
        removeHighDeg(instance2);
        removeDegOne(instance2);
        removeSingletons(instance2.getGraph());

        if (solve(instance1))
            return true;

        if (solve(instance2))
            return true;

        return false;
    }

    private class Instance {
        private Graph g;
        private int k;

        Instance(Graph graph, int k) {
            this.g = graph;
            this.k = k;
        }

        public Graph getGraph() {
            return g;
        }

        public int getK() {
            return k;
        }

        public void decK() {
            k--;
        }
    }
}
