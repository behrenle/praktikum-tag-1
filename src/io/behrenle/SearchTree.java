package io.behrenle;

import java.util.Set;

public class SearchTree {

    public int solve(Graph g) {
        for (int i = 1; i <= g.size(); i++) {
            if (solve(new Instance(g, i)))
                return i;
        }
        return g.size();
    }

    private boolean solve(Instance i) {
        if (i.getK() < 0)
            return false;

        if (i.getGraph().getEdgeCount() == 0)
            return true;

        Edge edge = null;
        for (int v : i.getGraph().getVertices()) {
            if (edge == null) {
                int degree = i.getGraph().degree(v);
                if (degree > 0) {
                    Set<Integer> neighbors = i.getGraph().getNeighbors(v);
                    edge = new Edge(v, neighbors.stream().toList().get(0));
                }
            }
        }

        assert edge != null;
        Graph graphCopy1 = i.getGraph().getCopy();
        graphCopy1.deleteVertex(edge.getFirstNode());
        Graph graphCopy2 = i.getGraph().getCopy();
        graphCopy2.deleteVertex(edge.getSecondNode());

        if (solve(new Instance(graphCopy1, i.getK() - 1)))
            return true;

        if (solve(new Instance(graphCopy2, i.getK() - 1)))
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
    }
}
